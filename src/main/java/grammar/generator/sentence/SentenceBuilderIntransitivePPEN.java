package grammar.generator.sentence;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.helper.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.generator.helper.parser.SentenceTemplateParser;
import static grammar.generator.sentence.BindingConstants.BINDING_TOKEN_TEMPLATE;
import util.io.TemplateConstants;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import util.exceptions.QueGGMissingFactoryClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import util.io.SentenceBuilderUtils;
import util.io.TemplateFinder;

public class SentenceBuilderIntransitivePPEN implements SentenceBuilder, TemplateConstants {

    private final Language language;
    private final FrameType frameType;
    private SentenceTemplateRepository sentenceTemplateRepository;
    private SentenceTemplateParser sentenceTemplateParser;
    private final LexicalEntryUtil lexicalEntryUtil;
    private TemplateFinder templateFinder = null;
    private static Map<String, String> templates = new HashMap<String, String>();
    private static List<PropertyValue> numberList = new ArrayList<PropertyValue>();

    public SentenceBuilderIntransitivePPEN(
            Language language,
            FrameType frameType,
            SentenceTemplateRepository sentenceTemplateRepository,
            SentenceTemplateParser sentenceTemplateParser,
            LexicalEntryUtil lexicalEntryUtil
    ) {
        this.language = language;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.templateFinder = new TemplateFinder(lexicalEntryUtil);
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
        this.frameType = frameType;
        this.sentenceTemplateParser = sentenceTemplateParser;
        this.sentenceTemplateRepository = sentenceTemplateRepository;
    }

    @Override
    public List<String> generateFullSentencesForward(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String template = this.templateFinder.getSELECTED_TEMPLATE();
        DomainOrRangeType domainOrRangeType = this.templateFinder.getForwardDomainOrRange();
        if (template.equals(WHEN_WHAT_PAST_THING)) {
            generatedSentences = forwardSentencesWhen(bindingVariable, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHEN_WHO_PAST_PERSON)) {
            generatedSentences = forwardSentencesWhen(bindingVariable, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHERE_WHO_PAST_PERSON)) {
            generatedSentences = forwardSentencesWhen(bindingVariable, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHAT_WHICH_DO_THING)) {
            generatedSentences = forwardSentences(bindingVariable, lexicalEntryUtil, domainOrRangeType);
        } else {
            System.out.println("forward template::" + template);
        }

        return generatedSentences;
    }

    @Override
    public List<String> generateFullSentencesBackward(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String template = this.templateFinder.getSELECTED_TEMPLATE();
        DomainOrRangeType domainOrRangeType = this.templateFinder.getOppositeDomainOrRange();
        if (template.equals(WHEN_WHAT_PAST_THING)) {
            generatedSentences = backWardSentencesWhen(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHEN_WHO_PAST_PERSON)) {
            generatedSentences = backWardSentencesWhen(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHERE_WHO_PAST_PERSON)) {
            generatedSentences = backWardSentencesWhen(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType);
        } else if (template.equals(WHAT_WHICH_DO_THING)) {
            generatedSentences = backwordSentences(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType);
        } else {
            System.out.println("forward template::" + template);
        }
        return generatedSentences;
    }

   

