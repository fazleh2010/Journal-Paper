package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import grammar.structure.component.Language;

import java.util.List;

import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
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
import static grammar.datasets.sentencetemplates.TempConstants.passiveTransitive;

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
           //List all the musicals with music by Elton John.
          "verb(imperative_verb:present:plural) determiner(all) determiner(component_the) noun(range:plural) noun(singular) preposition adjunct(domain)." 

        ),
        NounPPFrame,
        whQuestion
      )
    );
    
     //NounPPFrame boolean question
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Ist Rita Wilson die Frau von Tom Hanks?"
          //"verb(component_be:present:singular) subject(range) determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?",
          //"verb(component_be:present:plural) subject(range) determiner(component_the_nominative:reference:plural) noun(nominativeCase:plural) preposition adjunct(domain)?"
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
    
    
    
    
     /*sentenceTemplateRepository.add(
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
    );*/
    
    
  
  
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