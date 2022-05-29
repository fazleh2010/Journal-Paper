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
        questionWords.add(SubjectType.interrogativePronoun);
        questionWords.add(SubjectType.interrogativeDeterminer);
        questionWords.add(SubjectType.interrogativePronounPerson);
        questionWords.add(SubjectType.interrogativePronounThing);
        questionWords.add(SubjectType.interrogativePronounThingWhat);
        questionWords.add(SubjectType.interrogativeDeterminerSingular);
        questionWords.add(SubjectType.interrogativeDeterminerPlural);
        questionWords.add(SubjectType.interrogativeTemporal);
        questionWords.add(SubjectType.interrogativePlace);
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
                        SubjectType.interrogativePronounPerson,
                        new AnnotatedInterrogativePronoun("Chi", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePronounThing,
                        new AnnotatedInterrogativePronoun("Qual", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePronounThingWhat,
                        new AnnotatedInterrogativePronoun("Cosa", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerSingular,
                        new AnnotatedInterrogativeDeterminer("Quale", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerPlural,
                        new AnnotatedInterrogativeDeterminer("Quali", "plural", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeTemporal,
                        new AnnotatedInterrogativeDeterminer("Quando", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePlace,
                        new AnnotatedInterrogativeDeterminer("Dove", "singular", "commonGender", language)
                )
        );
    }
}
