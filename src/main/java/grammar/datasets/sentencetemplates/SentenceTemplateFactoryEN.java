package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import grammar.structure.component.Language;

import java.util.List;

import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;

class SentenceTemplateFactoryEN implements Factory<SentenceTemplateRepository> {

  private final SentenceTemplateRepository sentenceTemplateRepository;
  private final Language language;

  SentenceTemplateFactoryEN() {
    this.language = Language.EN;
    this.sentenceTemplateRepository = new SentenceTemplateDataset();
  }

  public SentenceTemplateRepository init() {
    this.initByLanguage(language);
    return sentenceTemplateRepository;
  }

  private void initByLanguage(Language language) {
    initEN(language);
  }

  private void initEN(Language language) {
    // NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?",
          "interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          "verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the) noun(root) preposition prepositionalAdjunct",
          "verb(reference:component_imperative_plural) determiner(reference:component_the) noun(root) preposition prepositionalAdjunct"
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
