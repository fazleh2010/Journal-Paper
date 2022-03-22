package grammar.datasets.sentencetemplates;

import grammar.datasets.Factory;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createAPTemplate;
import grammar.structure.component.Language;

import java.util.List;

import static grammar.datasets.sentencetemplates.SentenceTemplate.createNPTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createSentenceTemplate;
import static grammar.datasets.sentencetemplates.SentenceTemplate.createVPTemplate;
import static grammar.datasets.sentencetemplates.TempConstants.IntransitivePPFrame;


class SentenceTemplateFactoryES  implements Factory<SentenceTemplateRepository>,TempConstants {

    private final SentenceTemplateRepository sentenceTemplateRepository;
    private final Language language;

    SentenceTemplateFactoryES() {
        this.language = Language.ES;
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
   //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"¿Cuál es la capital de Camerún?",
          "interrogativePronoun(range:singular) verb(component_be:present:singular) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:plural) verb(component_be:present:plural) noun(plural) preposition adjunct(domain)?",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:plural) verb(component_be:past:plural) noun(plural) preposition adjunct(domain)?",
           //Wer war der Vizepräsident unter Samuel Schmid?
           "interrogativePronoun(range:plural) verb(component_be:past:plural) noun(plural) preposition adjunct(domain)?",
          
          //Gib mir die Enkel von Elvis Presley.Dame una lista de los hijos de Margaret Thatcher.
           "verb(component_imperative_transitive:present:singular) determiner(component_una) determiner(component_lista) noun(singular) preposition adjunct(domain).",
           "verb(component_imperative_transitive:present:singular) determiner(component_una) determiner(component_lista) noun(plural) preposition adjunct(domain).",
           //Dame todos los miembros de la banda Prodigy.
           "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) preposition adjunct(domain).",
           "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(plural) preposition adjunct(domain).",
            //¿Qué forma de gobierno tiene Rusia?¿Qué pa�ses de gobierno lo hace ($x | Country_NP)?
           "interrogativeVariableDeterminer noun(singular) determiner(component_tiene) adjunct(domain)?"
           //"interrogativeDeterminer(range:singular) preposition  noun(singular) verb(component_do:present:singular) adjunct(domain)?"

                
          /*//"Wer ist der Bürgermeister von Tel Aviv?",
          "interrogativeDeterminer(range:singular) verb(component_be:present:singular) preposition adjunct(domain)?",
          "interrogativeDeterminer(range:singular) verb(component_be:past:singular) preposition adjunct(domain)?",
          "interrogativeDeterminer(range:plural) verb(component_be:present:plural) preposition adjunct(domain)?",
          "interrogativeDeterminer(range:plural) verb(component_be:past:plural) preposition adjunct(domain)?",*/

           //"verb(imperative_transitive:present:singular) pronoun(object_pronoun) noun(singular) preposition adjunct(domain).",
           //"verb(imperative_transitive:present:singular) pronoun(object_pronoun) noun(plural) preposition adjunct(domain)."
           
