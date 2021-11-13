package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import util.io.TemplateConstants;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TemplateVariable;
import static grammar.datasets.sentencetemplates.TemplateVariable.variableIndicator;
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
import util.io.FindQuestionWord;
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
        this.templateFinder = new TemplateFinder(lexicalEntryUtil, frameType);
        this.frameType = frameType;
        this.sentenceTemplateParser = sentenceTemplateParser;
        this.sentenceTemplateRepository = sentenceTemplateRepository;
    }

    @Override
    public List<String> generateFullSentencesForward(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();

        if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
            DomainOrRangeType domainOrRangeType = this.templateFinder.getForwardDomainOrRange();
            generatedSentences = forwardSentences(bindingVariable, lexicalEntryUtil, domainOrRangeType, template);
        } else if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil);
        }
        return generatedSentences;
    }

    @Override
    public List<String> generateFullSentencesBackward(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
            DomainOrRangeType domainOrRangeType = this.templateFinder.getOppositeDomainOrRange();
            generatedSentences = backwordSentences(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType, template);
        } else if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPhrase(bindingVariable, lexicalEntryUtil);
        }

        return generatedSentences;
    }

    private List<String> forwardSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType, String type) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        /*String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );*/

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), type, FORWARD, language.toString()});
        //System.out.println("sentenceTemplates::" + type);

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            //System.out.println("positionTokens::" + positionTokens);
            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            //System.out.println("sentence::" + sentence);
            sentences.add(newSentence);
        }

        return new ArrayList<String>(sentences);
    }

    public List<String> backwordSentences(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType, String type) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        /*String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );*/

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), type, FORWARD, language.toString()});
        //System.out.println("sentenceTemplates::" + type);

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            //System.out.println("sentences:::" + sentences);
            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            sentences.add(newSentence);
            //System.out.println("sentences:::" + sentences);
        }

        return new ArrayList<String>(sentences);
    }

    private List<String> nounPPframeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), TemplateVariable.sentence});

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String str = "", positionWord = "";
            for (String positionString : positionTokens) {
                String npCategory = findNounPhraseCategory(positionString);
                if (npCategory.isEmpty()) {
                    String[] parseToken = sentenceBuilderFromTemplates.parseToken(positionString);
                    positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index);
                } else if (npCategory.equals(TemplateVariable.nounPhrase)) {
                    List<String> nps = nounPhrase(bindingVariable, lexicalEntryUtil);
                    positionWord = nps.iterator().next();
                } else if (npCategory.equals(TemplateVariable.noun)) {
                    positionWord = noun(bindingVariable, lexicalEntryUtil);
                }

                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
            }
            String newsSentence = str.stripTrailing();

            //String newsSentences = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            sentences.add(newsSentence);
            System.out.println("sentences:::" + sentences);
        }
        exit(1);
        return new ArrayList<String>(sentences);

    }

    private List<String> nounPhrase(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;

        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        /*String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );*/
        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), TemplateVariable.nounPhrase});
        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String str = "", positionWord = "";
            for (String positionString : positionTokens) {
                String[] parseToken = sentenceBuilderFromTemplates.parseToken(positionString);
                positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index);
                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
                System.out.println("positionString:::" + positionString + " positionWord:" + positionWord);
            }
            String newsSentence = str.stripTrailing();

            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            sentences.add(newSentence);
        }

        return new ArrayList<String>(sentences);
    }

    private String noun(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;

        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );
        return variable;

    }

    private String findNounPhraseCategory(String stringPosition) {
        String result = "";
        List<String> tokens = SentenceBuilderUtils.parseTemplate(stringPosition);
        for (String token : tokens) {
            token = token.replace("?", "");
            token = token.replace(".", "");
            if (token.equals(TemplateVariable.noun)) {
                return token;
            } else if (token.equals(TemplateVariable.nounPhrase)) {
                return token;
            }
        }
        return result;
    }

}
