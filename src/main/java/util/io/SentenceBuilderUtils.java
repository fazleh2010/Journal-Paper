/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

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
import static grammar.datasets.questionword.QuestionWordFactoryDE.questionWords;
import grammar.datasets.questionword.QuestionWordFactoryIT;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.generator.SentenceBuilderTransitiveVPEN;
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

/**
 *
 * @author elahi
 */
public class SentenceBuilderUtils implements TempConstants {

    private LexicalEntryUtil lexicalEntryUtil = null;
    private Language language = null;
    private SelectVariable rangeSelectable = null;
    private SelectVariable domainSelectable = null;
    private String domainVariable = null;
    private String rangeVariable = null;
    private List<PropertyValue> numberList = new ArrayList<PropertyValue>();
    private String subjectUri = null;
    private String objectUri = null;
    private String referenceUri = null;

    public SentenceBuilderUtils(FrameType frameType, Language language, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, String variable) {
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.language = language;
        this.rangeSelectable = selectVariable;
        this.domainSelectable = oppositeSelectVariable;
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
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

    public String prepareSentence(List<String> positionTokens) throws QueGGMissingFactoryClassException {
        String str = "";
        Integer index = 1;
        for (String positionString : positionTokens) {
            String positionWord = "";
            positionWord = this.getWords(parseToken(positionString), index);
            positionWord = positionWord + " ";
            str += positionWord;

            index = index + 1;
        }
        str = str.stripTrailing();
        return str;
    }

    public String[] parseToken(String string) {
        if (string.contains("(") && string.contains(")")) {
            String reference = StringUtils.substringBetween(string, "(", ")");
            String attribute = string.replaceAll("\\((.*?)\\)", "");
            return new String[]{attribute, reference};
        }
        return new String[]{string};
    }

    public String getWords(String[] tokens, Integer index) throws QueGGMissingFactoryClassException {
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
            word = this.getSingle(reference);
        } else if (attribute.contains(preposition)) {
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
                word = getEntryOneAtrributeCheck(subjectType.name(), TempConstants.caseType, col[0], TempConstants.number, col[2]);

            }

        } else if (flagReference && isInterrogativeAmount(attribute).first) {
            SubjectType subjectType = isInterrogativeAmount(attribute).second;

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                subjectType = isInterrogativeAmount(attribute).second;
                word = getEntryOneAtrributeCheck(subjectType.name(), TempConstants.number, col[1], TempConstants.gender, defaultGender);
            }

        } else if (flagReference && isInterrogativePlace(attribute).first) {
            SubjectType subjectType = isInterrogativePlace(attribute).second;

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = getEntryOneAtrributeCheck(subjectType.name(), TempConstants.number, col[1], TempConstants.gender, defaultGender);
            }

        } else if (flagReference && isInterrogativeDeterminer(attribute).first) {
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
              word = this.getSingle(subjectType.name());

        }else if (flagReference && attribute.contains(noun)) {
            //AnnotatedNounOrQuestionWord annotatedNoun = annotatedLexicalEntryNouns.iterator().next();

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getReferenceWrttienForm(col[0], col[1]);

            }

        } else if (flagReference && (attribute.contains(verb) || attribute.contains(mainVerb))) {
            word = this.findVerb(attribute, reference);

        } else if (flagReference && attribute.equals(determiner)) {

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                if (col.length == 2) {
                    String first = col[0];
                    String second = col[1];
                    String article = this.getArticleFromUri(second);
                    word = getEntryOneAtrributeCheck(first, gender, article);
                } else if (col.length == 3) {
                    String baseReference = col[0];
                    String article = this.getArticleFromUri(col[1]);
                    String givenNumber = col[2];
                    word = this.getEntryOneAtrributeCheck(baseReference, gender, article, number, givenNumber);
                }

            } else {
                word = this.getSingle(reference);
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
                word = getEntryOneAtrributeCheck(col[0], TempConstants.caseType, col[1], TempConstants.gender, col[2]);
            }

        }

        if (attribute.contains(QuestionMark)) {
            word = word + QuestionMark;

        }

        System.out.println("word:::" + word);


        return word;
    }

    private String getEntryOneAtrributeCheck(String reference, String attr, String value) {

        String result = "";
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        LexicalEntry lexicalEntry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(reference);
        Collection<LexicalForm> forms = lexicalEntry.getForms();

        for (LexicalForm lexicalForm : forms) {
            Collection<PropertyValue> propertyValues = lexicalForm.getProperty(lexInfo.getProperty(attr));
            for (PropertyValue propertyValue : propertyValues) {
                if (propertyValue.toString().contains(value)) {
                    result = lexicalForm.getWrittenRep().value;
                    break;
                }

            }
        }

        return result;
    }

    private String getEntryOneAtrributeCheck(String reference, String attrFirst, String valueFirst, String attrSecond, String valueSecond) {

        String result = "";
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        LexicalEntry lexicalEntry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(reference);
        Collection<LexicalForm> forms = lexicalEntry.getForms();

        for (LexicalForm lexicalForm : forms) {
            Boolean firstMatchFlag = false, secondMatchFlag = false;
            Collection<PropertyValue> propertyValuesFirst = lexicalForm.getProperty(lexInfo.getProperty(attrFirst));
            Collection<PropertyValue> propertyValuesSecond = lexicalForm.getProperty(lexInfo.getProperty(attrSecond));
            for (PropertyValue first : propertyValuesFirst) {
                if (first.toString().contains(valueFirst)) {
                    firstMatchFlag = true;
                    break;
                }

            }
            for (PropertyValue second : propertyValuesSecond) {
                if (second.toString().contains(valueSecond)) {
                    secondMatchFlag = true;
                    break;
                }

            }
            if (firstMatchFlag && secondMatchFlag) {
                result = lexicalForm.getWrittenRep().value;
            }
        }

        return result;
    }

    private String getEntryOneAtrributeCheck(String reference, String attrFirst, String valueFirst, String attrSecond, String valueSecond, String attrThrid, String valueThrid) {

        String result = "";
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        LexicalEntry lexicalEntry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(reference);
        Collection<LexicalForm> forms = lexicalEntry.getForms();

        for (LexicalForm lexicalForm : forms) {
            Boolean firstMatchFlag = false, secondMatchFlag = false, thirdMatchFlag = false;
            for (PropertyValue first : lexicalForm.getProperty(lexInfo.getProperty(attrFirst))) {
                if (first.toString().contains(valueFirst)) {
                    firstMatchFlag = true;
                    break;
                }

            }
            for (PropertyValue second : lexicalForm.getProperty(lexInfo.getProperty(attrSecond))) {

                if (second.toString().contains(valueSecond)) {
                    secondMatchFlag = true;
                    break;
                }

            }

            for (PropertyValue third : lexicalForm.getProperty(lexInfo.getProperty(attrThrid))) {
                if (third.toString().contains(valueThrid)) {
                    thirdMatchFlag = true;
                    break;
                }

            }
            if (firstMatchFlag && secondMatchFlag && thirdMatchFlag) {
                result = lexicalForm.getWrittenRep().value;
            }
        }

        return result;
    }

    /*private String getWrittenValueMatchedFromEntry(String reference) {
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        LexicalEntry lexicalEntry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(reference);
        Collection<LexicalForm> forms = lexicalEntry.getForms();
                      

        for (LexicalForm lexicalForm : forms) {
            String writtenValue = lexicalForm.getWrittenRep().value;
            if (writtenValue.equals(reference)) {
                return writtenValue;
            }
        }

        return preposition;
    }*/
    private String getSingle(String reference) throws QueGGMissingFactoryClassException {
        String writtenValue = "";
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        LexicalEntry lexicalEntry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(reference);
        Collection<LexicalForm> forms = lexicalEntry.getForms();
        LexicalForm lexicalForm = forms.iterator().next();
        writtenValue = lexicalForm.getWrittenRep().value;

        return writtenValue;
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
        /*if (domainOrRange.contains(range)) {
            selectVariable = this.rangeSelectable;
        } else if (domainOrRange.contains(domain)) {
            selectVariable = this.domainSelectable;
        } else {
            selectVariable = this.domainSelectable;
        }*/

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
            String questionWord = getEntryOneAtrributeCheck(subjectType.name(), TempConstants.number, number, TempConstants.gender, article);
            return determinerToken = questionWord + " " + noun;
        } else {

        }

        return determinerToken;
    }

    /*private String getDeteminerToken(SubjectType subjectType, String givenCase,String domainOrRange, String number) throws QueGGMissingFactoryClassException {
        SelectVariable selectVariable = null;
        String determinerToken ="";
          System.out.println("number::"+number);
                         System.out.println("givenCase.name()::"+givenCase);
                          System.out.println("number::"+number);
                         System.out.println("domainOrRange.name()::"+domainOrRange);
                         

        if ((domainOrRange.contains(range) || domainOrRange.contains(domain))) {
            if (domainOrRange.contains(range)) {
                selectVariable = this.rangeSelectable;
            } else {
                selectVariable = this.domainSelectable;
            }
            String noun = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            if (noun == null || noun.isEmpty()) {
                noun = this.getConditionLabelManually(domainOrRange,number);
            }
            String article = this.getArticleFromUri(domainOrRange);
                      

            String questionWord = getEntryOneAtrributeCheck(subjectType.name(), TempConstants.number, number, TempConstants.gender, article,TempConstants.caseType, givenCase);
            return determinerToken = questionWord + " " + noun;
            
        }
        else {
          
        }

        return determinerToken;
    }*/
    private String getDeteminerTokenManual(SubjectType subjectType, String givenCase, String domainOrRange, String number) throws QueGGMissingFactoryClassException {
        String noun = this.getConditionLabelManually(domainOrRange, number);
        String article = this.getArticleFromUri(domainOrRange);
        String questionWord = getEntryOneAtrributeCheck(subjectType.name(), TempConstants.number, number, TempConstants.gender, article, TempConstants.caseType, givenCase);
        return questionWord + " " + noun;

    }

    /*private String getConditionLabelManually(SelectVariable selectVariable, String numberType) {
        String uri = lexicalEntryUtil.getConditionUriBySelectVariable(selectVariable).toString();
        if (numberType.contains(singular)) {
            return GenderUtils.getWrittenFormSingular(uri);
        }
        {
            return GenderUtils.getWrittenFormPlural(uri);
        }
    }*/
    private String getConditionLabelManually(String domainOrRange, String numberType) {
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


    /*private String getArticleFromUri(String parameter) throws QueGGMissingFactoryClassException {
        String uri = "", article = "";
        if (parameter.contains(SelectVariable.reference.name())) {
            uri = this.getReference();
        } else if (parameter.contains(domain)) {
            uri = this.getSubjectOfProperty();
        } else if (parameter.contains(range)) {
            uri = this.getObjectOfProperty();
        }
       

        GenderUtils genderUtils = new GenderUtils(uri);
        if (genderUtils.getArticle() != null) {
            article = genderUtils.getArticle();
        }

        return article;

    }*/
    private Boolean isAuxilaryVerb(String reference) {
        if (reference.contains(component_be)) {
            return true;
        }
        return false;
    }

    private boolean isArticle(String reference) {
        if (reference.contains("article")) {
            return true;
        }
        return false;
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

    private static String getQuestionWord(LexicalEntryUtil lexicalEntryUtil, Language language, SubjectType subjectType) throws QueGGMissingFactoryClassException {
        String result = "";
        result = lexicalEntryUtil.getSubjectBySubjectType(subjectType,
                language,
                null
        );
        return result;

    }

    /* private String getVariable(LexicalEntryUtil lexicalEntryUtil,String bindingVar) {
        return String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVar,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                )).name(),
                SentenceType.NP.toString()
        );
    }*/
   

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

    private String getMainVerb(ParamterFinder paramterFinder) {
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();


            for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
                if (annotatedVerb.getTense().toString().contains(paramterFinder.getTensePair().second) && annotatedVerb.getPerson().toString().contains(paramterFinder.getPersonPair().second)) {
                    return annotatedVerb.getWrittenRepValue();
                }

            }
        

        return null;
    }
    
     private String getMainVerbPresent(String tense) {
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();

        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            if (annotatedVerb.getTense().toString().contains(tense)) {
                return annotatedVerb.getWrittenRepValue();
            }

        }
        return null;
    }


    private String findVerb(String attribute, String reference) throws QueGGMissingFactoryClassException {
        String word = null,wordDefault="XX";
        ParamterFinder paramterFinder = new ParamterFinder(attribute, reference);
        
        if (paramterFinder.getReference().contains(mainVerb) || paramterFinder.getReference().contains(TrennVerb)) {
            word = findMainVerb(attribute, reference);
        } else {
            if (paramterFinder.getParameterLength() == 2 && paramterFinder.getTensePair().first != null) {
                word = getEntryOneAtrributeCheck(paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second);
            } else if (paramterFinder.getParameterLength() == 3 && paramterFinder.getTensePair().first != null && paramterFinder.getNumberPair().first != null) {
                word = getEntryOneAtrributeCheck(paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second,
                        paramterFinder.getNumberPair().first, paramterFinder.getNumberPair().second);
            } else {
                word = getSingle(paramterFinder.getReference());
            }

        }
        
        
        /*if (paramterFinder.getTensePair().second.contains(perfect)) {
            word = getMainVerbPresent(present);
        } else {
            word = this.getMainVerb(paramterFinder);
        }

        if (this.isTrennVerb(word)) {
            if (paramterFinder.getReference().contains(mainVerb) && paramterFinder.getTensePair().second.contains(perfect)) {
                word=this.getPerfectTrennVerb(word);

            } else if (paramterFinder.getReference().contains(TrennVerbPart1) || paramterFinder.getReference().contains(TrennVerbPart2)) {
                if (paramterFinder.getTensePair().second.contains(past) || paramterFinder.getTensePair().second.contains(present)) {
                    word = this.getMainVerb(paramterFinder);
                    String[] info = word.split(" ");
                    if (paramterFinder.getReference().contains(TrennVerbPart1)) {
                        word = info[0];
                    } else {
                        word = info[1];
                    }

                } else {
                    System.out.println("Invalid trenn Verb!!!");
                    return wordDefault;

                }

            }

        } else {
            if(paramterFinder.getReference().contains(TrennVerbPart1) || paramterFinder.getReference().contains(TrennVerbPart2)){
                   System.out.println("Invalid trenn Verb!!!");
                    return wordDefault; 
            }
            else if (paramterFinder.getReference().contains(mainVerb)
                    && paramterFinder.getTensePair().second.contains(present)
                    || paramterFinder.getTensePair().second.contains(past)) {
                word = this.getMainVerb(paramterFinder);
            } else if (paramterFinder.getReference().contains(mainVerb)
                    && paramterFinder.getTensePair().second.contains(perfect)) {
                  word=getPerfectMainVerb(word);
            }

        }*/
        
        return word;
    }
    
    private String findMainVerb(String attribute, String reference) throws QueGGMissingFactoryClassException {
        String word = null;
        ParamterFinder paramterFinder = new ParamterFinder(attribute, reference);

        if (paramterFinder.getTensePair().second.contains(perfect)) {
            word = getMainVerbPresent(present);
        } else {
            word = getMainVerb(paramterFinder);
        }
       
        if (this.isTrennVerb(word)) {
            return word=this.findTrennVerb(word,paramterFinder);
        
        } else {
            if (paramterFinder.getReference().contains(mainVerb)&& paramterFinder.getTensePair().second.contains(perfect)) {
                word = getPerfectMainVerb(word);
            }
            else if(paramterFinder.getReference().contains(mainVerb)&& (paramterFinder.getTensePair().second.contains(present)
                    || paramterFinder.getTensePair().second.contains(past))){
                word = this.getMainVerb(paramterFinder);
            }
            else if(paramterFinder.getReference().contains(TrennVerb)){
                return "XX";
            }
            
            /*if (paramterFinder.getReference().contains(TrennVerb)) {
                System.out.println("Invalid trenn Verb!!!");
                return "XX";
            } else if (paramterFinder.getReference().contains(mainVerb)
                    && paramterFinder.getTensePair().second.contains(present)
                    || paramterFinder.getTensePair().second.contains(past)) {
                word = this.getMainVerb(paramterFinder);
            } else if (paramterFinder.getReference().contains(mainVerb)
                    && paramterFinder.getTensePair().second.contains(perfect)) {
                word = getPerfectMainVerb(word);
            }*/

        }

        return word;
    }

    /*word = this.lexicalEntryUtil.getPreposition();
            System.out.println("word::"+word);
            exit(1);*/
    private String findPreposition(String attribute, String reference, Boolean flagReference) throws QueGGMissingFactoryClassException {
        String word = null;
        if (!flagReference) {
            word = this.getSingle(attribute);

        } else if (flagReference) {
            word = this.getSingle(reference);

        }
        return word;

    }

    private boolean isTrennVerb(String word) {
      if(word.contains(" "))
            return true;
       return false;
    }

    private String getPerfectTrennVerb(String word) {
        word = getMainVerbPresent(present);
        Pair<Boolean, String> pair = GenderUtils.getTrennVerbType(word, perfect, mainVerb);
        if (pair.first) {
            return word = pair.second;
        } else {
            return "XX";

        }
    }
   
    private String getPerfectMainVerb(String word) {
        word = getMainVerbPresent(past);
        Pair<Boolean, String> pair = GenderUtils.getPerfecterbType(word, perfect);
        if (pair.first) {
            return word = pair.second;
        } else {
            return "XX";

        }
    }
    
     
        
        
        

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

    private String findTrennVerb(String word, ParamterFinder paramterFinder) {
        if (paramterFinder.getReference().contains(TrennVerb)) {
            if (paramterFinder.getTensePair().second.contains(past) || paramterFinder.getTensePair().second.contains(present)) {
                word = this.getMainVerb(paramterFinder);
                String[] info = word.split(" ");
                if (paramterFinder.getReference().contains(TrennVerbPart1)) {
                    return word = info[0];
                } else {
                    return word = info[1];
                }

            } else if (paramterFinder.getTensePair().second.contains(perfect)) {
                return word = this.getPerfectTrennVerb(word);
            } else {
                return "XX";

            }
        }
        else {
            return "XX";
        }

    }

  

}
