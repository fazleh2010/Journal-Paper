package grammar.generator.helper.datasets.sentencetemplates;

import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.sentencetemplates.SentenceTemplateFactory;
import grammar.structure.component.Language;
import org.junit.jupiter.api.Test;
import util.exceptions.QueGGMissingFactoryClassException;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SentenceTemplateFactoryTest {

  private static final Language LANGUAGE = Language.EN;

  @Test
  void testInit() throws QueGGMissingFactoryClassException {
    SentenceTemplateRepository questionWordRepository = new SentenceTemplateFactory(LANGUAGE).init();
    assertFalse(questionWordRepository.getAll().isEmpty());
  }
}
