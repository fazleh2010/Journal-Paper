/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.datasets.questionword;

import grammar.generator.SubjectType;
import grammar.datasets.annotated.AnnotatedInterrogativeDeterminer;
import grammar.datasets.annotated.AnnotatedInterrogativePronoun;
import grammar.structure.component.Language;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class QuestionWordFactoryIT {

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

    QuestionWordFactoryIT() {
        this.language = Language.IT;
        this.questionWordRepository = new QuestionWordDataset();
    }

    public QuestionWordRepository init() {
        this.initByLanguage(language);
        return questionWordRepository;
    }

    private void initByLanguage(Language language) {
        initBN(language);
    }

    private void initBN(Language language) {
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.PERSON_INTERROGATIVE_PRONOUN,
                        new AnnotatedInterrogativePronoun("Chi", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.THING_INTERROGATIVE_PRONOUN,
                        new AnnotatedInterrogativePronoun("Qual", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_DETERMINER_SINGULAR,
                        new AnnotatedInterrogativeDeterminer("Quale", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_DETERMINER_PLURAL,
                        new AnnotatedInterrogativeDeterminer("Quali", "plural", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_TEMPORAL,
                        new AnnotatedInterrogativeDeterminer("Quando", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.INTERROGATIVE_PLACE,
                        new AnnotatedInterrogativeDeterminer("Dove", "singular", "commonGender", language)
                )
        );
    }
}
