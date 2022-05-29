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
public class QuestionWordFactoryDE {

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
        initBN(language);
    }

    private void initBN(Language language) {
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePronounPerson,
                        new AnnotatedInterrogativePronoun("Wer", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePronounThing,
                        new AnnotatedInterrogativePronoun("Was", "singular", "commonGender", language)
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
                        new AnnotatedInterrogativeDeterminer("Welcher", "singular", "masculine", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerSingular,
                        new AnnotatedInterrogativeDeterminer("Welches", "singular", "neuter", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerSingular,
                        new AnnotatedInterrogativeDeterminer("Welche", "singular", "feminine", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerPlural,
                        new AnnotatedInterrogativeDeterminer("Welche", "plural", "masculine", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeDeterminerPlural,
                        new AnnotatedInterrogativeDeterminer("Welche", "plural", "feminine", language)
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
                        new AnnotatedInterrogativeDeterminer("Wann", "singular", "commonGender", language)
                )
        );
        questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativePlace,
                        new AnnotatedInterrogativeDeterminer("Wo", "singular", "commonGender", language)
                )
        );
        
         questionWordRepository.add(
                new QuestionWord(
                        language,
                        SubjectType.interrogativeAmount,
                        new AnnotatedInterrogativeDeterminer("wieviel", "singular", "commonGender", language)
                )
        );
        
    }

}
