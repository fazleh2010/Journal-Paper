package grammar.generator;

import grammar.datasets.annotated.AnnotatedVerb;
import grammar.structure.component.Language;
import net.lexinfo.LexInfo;
import org.junit.jupiter.api.Test;

import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GrammarRuleGeneratorRootTest {

  private final AnnotatedVerb annotatedVerb;

  GrammarRuleGeneratorRootTest() {
    GrammarRuleGeneratorRoot generatorRoot = new GrammarRuleGeneratorRootImpl(Language.EN);
    this.annotatedVerb =
      new AnnotatedVerb(
        "are",
        generatorRoot.getLanguage(),
        new LexInfo().getPropertyValue("plural"),
        new LexInfo().getPropertyValue("thirdPerson"),
        new LexInfo().getPropertyValue("present")
      );
  }

  @Test
  void testGetDeterminerTokenByToBeVerbNumber_withPluralAndEnding_Y() {
    String result = getDeterminerTokenByNumber(annotatedVerb.getNumber(), "city", "Which");
    assertEquals("Which cities", result);
  }

  @Test
  void testGetDeterminerTokenByToBeVerbNumber_withPluralAndEnding_S() {
    String result = getDeterminerTokenByNumber(annotatedVerb.getNumber(), "boss", "Which");
    assertEquals("Which bosses", result);
  }
}
