package grammar.datasets.questionword;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.SubjectType;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.structure.component.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class QuestionWordDataset implements QuestionWordRepository {

  private final List<QuestionWord> questionWordDataSet;

  QuestionWordDataset() {
    this.questionWordDataSet = new ArrayList<>();
  }

  @Override
  public List<AnnotatedNounOrQuestionWord> getAll() {
    return this.questionWordDataSet
      .stream()
      .map(QuestionWord::getAnnotatedQuestionWord)
      .collect(Collectors.toList());
  }

  @Override
  public List<AnnotatedNounOrQuestionWord> findByLanguageAndSubjectType(
    Language language, SubjectType subjectType
  ) {
    return this.questionWordDataSet
      .stream()
      .filter(questionWord -> questionWord.getLanguage().equals(language))
      .filter(questionWord -> questionWord.getSubjectType().equals(subjectType))
      .map(QuestionWord::getAnnotatedQuestionWord)
      .collect(Collectors.toList());
  }

  @Override
  public List<AnnotatedNounOrQuestionWord> findByLanguageAndSubjectTypeAndNumberAndGender(
    Language language, SubjectType subjectType, PropertyValue number, PropertyValue gender
  ) {
    return this.questionWordDataSet
      .stream()
      .filter(questionWord -> questionWord.getLanguage().equals(language))
      .filter(questionWord -> questionWord.getSubjectType().equals(subjectType))
      .map(QuestionWord::getAnnotatedQuestionWord)
      .filter(annotatedQuestionWord -> annotatedQuestionWord.getNumber().equals(number))
      .filter(annotatedQuestionWord -> annotatedQuestionWord.getGender().equals(gender))
      .collect(Collectors.toList());
  }

  @Override
  public List<AnnotatedNounOrQuestionWord> findByLanguageAndSubjectTypeAndNumber(
    Language language, SubjectType subjectType, PropertyValue number
  ) {
    return this.questionWordDataSet
      .stream()
      .filter(questionWord -> questionWord.getLanguage().equals(language))
      .filter(questionWord -> questionWord.getSubjectType().equals(subjectType))
      .map(QuestionWord::getAnnotatedQuestionWord)
      .filter(annotatedQuestionWord -> annotatedQuestionWord.getNumber().equals(number))
      .collect(Collectors.toList());
  }

  @Override
  public void add(QuestionWord questionWord) {
    this.questionWordDataSet.add(questionWord);
  }
}
