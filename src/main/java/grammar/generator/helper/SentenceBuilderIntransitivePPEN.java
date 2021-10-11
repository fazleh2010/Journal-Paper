package grammar.generator.helper;

import com.github.andrewoma.dexx.collection.Pair;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.PropertyValue;
import static grammar.generator.helper.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.List;

import grammar.generator.helper.parser.SentenceToken;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Optional;
import java.util.Set;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumberNew;
import util.io.TemplateConstants;
import static util.io.TemplateConstants.WHAT_WHICH_DO_THING;
import static util.io.TemplateConstants.WHEN_WHAT_PAST_THING;
import static util.io.TemplateConstants.WHEN_WHO_PAST_PERSON;
import static util.io.TemplateConstants.WHERE_WHO_PAST_PERSON;
import util.io.TemplateFinder;

public class SentenceBuilderIntransitivePPEN implements SentenceBuilder,TemplateConstants {

    private final Language language;
    private final AnnotatedVerb annotatedVerb;
    private final LexicalEntryUtil lexicalEntryUtil;
    private TemplateFinder templateFinder=null;
    private static Map<String, String> templates = new HashMap<String, String>();
    private static List<PropertyValue> numberList = new ArrayList<PropertyValue>();

    public SentenceBuilderIntransitivePPEN(
            Language language,
            AnnotatedVerb annotatedVerb,
            LexicalEntryUtil lexicalEntryUtil
    ) {
        this.language =language;
        this.annotatedVerb = annotatedVerb;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.templateFinder=new TemplateFinder(lexicalEntryUtil);
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
    }

    @Override
    public List<String> generateFullSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String template = this.templateFinder.getSELECTED_TEMPLATE();
        DomainOrRangeType domainOrRangeType = this.templateFinder.getForwardDomainOrRange();
        if (template.equals(WHEN_WHAT_PAST_THING) || template.equals(WHEN_WHO_PAST_PERSON)) {
            generatedSentences = whenWhatSentences(bindingVariable, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHERE_WHO_PAST_PERSON)) {
            generatedSentences = whenWhatSentences(bindingVariable, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHAT_WHICH_DO_THING)) {
            generatedSentences = whatWhichSentecnes(bindingVariable, lexicalEntryUtil, domainOrRangeType);
        }

