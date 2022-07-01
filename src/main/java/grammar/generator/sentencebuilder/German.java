/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator.sentencebuilder;

import com.google.gdata.util.common.base.Pair;
import eu.monnetproject.lemon.impl.LexicalEntryImpl;
import eu.monnetproject.lemon.model.Frame;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.Property;
import eu.monnetproject.lemon.model.PropertyValue;
import eu.monnetproject.lemon.model.SynArg;
import eu.monnetproject.lemon.model.SyntacticRoleMarker;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.questionword.QuestionWordFactoryIT;
import grammar.datasets.sentencetemplates.TempConstants;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static java.util.Objects.isNull;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexicon.LexicalEntryUtil;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import org.apache.commons.lang3.StringUtils;
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
public class German implements TempConstants,MultilingualBuilder {

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


    public German(FrameType frameType, Language language, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, String variable) {
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
        //adjective(degree:superlative)
        else if (flagReference &&attribute.contains(adjective)) {
           word = this.lexicalEntryUtil.getAdjectiveReference(reference);
  
        } 
        else if (attribute.contains(Apostrophe)) {
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, Apostrophe);
        }
        else if (attribute.contains(preposition)) {
            word = this.findPreposition(attribute, reference, flagReference);
           
        } else if (flagReference && isIntergativePronoun(attribute).first) {
            SubjectType subjectType = null;

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                if (col[1].contains(range)) {
                    subjectType = findIntergativePronoun(lexicalEntryUtil, this.rangeSelectable);
                } else {
                    subjectType = findIntergativePronoun(lexicalEntryUtil, this.domainSelectable);
                }
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,subjectType.name(), TempConstants.caseType, col[0], TempConstants.number, col[2]);

            }

        } else if (flagReference && isInterrogativePronounThing(attribute).first) {
            SubjectType subjectType = isInterrogativePronounThing(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, subjectType.name(), TempConstants.caseType, col[0], TempConstants.number, col[2]);
            }

        }else if (flagReference && isInterrogativeAmount(attribute).first) {
            SubjectType subjectType = isInterrogativeAmount(attribute).second;
           
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                if (col.length == 2) {
                    word = this.getIntergativeAmountToken(subjectType, col[0], col[1]);
                } else if (col.length == 3) {
                    word = this.getDeteminerTokenManual(subjectType, col[0], col[1], col[2]);
                }

            } else {
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, subjectType.name(),TempConstants.number,reference);
            }

        }
        else if (isInterrogativeAmount(attribute).first) {
            SubjectType subjectType = isInterrogativeAmount(attribute).second;
            word=LexicalEntryUtil.getSingle(lexicalEntryUtil, subjectType.name());

        }/*else if (!flagReference && isInterrogativeAmount(attribute).first) {
            SubjectType subjectType = isInterrogativeAmount(attribute).second;
            word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, subjectType.name());
        }*/ else if (flagReference && isInterrogativePlace(attribute).first) {
            SubjectType subjectType = isInterrogativePlace(attribute).second;

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,subjectType.name(), TempConstants.number, col[1], TempConstants.gender, defaultGender);
            }
          

        } 
        /*else if (flagReference && interrogativeCause(attribute).first) {
            SubjectType subjectType = interrogativeCause(attribute).second; 
            System.out.println("subjectType::"+subjectType);
            System.out.println("preposition::"+preposition);
            String preposition = this.findPrepositionForQuestion(attribute, reference, flagReference);
            word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, subjectType.name()+"_"+preposition);
        }*/
        else if (flagReference && interrogativeCause(attribute).first) {
            SubjectType subjectType = interrogativeCause(attribute).second; 
            //String preposition = this.findPrepositionForQuestion(attribute, reference, flagReference);
            word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, subjectType.name());
             //System.out.println("subjectType::"+subjectType);
            //System.out.println("preposition::"+preposition);
            //exit(1);
        }
        else if (!flagReference && isInterrogativePlace(attribute).first) {
            SubjectType subjectType = isInterrogativePlace(attribute).second;
                word =  LexicalEntryUtil.getSingle(lexicalEntryUtil, subjectType.name());

        }else if (flagReference && isInterrogativeDeterminer(attribute).first) {
            SubjectType subjectType = isInterrogativeDeterminer(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                if (col.length == 2) {
                    word = this.getDeteminerToken(subjectType, col[0], col[1]);
                } else if (col.length == 3) {
                    word = this.getDeteminerTokenManual(subjectType, col[0], col[1], col[2]);
                }

            }

        } else if (flagReference && interrogativePlace(attribute).first) {
            SubjectType subjectType = interrogativePlace(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getDeteminerToken(subjectType, col[0], col[1]);
            }

        } else if (!flagReference && interrogativeTemporal(attribute).first) {
              SubjectType subjectType = interrogativeTemporal(attribute).second;
              word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil,subjectType.name());

        }else if (flagReference && attribute.contains(noun)) {
            //AnnotatedNounOrQuestionWord annotatedNoun = annotatedLexicalEntryNouns.iterator().next();

            if (reference.contains(range) || reference.contains(domain)) {
                if (reference.contains(colon)) {
                    String[] col = reference.split(colon);
                    word = GenderUtils.getConditionLabelManually(col[0], col[1], this.subjectUri, this.objectUri);
                } else {
                    System.out.println("number of paramters are not correct::" + reference);
                }
            } else {
                if (reference.contains(colon)) {
                    String[] col = reference.split(colon);
                    word = this.getReferenceWrttienForm(col[0], col[1]);

                }
            }

        } else if (flagReference && attribute.contains(verb)) {
            word = new VerbFinderGerman(this.frameType,this.lexicalEntryUtil,attribute, reference).getWord();

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

        } else if (flagReference && (attribute.contains(article))) {
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,col[0], TempConstants.caseType, col[1], TempConstants.gender, col[2]);
            }

        }

        if (attribute.contains(QuestionMark)) {
            word = word + QuestionMark;

        }

        System.out.println("word:::" + word);
         System.out.println("domainVariable:::" + this.domainVariable);
         System.out.println("rangeVariable:::" + this.rangeVariable);

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
                noun = this.getConditionLabelManually(domainOrRange, number);
            }
            String article = this.getArticleFromUri(domainOrRange);
            String questionWord = LexicalEntryUtil.getEntryOneAtrributeCheck(lexicalEntryUtil,subjectType.name(), TempConstants.number, number, TempConstants.gender, article);
            return determinerToken = questionWord + " " + noun;
        } else {

        }

        return determinerToken;
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
                noun = this.getConditionLabelManually(domainOrRange, number);
            }
            String article = this.getArticleFromUri(domainOrRange);
            String questionWord = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,subjectType.name(), TempConstants.number, number, TempConstants.gender, article);
            return determinerToken = questionWord + " " + noun;
        } 

        return determinerToken;
    }

  
    private String getDeteminerTokenManual(SubjectType subjectType, String givenCase, String domainOrRange, String number) throws QueGGMissingFactoryClassException {
        String noun = this.getConditionLabelManually(domainOrRange, number);
        String article = this.getArticleFromUri(domainOrRange);
        String subjectString = subjectType.name();

        if (givenCase.contains(preposition)) {
            givenCase = this.findPreposition(preposition, null, false);
            givenCase = givenCase.toLowerCase().stripLeading().stripTrailing().trim();
            givenCase = GenderUtils.getPrepositionCase(givenCase);
        }
        
       

        String questionWord = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, subjectType.name(), TempConstants.number, number, TempConstants.gender, article, TempConstants.caseType, givenCase);

        if (noun.contains("-")) {
            questionWord = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, subjectType.name());
            noun="";
        }
        System.out.println("subjectType.name()::" + subjectType.name());
        System.out.println("noun::" + noun);
        System.out.println("questionWord::" + questionWord);
        //exit(1);

        return questionWord + " " + noun;

    }

    private String getConditionLabelManually(String domainOrRange, String numberType) {
        System.out.println("domainOrRange::"+domainOrRange);
        System.out.println("subjectUri::"+subjectUri);
        System.out.println("objectUri::"+objectUri);

        if (domainOrRange.contains(domain) && numberType.contains(singular)) {
            return GenderUtils.getWrittenFormSingular(this.subjectUri);
        } else if (domainOrRange.contains(domain) && numberType.contains(plural)) {
            return GenderUtils.getWrittenFormPlural(this.subjectUri);
        } else if (domainOrRange.contains(range) && numberType.contains(singular)) {
            return GenderUtils.getWrittenFormSingular(this.objectUri);
        } else {
            return GenderUtils.getWrittenFormPlural(this.objectUri);
        }
    }

    private String getDeterminerToken(String domainOrRange, String reference, String numberType) {
        /* String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
        if (conditionLabel == null || conditionLabel.isEmpty()) {
            conditionLabel = this.getConditionLabelManually(selectVariable,numberType);
        }*/
        String conditionLabel = this.getConditionLabelManually(domainOrRange, numberType);
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


    public static SubjectType findIntergativePronoun(LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable) throws QueGGMissingFactoryClassException {
        String uri = null;SubjectType subjectType=null;
        uri = LexicalEntryUtil.getUri(lexicalEntryUtil, selectVariable);
        //System.out.println("uri::"+uri);
        //System.out.println("selectVariable::"+selectVariable);
        if (TemplateFinder.isPerson(uri)) {
            subjectType=SubjectType.interrogativePronounPerson;
        } else {
            subjectType= SubjectType.interrogativePronounThing;

        }
        //System.out.println("subjectType::"+subjectType);
        //exit(1);
      return subjectType;
    }
    
    
    
    public static Pair<Boolean, SubjectType> isInterrogativePronounThing(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativePronounThing.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePronounThing);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
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
    

    public static Pair<Boolean, SubjectType> isInterrogativePlace(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativePlace.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePlace);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }
    
    public static Pair<Boolean, SubjectType> interrogativeCause(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeCause.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeCause);
        }
        return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }

    public static Pair<Boolean, SubjectType> isInterrogativeDeterminer(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeDeterminer.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeDeterminer);
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

    private String findPreposition(String attribute, String reference, Boolean flagReference) throws QueGGMissingFactoryClassException {
        String word = "XX";
        if (!flagReference) {
            reference = this.lexicalEntryUtil.getPrepositionReference();
             //System.out.println("reference::"+reference);
            
            word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);
             ///System.out.println("word::"+word);
            //exit(1);
        } else if (flagReference) {
            word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);

        }
        return word;
    }
    
    private String findPrepositionForQuestion(String attribute, String reference, Boolean flagReference) throws QueGGMissingFactoryClassException {
        String word = "XX";
        reference = this.lexicalEntryUtil.getPrepositionReference();
        word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);
        return word;
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
    

}
