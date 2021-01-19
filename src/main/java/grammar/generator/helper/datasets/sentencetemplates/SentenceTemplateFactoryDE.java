package grammar.generator.helper.datasets.sentencetemplates;

import grammar.generator.helper.datasets.Factory;
import grammar.structure.component.Language;

import java.util.List;

import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;

class SentenceTemplateFactoryDE implements Factory<SentenceTemplateRepository> {

  private final SentenceTemplateRepository sentenceTemplateRepository;
  private final Language language;

  SentenceTemplateFactoryDE() {
    this.language = Language.DE;
    this.sentenceTemplateRepository = new SentenceTemplateDataset();
  }

  public SentenceTemplateRepository init() {
    this.initByLanguage(language);
    return sentenceTemplateRepository;
  }

  private void initByLanguage(Language language) {
    initDE(language);
  }

  private void initDE(Language language) {
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?"
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
  }
}
