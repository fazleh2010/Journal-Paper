package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
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
    initDE(language);
  }

  private void initDE(Language language) {

    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
          //"Wer ist der Bürgermeister von Tel Aviv?",
          /*"interrogativePronoun(range:singular) verb(component_be:present:singular) nounPhrase?",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) nounPhrase?",
          "interrogativePronoun(range:singular) verb(component_be:present:plural) nounPhrase?",
          "interrogativePronoun(range:singular) verb(component_be:past:plural) nounPhrase?",
          "interrogativeDeterminer(range:singular) verb(component_be:present:singular) nounPhrase?",
          "interrogativeDeterminer(range:singular) verb(component_be:past:singular) nounPhrase?",
          "interrogativeDeterminer(range:singular) verb(component_be:present:plural) nounPhrase?",
          "interrogativeDeterminer(range:singular) verb(component_be:past:plural) nounPhrase?",*/
          //"Liste die Kinder von Margaret Thatcher auf.",
          //"verb(imperative_plural) nounPhrase preposition(auf)."
           "verb(component_be:present:singular) subject(range) nounPhrase?"
          
          //In welchem Land ist der Mount Everest?
          //"preposition interrogativeDeterminer(range:singular) verb(component_be:present:singular) noun?"
          //Wieviel hat Pulp Fiction gekostet?
          
          //Welche Person ist das Mitglied von...?
          //"interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
          //Wer ist das Mitglied von...?
          //"interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          //Gib mir das Mitglied von...?
          //"verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
          ),
        TemplateVariable.NounPPFrame,
        TemplateVariable.sentence
      )
    );
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "determiner(component_the_nominative:reference) noun(nominativeCase) preposition adjunct(domain)"
          //Welche Person ist das Mitglied von...?
          //"interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
          //Wer ist das Mitglied von...?
          //"interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          //Gib mir das Mitglied von...?
          //"verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
          ),
        TemplateVariable.NounPPFrame,
        TemplateVariable.nounPhrase
      )
    );
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "determiner(component_the_nominative:reference) noun(nominativeCase)"
          //Welche Person ist das Mitglied von...?
          //"interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
          //Wer ist das Mitglied von...?
          //"interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          //Gib mir das Mitglied von...?
          //"verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
          ),
        TemplateVariable.NounPPFrame,
        TemplateVariable.noun
      )
    );
   /* // NP(prepositionalAdjunct)
    sentenceTemplateRepository.add(
      createNPTemplate(
        language,
        List.of(
          //das Mitglied von...
          "determiner(component_the_nominative) noun(nominativeCase) preposition prepositionalAdjunct"
        ),
        TemplateEntities.NOUN_PP_FRAME,
        TemplateEntities.NounPhrase
      )
    );*/
    /* Not working yet
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
    );*/
    // AdjectivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //Welches Kunstwerk wird von ... ausgestellt
          "interrogativeDeterminer noun(condition:copulativeSubject) verb(reference:component_be_passive) AP(prepositionalAdjunct)?",
          //Was wird von ... ausgestellt
          "interrogativePronoun verb(reference:component_be_passive) AP(prepositionalAdjunct)?"
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
          //Kunstwerke ausgestellt von...
          "noun(condition:copulativeSubject,number:plural) verb(root,verbFormMood:participle) preposition prepositionalAdjunct"
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
          //von ... ausgestellt
          "preposition prepositionalAdjunct verb(root,verbFormMood:participle)"
        ),
        "prepositionalAdjunct"
      )
    );
    /* Not working yet
    // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //Welcher Fluss fließt durch...?
          "interrogativeDeterminer noun(condition:subject) VP(prepositionalAdjunct)?",
          //Was fließt durch...?
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
          //fließt durch...
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
    );*/
    // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //Welche Person entwickelte...?
          "interrogativeDeterminer noun(condition:subject) VP(directObject)?",
          //Wer entwickelte...?
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
          //entwickelte...
          "verb(root) directObject"
        ),
        "directObject"
      )
    );
  }
}