package grammar.generator.helper.parser;

import grammar.generator.SentenceTemplateParser;
import grammar.generator.SentenceToken;
import grammar.datasets.sentencetemplates.SentenceTemplateFactory;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.structure.component.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SentenceTemplateParserTest {

  SentenceTemplateParser sentenceTemplateParser;
  private final static Language LANGUAGE = Language.EN;
  // This sentence template is for testing purposes only. It is no grammatically correct sentence template.
  private final static String SENTENCE_TEMPLATE =
    "interrogativeDeterminer noun(condition:copulativeSubject) verb(reference:component_be) AP(prepositionalAdjunct) NP(attributiveArg)?";

  @BeforeEach
  void setUp() throws QueGGMissingFactoryClassException {
    SentenceTemplateRepository sentenceTemplateRepository = new SentenceTemplateFactory(LANGUAGE).init();
    sentenceTemplateParser = new SentenceTemplateParser(
      LANGUAGE,
      sentenceTemplateRepository
    );
  }

  @Test
  void testParseAndResolveNestedSentenceTemplates() {
    List<List<SentenceToken>> outerSentenceTokenList =
      sentenceTemplateParser.parseAndResolveNestedSentenceTemplates(SENTENCE_TEMPLATE);
    assertEquals(4, outerSentenceTokenList.size());
    assertEquals(4L, outerSentenceTokenList.stream().distinct().count());
    assertEquals(2L, outerSentenceTokenList.stream().filter(sentenceTokens -> sentenceTokens.size() == 8).count());
    assertEquals(2L, outerSentenceTokenList.stream().filter(sentenceTokens -> sentenceTokens.size() == 9).count());
  }

  @Test
  void testParseAndResolveMultipleNestedSentenceTemplates() {
    List<String> list = List.of(
      "interrogativeDeterminer noun(condition:copulativeSubject) verb(reference:component_be) AP(prepositionalAdjunct)?",
      "interrogativePronoun verb(reference:component_be) AP(prepositionalAdjunct)?"
    );
    List<List<SentenceToken>> outerSentenceTokenList =
      sentenceTemplateParser.parseAndResolveMultipleNestedSentenceTemplates(list);
    assertEquals(4, outerSentenceTokenList.size());
    assertEquals(4L, outerSentenceTokenList.stream().distinct().count());
    assertEquals(2L, outerSentenceTokenList.stream().filter(sentenceTokens -> sentenceTokens.size() == 5).count());
    assertEquals(2L, outerSentenceTokenList.stream().filter(sentenceTokens -> sentenceTokens.size() == 6).count());
  }


  void printParsedSentenceTokens() {
    List<List<SentenceToken>> outerSentenceTokenList =
      sentenceTemplateParser.parseAndResolveNestedSentenceTemplates(SENTENCE_TEMPLATE);
    for (List<SentenceToken> sentenceTokens : outerSentenceTokenList) {
      System.out.println("-".repeat(50));
      for (SentenceToken sentenceToken : sentenceTokens) {
        System.out.println(String.format("%25s %s", "getPosition"              , sentenceToken.getPosition()));
        System.out.println(String.format("%25s %s", "getLocalReference"        , sentenceToken.getLocalReference()));
        System.out.println(String.format("%25s %s", "getPartOfSpeechValue"     , sentenceToken.getPartOfSpeechValue()));
        System.out.println(String.format("%25s %s", "getPropertyMap"           , sentenceToken.getPropertyMap().toString()));
        System.out.println(String.format("%25s %s", "isConditionLabel"         , sentenceToken.isConditionLabel()));
        System.out.println(String.format("%25s %s", "getSynArgForCondition"    , sentenceToken.getSynArgForCondition()));
        System.out.println(String.format("%25s %s", "isNestedSentenceTemplate" , sentenceToken.isNestedSentenceTemplate()));
        System.out.println(String.format("%25s %s", "getSynArgValue"           , sentenceToken.getSynArgValue()));
        if (sentenceToken.isNestedSentenceTemplate()) {
          System.out.println(String.format("%25s %s", "getNestedSentenceTemplateData getType"      , sentenceToken.getNestedSentenceTemplateData().getType()));
          System.out.println(String.format("%25s %s", "getNestedSentenceTemplateData getArguments" , Arrays.toString(sentenceToken.getNestedSentenceTemplateData().getArguments())));
        }
        System.out.println("-".repeat(30));
      }
    }
  }
}
