package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import grammar.structure.component.Language;

import java.util.List;

import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import static grammar.datasets.sentencetemplates.TempConstants.AdjectivePPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.HOW_MANY_PRICE;
import static grammar.datasets.sentencetemplates.TempConstants.HOW_MANY_THING;
import static grammar.datasets.sentencetemplates.TempConstants.IntransitivePPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.NounPPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.TransitiveFrame;
import static grammar.datasets.sentencetemplates.TempConstants.WHAT_WHICH_PRESENT_THING;
import static grammar.datasets.sentencetemplates.TempConstants.WHEN_WHAT_PAST_THING;
import static grammar.datasets.sentencetemplates.TempConstants.WHEN_WHO_PAST_PERSON;
import static grammar.datasets.sentencetemplates.TempConstants.WHERE_WHO_PAST_PERSON;
import static grammar.datasets.sentencetemplates.TempConstants.backward;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomain;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomainRange;
import static grammar.datasets.sentencetemplates.TempConstants.forward;
import static grammar.datasets.sentencetemplates.TempConstants.location;
import static grammar.datasets.sentencetemplates.TempConstants.noun;
import static grammar.datasets.sentencetemplates.TempConstants.nounPhrase;
import static grammar.datasets.sentencetemplates.TempConstants.whQuestion;
import static grammar.structure.component.FrameType.APP;
import static grammar.datasets.sentencetemplates.TempConstants.activeTransitive;
import static grammar.datasets.sentencetemplates.TempConstants.adjectiveBaseForm;
import static grammar.datasets.sentencetemplates.TempConstants.comparative;
import static grammar.datasets.sentencetemplates.TempConstants.passiveTransitive;
import static grammar.datasets.sentencetemplates.TempConstants.superlativeCountry;
import static grammar.datasets.sentencetemplates.TempConstants.superlativeWorld;

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

    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //What is the capital of Cameron?
          "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:singular) verb(component_be:present:plural) determiner(component_the) noun(plural) preposition adjunct(domain)?",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) determiner(component_the) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:singular) verb(component_be:past:plural) determiner(component_the) noun(plural) preposition adjunct(domain)?",
          //"interrogativePronoun verb(component_be:present:singular) determiner(component_the) noun(singular) preposition adjunct(domain)?",
          //"Who is the mayor of Paris?",
          "interrogativePronoun verb(component_be:present:singular) determiner(component_the) noun(singular) preposition adjunct(domain)?", 
          "interrogativePronoun verb(component_be:past:singular) determiner(component_the) noun(singular) preposition adjunct(domain)?",   
           //List all the musicals von Elton John.
          "verb(imperative_verb:present:plural) determiner(all) determiner(component_the) noun(range:plural) preposition adjunct(domain)." 
          //List all the musicals with music by Elton John.
          //"verb(imperative_verb:present:plural) determiner(all) determiner(component_the) noun(range:plural) noun(singular) preposition adjunct(domain)." 

        ),
        NounPPFrame,
        whQuestion
      )
    );
    
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
      
                 ),
        NounPPFrame,
        booleanQuestionDomainRange
      )
    );
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Hatte Che Guevara Kinder?",
         // "verb(component_haben:past:singular) object(domain) noun(nominativeCase)?" 
        
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
        NounPPFrame,
        booleanQuestionDomain
      )
    );
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //the capital of germany
         "determiner(component_the) noun(singular) preposition adjunct(domain)?",
         "determiner(component_the) noun(plural) preposition adjunct(domain)?"
                ),
        NounPPFrame,
        nounPhrase
      )
    );
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "noun(singular)"
          //Welche Person ist das Mitglied von...?
          //"interrogativeDeterminer noun(condition:copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
          //Wer ist das Mitglied von...?
          //"interrogativePronoun verb(reference:component_be) NP(prepositionalAdjunct)?",
          //Gib mir das Mitglied von...?
          //"verb(reference:component_imperative_transitive) pronoun(reference:object_pronoun) determiner(reference:component_the_accusative) noun(root:accusativeCase) preposition prepositionalAdjunct"
          ),
        NounPPFrame,
        noun
      )
    );
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Wo ist der Westminster-Palast?",
          "interrogativePlace(nominativeCase:range:singular) verb(component_be:present:singular) object(domain)?"
          ),
        NounPPFrame,
        location
      )
    );
    
     // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //Who presented BBC Wildlife Specials?
        "interrogativePronoun(range) verb(mainVerb:present3rd:thirdPerson) object(domain)?",
        "interrogativePronoun(range) verb(mainVerb:past:thirdPerson) object(domain)?",
        //Which person presented BBC Wildlife Specials?
        "interrogativeDeterminer(range:singular) verb(mainVerb:present3rd:thridPerson) object(domain)?",
        "interrogativeDeterminer(range:singular) verb(mainVerb:past:thridPerson) object(domain)?"
        ),
       TransitiveFrame,
        activeTransitive
      )
    );
    // TransitiveFrame passive
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //"What was developed by X?
        "interrogativeDeterminer(domain:singular) verb(component_be:past:singular) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?",
        //"What were developed by X?
        "interrogativeDeterminer(domain:plural) verb(component_be:past:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" 
        ),
        TransitiveFrame,
        passiveTransitive
      )
    );
    
       // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //
           ),
       TransitiveFrame,
       HOW_MANY_THING,
       activeTransitive
      )
    );
    
    // TransitiveFrame passive amount
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //How many ethnic groups live in Slovenia?
        "interrogativeAmount(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?" 
      
            ),
        TransitiveFrame,
        HOW_MANY_THING,
        passiveTransitive
      )
    );
    
    
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
                
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:singular) preposition object(range) verb(mainVerb:past:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:plural) preposition object(range) verb(mainVerb:past:thridPerson)?"
    
        ),
        TransitiveFrame,
        APP.toString()
      )
    );
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(           
        //Which country does X flow through?
        "interrogativeDeterminer(range:singular) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?", 
        "interrogativeDeterminer(range:plural) verb(component_do:present:singular) object(domain) verb(mainVerb:present:singular) preposition?"        
                ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING,
        forward
      )
    );
    
    
    
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //what flows through Germany?
          "interrogativePronoun(domain) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
          //Which river flows through Germany?
          "interrogativeDeterminer(domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
          //what flew through Germany?
          "interrogativePronoun(domain) verb(mainVerb:past:thridPerson) preposition adjunct(range)?",
          //Which rivers flow through Germany?
          "interrogativeDeterminer(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?"
            ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING,
        backward
      )
    );
      ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //When was X completed?
          "interrogativeTemporal verb(component_be:past:singular) object(domain) verb(mainVerb:past:thridPerson)?"
           //When were X completed?
          //"interrogativeTemporal verb(component_do:past:plural) object(domain) verb(mainVerb:past:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        forward
      )
    );
       
        // TransitiveFrame passive amount
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        "interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?"  
            ),
        IntransitivePPFrame,
        HOW_MANY_THING,
        backward
      )
    );
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //what took place in Date?  
        "interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
         ////what happened in Date?
        //"interrogativePronoun(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
              ),
        IntransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        backward
      )
    );
    ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //When was X completed?
        //"interrogativeTemporal verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHEN_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHEN_WHO_PAST_PERSON,
        backward
      )
    );
      
       ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //"Welche Profisurfer wurden auf den Philippinen geboren?",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"

             ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Wo wurde Donald Trump geboren?
        // "interrogativePlace(nominativeCase:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        backward
      )
    );
      
      
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //"Welche Profisurfer wurden auf den Philippinen geboren?",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"

             ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Wo wurde Donald Trump geboren?
         //"interrogativePlace(nominativeCase:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        backward
      )
    );
      
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
                 //"Wieviel hat Pulp Fiction gekostet?","Wieviele Sprachen werden in Turkmenistan gesprochen?"
        // "interrogativeAmount(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?"
          //was kostet der film?
          //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:present:thridPerson) determiner(component_the_nominative:domain) object(domain)?"
         //"Wieviele Sprachen werden in Turkmenistan gesprochen?"  
         //"interrogativeAmountDeterminer(nominativeCase:range:singular) verb(component_werden:present:singular) preposition determiner(component_the_nominative:domain) object(domain) verb(mainVerb:perfect:thridPerson)?"  

       
        //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      
       
    
        ),
        IntransitivePPFrame,
        HOW_MANY_PRICE,
        forward
      )
    );
   
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
        //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:perfect:present) object(range)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      

           
        ),
        IntransitivePPFrame,
        HOW_MANY_PRICE,
        backward
      )
    );
      
            ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        ////Wieviele Sprachen werden in Turkmenistan gesprochen?
        //"interrogativeAmount(nominativeCase:singular) interrogativeDeterminer(nominativeCase:range:singular) verb(component_werden:present:singular) preposition object(domain) verb(mainVerb:perfect:thridPerson)?",
       //Wieviele Sprachen werden in Turkmenistan gesprochen?
        "interrogativeAmount(nominativeCase:range:plural) verb(component_werden:present:plural) preposition object(domain) verb(mainVerb:perfect:thridPerson)?"
    
        ),
        IntransitivePPFrame,
        HOW_MANY_THING,
        forward
      )
    );
   
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
             
        ),
        IntransitivePPFrame,
        HOW_MANY_THING,
        backward
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
        NounPPFrame,
        nounPhrase
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
    // AdjectivePPFrame...adjectiveBaseForm
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
           
        ),
        AdjectivePPFrame,
        adjectiveBaseForm
      )
    );

    // AdjectivePPFrame...comparative
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         
        ),
        AdjectivePPFrame,
        comparative
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //What is the highest mountain in Australia?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) noun(range:singular) preposition adjunct(domain)?"
        ),
        AdjectivePPFrame,
        superlativeCountry,
        forward
      )
    );
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
         //What is the largest country in the world?
        "interrogativePronoun(range:singular) verb(component_be:present:singular) determiner(component_the) adjective(superlative) noun(range:singular) preposition determiner(component_the) adjunct(domain)?"
        ),
        AdjectivePPFrame,
        superlativeWorld,
        forward
      )
    );
    
      // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Where is Mount Everest Located?
        "interrogativePlace verb(component_be:present:singular) adjunct(range) verb(verb_location:past:singular)?",
        ////In which country is Mount Everest Located?
        "preposition interrogativeDeterminer(domain:singular) adjunct(range) verb(component_be:present:singular) verb(verb_location:past:singular)?"
        ),
        AdjectivePPFrame,
        superlativeCountry,
        backward
      )
    );
    
    
  }
          
}