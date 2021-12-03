package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.Property;
import eu.monnetproject.lemon.model.PropertyValue;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.annotated.AnnotatedVerb;
import static grammar.datasets.questionword.QuestionWordFactoryDE.questionWords;
import grammar.datasets.questionword.QuestionWordFactoryIT;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.generator.SentenceTemplateParser.QUESTION_MARK;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexicon.LexicalEntryUtil;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import org.apache.commons.lang3.StringUtils;
import util.exceptions.QueGGMissingFactoryClassException;
import util.io.GenderUtils;

public class SentenceBuilderTransitiveVPEN  implements SentenceBuilder,TempConstants {

    private String bindingVariable;
    private LexicalEntryUtil lexicalEntryUtil;
    private List<String> sentenceTemplates = new ArrayList<String>();
    private SelectVariable determinerObject = null;
    private Language language = null;

   

    public SentenceBuilderTransitiveVPEN(
            Language language,
            LexicalEntryUtil lexicalEntryUtil,
            List<String> sentenceTemplates
    ) throws QueGGMissingFactoryClassException, Exception {
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.language = language;
        this.sentenceTemplates = sentenceTemplates;
    }

    @Override
    public List<String> generateFullSentencesForward(String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        Integer index = 0;
        List<String> sentences = new ArrayList<String>();
        this.bindingVariable = String.format(BINDING_TOKEN_TEMPLATE,
                bindingVar,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(LexicalEntryUtil.getRangeSelectable(lexicalEntryUtil))
                ).name(),
                SentenceType.NP);
        this.determinerObject = LexicalEntryUtil.getDomainSelectable(lexicalEntryUtil);
        return this.findSentencesFromTemplates(this.sentenceTemplates);
    }

    @Override
    public List<String> generateFullSentencesBackward(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        this.bindingVariable = String.format(BINDING_TOKEN_TEMPLATE,
                bindingVar,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(LexicalEntryUtil.getDomainSelectable(lexicalEntryUtil))
                ).name(),
                SentenceType.NP);
        this.determinerObject = LexicalEntryUtil.getRangeSelectable(lexicalEntryUtil);
        return this.findSentencesFromTemplates(this.sentenceTemplates);
       

    }
    
    @Override
    public List<String> generateBooleanQuestionDomainRange(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateBooleanQuestionsDomain(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<String> findSentencesFromTemplates(List<String> sentenceTemplates) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    /*private List<String> findSentencesFromTemplates(List<String> sentenceTemplates) {
        Integer index = 0;
        List<String> sentences = new ArrayList<String>();
        for (String sentenceTemplate : sentenceTemplates) {
            //System.out.println(sentenceTemplate + " " + index);
            index = index + 1;
            List<String> positionTokens = this.parseTemplate(sentenceTemplate);
            String sentence;
            try {
                sentence = this.prepareSentence(positionTokens);
                if (sentenceTemplate.contains(QUESTION_MARK)) {
                    sentence += QUESTION_MARK;
                } else if (sentenceTemplate.contains(FULL_STOP)) {
                    sentence += FULL_STOP;
                }
                sentences.add(sentence);
            } catch (Exception ex) {
                Logger.getLogger(SentenceBuilderTransitiveVPEN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return sentences;
    }

    private List<String> parseTemplate(String sentenceTemplate) {
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

    private String[] parseToken(String string) {
        if (string.contains("(") && string.contains(")")) {
            String reference = StringUtils.substringBetween(string, "(", ")");
            String attribute = string.replaceAll("\\((.*?)\\)", "");
            return new String[]{attribute, reference};
        }
        return new String[]{string};
    }

    private String getWord(String[] tokens) throws QueGGMissingFactoryClassException, Exception {
        String word="XX";
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        String attribute = null, reference = null;
        Map<String, String> determinerTokens = new TreeMap<String, String>();
        if (tokens.length == 2) {
            attribute = tokens[0];
            reference = tokens[1];
        } else if (tokens.length == 1) {
            attribute = tokens[0];
        }

        //System.out.println("attribute::" + attribute + " reference:" + reference);

        if (reference.contains(variableIndicator)) {
            word = this.bindingVariable;

        }
        if (attribute.equals(verb)) {
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                word = getEntry(col[0], col[1]);
            } else {
                for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
                    if (annotatedVerb.getTense().toString().contains(reference)) {
                        word = annotatedVerb.getWrittenRepValue();
                        break;
                    }
                }
            }

        } else if (attribute.equals(TemplateVariable.directObject)) {
            String determinterToken = this.getDeterminerToken(this.determinerObject, reference, TemplateVariable.directObject);
            if (!determinterToken.isEmpty()) {
                word = determinterToken;
            } else {
                throw new Exception("No reference is provided in direct object!!");
            }

        } else if (attribute.equals(TemplateVariable.subject)) {
            String determinterToken = this.getDeterminerToken(this.determinerObject, reference, TemplateVariable.subject);
            if (!determinterToken.isEmpty()) {
                word = determinterToken;
            } else {
                word = this.getQuestionWord(reference);
            }
        }else if (attribute.equals(TemplateVariable.DETERMINER) || attribute.equals(TemplateVariable.PREPOSITION)) {
            word = getSubjectObjectBased(reference);

        }
       

        return word;
    }

    private String prepareSentence(List<String> positionTokens) throws QueGGMissingFactoryClassException, Exception {
        String str = "";
        for (String positionString : positionTokens) {
            String positionWord = this.getWord(parseToken(positionString)) + " ";
            str += positionWord;
        }
        return str.stripTrailing();
    }

    private String getEntry(String reference, String tense) {
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        LexicalEntry lexicalEntry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(reference);
        Collection<LexicalForm> forms = lexicalEntry.getForms();

        //if (tense.contains(PRESENT)||tense.contains(PAST)) {
            for (LexicalForm lexicalForm : forms) {
                Collection<PropertyValue> propertyValues = lexicalForm.getProperty(lexInfo.getProperty(TemplateVariable.tense));
                for (PropertyValue propertyValue : propertyValues) {
                    if (propertyValue.toString().contains(tense)) {
                        return lexicalForm.getWrittenRep().value;
                    }

                }
            }
        //}
        return TemplateVariable.tense;
    }

    private String getSubjectObjectBased(String reference) {
        if (reference.contains(TemplateVariable.directObject)) {
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

    private Map<String, String> getDeteminerTokens(SelectVariable selectVariable, SubjectType subjectType) throws QueGGMissingFactoryClassException {
        Map<String, String> determinerTokens = new TreeMap<String, String>();
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            if (conditionLabel == null || conditionLabel.isEmpty()) {
                conditionLabel = this.getConditionLabelManually(selectVariable);
            } 
            String determiner = lexicalEntryUtil.getSubjectBySubjectType(
                    subjectType,
                    this.language,
                    null
            );
            String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
            String number = annotatedVerb.getNumber().toString();
            if (annotatedVerb.getNumber().toString().contains(TemplateVariable.SLASH)) {
                number = number.split(TemplateVariable.SLASH)[1];
                determinerTokens.put(number, determinerToken);
            }

        }
        return determinerTokens;
    }

    

    private String getConditionLabelManually(SelectVariable selectVariable) {
        return GenderUtils.getManuallyCreatedLabel(lexicalEntryUtil.getConditionUriBySelectVariable(selectVariable).toString());
    }

    private String getDeterminerToken(SelectVariable determinerObject, String reference, String subject) throws QueGGMissingFactoryClassException {
        String result = "";

        for (SubjectType subjectType : questionWords) {

            if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativeDeterminerSingular.name())) {
                Map<String, String> determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(TemplateVariable.singular)) {
                    result = determinerTokens.get(TemplateVariable.singular);
                    break;
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativeDeterminerPlural.name())) {
                Map<String, String> determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(TemplateVariable.PLURAL)) {
                    result = determinerTokens.get(TemplateVariable.PLURAL);
                    break;
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePronounPerson.name())
                    && subject.contains(TemplateVariable.directObject)) {
                Map<String, String> determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(TemplateVariable.singular)) {
                    result = determinerTokens.get(TemplateVariable.singular);
                    break;
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.interrogativePronounPerson.name())
                    && subject.contains(TemplateVariable.directObject)) {
                Map<String, String> determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey(TemplateVariable.PLURAL)) {
                    result = determinerTokens.get(TemplateVariable.PLURAL);
                    break;
                }

            }

        }

        return result;
    }

    private String getQuestionWord(String reference) throws QueGGMissingFactoryClassException {
        String result = "";
        for (SubjectType subjectType : questionWords) {
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

    @Override
    public List<String> generateBooleanQuestionDomainRange(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateBooleanQuestionsDomain(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

   
   
   
}
