/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.util.common.base.Pair;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.Property;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.questionword.QuestionWordFactoryIT;
import static grammar.datasets.sentencetemplates.TemplateVariable.*;
import grammar.datasets.sentencetemplates.TemplateVariable;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.generator.SentenceBuilderTransitiveVPEN;
import grammar.generator.SubjectType;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class SentenceBuilderUtils implements TemplateConstants {

    private LexicalEntryUtil lexicalEntryUtil = null;
    private Language language = null;
    private SelectVariable rangeSelectable = null;
    private SelectVariable domainSelectable = null;
    private String domainVariable = null;
    private String rangeVariable = null;
    private List<PropertyValue> numberList = new ArrayList<PropertyValue>();

    /*public SentenceBuilderUtils(Language language, LexicalEntryUtil lexicalEntryUtil, String bindingVariable, SelectVariable determinerObject, SelectVariable oppositiveObject) {
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.language = language;
        this.bindingVariable = bindingVariable;
        this.rangeSelectable = determinerObject;
        this.domainSelectable = oppositiveObject;
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
    }*/

    public SentenceBuilderUtils(Language language, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, String variable) {
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
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();

        String attribute = null, reference = null;
        Boolean flagReference = false;
        if (tokens.length == 2) {
            attribute = tokens[0];
            reference = tokens[1];
            flagReference = true;

        } else if (tokens.length == 1) {
            attribute = tokens[0];
        }

        System.out.println("attribute::" + attribute + " reference::" + reference );
        
         if (!flagReference && attribute.contains(preposition)) {
            word = this.lexicalEntryUtil.getPreposition();
            return word;

        } else if (flagReference && attribute.contains(preposition)) {
            word = this.getSingle(reference);

        } else if (flagReference && isIntergativePronoun(attribute).first) {
            SubjectType subjectType = null;

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                if (col[0].contains(range)) {
                    subjectType = findIntergativePronoun(lexicalEntryUtil, this.rangeSelectable);
                } else {
                    subjectType = findIntergativePronoun(lexicalEntryUtil, this.domainSelectable);
                }
                word = getEntryOneAtrributeCheck(subjectType.name(), TemplateVariable.number, col[1], TemplateVariable.gender, defaultGender);
            }

        } else if (flagReference && isDeterminerQuestionWord(attribute).first) {
            SubjectType subjectType = isDeterminerQuestionWord(attribute).second;
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = this.getDeteminerToken(subjectType, col[0], col[1]);

            }

        } else if (flagReference && attribute.contains(noun)) {
            List<AnnotatedNounOrQuestionWord> annotatedLexicalEntryNouns = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords();
            AnnotatedNounOrQuestionWord annotatedNoun = annotatedLexicalEntryNouns.iterator().next();
            word = annotatedNoun.getWrittenRepValue();

        } else if (flagReference && attribute.equals(verb)) {
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                if (col.length == 2) {
                    word = getEntryOneAtrributeCheck(col[0], TemplateVariable.tense, col[1]);
                } else if (col.length == 3) {
                    word = getEntryOneAtrributeCheck(col[0], TemplateVariable.tense, col[1], TemplateVariable.number, col[2]);
                }

            }else{
                word =getSingle(reference);
            } 
            /*else {
                for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
                    if (annotatedVerb.getTense().toString().contains(reference)) {
                        word = annotatedVerb.getWrittenRepValue();
                        break;
                    }
                }
            }*/

        } else if (flagReference && attribute.equals(determiner)) {

            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                String first = col[0];
                String second = col[1];
                String article = this.getArticleFromUri(second);
                word = getEntryOneAtrributeCheck(first, gender, article);
            }

        } else if (flagReference && (attribute.equals(determiner) && reference.contains(subject))) {
            String determinterToken = this.getArticleToken(this.domainSelectable, reference);
            if (!determinterToken.isEmpty()) {
                word = determinterToken;
                if (index > 1) {
                    word = word.toLowerCase();
                }

            }

        }
        else if (flagReference && (attribute.equals(subject)||attribute.equals(adjunct))) {
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
            return new GenderUtils(uri).getArticle();
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
        SelectVariable selectVariable=null;
        if (domainOrRange.contains(range)) {
            selectVariable = this.rangeSelectable;
        } else {
            selectVariable = this.domainSelectable;
        }
        
        String noun = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
        if (noun == null || noun.isEmpty()) {
            noun = this.getConditionLabelManually(selectVariable);
        }
        String article = this.getArticleFromUri(domainOrRange);
        String questionWord = getEntryOneAtrributeCheck(subjectType.name(), TemplateVariable.number, number, TemplateVariable.gender, article);
        String determinerToken = questionWord + " " + noun;
        return determinerToken;
    }
        
       
    


    private String getConditionLabelManually(SelectVariable selectVariable) {
        return GenderUtils.getManuallyCreatedLabel(lexicalEntryUtil.getConditionUriBySelectVariable(selectVariable).toString());
    }

    
    
    /*private String getDeterminerToken(SelectVariable determinerObject, String reference) throws QueGGMissingFactoryClassException {
        String result = "";
        Map<String, String>  determinerTokens =new TreeMap<String,String>();

        for (SubjectType subjectType : QuestionWordFactoryIT.questionWords) {
            if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativeDeterminerSingular.name())) {
                determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(SINGULAR)) {
                    return result = determinerTokens.get(SINGULAR);
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativeDeterminerPlural.name())) {
                determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(PLURAL)) {
                    return result = determinerTokens.get(PLURAL);
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePronounThingWhat.name())) {
                result = this.getSingle(reference);
                return result;

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePronounPerson.name())) {
                result = this.getSingle(reference);
                return result;

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePlace.name())) {
                result = this.getSingle(reference);
                return result;

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativeTemporal.name())) {
                return result = lexicalEntryUtil.getSubjectBySubjectType(
                        subjectType,
                        this.language,
                        null
                );

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePronounPerson.name())
                    && subject.contains(grammar.datasets.sentencetemplates.TemplateVariable.directObject)) {
                determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(SINGULAR)) {
                    return result = determinerTokens.get(SINGULAR);
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePronounPerson.name())
                    && subject.contains(grammar.datasets.sentencetemplates.TemplateVariable.directObject)) {
                determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(PLURAL)) {
                    return result = determinerTokens.get(PLURAL);
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePronounPerson.name())
                    && subject.contains(grammar.datasets.sentencetemplates.TemplateVariable.directObject)) {
                determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(PLURAL)) {
                    return result = determinerTokens.get(PLURAL);
                }

            }

        }

        return result;
    }*/

    private String getQuestionWord(String reference) throws QueGGMissingFactoryClassException {
        String result = "";
        for (SubjectType subjectType : QuestionWordFactoryIT.questionWords) {
            if (reference.contains(subjectType.name())) {
                result = lexicalEntryUtil.getSubjectBySubjectType(SubjectType.interrogativePronounPerson,
                        this.language,
                        null
                );
                return result;
            }
        }

        return result;
    }

    /*private List<String> findSentencesFromTemplates(List<String> sentenceTemplates) {
        Integer index = 0;
        List<String> sentences = new ArrayList<String>();
        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = this.parseTemplate(sentenceTemplate);
            String sentence;
            try {
                List<String> newSentence = this.prepareSentence(positionTokens);
                if (sentenceTemplate.contains(QuestionMark)) {
                    newSentence += QuestionMark;
                } else if (sentenceTemplate.contains(FULL_STOP)) {
                    newSentence += FULL_STOP;
                }
                sentences.addAll(newSentence);
            } catch (Exception ex) {
                Logger.getLogger(SentenceBuilderTransitiveVPEN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return sentences;
    }*/
    private String getArticleToken(SelectVariable selectVariable, String reference) {
        String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
        if (conditionLabel == null || conditionLabel.isEmpty()) {
            conditionLabel = this.getConditionLabelManually(selectVariable);
        }
        String domain = lexicalEntryUtil.getDomain(lexicalEntryUtil);
        String determiner = new GenderUtils(domain).getArticle();

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
       

        GenderUtils genderUtils = new GenderUtils(uri);
        if (genderUtils.getArticle() != null) {
            article = genderUtils.getArticle();
        }

        return article;

    }
    
    
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

    public static SubjectType  findIntergativePronoun(LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable) throws QueGGMissingFactoryClassException {
        String uri = null;
        uri = LexicalEntryUtil.getUri(lexicalEntryUtil, selectVariable);
        if (TemplateFinder.isPerson(uri)) {
            return SubjectType.interrogativePronounPerson;
        } else {
            return SubjectType.interrogativePronounThing;

        }

    }

    public static Pair<Boolean, SubjectType>  isIntergativePronoun(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativePronoun.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativePronoun);
        }
       return new Pair<Boolean, SubjectType>(Boolean.FALSE, null);
    }

    public static Pair<Boolean, SubjectType> isDeterminerQuestionWord(String questionType) throws QueGGMissingFactoryClassException {
        if (questionType.equals(SubjectType.interrogativeDeterminer.toString())) {
            return new Pair<Boolean, SubjectType>(Boolean.TRUE, SubjectType.interrogativeDeterminer);
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

}