    private List<String> forwardSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        
        Integer index = 0;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(this.lexicalEntryUtil.getSelectVariable());
        String qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        Map<String, String> verbTokens = this.getVerbTokens(annotatedVerbs, lexicalEntryUtil,lexicalEntryUtil.getSelectVariable());
        Map<String, String> auxilaries = this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);
        String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
                )).name(),
                SentenceType.NP
        );
        
        //Map<String, String>  determinerTokens=this.getDeterMinerTokens(this.lexicalEntryUtil.getSelectVariable());
        
       

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), WHAT_WHICH_DO_THING, FORWARD, language.toString()});

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, lexicalEntryUtil, variable, verbTokens,auxilaries);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String sentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            //System.out.println("sentenceTemplate::"+sentenceTemplate);
            //System.out.println("sentence::"+sentence);
            sentences.add(sentence);
        }
        

        return new ArrayList<String>(sentences);
    }

    public List<String> backwordSentences(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        List<String> backWordsentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), WHAT_WHICH_DO_THING, BACKWARD, language.toString()});
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(oppositeSelectVariable);
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        Map<String, String> verbTokens = this.getVerbTokens(annotatedVerbs, lexicalEntryUtil,oppositeSelectVariable);
        Map<String, String> auxilaries = this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);
        String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(oppositeSelectVariable)
                )).name(),
                SentenceType.NP
        );
        
      

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, lexicalEntryUtil, variable, verbTokens,auxilaries);
        for (String sentenceTemplate : backWordsentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String sentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            sentences.add(sentence);
        }

        return new ArrayList<String>(sentences);
    }
    
     public List<String> forwardSentencesWhen(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        String determinerStr = "";
        String verb = null;
        Integer index = 0;

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

        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        Map<String, String> verbTokens = this.getVerbTokens(annotatedVerbs, lexicalEntryUtil,lexicalEntryUtil.getSelectVariable());
        determinerStr = this.templateFinder.getDeterminer(language, domainOrRangeType);

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), WHEN_WHAT_PAST_THING, FORWARD, language.toString()});

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, lexicalEntryUtil, binding, verbTokens, auxilaries);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String sentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            //System.out.println("sentenceTemplate::"+sentenceTemplate);
            //System.out.println("sentence::"+sentence);
            sentences.add(sentence);
        }

        return new ArrayList<String>(sentences);
    }


    public List<String> backWardSentencesWhen(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        String qWord, binding;
        Integer index = 0;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        String preposition = this.lexicalEntryUtil.getPreposition();
        Map<String, String> auxilaries = this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);
        // opposite select variable
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        // get subjectType of this sentence's object
        SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(oppositeSelectVariable, domainOrRangeType);
        qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);

        binding = this.getBindingString();

        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        Map<String, String> verbTokens = this.getVerbTokens(annotatedVerbs, lexicalEntryUtil,oppositeSelectVariable);
        
        //System.out.println("verbTokens::"+verbTokens);

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), WHEN_WHAT_PAST_THING, BACKWARD, language.toString()});

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, lexicalEntryUtil, binding, verbTokens, auxilaries);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String sentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            sentences.add(sentence);
            //System.out.println("sentenceTemplate::"+sentenceTemplate);
            //System.out.println("sentence::"+sentence);
        }
        
        return new ArrayList<String>(sentences);
    }

    /*public List<String> whatWhoNP(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
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
    }*/

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

    private Map<String, String> getVerbTokens(List<AnnotatedVerb> annotatedVerbs,LexicalEntryUtil lexicalEntryUtil,SelectVariable selectVariable) throws QueGGMissingFactoryClassException {
         Map<String, String> verbTokens=new TreeMap<String, String>();
         
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            String determiner = lexicalEntryUtil.getSubjectBySubjectType(
                    SubjectType.INTERROGATIVE_DETERMINER,
                    language,
                    null
            );
            String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
            String number = annotatedVerb.getNumber().toString();
            if (annotatedVerb.getNumber().toString().contains("#")) {
                number = number.split("#")[1];
                verbTokens.put(number, determinerToken);
            }

        }
        return verbTokens;
    }

    private Map<String, String> getDeterMinerTokens(SelectVariable selectVariable) throws QueGGMissingFactoryClassException {
        Map<String, String> determinerTokens=new TreeMap<String,String>();
         for (PropertyValue number : numberList) {
            String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(selectVariable);
            String determiner = this.lexicalEntryUtil.getSubjectBySubjectType(
                    SubjectType.INTERROGATIVE_DETERMINER,
                    language,
                    null
            );
            String determinerToken = getDeterminerTokenByNumber(number, conditionLabel, determiner);
            determinerTokens.put(number.toString(), determinerToken);

        }
         return determinerTokens;
    }
}
