package grammar.generator.helper.datasets.questionword;

import grammar.datasets.questionword.QuestionWordFactory;
import grammar.datasets.questionword.QuestionWordRepository;
import grammar.structure.component.Language;
import org.junit.jupiter.api.Test;
import util.exceptions.QueGGMissingFactoryClassException;

import static org.junit.jupiter.api.Assertions.assertFalse;

class QuestionWordFactoryTest {

  private static final Language LANGUAGE = Language.EN;

  @Test
  void testInit() throws QueGGMissingFactoryClassException {
    QuestionWordRepository questionWordRepository = new QuestionWordFactory(LANGUAGE).init();
    assertFalse(questionWordRepository.getAll().isEmpty());
  }
}
