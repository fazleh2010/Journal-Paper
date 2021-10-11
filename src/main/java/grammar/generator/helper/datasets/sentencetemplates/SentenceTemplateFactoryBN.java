package grammar.generator.helper.datasets.sentencetemplates;

import grammar.generator.helper.datasets.Factory;
import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import grammar.structure.component.Language;
import java.util.List;

class SentenceTemplateFactoryBN implements Factory<SentenceTemplateRepository> {

    private final SentenceTemplateRepository sentenceTemplateRepository;
    private final Language language;

    SentenceTemplateFactoryBN() {
        this.language = Language.BN;
        this.sentenceTemplateRepository = new SentenceTemplateDataset();
    }

    public SentenceTemplateRepository init() {
        this.initByLanguage(language);
        return sentenceTemplateRepository;
    }

    private void initByLanguage(Language language) {
        init(language);
    }

    private void init(Language language) {
    // NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "prepositionalAdjunct preposition noun(root) determiner(reference:component_the_2) verb(reference:component_imperative_transitive) .",
          "prepositionalAdjunct preposition noun(root) determiner(reference:component_the_2) verb(reference:component_interrogativeDeterminer_ki) ?"
          //"prepositionalAdjunct preposition noun(root) determiner(reference:component_the_2) verb(reference:component_interrogativeDeterminer_kothay) ?"
        ),
        "copulativeArg",
        "prepositionalAdjunct"
      )
    );
    // NP(prepositionalAdjunct)
    sentenceTemplateRepository.add(
      createNPTemplate(
        language,
        List.of(
          "determiner(reference:component_the) noun(root) preposition prepositionalAdjunct"
        ),
        "prepositionalAdjunct"
      )
    );
    // NP(attributiveArg)
    sentenceTemplateRepository.add(
      createNPTemplate(
        language,
        List.of(
          "determiner adjective(root) attributiveArg(number:singular)",
          "adjective(root) attributiveArg(number:plural)"
        ),
        "attributiveArg"
      )
    );
    // AdjectiveAttributiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "interrogativePronoun verb(reference:component_be) NP(attributiveArg)?"
        ),
        "attributiveArg"
      )
    );
    // AdjectivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "interrogativeDeterminer noun(condition:copulativeSubject) verb(reference:component_be) AP(prepositionalAdjunct)?",
          "interrogativePronoun verb(reference:component_be) AP(prepositionalAdjunct)?"
        ),
        "copulativeSubject",
        "prepositionalAdjunct"
      )
    );
    // AdjectivePPFrame NP
    sentenceTemplateRepository.add(
      createNPTemplate(
        language,
        List.of(
          "noun(condition:copulativeSubject,number:plural) AP(prepositionalAdjunct)"
        ),
        "copulativeSubject",
        "prepositionalAdjunct"
      )
    );
    // AP(prepositionalAdjunct)
    sentenceTemplateRepository.add(
      createAPTemplate(
        language,
        List.of(
          "adjective(root) preposition prepositionalAdjunct",
          "verb(root,verbFormMood:participle) preposition prepositionalAdjunct"
        ),
        "prepositionalAdjunct"
      )
    );
    // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "interrogativeDeterminer noun(condition:subject) VP(prepositionalAdjunct)?",
          "interrogativePronoun VP(prepositionalAdjunct)?"
        ),
        "subject",
        "prepositionalAdjunct"
      )
    );
    // VP(prepositionalAdjunct)
    sentenceTemplateRepository.add(
      createVPTemplate(
        language,
        List.of(
          "verb(root) preposition prepositionalAdjunct"
        ),
        "prepositionalAdjunct"
      )
    );
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "TemporalDeterminer noun(condition:subject) VP(temporalAdjunct)?"
        ),
        "subject",
        "temporalAdjunct"
      )
    );
    // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "interrogativeDeterminer noun(condition:subject) VP(directObject)?",
          "interrogativePronoun VP(directObject)?"
        ),
        "subject",
        "directObject"
      )
    );
    // VP(directObject)
    sentenceTemplateRepository.add(
      createVPTemplate(
        language,
        List.of(
          "verb(root) directObject"
        ),
        "directObject"
      )
    );
  }
}
