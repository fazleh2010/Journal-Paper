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
          "prepositionalAdjunct preposition noun(root) determiner(reference:component_the_2) verb(reference:component_interrogativeDeterminer_ki) ?",
          "prepositionalAdjunct preposition noun(root) determiner(reference:component_the_2) verb(reference:component_interrogativeDeterminer_kothay) ?"
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
          "prepositionalAdjunct preposition noun(root)"
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
  }

}
