package grammar.datasets.questionword;

import grammar.generator.SubjectType;
import grammar.datasets.Factory;
import grammar.datasets.annotated.AnnotatedInterrogativeDeterminer;
import grammar.datasets.annotated.AnnotatedInterrogativePronoun;
import grammar.structure.component.Language;

class QuestionWordFactoryTA implements Factory<QuestionWordRepository> {

  private final QuestionWordRepository questionWordRepository;
  private final Language language;

  QuestionWordFactoryTA() {
    this.language = Language.TA;
    this.questionWordRepository = new QuestionWordDataset();
  }

  public QuestionWordRepository init() {
    this.initByLanguage(language);
    return questionWordRepository;
  }

  private void initByLanguage(Language language) {
    initTA(language);
  }

  private void initTA(Language language) {
    questionWordRepository.add(
      new QuestionWord(
        language,
        SubjectType.PERSON_INTERROGATIVE_PRONOUN,
        new AnnotatedInterrogativePronoun("নাম কি", "singular", "commonGender", language)
      )
    );
  }
}