        System.out.println(generatedSentences);
        return generatedSentences;
    }

    @Override
    public List<String> generateNP(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String template = this.templateFinder.getSELECTED_TEMPLATE();
        DomainOrRangeType domainOrRangeType = this.templateFinder.getOppositeDomainOrRange();
        if (template.equals(WHEN_WHAT_PAST_THING) || template.equals(WHEN_WHO_PAST_PERSON)) {
            generatedSentences = whenWhatNP(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHERE_WHO_PAST_PERSON)) {
            generatedSentences = whenWhatNP(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHAT_WHICH_DO_THING)) {
            generatedSentences = whatWhichNP(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType);
        }
        return generatedSentences;
    }

    public List<String> whenWhatSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil,DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String sentence, determinerStr = "";
        String verb = null;
    
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        String preposition = this.lexicalEntryUtil.getPreposition();
        Map<String, String> auxilaries = this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);
        SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(this.lexicalEntryUtil.getSelectVariable(), domainOrRangeType);
        String qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);
               
        String bindingString = DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
        )).name();

        String nounToken = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());
        String binding = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                bindingString,
                SentenceType.NP
        );
        
      
        determinerStr=this.templateFinder.getDeterminer(language,domainOrRangeType);
            
        
      
     if (!lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
            if (lexInfo.getPropertyValue("singular").equals(annotatedVerb.getNumber())) {
                for (String key : auxilaries.keySet()) {
                    String auxilariesVerb = auxilaries.get(key);
                    sentence = String.format(
                            "%s %s %s %s %s?",
                            qWord,
                            auxilariesVerb,
                            determinerStr,
                            binding,
                            annotatedVerb.getWrittenRepValue()
                    );
                    generatedSentences.add(sentence);
                }
            }
            if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
                String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(this.lexicalEntryUtil.getSelectVariable());
                String determiner = this.lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.INTERROGATIVE_DETERMINER,
                        language,
                        null
                );
                String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
                bindingString = DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
                )).name();
                binding = String.format(
                        BINDING_TOKEN_TEMPLATE,
                        bindingVariable,
                        bindingString,
                        SentenceType.NP
                );

                for (String key : auxilaries.keySet()) {
                    String auxilariesVerb = auxilaries.get(key);

                    sentence = String.format(
                            "%s %s %s %s %s?",
                            determinerToken,
                            auxilariesVerb,
                            determinerStr,
                            binding,
                            annotatedVerb.getWrittenRepValue()
                    );
                    generatedSentences.add(sentence);
                }
            }
        }

        return generatedSentences;
    }

    public List<String> whenWhatNP(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil,DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String sentence, qWord, binding ;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        String preposition = this.lexicalEntryUtil.getPreposition();
        Map<String, String> auxilaries = this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);
        // opposite select variable
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        // get subjectType of this sentence's object
        SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(oppositeSelectVariable, domainOrRangeType);
        qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);
        binding=this.getBindingString();
     
        if (!lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
            for (String key : auxilaries.keySet()) {
                String auxilariesVerb = auxilaries.get(key);
                sentence = String.format(
                        "%s %s %s %s %s?",
                        qWord,
                        auxilariesVerb,
                        annotatedVerb.getWrittenRepValue(),
                        preposition,
                        binding
                );
                generatedSentences.add(sentence);
            }

            if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
                for (PropertyValue number : numberList) {
                    String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(oppositeSelectVariable);
                    String determiner = this.lexicalEntryUtil.getSubjectBySubjectType(
                            SubjectType.INTERROGATIVE_DETERMINER,
                            language,
                            null
                    );
                    Pair<String, String> determinerTokenPair = getDeterminerTokenByNumberNew(number, conditionLabel, determiner);
                    String determinerToken = determinerTokenPair.component1();
                    String determinerTokenNumber = determinerTokenPair.component2();
                    for (String key : auxilaries.keySet()) {
                        String auxilariesVerb = auxilaries.get(key);
                        if (key.contains(determinerTokenNumber)) {
                            sentence = String.format(
                                    "%s %s %s %s %s?",
                                    determinerToken,
                                    auxilariesVerb,
                                    annotatedVerb.getWrittenRepValue(),
                                    preposition,
                                    binding
                            );
                            generatedSentences.add(sentence);
                        }
                    }
                }
            }
        }
        return generatedSentences;
    }

    private List<String> whatWhichSentecnes(String bindingVariable, LexicalEntryUtil lexicalEntryUtil,DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        String sentence;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        String preposition = this.lexicalEntryUtil.getPreposition();

        if (!lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
            // Make simple sentence (Which river flows through $x?)
            if (lexInfo.getPropertyValue("singular").equals(annotatedVerb.getNumber())) {
                SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(this.lexicalEntryUtil.getSelectVariable());
                String qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);
                String variable = String.format(
                        BINDING_TOKEN_TEMPLATE,
                        bindingVariable,
                        DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                                LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
                        )).name(),
                        SentenceType.NP
                );
                System.out.println("variable1!!!!!!!!!!!!!!!!!!!!!!!::" + variable);

                sentence = String.format(
                        "%s %s %s %s?",
                        qWord,
                        annotatedVerb.getWrittenRepValue(),
                        preposition,
                        variable
                );
                generatedSentences.add(sentence);

            }
            // Make sentence using the specified domain or range property (Which museum exhibits $x?)
            // Only generate "Which <condition-label>" if condition label is a DBPedia entity
            if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
                String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(this.lexicalEntryUtil.getSelectVariable());
                String determiner = this.lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.INTERROGATIVE_DETERMINER,
                        language,
                        null
                );
                String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
                String variable = String.format(
                        BINDING_TOKEN_TEMPLATE,
                        bindingVariable,
                        DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                                LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
                        )).name(),
                        SentenceType.NP
                );
                System.out.println("variable2!!!!!!!!!!!!!!!!!!!!!!!::" + variable);

                sentence = String.format(
                        "%s %s %s %s?",
                        determinerToken,
                        annotatedVerb.getWrittenRepValue(),
                        preposition,
                        variable
                );
                generatedSentences.add(sentence);
            }
        }
        return generatedSentences;
    }

    public List<String> whatWhichNP(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        String sentence;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        String preposition = this.lexicalEntryUtil.getPreposition();

        if (lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
            // E.g. Which cities does $x flow through?
            List<PropertyValue> numberList = new ArrayList<>();
            numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
            numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
            // Get verb "do"
            LexicalEntry component_do = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource("component_do");
            String form_does
                    = component_do.getForms().stream()
                            .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("tense"))
                            .contains(lexInfo.getPropertyValue("present")))
                            .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("person"))
                            .contains(lexInfo.getPropertyValue("thirdPerson")))
                            .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("number"))
                            .contains(lexInfo.getPropertyValue("singular")))
                            .findFirst()
                            .orElseThrow()
                            .getWrittenRep().value;

            // opposite select variable
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            // get subjectType of this sentence's object
            SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(oppositeSelectVariable);
            String qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null); // Who / What
            sentence = String.format(
                    "%s %s %s %s %s?",
                    qWord,
                    form_does,
                    bindingVariable, // won't use BINDING_TOKEN_TEMPLATE here because invalid sentences like "Which city does rivers crossed by $x flow through?" are generated - sentence needs an extension e.g. a PropertyValue (number) -> String map
                    annotatedVerb.getWrittenRepValue(),
                    preposition
            );
            generatedSentences.add(sentence);
            if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
                for (PropertyValue number : numberList) {
                    String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(oppositeSelectVariable);
                    String determiner = this.lexicalEntryUtil.getSubjectBySubjectType(
                            SubjectType.INTERROGATIVE_DETERMINER,
                            language,
                            null
                    );
                    String determinerToken = getDeterminerTokenByNumber(number, conditionLabel, determiner);
                    sentence = String.format(
                            "%s %s %s %s %s?",
                            determinerToken,
                            form_does,
                            bindingVariable,
                            annotatedVerb.getWrittenRepValue(),
                            preposition
                    );
                    generatedSentences.add(sentence);
                }
            }
        }
        return generatedSentences;
    }

    public List<String> whatWhoNP(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        String sentence;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        String preposition = this.lexicalEntryUtil.getPreposition();

        if (lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
            // E.g. Which cities does $x flow through?
            List<PropertyValue> numberList = new ArrayList<>();
            numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
            numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
            // Get verb "do"
            LexicalEntry component_do = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource("component_do");
            String form_does
                    = component_do.getForms().stream()
                            .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("tense"))
                            .contains(lexInfo.getPropertyValue("present")))
                            .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("person"))
                            .contains(lexInfo.getPropertyValue("thirdPerson")))
                            .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("number"))
                            .contains(lexInfo.getPropertyValue("singular")))
                            .findFirst()
                            .orElseThrow()
                            .getWrittenRep().value;

            // opposite select variable
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            // get subjectType of this sentence's object
            SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(oppositeSelectVariable);
            String qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null); // Who / What
            sentence = String.format(
                    "%s %s %s %s %s?",
                    qWord,
                    form_does,
                    bindingVariable, // won't use BINDING_TOKEN_TEMPLATE here because invalid sentences like "Which city does rivers crossed by $x flow through?" are generated - sentence needs an extension e.g. a PropertyValue (number) -> String map
                    annotatedVerb.getWrittenRepValue(),
                    preposition
            );
            generatedSentences.add(sentence);
            if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
                for (PropertyValue number : numberList) {
                    String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(oppositeSelectVariable);
                    String determiner = this.lexicalEntryUtil.getSubjectBySubjectType(
                            SubjectType.INTERROGATIVE_DETERMINER,
                            language,
                            null
                    );
                    String determinerToken = getDeterminerTokenByNumber(number, conditionLabel, determiner);
                    sentence = String.format(
                            "%s %s %s %s %s?",
                            determinerToken,
                            form_does,
                            bindingVariable,
                            annotatedVerb.getWrittenRepValue(),
                            preposition
                    );
                    generatedSentences.add(sentence);
                }
            }
        }
        return generatedSentences;
    }

   
  
    private Map<String, String> getAuxilariesVerb(List<PropertyValue> numberList, String auxilaryVerbString, LexInfo lexInfo) {
        LexicalEntry auxilaryVerb = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(auxilaryVerbString);

        Map<String, String> auxilaries = new HashMap<String, String>();
        for (PropertyValue number : numberList) {
            String[] info = number.toString().split("#");
            String auxVerb = auxilaryVerb.getForms().stream()
                    .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("tense"))
                    .contains(lexInfo.getPropertyValue("past")))
                    .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("number"))
                    .contains(lexInfo.getPropertyValue(info[1])))
                    .findFirst()
                    .orElseThrow()
                    .getWrittenRep().value;
            auxilaries.put(info[1], auxVerb);

        }
        return auxilaries;
    }

    private String getBindingString() {
        String binding = "$x";
           /*String bindingString = DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                LexicalEntryUtil.getOppositeSelectVariable(SelectVariable.SUBJECT_OF_PROPERTY)
        )).name();

        String binding = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                bindingString,
                SentenceType.NP
        );*/
        
    
        return binding;

    }
}
