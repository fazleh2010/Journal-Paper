package grammar.datasets.questionword;

import grammar.generator.SubjectType;
import grammar.datasets.Factory;
import grammar.datasets.annotated.AnnotatedInterrogativeDeterminer;
import grammar.datasets.annotated.AnnotatedInterrogativePronoun;
import grammar.structure.component.Language;

class QuestionWordFactoryEN implements Factory<QuestionWordRepository> {

  private final QuestionWordRepository questionWordRepository;
  private final Language language;

  QuestionWordFactoryEN() {
    this.language = Language.EN;
    this.questionWordRepository = new QuestionWordDataset();
  }

  public QuestionWordRepository init() {
    this.initByLanguage(language);
    return questionWordRepository;
  }

  private void initByLanguage(Language language) {
    initEN(language);
  }

  private void initEN(Language language) {
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.PERSON_INTERROGATIVE_PRONOUN,
        new AnnotatedInterrogativePronoun("Who", "singular", "commonGender", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.THING_INTERROGATIVE_PRONOUN,
        new AnnotatedInterrogativePronoun("What", "singular", "commonGender", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_DETERMINER,
        new AnnotatedInterrogativeDeterminer("Which", "singular", "commonGender", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_TEMPORAL,
        new AnnotatedInterrogativeDeterminer("When", "singular", "commonGender", language)
      )
    );
  }
}
