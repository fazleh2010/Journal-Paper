package grammar.generator.helper.datasets.questionword;

import grammar.generator.helper.SubjectType;
import grammar.generator.helper.datasets.Factory;
import grammar.generator.helper.sentencetemplates.AnnotatedInterrogativeDeterminer;
import grammar.generator.helper.sentencetemplates.AnnotatedInterrogativePronoun;
import grammar.structure.component.Language;

class QuestionWordFactoryBN implements Factory<QuestionWordRepository> {

    private final QuestionWordRepository questionWordRepository;
    private final Language language;

    QuestionWordFactoryBN() {
        this.language = Language.BN;
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
                        new AnnotatedInterrogativePronoun("নাম কি", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.THING_INTERROGATIVE_PRONOUN,
                        new AnnotatedInterrogativePronoun("কে", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_DETERMINER,
                        new AnnotatedInterrogativeDeterminer("কোন", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_TEMPORAL,
                        new AnnotatedInterrogativeDeterminer("কখন", "singular", "commonGender", language)
                )
        );
    }
}
