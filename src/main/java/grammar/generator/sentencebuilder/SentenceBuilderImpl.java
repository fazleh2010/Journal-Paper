package grammar.generator;

import eu.monnetproject.lemon.model.Property;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.questionword.QuestionWordFactory;
import grammar.datasets.questionword.QuestionWordRepository;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.annotated.AnnotatedNoun;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lombok.SneakyThrows;
import net.lexinfo.LexInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;
import grammar.structure.component.DomainOrRangeMorphologicalProperties;
import grammar.structure.component.DomainOrRangeMorphologicalPropertiesIT;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public abstract class SentenceBuilderImpl implements SentenceBuilder {
  private static final Logger LOG = LogManager.getLogger(LexicalEntryUtil.class);

  private final Language language;
  private final SentenceTemplateRepository sentenceTemplateRepository;
  private final SentenceTemplateParser sentenceTemplateParser;
  private final FrameType frameType;
  private final LexInfo lexInfo;
  // Save some calculation time by keeping a list of parsed sentence tokens by frameArgs and sentenceType
  private final static Map<String, List<List<SentenceToken>>> PREPARSED_LIST_MAP = new HashMap<>();

  @SneakyThrows
  public SentenceBuilderImpl(
    Language language,
    FrameType frameType,
    SentenceTemplateRepository sentenceTemplateRepository,
    SentenceTemplateParser sentenceTemplateParser
  ) {
    this.language = language;
    this.frameType = frameType;
    this.sentenceTemplateRepository = sentenceTemplateRepository;
    this.sentenceTemplateParser = sentenceTemplateParser;
    this.lexInfo = new LexInfo();
  }

  @Override
  public List<String> generateFullSentencesForward(String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws
                                                                                                  QueGGMissingFactoryClassException {
    List<String> generatedSentences = new ArrayList<>();
    List<List<SentenceToken>> parsedSentenceTemplates = getParsedSentenceTemplates(
      SentenceType.SENTENCE,
      getFrameArgsForFrameType()
    );
    for (List<SentenceToken> parsedSentenceTemplate : parsedSentenceTemplates) {
      generatedSentences.addAll(interpretSentenceToken(parsedSentenceTemplate, bindingVar, lexicalEntryUtil));
    }
    generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
    generatedSentences.sort(String::compareToIgnoreCase);
    return generatedSentences;
  }

  protected abstract List<String> interpretSentenceToken(
    List<SentenceToken> sentenceTokens,
    String bindingVar,
    LexicalEntryUtil lexicalEntryUtil
  ) throws
    QueGGMissingFactoryClassException;

  @Override
  public List<String> generateFullSentencesBackward(String bindingVar, String[] arguments, LexicalEntryUtil lexicalEntryUtil) throws
                                                                                                           QueGGMissingFactoryClassException {
    List<String> generatedSentences = new ArrayList<>();
    List<List<SentenceToken>> parsedSentenceTemplates = getParsedSentenceTemplates(
      SentenceType.NP,
      arguments
    );
    for (List<SentenceToken> parsedSentenceTemplate : parsedSentenceTemplates) {
      generatedSentences.addAll(interpretSentenceTokenNP(
        parsedSentenceTemplate,
        bindingVar,
        lexicalEntryUtil
      ).values());
    }
    generatedSentences.sort(String::compareToIgnoreCase);
    //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!generatedSentences::"+generatedSentences);
    return generatedSentences;
  }

  protected abstract Map<PropertyValue, String> interpretSentenceTokenNP(
    List<SentenceToken> parsedSentenceTemplate,
    String bindingVar,
    LexicalEntryUtil lexicalEntryUtil
  ) throws
    QueGGMissingFactoryClassException;

  private List<List<SentenceToken>> getParsedSentenceTemplates(SentenceType sentenceType, String[] arguments) {
    String key = language.name() + sentenceType + Arrays.stream(arguments).reduce((x, y) -> x + y).orElse("");
    List<List<SentenceToken>> list;
    if (PREPARSED_LIST_MAP.containsKey(key)) {
      list = PREPARSED_LIST_MAP.get(key);
    } else {
      List<String> sentenceTemplates =
        sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(
          sentenceType,
          language,
          arguments
        );
      list = sentenceTemplateParser.parseAndResolveMultipleNestedSentenceTemplates(sentenceTemplates);
      PREPARSED_LIST_MAP.put(key, list);
    }
    return list;
  }

  private String[] getFrameArgsForFrameType() {
    return lexInfo.getSynArgsForFrame(lexInfo.getFrameClass(frameType.getName()))
                  .stream()
                  .map(synArg -> synArg.getURI().getFragment()).toArray(String[]::new);
  }


  protected AnnotatedNounOrQuestionWord getAnnotatedQuestionWordBySubjectType(
    SubjectType subjectType,
    Language language,
    AnnotatedNounOrQuestionWord annotatedNounOrQuestionWord
  ) throws QueGGMissingFactoryClassException {
    AnnotatedNounOrQuestionWord sbjType = new AnnotatedNoun("", "singular", language);
    QuestionWordRepository questionWordRepository = new QuestionWordFactory(language).init();
    List<AnnotatedNounOrQuestionWord> questionWords;
    questionWords = questionWordRepository
      .findByLanguageAndSubjectType(language, subjectType);
    if (questionWords.size() != 1) {
      questionWords = questionWordRepository
        .findByLanguageAndSubjectTypeAndNumberAndGender(
          language,
          subjectType,
          lexInfo.getPropertyValue("singular"),
          lexInfo.getPropertyValue("commonGender")
        );
    }
    if (!isNull(annotatedNounOrQuestionWord)) {
      if (questionWords.size() != 1) {
        questionWords = questionWordRepository
          .findByLanguageAndSubjectTypeAndNumber(
            language,
            subjectType,
            annotatedNounOrQuestionWord.getNumber()
          );
      }
      if (questionWords.size() != 1) {
        questionWords = questionWordRepository
          .findByLanguageAndSubjectTypeAndNumberAndGender(
            language,
            subjectType,
            annotatedNounOrQuestionWord.getNumber(),
            annotatedNounOrQuestionWord.getGender()
          );
      }
    }
    if (questionWords.size() != 1) {
      LOG.error("Cannot find a matching subject in QuestionWordFactory({})", language);
    } else {
      sbjType = questionWords.get(0);
    }
    return sbjType;
  }
  
  protected AnnotatedNounOrQuestionWord getAnnotatedQuestionWordBySubjectTypeAndNumber(
    SubjectType subjectType,
    Language language,
    LexicalEntryUtil lexicalEntryUtil,
    PropertyValue number,
    AnnotatedNounOrQuestionWord annotatedNounOrQuestionWord
  ) throws QueGGMissingFactoryClassException {
    AnnotatedNounOrQuestionWord sbjType = new AnnotatedNoun("", "singular", language);
    QuestionWordRepository questionWordRepository = new QuestionWordFactory(language).init();
    List<AnnotatedNounOrQuestionWord> questionWords;
    questionWords = questionWordRepository
      .findByLanguageAndSubjectType(language, subjectType);
    if (questionWords.size() != 1 && language.equals(Language.DE)) {
      questionWords = questionWordRepository
        .findByLanguageAndSubjectTypeAndNumberAndGender(
          language,
          subjectType,
          number,
          lexInfo.getPropertyValue(DomainOrRangeMorphologicalProperties.getMatchingGender(lexicalEntryUtil.getConditionUriBySelectVariable(lexicalEntryUtil.getSelectVariable())).toString().toLowerCase())
        );
    }
    if (questionWords.size() != 1 && language.equals(Language.IT)) {
      questionWords = questionWordRepository
        .findByLanguageAndSubjectTypeAndNumberAndGender(
          language,
          subjectType,
          number,
          lexInfo.getPropertyValue(DomainOrRangeMorphologicalPropertiesIT.getMatchingGender(lexicalEntryUtil.getConditionUriBySelectVariable(lexicalEntryUtil.getSelectVariable())).toString().toLowerCase())
        );
    }
    if (questionWords.size() != 1) {
      questionWords = questionWordRepository
        .findByLanguageAndSubjectTypeAndNumberAndGender(
          language,
          subjectType,
          number,
          lexInfo.getPropertyValue("commonGender")
        );
    }
    if (!isNull(annotatedNounOrQuestionWord)) {
      if (questionWords.size() != 1) {
        questionWords = questionWordRepository
          .findByLanguageAndSubjectTypeAndNumber(
            language,
            subjectType,
            annotatedNounOrQuestionWord.getNumber()
          );
      }
      if (questionWords.size() != 1) {
        questionWords = questionWordRepository
          .findByLanguageAndSubjectTypeAndNumberAndGender(
            language,
            subjectType,
            annotatedNounOrQuestionWord.getNumber(),
            annotatedNounOrQuestionWord.getGender()
          );
      }
    }
    if (questionWords.size() != 1) {
      LOG.error("Cannot find a matching subject in QuestionWordFactory({})", language);
    } else {
      sbjType = questionWords.get(0);
    }
    return sbjType;
  }

  protected String buildSentence(List<String> sentenceTokens) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String sentenceToken : sentenceTokens) {
      if (!(isNull(sentenceToken) || sentenceToken.isBlank())) {
        stringBuilder.append(sentenceToken)
                     .append(" ");
      }
    }
    return stringBuilder.toString().trim();
  }

  protected PropertyValue getNumber(Map<Property, PropertyValue> propertyValueMap) {
    return propertyValueMap.getOrDefault(
      lexInfo.getProperty("number"),
      lexInfo.getPropertyValue("singular")
    );
  }

  protected PropertyValue getGender(Map<Property, PropertyValue> propertyValueMap) {
    return propertyValueMap.getOrDefault(
      lexInfo.getProperty("gender"),
      lexInfo.getPropertyValue("commonGender")
    );
  }

  public Language getLanguage() {
    return language;
  }

  protected SentenceTemplateRepository getSentenceTemplateRepository() {
    return sentenceTemplateRepository;
  }

  protected SentenceTemplateParser getSentenceTemplateParser() {
    return sentenceTemplateParser;
  }

  protected FrameType getFrameType() {
    return frameType;
  }

  protected LexInfo getLexInfo() {
    return lexInfo;
  }
}
