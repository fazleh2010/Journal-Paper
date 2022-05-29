/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import grammar.structure.component.Language;
import java.util.List;

/**
 *
 * @author elahi
 */
public class SentenceTemplateFactoryTA implements Factory<SentenceTemplateRepository>,TempConstants {

    private final SentenceTemplateRepository sentenceTemplateRepository;
    private final Language language;

    SentenceTemplateFactoryTA() {
        this.language = Language.IT;
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
           "interrogativeDeterminer verb(reference:component_be)  NP(prepositionalAdjunct)?"
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
 
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        "subject(INTERROGATIVE_DETERMINER_SINGULAR) verb(present) preposition adjunct(X)?" //Quale uva cresce in [entity]?
        ),
        "IntransitivePPFrame",
        WHAT_WHICH_PRESENT_THING_2,
        forward,
        Language.IT.toString()
      )
    );
    
     // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          "object(INTERROGATIVE_PLACE) verb(present) subject(X)?"//Dove cresce [entity]?
        ),
       "IntransitivePPFrame",
        WHAT_WHICH_PRESENT_THING_2,
        backward,
        Language.IT.toString()
      )
    );
    //Quando è stato arruolato [entity]?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(      
        "object(INTERROGATIVE_TEMPORAL) component_aux_object_past(singular) verb(present) subject(X)?" ,
        "object(INTERROGATIVE_TEMPORAL) component_aux_object_past(plural) verb(present) subject(X)?" 
        ),
        "IntransitivePPFrame",
        WHEN_WHAT_PAST_THING,
        forward,
        Language.IT.toString()
      )
    );
    
     // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        "subject(INTERROGATIVE_DETERMINER_SINGULAR) component_aux_object_past(singular) verb(past) preposition adjunct(X)?"
        //"subject(INTERROGATIVE_DETERMINER_PLURAL) component_aux_object_past(plural) verb(past) preposition adjunct(X)?"
        ),
       "IntransitivePPFrame",
        WHEN_WHAT_PAST_THING,
        backward,
        Language.IT.toString()
      )
    );
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
    //Qald-7: Che film ha diretto Kurosawa?,
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          "subject(PERSON_INTERROGATIVE_PRONOUN) verb(present) determiner(directObject) directObject(X)?",
          "subject(PERSON_INTERROGATIVE_PRONOUN) verb(past) determiner(directObject) directObject(X)?",
          "subject(INTERROGATIVE_DETERMINER_SINGULAR) verb(present) determiner(directObject) directObject(X)?",
          "subject(INTERROGATIVE_DETERMINER_SINGULAR) verb(past) determiner(directObject) directObject(X)?"
          
        ),
        "TransitiveFrame",
        forward,
        Language.IT.toString()
      )
    );
    // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
      // Da chi è stata costruita [entity]?	
      // Da chi è stata costruita l'['entity]?	
      // Da qualle persona è stata costruita [entity]?	
      // Da quale persona è stata costruita l'['entity]?
      // Qald-7: Che film ha diretto Kurosawa?,
       //Qald-7: Dammi tutti i film diretti da Francis Ford Coppola.
       //Qald-7: Quanti film ha diretto Stanley Kubrick?",
      "directObject(INTERROGATIVE_DETERMINER_SINGULAR) verb(past) subject(X)?",
      "directObject(PERSON_INTERROGATIVE_PRONOUN) verb(past) subject(X)?",
      "verb(component_imperative_transitive:present) determiner(determiner_plural) verb(past) preposition(da) subject(X)."
      //"preposition(In) subject(INTERROGATIVE_DETERMINER_PLURAL) verb(present) preposition(si) directObject(X)."
        ),
        "TransitiveFrame",
        backward,
        Language.IT.toString()
      )
    );
    // TransitiveFrame
    /*sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "subject(INTERROGATIVE_PRONOUN) verb(past) directObject(X)?",
          "subject(INTERROGATIVE_PRONOUN) verb(past) determiner directObject(X)?",
          "subject(INTERROGATIVE_DETERMINER) verb(past) directObject(X)?",
          "subject(INTERROGATIVE_DETERMINER) verb(past) determiner directObject(X)"
        ),
        "TransitiveFrame",
        "active",
        Language.IT.toString()
      )
    );*/
    // TransitiveFrame
    /*sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
      // Da chi è stata costruita [entity]?	
      // Da chi è stata costruita l'['entity]?	
      // Da qualle persona è stata costruita [entity]?	
      // Da quale persona è stata costruita l'['entity]?	
    
          "Da subject(INTERROGATIVE_PRONOUN) è stata verb(past) directObject(X)?",
          "Da subject(INTERROGATIVE_PRONOUN) è stata verb(past) determiner directObject(X)?",
          "Da subject(INTERROGATIVE_DETERMINER) è stata verb(past) directObject(X)?",
          "Da subject(INTERROGATIVE_DETERMINER) è stata verb(past) determiner directObject(X)"
        ),
        "TransitiveFrame",
        "passive",
        Language.IT.toString()
      )
    );*/
    
    /*sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "subject(interrogativeDeterminer) verb(past) directObject(X)?",
          "subject(interrogativeDeterminer) verb(past) determiner(component_the) directObject(X)",
          "subject(PersonInterrogativePronoun) verb(past) directObject(X)?",
          "subject(PersonInterrogativePronoun) verb(past) determiner(component_the) directObject(X)",
          "verb(past) directObject(X)?",
          "verb(past) determiner(component_the) directObject(X)"
        ),
        "TransitiveFrame",
        "active",
        Language.IT.toString()
      )
    );*/
    // VP(directObject)
    sentenceTemplateRepository.add(
      createVPTemplate(
        language,
        List.of(
          "verb(root) directObject",
          "verb(root) determiner(reference:component_the) directObject"
        ),
        "directObject"
      )
    );
  }
   //  TransitiveFrame active
   // WP transitiveverb [domain] Chi ha costruito [entity]? 	
   // WDT dbo:range transitiveverb [domain]? Quale persona ha costruito [entity]?
   // WDT dbo:range transitiveverb DT [domain] Quale persona ha costruito l'[entity]?	
   // WP transitiveverb DT [domain]  Chi ha costruiito l'[entiy]?	
   // generated [Chi ha creato ($x | TELEVISIONSHOW_NP), Chi ha creato IL ($x | TELEVISIONSHOW_NP), Quale agente ha creato ($x | TELEVISIONSHOW_NP), Quale agente ha creato IL ($x | TELEVISIONSHOW_NP)]

   
   // Transitive passive
      // Da chi è stata costruita [entity]?	
      // Da qualle persona è stata costruita [entity]?	
      // Da quale persona è stata costruita l'['entity]?	
      // Da chi è stata costruita l'['entity]?	
    
    
   //Intransitive
     //WRB intransitiveverb [domain]	Dove cresce [entity]?
     //WDT dbo:range intransitiveverb IN DT [domain] Quale uva cresce nella [entity]?	
     //WDT dbo:domain intransitiveverb IN [range] Quale uva cresce in [entity]?
     //IN WDT dbo:domain intransitiveverb [range]? In quale regione cresce [entity]?	
    
    
    //WRB intransitiveverb [domain]	Dove cresce [X|Grape]?
    //IN WDT dbo:domain intransitiveverb [range]? In quale regione cresce (X|grape)?	
    //WDT dbo:range intransitiveverb IN DT [domain] Quale uva cresce nella (X|region)?	 
    //WDT dbo:domain intransitiveverb IN [range] Quale uva cresce in (X|Region)?
    
    //"Qual cresceva nel ($x | WINEREGION_NP)?", "Quale uva cresceva nel ($x | WINEREGION_NP)?"
    
    
     
     
      //"verb(root) preposition prepositionalAdjunct"
    
     // WRB intransitiveverb [domain] Where does [X | Grape] grow?
     // IN WDT dbo: domain intransitiveverb [range]? In which region does (X | grape) grow?
     // WDT dbo: range intransitiveverb IN DT [domain] Which grapes grow in the (X | region)?
     // WDT dbo: domain intransitiveverb IN [range] Which grapes grow in (X | Region)?
     
    
     /*
    // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
           //Chi ha creato Y?
           //Who created Y?
          "chi (interrogativePronoun) ha creato (verb) subject?",
          //quale Y è stata creata da X?
          //Which Y was created by X?
          "quale (interrogativeDeterminer) directObject è stata creata (verb) da directObject?",
          // cosa rappresenta l'opera Y?
          // what does Y represent?
          "cosa (interrogativePronoun) rappresenta (verb) directObject?",
          // Quale Y rappresenta X?
          // Which Y represent X?
          "quale (interrogativeDeterminer) directObject rappresenta(verb) subject?",
        ),
        "subject",
        "directObject"
      )
    );
// IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
           //Quando è stata costruita Y?
           //When was Y built?
          "quando (interrogativePronoun) è stata costruita (verb) subject?",
          //Cos'è (Y) è stato costruito nell'X?
          // Which Y was built in X?
          "cosa (interrogativePronoun) è stato costruito (verb) nel (prepositionalAdjunct)?",
          //Dove si trova Y?
          // Where is Y located?
          "dove (interrogativePronoun) si trova (verb) subject?",
        ),
        "subject",
        "prepositionalAdjunct"
      )
    );
     */
    
    /*
    For Male gender:
- IL is the one used by default
- LO is used when the noun it precedes begins with z, s+consonant, gn, ps, x
- L' is used when the noun it precedes begins with a vowel
For Female gender:
-LA is the one used by default
-L' is used when the noun it precedes begins with a vowel
For Plural number:
- I for words starting with consonant
- GLI for words starting with z,s + consonant, gn,ps, x and for words starting with a vowel
- LE if every entity is of female gender
    */


}