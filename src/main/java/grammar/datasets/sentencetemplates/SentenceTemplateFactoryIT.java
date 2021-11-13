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
import static util.io.TemplateConstants.BACKWARD;
import static util.io.TemplateConstants.FORWARD;
import static util.io.TemplateConstants.WHAT_WHICH_DO_THING;
import static util.io.TemplateConstants.WHEN_WHAT_PAST_THING;
import static util.io.TemplateConstants.WHEN_WHO_PAST_PERSON;
import static util.io.TemplateConstants.WHERE_WHO_PAST_PERSON;

/**
 *
 * @author elahi
 */
public class SentenceTemplateFactoryIT implements Factory<SentenceTemplateRepository> {

    private final SentenceTemplateRepository sentenceTemplateRepository;
    private final Language language;

    SentenceTemplateFactoryIT() {
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
           "interrogativePronoun verb(reference:component_be)  NP(prepositionalAdjunct)?",
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
      createSentenceTemplate(
        language,
        List.of(
        //"subject(INTERROGATIVE_DETERMINER_SINGULAR) verb(present) preposition adjunct(X)?", //Quale uva cresce in [entity]?
        "preposition object(interrogativeDeterminerSingular) verb(past) subject(X)?",
        "preposition object(interrogativeDeterminerSingular) verb(present) subject(X)?",
        "preposition object(interrogativeDeterminerSingular) verb(past) determiner(subject) subject(X)?",
        "preposition object(interrogativeDeterminerSingular) verb(present) determiner(subject) subject(X)?"
        //"preposition adjunct(INTERROGATIVE_DETERMINER_PLURAL) verb(past) subject(X)?",
        //"preposition adjunct(INTERROGATIVE_DETERMINER_PLURAL) verb(present) subject(X)?"
        ),
        "IntransitivePPFrame",
        WHAT_WHICH_DO_THING,
        FORWARD,
        Language.IT.toString()
      )
    );
/*
PREPOSITION + (QUALE, QUALI) + (LabelOfObject) +  VERB(present, past) + subject
PREPOSITION + (QUALE, QUALI) + LabelOfObject + aux + VERB(pas) + subject
(QUALE, QUALI) + LabelOfObject + VERB(present, past) + SUBJECT
(QUALE, QUALI) + LabelOfObject + aux + VERB(past) + SUBJECT
QUANTO + verb(present, past) + SUBJECT
QUANTO + aux +  verb(past) + SUBJECT
QUANDO + verb(present, past) + SUBJECT
QUANDO + aux + verb(past) + SUBJECT
(QUANTI, QUANTE) + LabelOfObject + VERB(PAST, PRESENT) + SUBJECT
(QUANTI, QUANTE) + LabelOfObject + aux + VERB(PAST,) + SUBJECT
PREPOSITION + (COSA, CHI, DOVE) + VERB(PRESENT, PAST) + SUBJECT
PREPOSITION + (COSA, CHI, DOVE) + aux + VERB( PAST) + SUBJECT*/
//Attraverso quali città scorre il fiume Weser?",
    
     // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         "preposition object(interrogativePronounThingWhat) verb(present) subject(X)?",
         "preposition object(interrogativePronounThingWhat) verb(past) subject(X)?",
         "preposition object(interrogativePronounThingWhat) verb(present) subject(X)?",
         "preposition object(interrogativePronounThingWhat) verb(past) subject(X)?"
        ),
       "IntransitivePPFrame",
        WHAT_WHICH_DO_THING,
        BACKWARD,
        Language.IT.toString()
      )
    );
    //Quando è stato arruolato [entity]?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(    
        //"object(INTERROGATIVE_PLACE) verb(present) subject(X)?",//Dove cresce [entity]?
        "object(interrogativeTemporal) component_be(present:singular) verb(past) subject(X)?",
        "object(interrogativeTemporal) component_be(present:singular) verb(present) subject(X)?",
        "object(interrogativeTemporal) component_aux_object_past(past:singular) verb(past) subject(X)?",
        "object(interrogativeTemporal) component_aux_object_past(past:plural) verb(present) subject(X)?"
        ),
        "IntransitivePPFrame",
        WHEN_WHAT_PAST_THING,
        FORWARD,
        Language.IT.toString()
      )
    );
    
     // IntransitivePPFrame
     //Quale nave è stata completata nel 2010?
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        "subject(interrogativePronounThingWhat) verb(past) preposition(date) adjunct(X)?",
        "subject(interrogativePronounThingWhat) verb(present) preposition(date) adjunct(X)?"
        ),
       "IntransitivePPFrame",
        WHEN_WHAT_PAST_THING,
        BACKWARD,
        Language.IT.toString()
      )
    );
    //"Quando è morta la principessa Diana?",
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(    
       "object(interrogativeTemporal) component_be(present:singular) verb(past) subject(X)?"
        ),
        "IntransitivePPFrame",
        WHEN_WHO_PAST_PERSON,
        FORWARD,
        Language.IT.toString()
      )
    );
      //Chi è morto nel 2010?
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        "subject(interrogativePronounPerson) component_be(present:singular) verb(past) preposition(date) adjunct(X)?"        
       
        ),
       "IntransitivePPFrame",
        WHEN_WHO_PAST_PERSON,
        BACKWARD,
        Language.IT.toString()
      )
    );
     //"Dove è nato Bach?",
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(    
       "object(interrogativePlace) component_be(present:singular) verb(past) subject(X)?"
        ),
        "IntransitivePPFrame",
        WHERE_WHO_PAST_PERSON,
        FORWARD,
        Language.IT.toString()
      )
    );
      //Chi è nato in Germania?
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        "subject(interrogativePronounPerson) component_be(present:singular) verb(past) preposition(place) adjunct(X)?"          
       
        ),
       "IntransitivePPFrame",
        WHERE_WHO_PAST_PERSON,
        BACKWARD,
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
      createSentenceTemplate(
        language,
        List.of(
          "subject(interrogativePronounPerson) verb(present) determiner(directObject) directObject(X)?",
          "subject(interrogativePronounPerson) verb(past) determiner(directObject) directObject(X)?",
          "subject(interrogativeDeterminerSingular) verb(present) determiner(directObject) directObject(X)?",
          "subject(interrogativeDeterminerSingular) verb(past) determiner(directObject) directObject(X)?"
          
        ),
        "TransitiveFrame",
        FORWARD,
        Language.IT.toString()
      )
    );
    // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
      // Da chi è stata costruita [entity]?	
      // Da chi è stata costruita l'['entity]?	
      // Da qualle persona è stata costruita [entity]?	
      // Da quale persona è stata costruita l'['entity]?
      // Qald-7: Che film ha diretto Kurosawa?,
       //Qald-7: Dammi tutti i film diretti da Francis Ford Coppola.
       //Qald-7: Quanti film ha diretto Stanley Kubrick?",
      "directObject(interrogativeDeterminerSingular) verb(past) subject(X)?",
      //"directObject(interrogativePronounPerson) verb(past) subject(X)?",
      "verb(component_imperative_transitive:present) determiner(determiner_plural) verb(past) preposition(da) subject(X)."
      //"preposition(In) subject(INTERROGATIVE_DETERMINER_PLURAL) verb(present) preposition(si) directObject(X)."
        ),
        "TransitiveFrame",
        BACKWARD,
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