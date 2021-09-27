package grammar.generator.helper.datasets.questionword;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.sentence.SubjectType;
import grammar.generator.helper.sentencetemplates.AnnotatedNounOrQuestionWord;
import grammar.structure.component.Language;

import java.util.List;

public interface QuestionWordRepository {
  List<AnnotatedNounOrQuestionWord> getAll();

  List<AnnotatedNounOrQuestionWord> findByLanguageAndSubjectType(Language language, SubjectType subjectType);

  List<AnnotatedNounOrQuestionWord> findByLanguageAndSubjectTypeAndNumberAndGender(
    Language language,
    SubjectType subjectType,
    PropertyValue number,
    PropertyValue gender
  );

  List<AnnotatedNounOrQuestionWord> findByLanguageAndSubjectTypeAndNumber(
    Language language,
    SubjectType subjectType,
    PropertyValue number
  );

  void add(QuestionWord questionWord);
}
