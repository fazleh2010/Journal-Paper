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
        questionWords.add(SubjectType.interrogativePronounPerson);
        questionWords.add(SubjectType.interrogativePronounThing);
        questionWords.add(SubjectType.interrogativeDeterminerSingular);
        questionWords.add(SubjectType.interrogativeDeterminerPlural);
        questionWords.add(SubjectType.interrogativeTemporal);
        questionWords.add(SubjectType.interrogativePlace);
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
                        SubjectType.interrogativePronounPerson,
                        new AnnotatedInterrogativePronoun("யார்", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePronounThing,
                        new AnnotatedInterrogativePronoun("என்ன", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerSingular,
                        new AnnotatedInterrogativeDeterminer("எந்த", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerPlural,
                        new AnnotatedInterrogativeDeterminer("எந்த", "plural", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeTemporal,
                        new AnnotatedInterrogativeDeterminer("எப்போது", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePlace,
                        new AnnotatedInterrogativeDeterminer("எங்கே", "singular", "commonGender", language)
                )
        );
    }
}
