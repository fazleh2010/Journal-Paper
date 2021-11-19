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
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:past:singular) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
          "interrogativePronoun(nominativeCase:range:plural) verb(component_be:present:plural) noun(nominativeCase:plural) preposition adjunct(domain)?",
          "interrogativePronoun(nominativeCase:range:plural) verb(component_be:past:plural) noun(nominativeCase:plural) preposition adjunct(domain)?",
          "interrogativeDeterminer(nominativeCase:range:singular) verb(component_be:present:singular) nounPhrase?",
          "interrogativeDeterminer(nominativeCase:range:singular) verb(component_be:past:singular) nounPhrase?"
          //"interrogativeDeterminer(nominativeCase:range:plural) verb(component_be:present:plural) nounPhrase?",
          //"interrogativeDeterminer(nominativeCase:range:plural) verb(component_be:past:plural) nounPhrase?"
          //Gib mir die Enkel von Elvis Presley.
         //"verb(imperative_transitive) pronoun(object_pronoun) determiner(component_the_accusative:reference:singular) noun(accusativeCase:singular) preposition adjunct(domain).",
         // "verb(imperative_transitive) pronoun(object_pronoun) determiner(component_the_accusative:reference:plural) noun(accusativeCase:plural) preposition adjunct(domain).",
          //Gib mir alle Bandmitglieder von Prodigy
         // "verb(imperative_transitive) pronoun(object_pronoun) determiner(alle) noun(accusativeCase:plural) preposition adjunct(domain).",
          //"In welchem Land ist der Mount Everest?"
           //"preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_be:present:singular) determiner(component_the_nominative:domain:singular) adjunct(domain)?",
           //"interrogativePlace(nominativeCase:singular) verb(component_be:present:singular) determiner(component_the_nominative:domain:singular) object(domain)?"     
                 ),
        TemplateVariable.NounPPFrame,
        TemplateVariable.whQuestion
      )
    );
    
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Ist Rita Wilson die Frau von Tom Hanks?"
          "verb(component_be:present:singular) subject(range) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
          "verb(component_be:present:plural) subject(range) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain)?"
          //Heißt die Frau von Präsident Obama Michelle?"
          //"verb(component_heißen:present:singular) determiner(component_the_nominative:reference:singular) noun(nominativeCase:singular) preposition adjunct(domain) subject(range)?",
          //"verb(component_heißen:present:plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain) subject(range)?"    
          //Ist Proinsulin ein Protein?
          //"verb(component_be:present:singular) subject(range) noun(nominativeCase:singular) article(definite_article:nominativeCase:neuter) object(domain)?",   
          //"Sind Laubfrösche Amphibien?"
          //"verb(component_be:present:plural) subject(domain) noun(nominativeCase:singular) object(range)?",
          //Hat Abraham Lincolns Sterbeort eine Webseite?", 
          //"verb(component_haben:present:singular) object(domain) article(definite_article:nominativeCase:feminine) subject(range)?"
              
                
          //"Hatte Che Guevara Kinder?",
          //"verb(component_haben:past:singular) object(domain) noun(nominativeCase)?"
          //Gibt es ein Videospiel, das Battle Chess heißt?
          // "verb(imperative_transitive) pronoun(object_pronoun_es) article(definite_article:nominativeCase:neuter), noun(nominativeCase), article(component_the_nominative:nominativeCase:neuter) object(domain)"    
           //"Was ist Batmans richtiger Name?",                
           

            // "interrogativePronoun(range:singular)  verb(component_be:present:singular) object(range) noun(nominativeCase)?"    
            //"Welche Regierungsform hat Russland?",
            //"interrogativeDeterminer(range:singular) verb(component_haben:present:singular) object(domain)?"   
            //"Aus welcher Region ist der Melon de Bourgogne?"
            // "preposition(auf) interrogativeDeterminer(range:singular) verb(component_be:present:singular) adjunct(domain)? "
           // Wieviele Seiten hat Krieg und Frieden?
           // "interrogativeAmount(range:singular) noun(nominativeCase:plural) verb(component_haben:present:singular) object(domain)?"        
                 ),
        TemplateVariable.NounPPFrame,
        TemplateVariable.booleanQuestionDomainRange
      )
    );
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Hatte Che Guevara Kinder?",
          "verb(component_haben:past:singular) object(domain) noun(nominativeCase)?" 
        
          //Gibt es ein Videospiel, das Battle Chess heißt?
          // "verb(imperative_transitive) pronoun(object_pronoun_es) article(definite_article:nominativeCase:neuter), noun(nominativeCase), article(component_the_nominative:nominativeCase:neuter) object(domain)"    
          //Hat Abraham Lincolns Sterbeort eine Webseite?", 
          //"verb(component_haben:present:singular) object(domain) article(definite_article:nominativeCase:feminine) noun(nominativeCase)?"
           //"Was ist Batmans richtiger Name?",                
           

            // "interrogativePronoun(range:singular)  verb(component_be:present:singular) object(range) noun(nominativeCase)?"    
            //"Welche Regierungsform hat Russland?",
            //"interrogativeDeterminer(range:singular) verb(component_haben:present:singular) object(domain)?"   
            //"Aus welcher Region ist der Melon de Bourgogne?"
            // "preposition(auf) interrogativeDeterminer(range:singular) verb(component_be:present:singular) adjunct(domain)? "
           // Wieviele Seiten hat Krieg und Frieden?
           // "interrogativeAmount(range:singular) noun(nominativeCase:plural) verb(component_haben:present:singular) object(domain)?"        
                 ),
        TemplateVariable.NounPPFrame,
        TemplateVariable.booleanQuestionDomain
      )
    );
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
         "noun(nominativeCase:plural) preposition adjunct(domain)"      
         //"determiner(component_the_nominative:reference) noun(nominativeCase) preposition nounPhrase"
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
    
    /*sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Wo ist der Westminster-Palast?",
          "interrogativePlace(nominativeCase:range:singular) verb(component_be:present:singular) object(domain)?"
          ),
        TemplateVariable.NounPPFrame,
        TemplateVariable.location
      )
    );*/
    
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
  
        
           //"Hatte Che Guevara Kinder?",
           //"verb(component_haben:past:singular) object(domain) noun(nominativeCase:singular)?",
           //"verb(component_haben:past:plural) object(domain) noun(nominativeCase:plural)?"
           //Hat Abraham Lincolns Sterbeort Kinder?", 
           // "verb(component_haben:present:plural) object(domain) noun(nominativeCase:plural)?",
           // "verb(component_haben:present:singular) object(domain) noun(nominativeCase:singular)?"     
           //"Liste die Kinder von Margaret Thatcher auf.",
           //"verb(imperative_plural) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain) preposition(auf)."     
           
           //"Welche Regierungsform hat Russland?", 
           //"interrogativeDeterminer(reference:singular) verb(component_haben:present:singular) object(domain)?"     
           
                
                
          //Gibt es ein Videospiel, das Battle Chess heißt?
          // "verb(imperative_transitive) pronoun(object_pronoun_es) article(definite_article:nominativeCase:neuter), noun(nominativeCase), article(component_the_nominative:nominativeCase:neuter) object(domain)"    
           //"Was ist Batmans richtiger Name?",                
           // "interrogativePronoun(range:singular)  verb(component_be:present:singular) object(range) noun(nominativeCase)?"    
            //"Welche Regierungsform hat Russland?",
            //"interrogativeDeterminer(range:singular) verb(component_haben:present:singular) object(domain)?"   
            //"Aus welcher Region ist der Melon de Bourgogne?"
            // "preposition(auf) interrogativeDeterminer(range:singular) verb(component_be:present:singular) adjunct(domain)? "
           // Wieviele Seiten hat Krieg und Frieden?
           // "interrogativeAmount(range:singular) noun(nominativeCase:plural) verb(component_haben:present:singular) object(domain)?"
               
                
                
           //Gib mir das Mitglied von...?
          //"verb(imperative_transitive) pronoun(object_pronoun) determiner(component_the_accusative) noun(accusativeCase) preposition adjunct(domain)."      
          //Who are the children of the children of Elvis Presley?",      
          //"interrogativePronoun(range:plural) verb(component_be:present:plural) nounPhrase?"   
          //Wo verb(component_be:present:singular)  noun?
         //  "interrogativePlace(range:singular) verb(component_be:present:singular) noun?"
          //In welchem Land ist der Mount Everest?
          //"preposition interrogativeDeterminer(range:singular) verb(component_be:present:singular) noun?"
          //Wieviel hat Pulp Fiction gekostet?
                

  
  
         //Ist Proinsulin ein Protein?
         // "verb(component_be:present:singular) subject(range) article(definite_article:nominativeCase:neuter) object(domain)?",   
          //"Sind Laubfrösche Amphibien?"
         // "verb(component_be:present:plural) subject(range) object(domain)?"
          
}