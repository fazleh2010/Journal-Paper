/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator.sentencebuilder;

import com.google.gdata.util.common.base.Pair;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.QuestionMark;
import static grammar.datasets.sentencetemplates.TempConstants.adjunct;
import static grammar.datasets.sentencetemplates.TempConstants.colon;
import static grammar.datasets.sentencetemplates.TempConstants.directObject;
import static grammar.datasets.sentencetemplates.TempConstants.domain;
import static grammar.datasets.sentencetemplates.TempConstants.gender;
import static grammar.datasets.sentencetemplates.TempConstants.object;
import static grammar.datasets.sentencetemplates.TempConstants.plural;
import static grammar.datasets.sentencetemplates.TempConstants.preposition;
import static grammar.datasets.sentencetemplates.TempConstants.pronoun;
import static grammar.datasets.sentencetemplates.TempConstants.range;
import static grammar.datasets.sentencetemplates.TempConstants.singular;
import static grammar.datasets.sentencetemplates.TempConstants.subject;
import static grammar.datasets.sentencetemplates.TempConstants.verb;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.generator.SubjectType;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import util.exceptions.QueGGMissingFactoryClassException;
import util.io.GenderUtils;
import util.io.ParamterFinder;
import util.io.PronounFinder;
import util.io.StringMatcher;
import util.io.TemplateFeatures;

/**
 *
 * @author elahi
 */
public class English implements TempConstants,MultilingualBuilder {

    private LexicalEntryUtil lexicalEntryUtil = null;
    private Language language = null;
    private SelectVariable rangeSelectable = null;
    private SelectVariable domainSelectable = null;
    private String domainVariable = null;
    private String rangeVariable = null;
    private String subjectUri = null;
    private String objectUri = null;
    private String referenceUri = null;
    private FrameType frameType = null;


