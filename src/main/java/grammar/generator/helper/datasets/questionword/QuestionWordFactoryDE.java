package grammar.generator.helper.datasets.questionword;

import grammar.generator.helper.SubjectType;
import grammar.generator.helper.datasets.Factory;
import grammar.generator.helper.sentencetemplates.AnnotatedInterrogativeDeterminer;
import grammar.generator.helper.sentencetemplates.AnnotatedInterrogativePronoun;
import grammar.structure.component.Language;

class QuestionWordFactoryDE implements Factory<QuestionWordRepository> {

  private final QuestionWordRepository questionWordRepository;
  private final Language language;

  QuestionWordFactoryDE() {
    this.language = Language.DE;
    this.questionWordRepository = new QuestionWordDataset();
  }

  public QuestionWordRepository init() {
    this.initByLanguage(language);
    return questionWordRepository;
  }

  private void initByLanguage(Language language) {
    initDE(language);
  }

  private void initDE(Language language) {
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.PERSON_INTERROGATIVE_PRONOUN,
        new AnnotatedInterrogativePronoun("Wer", "singular", "commonGender", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.THING_INTERROGATIVE_PRONOUN,
        new AnnotatedInterrogativePronoun("Was", "singular", "commonGender", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_DETERMINER,
        new AnnotatedInterrogativeDeterminer("Welcher", "singular", "masculine", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_DETERMINER,
        new AnnotatedInterrogativeDeterminer("Welche", "singular", "feminine", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_DETERMINER,
        new AnnotatedInterrogativeDeterminer("Welches", "singular", "neuter", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_DETERMINER,
        new AnnotatedInterrogativeDeterminer("Welche", "plural", "masculine", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_DETERMINER,
        new AnnotatedInterrogativeDeterminer("Welche", "plural", "feminine", language)
      )
    );
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.INTERROGATIVE_DETERMINER,
        new AnnotatedInterrogativeDeterminer("Welche", "plural", "neuter", language)
      )
    );
  }
}
