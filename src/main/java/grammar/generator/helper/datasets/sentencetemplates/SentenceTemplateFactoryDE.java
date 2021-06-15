package grammar.generator.helper.datasets.sentencetemplates;

import grammar.generator.helper.datasets.Factory;
import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.generator.helper.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import grammar.structure.component.Language;
import java.util.List;

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
        initBN(language);
    }

    private void initBN(Language language) {
         // NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         "interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
         "verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
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
           "determiner(reference:component_the_nominative) noun(root:nominativeCase) preposition prepositionalAdjunct"
        ),
        "prepositionalAdjunct"
      )
    );
    }

}