    public English(FrameType frameType, Language language, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, String variable) {
        this.frameType=frameType;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.language = language;
        this.rangeSelectable = selectVariable;
        this.domainSelectable = oppositeSelectVariable;
        this.domainVariable = String.format(
                BINDING_TOKEN_TEMPLATE,
                variable,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        this.domainSelectable)).name(),
                SentenceType.NP
        );
        this.rangeVariable = String.format(
                BINDING_TOKEN_TEMPLATE,
                variable,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        this.rangeSelectable)).name(),
                SentenceType.NP
        );
        this.subjectUri = lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.subjOfProp).toString();
        this.objectUri = lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.objOfProp).toString();
        this.referenceUri = lexicalEntryUtil.getReferenceUri();
    }

    public static List<String> parseTemplate(String sentenceTemplate) {
        List<String> list = new ArrayList<String>();
        if (sentenceTemplate.contains(" ")) {
            String[] value = sentenceTemplate.split(" ");
            for (String string : value) {
                list.add(string);
            }
        } else {
            list.add(sentenceTemplate);
        }
        return list;
    }

    public String prepareSentence(List<String> positionTokens,TemplateFeatures templateFeatures) throws QueGGMissingFactoryClassException {
        String str = "";
        Integer index = 1;
        for (String positionString : positionTokens) {
            String positionWord = "";
            positionWord = this.getWords(StringMatcher.parseToken(positionString), index,templateFeatures);
            positionWord = positionWord + " ";
            str += positionWord;

            index = index + 1;
        }
        str = str.stripTrailing();
        return str;
    }

   

    public String getWords(String[] tokens, Integer index,TemplateFeatures templateFeatures) throws QueGGMissingFactoryClassException {
        String word = "XX";
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();

        String attribute = null, reference = null;
        Boolean flagReference = false;
        if (tokens.length == 2) {
            attribute = tokens[0];
            reference = tokens[1];
            flagReference = true;

        } else if (tokens.length == 1) {
            attribute = tokens[0];
        }

         System.out.println("attribute::" + attribute + " reference::" + reference + " index::" + index);

        if (flagReference && (attribute.equals(pronoun))) {
            word = new PronounFinder(this.lexicalEntryUtil,attribute,reference,templateFeatures).getWord();
            
        }  
        /*else if (!flagReference&& attribute.equals(appos)) {
             word=LexicalEntryUtil.getSingle(lexicalEntryUtil, appos);
        }*/
        //adjective(degree:superlative)
        else if (flagReference &&attribute.contains(adjective)) {
            word = this.lexicalEntryUtil.getAdjectiveReference(reference);
  
        } 
       
        /*else if (flagReference &&attribute.equals(noun)) {
             if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getNoun(col[0], col[1]);              
             } 
  
        }*/ 
        else if (attribute.contains(preposition)) {
            word = this.findPreposition(attribute, reference, flagReference);
           
        }
        else if (attribute.contains(Apostrophe)) {
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, Apostrophe);
        }
        else if (attribute.contains(particleLocation)) {
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, attribute);

        }
        
        
        else if (flagReference && isIntergativePronoun(attribute).first) {
            SubjectType subjectType = null;
            if (reference.contains(range)) {
                subjectType = findIntergativePronoun(lexicalEntryUtil, this.rangeSelectable);
            } else {
                subjectType = findIntergativePronoun(lexicalEntryUtil, this.domainSelectable);
            }
            
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, subjectType.name());
          
          
        } else if (flagReference && isInterrogativeAmount(attribute).first) {
            SubjectType subjectType = isInterrogativeAmount(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getDeteminerTokenManual(subjectType, col[0], col[1]);
            }            

        } else if (isInterrogativeAmount(attribute).first) {
            SubjectType subjectType = isInterrogativeAmount(attribute).second;
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, subjectType.name());

        }
        
        else if (isInterrogativeOften(attribute).first) {
            SubjectType subjectType = isInterrogativeOften(attribute).second;
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, subjectType.name());

        }
        else if (isInterrogativeMuch(attribute).first) {
            SubjectType subjectType = isInterrogativeMuch(attribute).second;
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, subjectType.name());

        }
        
        
        else if (flagReference && isInterrogativeEvalution(attribute).first) {
            SubjectType subjectType = isInterrogativeEvalution(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getDeteminerTokenManual(subjectType, col[0], col[1]);
            }            

        }
        else if (isInterrogativeEvalution(attribute).first) {
            SubjectType subjectType = isInterrogativeEvalution(attribute).second;
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, subjectType.name());

        }
        else if ( isInterrogativePlace(attribute).first) {
            SubjectType subjectType = isInterrogativePlace(attribute).second;
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, attribute);

        } else if (flagReference && isInterrogativeDeterminer(attribute).first) {
            SubjectType subjectType = isInterrogativeDeterminer(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getDeteminerTokenManual(subjectType, col[0], col[1]);
            }

        } 
        
        else if (flagReference && isInterrogativePronounDeterminer(attribute).first) {
            SubjectType subjectType = isInterrogativePronounDeterminer(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getDeteminerTokenManual(subjectType, col[0], col[1]);
            }

        } 
        
        else if (flagReference &&isInterrogativeVariableDeterminer(attribute).first) {
             SubjectType subjectType = isInterrogativeVariableDeterminer(attribute).second;
              String quesWord = LexicalEntryUtil.getSingle(this.lexicalEntryUtil,subjectType.name());
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getTokenManual(col[0], col[1]);
                word=quesWord+" ($x | "+word.substring(0, 1).toUpperCase() + word.substring(1)+"_NP"+")";
            }

        }
        
        else if (interrogativeTemporal(attribute).first) {
              SubjectType subjectType = interrogativeTemporal(attribute).second;
              word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil,subjectType.name());

        }
       
        
        
        
        else if (flagReference && attribute.contains(noun)) {
            if (reference.contains(range) || reference.contains(domain)) {
                if (reference.contains(colon)) {
                    String[] col = reference.split(colon);
                    word = GenderUtils.getConditionLabelManually(col[0], col[1], this.subjectUri, this.objectUri);
                }
                else
                  System.out.println("number of paramters are not correct::"+reference);  
            }
            else{
               word = this.getReferenceWrttienForm(reference); 
            }

        } else if (flagReference && attribute.contains(verb)) {
            word = new VerbFinderEnglish(this.frameType,this.lexicalEntryUtil,attribute, reference).getWord();

        } else if (flagReference && attribute.equals(determiner)) {

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                if (col.length == 2) {
                    String first = col[0];
                    String second = col[1];
                    String article = this.getArticleFromUri(second);
                    word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,first, gender, article);
                } else if (col.length == 3) {
                    String baseReference = col[0];
                    String article = this.getArticleFromUri(col[1]);
                    String givenNumber = col[2];
                    word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,baseReference, gender, article, number, givenNumber);
                }

            } else {
                word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil,reference);
            }

        } else if (flagReference && (attribute.equals(determiner) && reference.contains(subject))) {
            String numberType = singular;
            String domainOrRange = domain;
            String determinterToken = this.getDeterminerToken(domainOrRange, reference, numberType);
            if (!determinterToken.isEmpty()) {
                word = determinterToken;
                if (index > 1) {
                    word = word.toLowerCase();
                }

            }

        } else if (flagReference && (attribute.contains(subject) || attribute.contains(adjunct) || attribute.contains(object))) {
            if (reference.contains(range)) {
                word = this.rangeVariable;
            } else if (reference.contains(domain)) {
                word = this.domainVariable;
            }

        } 
       
        
        if (attribute.contains(QuestionMark)) {
            word = word + QuestionMark;

        }
        
      
        System.out.println("word:::" + word);
        
        

        return word;
    }

  
    private String getSubjectObjectBased(String reference) {
        if (reference != null) {
            return "";
        }
        if (reference.contains(directObject)) {
            String uri = lexicalEntryUtil.getDomain(lexicalEntryUtil);
            return GenderUtils.getArticle(uri);
        } else {
            LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
            LexicalEntry lexicalEntry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(reference);
            Collection<LexicalForm> form = lexicalEntry.getForms();
            for (LexicalForm lexicalForm : form) {
                return lexicalForm.getWrittenRep().value;
            }
        }

        return null;
    }

    private String getDeteminerToken(SubjectType subjectType, String domainOrRange, String number) throws QueGGMissingFactoryClassException {
        SelectVariable selectVariable = null;
        String determinerToken = "";
      

        if ((domainOrRange.contains(range) || domainOrRange.contains(domain))) {
            if (domainOrRange.contains(range)) {
                selectVariable = this.rangeSelectable;
            } else {
                selectVariable = this.domainSelectable;
            }
            String noun = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            if (noun == null || noun.isEmpty()) {
                noun = GenderUtils.getConditionLabelManually(domainOrRange, number,this.subjectUri,this.objectUri);
            }
            String article = this.getArticleFromUri(domainOrRange);
            String questionWord = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,subjectType.name(), TempConstants.number, number, TempConstants.gender, article);
            return determinerToken = questionWord + " " + noun;
        } else {

        }

        return determinerToken;
    }
    
    private String getNoun(String domainOrRange, String number) throws QueGGMissingFactoryClassException {
        SelectVariable selectVariable = null;
        String noun = "";
      

        if ((domainOrRange.contains(range) || domainOrRange.contains(domain))) {
            if (domainOrRange.contains(range)) {
                selectVariable = this.rangeSelectable;
            } else {
                selectVariable = this.domainSelectable;
            }
            noun = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            if (noun == null || noun.isEmpty()) {
                noun = GenderUtils.getConditionLabelManually(domainOrRange, number,this.subjectUri,this.objectUri);
            }
            return noun;
        } 

        return noun;
    }
    
    private String getIntergativeAmountToken(SubjectType subjectType, String domainOrRange, String number) throws QueGGMissingFactoryClassException {
        SelectVariable selectVariable = null;
        String determinerToken = "";
      
        if ((domainOrRange.contains(range) || domainOrRange.contains(domain))) {
            if (domainOrRange.contains(range)) {
                selectVariable = this.rangeSelectable;
            } else {
                selectVariable = this.domainSelectable;
            }
            String noun = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            if (noun == null || noun.isEmpty()) {
                noun = GenderUtils.getConditionLabelManually(domainOrRange, number,this.subjectUri, this.objectUri);
            }
            String article = this.getArticleFromUri(domainOrRange);
            String questionWord = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,subjectType.name(), TempConstants.number, number, TempConstants.gender, article);
            return determinerToken = questionWord + " " + noun;
        } 

        return determinerToken;
    }
    
   

   
    private String getDeteminerTokenManual(SubjectType subjectType, String domainOrRange,String number) throws QueGGMissingFactoryClassException {
        String noun = GenderUtils.getConditionLabelManually(domainOrRange, number,this.subjectUri,this.objectUri);
        String questionWord = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, subjectType.name());
        return questionWord + " " + noun;

    }
    
     private String getTokenManual(String domainOrRange,String number) throws QueGGMissingFactoryClassException {
        return GenderUtils.getConditionLabelManually(domainOrRange, number,this.subjectUri,this.objectUri);
    }
    
  
   
    /*private String getConditionLabelManually(String domainOrRange, String numberType) {
        if (domainOrRange.contains(domain) && numberType.contains(singular)) {
            return GenderUtils.getWrittenFormSingular(this.subjectUri);
        } else if (domainOrRange.contains(domain) && numberType.contains(plural)) {
            return GenderUtils.getWrittenFormPlural(this.subjectUri);
        } else if (domainOrRange.contains(range) && numberType.contains(singular)) {
            return GenderUtils.getWrittenFormSingular(this.objectUri);
        } else {
            return GenderUtils.getWrittenFormPlural(this.objectUri);
        }
    }*/

    private String getDeterminerToken(String domainOrRange, String reference, String numberType) {
        /* String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
        if (conditionLabel == null || conditionLabel.isEmpty()) {
            conditionLabel = this.getConditionLabelManually(selectVariable,numberType);
        }*/
        String conditionLabel = GenderUtils.getConditionLabelManually(domainOrRange, numberType, this.subjectUri, this.objectUri);
        String domain = lexicalEntryUtil.getDomain(lexicalEntryUtil);
        String determiner = GenderUtils.getArticle(domain);

        return determiner + " " + conditionLabel;
    }

    private String getArticleFromUri(String parameter) throws QueGGMissingFactoryClassException {
        String uri = "", article = "";
        if (parameter.contains(SelectVariable.reference.name())) {
            uri = this.getReference();
        } else if (parameter.contains(domain)) {
            uri = this.getSubjectOfProperty();
        } else if (parameter.contains(range)) {
            uri = this.getObjectOfProperty();
        }
        article = GenderUtils.getArticle(uri);

        return article;
    }


  
    public String getSubjectOfProperty() {
        return this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.subjOfProp).toString();
    }

    public String getObjectOfProperty() {
        return this.lexicalEntryUtil.getConditionUriBySelectVariable(SelectVariable.objOfProp).toString();
    }

    public String getReference() {
        return lexicalEntryUtil.getReferenceUri().toString();
    }

    public static SubjectType findIntergativePronoun(LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable) throws QueGGMissingFactoryClassException {
        String uri = null;
        uri = LexicalEntryUtil.getUri(lexicalEntryUtil, selectVariable);
        if (TemplateFinder.isPerson(uri)) {
            return SubjectType.interrogativePronounPerson;
        } else {
            return SubjectType.interrogativePronounThing;

        }

    }

    public static Pair<Boolean, SubjectType> isIntergativePronoun(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativePronoun.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePronoun);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }

    public static Pair<Boolean, SubjectType> isInterrogativeAmount(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeAmount.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeAmount);
        }
        else if(questionType.equals(SubjectType.interrogativePronounWhom.toString())){
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePronounWhom);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }
    
    public static Pair<Boolean, SubjectType> isInterrogativeOften(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeOften.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeOften);
        }
        else 
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }
    
    public static Pair<Boolean, SubjectType> isInterrogativeMuch(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeMuch.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeMuch);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }
    
     public static Pair<Boolean, SubjectType> isInterrogativeEvalution(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeEvalution.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeEvalution);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }

    public static Pair<Boolean, SubjectType> isInterrogativePlace(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativePlace.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePlace);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }

    public static Pair<Boolean, SubjectType> isInterrogativeDeterminer(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeDeterminer.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeDeterminer);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }
    
     public static Pair<Boolean, SubjectType> isInterrogativeVariableDeterminer(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeVariableDeterminer.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeVariableDeterminer);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }
    
     public static Pair<Boolean, SubjectType> isInterrogativePronounDeterminer(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativePronounDeterminer.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePronounDeterminer);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }

    public static Pair<Boolean, SubjectType> interrogativePlace(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativePlace.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePlace);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }
    
     public static Pair<Boolean, SubjectType> interrogativeTemporal(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeTemporal.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeTemporal);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }

  
    

    private String getReferenceWrttienForm(String caseType, String numberType) {
        List<AnnotatedNounOrQuestionWord> annotatedLexicalEntryNouns = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords();
        String result = "";
        for (AnnotatedNounOrQuestionWord annotatedNounOrQuestionWord : annotatedLexicalEntryNouns) {
            if (annotatedNounOrQuestionWord.getNumber().toString().contains(numberType)) {
                /*System.out.println("case:::" + annotatedNounOrQuestionWord.getGender());
                 System.out.println("case:::" + annotatedNounOrQuestionWord.getPOSTag());
                System.out.println("number:::" + annotatedNounOrQuestionWord.getNumber());
                System.out.println("writtenForm:::" + annotatedNounOrQuestionWord.getWrittenRepValue());*/
                result = annotatedNounOrQuestionWord.getWrittenRepValue();
                break;
            }

        }
        return result;
    }
    
    private String getReferenceWrttienForm(String numberType) {
        List<AnnotatedNounOrQuestionWord> annotatedLexicalEntryNouns = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords();
        String result = "";
        for (AnnotatedNounOrQuestionWord annotatedNounOrQuestionWord : annotatedLexicalEntryNouns) {
             System.out.println(annotatedNounOrQuestionWord);
            if (annotatedNounOrQuestionWord.getNumber().toString().contains(numberType)) {
                result = annotatedNounOrQuestionWord.getWrittenRepValue();
                break;
            }

        }

        return result;
    }

   
   
    private String findPreposition(String attribute, String reference, Boolean flagReference) throws QueGGMissingFactoryClassException {
        String word = "XX";
        if (!flagReference) {
            reference = this.lexicalEntryUtil.getPrepositionReference() ;
            word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);
        } else if (flagReference) {
            word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);

        }
        return word;
    }

    private String findAdjectivel(String attribute, String reference, Boolean flagReference) {
        String word = "XX";
        
            String[] col = reference.split(colon);
            String property = this.lexicalEntryUtil.getOlisRestriction().getProperty();
            String value = this.lexicalEntryUtil.getOlisRestriction().getValue();
             word = this.lexicalEntryUtil.getAdjectiveReference(reference);
            /*String givenDegree = null;
            System.out.println("property::" + property + " objectUri::" + this.objectUri + " subjectUri::" + this.subjectUri);
            System.out.println("value::" + value);
            System.out.println("col::" + col[0]);
            System.out.println("col::" + col[1]);*/
            System.out.println("word::" + word);

      
        exit(1);
        /*if (reference.contains(colon)) {
            String[] col = reference.split(colon);
            word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, baseReference, gender, col[0], number, col[1]);
        }*/
        return word;
    }

    /*private boolean isTrennVerb(String word) {
      if(word.contains(" "))
            return true;
       return false;
    }*/

   
    
     
        
        
        

        /* if (paramterFinder.getReference().contains(TrennVerbPart1) || paramterFinder.getReference().contains(TrennVerbPart2)) {
            if (paramterFinder.getTensePair().second.contains(past)) {
                word = this.getMainVerb(paramterFinder);
                if (word.contains(" ")) {
                    String[] info = word.split(" ");
                    if (paramterFinder.getReference().contains(TrennVerbPart1)) {
                        word = info[0];
                    } else if (paramterFinder.getReference().contains(TrennVerbPart2)) {
                        word = info[1];
                    }

                }
            }

        } else if (paramterFinder.getReference().equals(mainVerb)) {
            if (paramterFinder.getTensePair().second.contains(perfect)&&!GenderUtils.trennVerbType.isEmpty()) {
                word = getMainVerbPresent(present);
                Pair<Boolean, String> pair = GenderUtils.getTrennVerbType(word, perfect, mainVerb);
                if (pair.first) {
                    word = pair.second;
                }
            } else {
                word = this.getMainVerb(paramterFinder);
            }

        }*/

  

    

    

  

}
