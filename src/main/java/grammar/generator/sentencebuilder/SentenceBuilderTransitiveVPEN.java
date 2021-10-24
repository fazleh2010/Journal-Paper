package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.Property;
import eu.monnetproject.lemon.model.PropertyValue;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.sentencetemplates.TemplateConstants;
import grammar.datasets.annotated.AnnotatedVerb;
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

public class SentenceBuilderTransitiveVPEN implements SentenceBuilder, TemplateConstants {

    private String bindingVariable;
    private LexicalEntryUtil lexicalEntryUtil;
    private List<String> sentenceTemplates = new ArrayList<String>();
    private SelectVariable determinerObject = null;
    private Language language = null;
    private static Set<SubjectType> questionWords = new TreeSet<SubjectType>();

    static {
        questionWords.add(SubjectType.PERSON_INTERROGATIVE_PRONOUN);
        questionWords.add(SubjectType.THING_INTERROGATIVE_PRONOUN);
        questionWords.add(SubjectType.INTERROGATIVE_DETERMINER);
        questionWords.add(SubjectType.INTERROGATIVE_TEMPORAL);
        questionWords.add(SubjectType.INTERROGATIVE_PLACE);
    }

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

    private List<String> findSentencesFromTemplates(List<String> sentenceTemplates) {
        Integer index = 0;
        List<String> sentences = new ArrayList<String>();
        for (String sentenceTemplate : sentenceTemplates) {
            System.out.println(sentenceTemplate + " " + index);
            index = index + 1;
            List<String> positionTokens = this.parseTemplate(sentenceTemplate);
            String sentence;
            try {
                sentence = this.prepareSentence(positionTokens);
                if (sentenceTemplate.contains("?")) {
                    sentence += "?";
                } else if (sentenceTemplate.contains(".")) {
                    sentence += ".";
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
        String word = " X ";
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        String attribute = null, reference = null;
        Map<String, String> determinerTokens = new TreeMap<String, String>();
        if (tokens.length == 2) {
            attribute = tokens[0];
            reference = tokens[1];
        } else if (tokens.length == 1) {
            attribute = tokens[0];
        }

        System.out.println("attribute::" + attribute + " reference:" + reference);

        if (reference.contains("X")) {
            word = this.bindingVariable;

        }
        if (attribute.equals(VERB)) {
            if (reference.contains(":")) {
                String[] col = reference.split(":");
                word = getEntry(col[0], col[1]);
            } else {
                for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
                    if (annotatedVerb.getTense().toString().contains(reference)) {
                        word = annotatedVerb.getWrittenRepValue();
                        break;
                    }
                }
            }

        } else if (attribute.equals(TemplateConstants.directObject)) {
            String determinterToken = this.getDeterminerToken(this.determinerObject, reference, TemplateConstants.directObject);
            if (!determinterToken.isEmpty()) {
                word = determinterToken;
            } else {
                throw new Exception("No reference is provided in direct object!!");
            }

        } else if (attribute.equals(subject)) {
            String determinterToken = this.getDeterminerToken(this.determinerObject, reference, TemplateConstants.subject);
            if (!determinterToken.isEmpty()) {
                word = determinterToken;
            } else {
                word = this.getQuestionWord(reference);
            }

        }else if (attribute.equals(DETERMINER) || attribute.equals(PREPOSITION)) {
            word = getSubjectObjectBased(reference);

        }
        System.out.println(word);

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

        if (tense.contains("present")) {
            for (LexicalForm lexicalForm : forms) {
                Collection<PropertyValue> propertyValues = lexicalForm.getProperty(lexInfo.getProperty("tense"));
                for (PropertyValue propertyValue : propertyValues) {
                    if (propertyValue.toString().contains(tense)) {
                        return lexicalForm.getWrittenRep().value;
                    }

                }
            }
        }
        return null;
    }

    private String getSubjectObjectBased(String reference) {
        if (reference.contains(directObject)) {
            String uri = this.getDomain();
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

    private String getRange() {
        SelectVariable selectVarForward = lexicalEntryUtil.getSelectVariable();
        return lexicalEntryUtil.getConditionUriBySelectVariable(selectVarForward).toString();
    }

    private String getDomain() {
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        return lexicalEntryUtil.getConditionUriBySelectVariable(oppositeSelectVariable).toString();
    }

    private Map<String, String> getDeteminerTokens(SelectVariable selectVariable, SubjectType subjectType) throws QueGGMissingFactoryClassException {
        Map<String, String> determinerTokens = new TreeMap<String, String>();
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            if (conditionLabel.isEmpty()) {
                conditionLabel = this.getConditionLabelManually(selectVariable);
            }
            String determiner = lexicalEntryUtil.getSubjectBySubjectType(
                    subjectType,
                    this.language,
                    null
            );
            String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
            String number = annotatedVerb.getNumber().toString();
            if (annotatedVerb.getNumber().toString().contains("#")) {
                number = number.split("#")[1];
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
            if (reference.contains(subjectType.name()) && reference.contains(SubjectType.INTERROGATIVE_DETERMINER.name())) {
                Map<String, String> determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey("singular")) {
                    result = determinerTokens.get("singular");
                    break;
                }

            } else if (reference.contains(subjectType.name()) && reference.contains(SubjectType.PERSON_INTERROGATIVE_PRONOUN.name())
                    && subject.contains(TemplateConstants.directObject)) {
                Map<String, String> determinerTokens = this.getDeteminerTokens(determinerObject, subjectType);
                if (determinerTokens.containsKey("singular")) {
                    result = determinerTokens.get("singular");
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
                result = lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.PERSON_INTERROGATIVE_PRONOUN,
                        this.language,
                        null
                );
                return result;
            }
        }

        return result;
    }

}