          /*//Gib mir alle Bandmitglieder von Prodigy
           "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) noun(plural) preposition adjunct(domain).",
           //"Liste die Kinder von Margaret Thatcher auf.",
           "verb(imperative_verb:present:plural) determiner(component_the_nominative:reference:plural) noun(plural) preposition adjunct(domain) preposition(auf)."  ,   
           //"Liste die Kinder von Margaret Thatcher auf.",
           "verb(imperative_verb:present:plural) determiner(alle) noun(plural) preposition adjunct(domain) preposition(auf)." ,
           //"In welchem Land ist der Mount Everest?", In welcher Stadt ist die Heinekenbrauerei?
            //"preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_be:present:singular) adjunct(domain)?"
            //"interrogativePlace(nominativeCase:singular) verb(component_be:present:singular) determiner(component_the_nominative:domain:singular) object(domain)?"             
            //Was ist Batmans richtiger Name?
           "interrogativePronoun(range:singular) verb(component_be:present:singular) adjunct(domain) Apostrophe noun(nominativeCase:singular)?" */
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
        NounPPFrame,
        booleanQuestionDomain
      )
    );
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Was ist die Hauptstadt von Kamerun?
         "determiner(component_the_nominative:reference) noun(nominativeCase:singular) preposition adjunct(domain)?"
         //"noun(nominativeCase:plural) preposition adjunct(domain)"      
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
        NounPPFrame,
        noun
      )
    );
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //Wo ist der Westminster-Palast?",
          //"interrogativePlace(nominativeCase:range:singular) verb(component_be:present:singular) object(domain)?"
          ),
        NounPPFrame,
        location
      )
    );
    
     // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //Wer moderiert die BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
        //¿En qué museo está expuesto el Grito?
        "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        //Dame todos los actores que hayan actuado en Last Action Hero.Dame todos protagonizar protagonizar ($x | Film_NP)
         //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(plural) verb(mainVerb:present:thridPerson) object(domain)"
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) interrogativeRelative1 determiner(component_hayan) verb(mainVerb:present:thridPerson) object(domain)"
      
        //Wer hat Slack entwickelt?
        /*"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",    
        ///Welche Person hat Slack entwickelt?
         "interrogativeDeterminer(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?" ,           
        //Wer hat sich Family Guy ausgedacht?"
         //"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(RefVerb:perfect:thridPerson)?"
         //"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(RefVerb:perfect:thridPerson)?"
         // Trenn Verb
        "interrogativePronoun(nominativeCase:range:singular) verb(TrennVerbPart1:past:thridPerson) object(domain) verb(TrennVerbPart2:past:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         //Gib mir alle von der NASA betriebenen Startrampen.
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:singular).",
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:plural).",
          //What is Batman"s real name?
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) Apostrophe noun(singular)?",
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:past:singular) adjunct(domain) Apostrophe noun(singular)?"
      */        
        ),
       TransitiveFrame,
        PERSON_CAUSE,
        activeTransitive
      )
    );
      // TransitiveFrame
      //"Was wird von ($x | PERSON_NP) entwickelt?", "Was wurde von ($x | PERSON_NP) entwickelt?", 
      //"Werk wird von ($x | PERSON_NP) entwickelt?", "Werk wurde von ($x | PERSON_NP) entwickelt?", 
      //"Werke werden von ($x | PERSON_NP) entwickelt?", 
      //"Werke wurden von ($x | PERSON_NP) entwickelt?"
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"¿Qué series televisivas ideó Walt Disney?"?"¿Qué series televisivas ideó Walt Disney?
        "interrogativeVariableDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(range)?",
        //Dame todas las plataformas de lanzamiento operadas por la NASA.    Dame todos operar operar por ($x | LaunchPad_NP) 
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(domain:singular) verb(mainVerb:perfect:thridPerson) preposition object(range)"

         //Was wurde von ($x | PERSON_NP) entwickelt?
        /*"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        //"Werke werden von ($x | PERSON_NP) entwickelt?", 
        //"Werke wurden von ($x | PERSON_NP) entwickelt?"
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
         //Wer war mit Präsident Chirac verheiratet?
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition(mit) object(domain) verb(mainVerb:perfect:thridPerson)?", 
        //Trenn
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(TrennVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(TrennVerb:perfect:thridPerson)?"
        */
        ),
        TransitiveFrame,
        PERSON_CAUSE,
        passiveTransitive
      )
    );
    
         // TransitiveFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
       //Wer moderiert die BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
        //¿En qué museo está expuesto el Grito?
        "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?"
        //Wer hat Slack entwickelt?
        /*"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",    
        ///Welche Person hat Slack entwickelt?
         "interrogativeDeterminer(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?" ,           
        //Wer hat sich Family Guy ausgedacht?"
         //"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(RefVerb:perfect:thridPerson)?"
         //"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(RefVerb:perfect:thridPerson)?"
         // Trenn Verb
        "interrogativePronoun(nominativeCase:range:singular) verb(TrennVerbPart1:past:thridPerson) object(domain) verb(TrennVerbPart2:past:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) pronoun(reflexive_pronoun) object(domain) verb(TrennVerb:perfect:thridPerson)?",
         //Gib mir alle von der NASA betriebenen Startrampen.
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:singular).",
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:plural).",
          //What is Batman"s real name?
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) Apostrophe noun(singular)?",
          "interrogativePronoun(nominativeCase:range:singular) verb(component_be:past:singular) adjunct(domain) Apostrophe noun(singular)?"
      */        
         ),
       TransitiveFrame,
        PERSON_PERSON,
        activeTransitive
      )
    );
    
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"¿Qué series televisivas ideó Walt Disney?"?"
        "interrogativePronounDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(range)?"
         //Was wurde von ($x | PERSON_NP) entwickelt?
        /*"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        //"Werke werden von ($x | PERSON_NP) entwickelt?", 
        //"Werke wurden von ($x | PERSON_NP) entwickelt?"
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:singular) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
         //Wer war mit Präsident Chirac verheiratet?
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition(mit) object(domain) verb(mainVerb:perfect:thridPerson)?", 
        //Trenn
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(TrennVerb:perfect:thridPerson)?",
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_be:future:singular) preposition object(range) verb(TrennVerb:perfect:thridPerson)?"
        */
    
        ),
        TransitiveFrame,
        PERSON_CAUSE,
        passiveTransitive
      )
    );
 
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        "subject(INTERROGATIVE_DETERMINER_SINGULAR) verb(present) preposition adjunct(X)?" //Quale uva cresce in [entity]?
        ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        forward
      )
    );
    
     // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          "object(INTERROGATIVE_PLACE) verb(present) subject(X)?"//Dove cresce [entity]?
        ),
       IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        backward
      )
    );
    //Quando è stato arruolato [entity]?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(      
        "object(INTERROGATIVE_TEMPORAL) component_aux_object_past(singular) verb(present) subject(X)?" ,
        "object(INTERROGATIVE_TEMPORAL) component_aux_object_past(plural) verb(present) subject(X)?" 
        ),
        "IntransitivePPFrame",
        WHEN_WHAT_PAST_THING,
        forward
      )
    );
    
     // IntransitivePPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        "subject(INTERROGATIVE_DETERMINER_SINGULAR) component_aux_object_past(singular) verb(past) preposition adjunct(X)?"
        //"subject(INTERROGATIVE_DETERMINER_PLURAL) component_aux_object_past(plural) verb(past) preposition adjunct(X)?"
        ),
        IntransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        backward
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
          "subject(PERSON_INTERROGATIVE_PRONOUN) verb(present) determiner(directObject) directObject(X)?",
          "subject(PERSON_INTERROGATIVE_PRONOUN) verb(past) determiner(directObject) directObject(X)?",
          "subject(INTERROGATIVE_DETERMINER_SINGULAR) verb(present) determiner(directObject) directObject(X)?",
          "subject(INTERROGATIVE_DETERMINER_SINGULAR) verb(past) determiner(directObject) directObject(X)?"
          
        ),
        TransitiveFrame,
        forward
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
      "directObject(INTERROGATIVE_DETERMINER_SINGULAR) verb(past) subject(X)?",
      "directObject(PERSON_INTERROGATIVE_PRONOUN) verb(past) subject(X)?",
      "verb(component_imperative_transitive:present) determiner(determiner_plural) verb(past) preposition(da) subject(X)."
      //"preposition(In) subject(INTERROGATIVE_DETERMINER_PLURAL) verb(present) preposition(si) directObject(X)."
        ),
        TransitiveFrame,
        backward
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