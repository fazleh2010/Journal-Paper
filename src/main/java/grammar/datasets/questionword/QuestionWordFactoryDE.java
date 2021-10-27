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
        SubjectType.THING_INTERROGATIVE_PRONOUN,
        new AnnotatedInterrogativePronoun("Was", "singular", "commonGender", language)
      )
    );
  }
}
