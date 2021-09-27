/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator.helper.datasets.questionword;

import grammar.generator.sentence.SubjectType;
import grammar.generator.helper.sentencetemplates.AnnotatedInterrogativeDeterminer;
import grammar.generator.helper.sentencetemplates.AnnotatedInterrogativePronoun;
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
