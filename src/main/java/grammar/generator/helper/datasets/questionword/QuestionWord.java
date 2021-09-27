package grammar.generator.helper.datasets.questionword;

import grammar.generator.sentence.SubjectType;
import grammar.generator.helper.sentencetemplates.AnnotatedNounOrQuestionWord;
import grammar.structure.component.Language;
import lombok.Getter;

@Getter
class QuestionWord {
  private final AnnotatedNounOrQuestionWord annotatedQuestionWord;
  private final Language language;
  private final SubjectType subjectType;

  QuestionWord(
    Language language,
    SubjectType subjectType,
    AnnotatedNounOrQuestionWord annotatedQuestionWord
  ) {
    this.annotatedQuestionWord = annotatedQuestionWord;
    this.language = language;
    this.subjectType = subjectType;
  }
}
