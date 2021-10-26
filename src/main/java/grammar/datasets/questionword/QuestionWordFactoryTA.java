package grammar.datasets.questionword;

import grammar.generator.SubjectType;
import grammar.datasets.Factory;
import grammar.datasets.annotated.AnnotatedInterrogativeDeterminer;
import grammar.datasets.annotated.AnnotatedInterrogativePronoun;
import grammar.structure.component.Language;
import java.util.Set;
import java.util.TreeSet;

//http://ilovelanguages.org/tamil_lesson9.php

class QuestionWordFactoryTA implements Factory<QuestionWordRepository> {

    private final QuestionWordRepository questionWordRepository;
    private final Language language;
    public static Set<SubjectType> questionWords = new TreeSet<SubjectType>();

    static {
        questionWords.add(SubjectType.PERSON_INTERROGATIVE_PRONOUN);
        questionWords.add(SubjectType.THING_INTERROGATIVE_PRONOUN);
        questionWords.add(SubjectType.INTERROGATIVE_DETERMINER_SINGULAR);
        questionWords.add(SubjectType.INTERROGATIVE_DETERMINER_PLURAL);
        questionWords.add(SubjectType.INTERROGATIVE_TEMPORAL);
        questionWords.add(SubjectType.INTERROGATIVE_PLACE);
    }

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
                        new AnnotatedInterrogativePronoun("யார்", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.THING_INTERROGATIVE_PRONOUN,
                        new AnnotatedInterrogativePronoun("எது", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_DETERMINER_SINGULAR,
                        new AnnotatedInterrogativeDeterminer("எந்த", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_DETERMINER_PLURAL,
                        new AnnotatedInterrogativeDeterminer("எந்த", "plural", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_TEMPORAL,
                        new AnnotatedInterrogativeDeterminer("எப்போது", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_PLACE,
                        new AnnotatedInterrogativeDeterminer("எங்கே", "singular", "commonGender", language)
                )
        );
    }
}
