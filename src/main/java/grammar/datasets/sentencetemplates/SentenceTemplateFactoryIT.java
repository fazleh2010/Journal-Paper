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
public class SentenceTemplateFactoryIT  implements Factory<SentenceTemplateRepository>,TempConstants {

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
   //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //"Qual è la capitale del Cameroon",
          "interrogativePronoun(range:singular) verb(component_be:present:singular) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:singular) verb(component_be:past:singular) noun(singular) preposition adjunct(domain)?",
          "interrogativePronoun(range:plural) verb(component_be:present:plural) noun(plural) preposition adjunct(domain)?",
          "interrogativePronoun(range:plural) verb(component_be:past:plural) noun(plural) preposition adjunct(domain)?",
           //"Dammi la valuta della Cina."
           "verb(component_imperative_transitive:present:singular) determiner(component_la) noun(singular) preposition adjunct(domain).",
           "verb(component_imperative_transitive:present:plural) determiner(component_la) noun(plural) preposition adjunct(domain).",
           //Dammi tutti i membri dei Prodigy.
           "verb(component_imperative_transitive:present:singular) determiner(component_tutti) determiner(component_i) noun(singular) preposition adjunct(domain).",
           "verb(component_imperative_transitive:present:plural) determiner(component_tutti) determiner(component_i) noun(plural) preposition adjunct(domain).",
            //Elenca tutti i musicals con musiche di Elton John.
           "verb(component_imperative_list:present:singular) determiner(component_tutti) determiner(component_i) noun(plural) preposition adjunct(domain).",
           //Mostrami l'autobiografia di Hemingway.
           "verb(component_imperative_show:present:singular) determiner(component_li) noun(singular) preposition adjunct(domain).",
           "verb(component_imperative_show:present:singular) determiner(component_li) noun(plural) preposition adjunct(domain)."
        ),
        NounPPFrame,
        whQuestion
      )
    );
    
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
                      ),
        NounPPFrame,
        booleanQuestionDomain
      )
    );
    //NounPPFrame
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //la capitale del Cameroon"
         "determiner(reference) noun(singular) preposition adjunct(domain)?"
                ),
        NounPPFrame,
        nounPhrase
      )
    );
    
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
          //del Cameroon?
         "determiner(reference) noun(singular)"
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
        //Chi ha creato I Griffin? Who created Family Guy?
        "interrogativePronoun(range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
        "interrogativePronoun(range:singular) verb(mainVerb:past:thirdPerson) object(domain)?",
        //In which museum is the Scream exhibited?
        "interrogativeDeterminerSingular(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPlural(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        //Dammi tutti gli attori che hanno recitato in Last Action Hero.Give me all the actors who have acted in Last Action Hero
        "verb(component_imperative_transitive:present:singular) determiner(component_tutti) determiner(component_have) preposition adjunct(domain).",
        "verb(component_imperative_transitive:present:plural) determiner(component_tutti) determiner(component_have) preposition adjunct(domain).",
         //Cosa contiene un biscotto con gocce di cioccolato?
         "interrogativePronounThingWhat verb(mainVerb:present:thirdPerson) object(domain)?"
         //Quale colore esprime Ricchezza?
         //"interrogativeDeterminerPlural(range:singular) verb(mainVerb:past:thridPerson) object(domain)?"
        /*"interrogativePronoun(nominativeCase:range:singular) verb(component_haben:present:singular) object(domain) verb(mainVerb:perfect:thridPerson)?",    
          //Dammi tutte le rampe di lancio gestite dalla NASA.
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:singular).",
          "verb(imperative_transitive:present:singular) pronoun(object_pronoun) determiner(alle) preposition(von) adjunct(domain) verb(mainVerb:perfect:thridPerson) noun(domain:plural).",
        */        
        ),
       TransitiveFrame,
        PERSON_CAUSE,
        activeTransitive
      )
    );
     
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        /*//"¿Qué series televisivas ideó Walt Disney?"?"¿Qué series televisivas ideó Walt Disney?
        "interrogativeVariableDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(range)?",
        //Dame todas las plataformas de lanzamiento operadas por la NASA.    Dame todos operar operar por ($x | LaunchPad_NP) 
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(domain:singular) verb(mainVerb:perfect:thridPerson) preposition object(range)",
          //¿En qué conflictos militares participó Lawrence de Arabia?
         "interrogativeDeterminerEn(domain:singular) verb(mainVerb:past:thridPerson) object(range)?"
         //Was wurde von ($x | PERSON_NP) entwickelt
        "interrogativePronoun(nominativeCase:domain:singular) verb(component_be:future:plural) preposition object(range) verb(mainVerb:perfect:thridPerson)?",
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
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(plural) verb(mainVerb:present:thridPerson) object(domain)"
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) interrogativeRelative1 determiner(component_hayan) verb(mainVerb:present:thridPerson) object(domain)",
         //┬┐Quien fue el papa que fundo la televisi├│n Vaticana?
         //"interrogativePronoun(range:singular) verb(component_be:was:thirdPerson) noun(range:singular)? interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?"
         "interrogativePronoun(range:singular) verb(component_be:past:singular) noun(range:singular) interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Quién estuvo casado con el Jacques Chirac?
         "interrogativePronoun(range:singular) verb(component_estuvo:present:singular) verb(mainVerb:perfect:thridPerson) object(domain)?",
         //¿Quien actuó como el agente Smith?
         "interrogativePronounPerson verb(mainVerb:present:thridPerson) object(domain)?",
         "interrogativePronounPerson verb(mainVerb:past:thridPerson) object(domain)?"
   
                
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
        //¿Con quien se casó Donald Trump?
        "interrogativePronounWhom verb(component_be:present:singular) verb(mainVerb:past:thridPerson) object(domain)?"
            ),
        TransitiveFrame,
        PERSON_PERSON,
        passiveTransitive
      )
    );
    
    
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"¿Qué series televisivas ideó Walt Disney?"?"
        "interrogativePronounDeterminer(domain:singular) verb(mainVerb:past:thridPerson) object(range)?",
         //Wer moderiert die BBC Wildlife Specials?
        "interrogativePronoun(range:singular) verb(mainVerb:present:thirdPerson) object(domain)?",
        //¿En qué museo está expuesto el Grito?
        "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        //"verb(component_imperative_transitive:present:singular) pronoun(pronoun_personal) determiner(all) noun(plural) verb(mainVerb:present:thridPerson) object(domain)"
         "verb(component_imperative_transitive:present:singular) determiner(component_todos) noun(singular) interrogativeRelative1 determiner(component_hayan) verb(mainVerb:present:thridPerson) object(domain)",
         //┬┐Quien fue el papa que fundo la televisi├│n Vaticana?
         //"interrogativePronoun(range:singular) verb(component_be:was:thirdPerson) noun(range:singular)? interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?"
         "interrogativePronoun(range:singular) verb(component_be:past:singular) noun(range:singular) interrogativeRelative2 verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Quién estuvo casado con el Jacques Chirac?
         "interrogativePronoun(range:singular) verb(component_estuvo:present:singular) verb(mainVerb:perfect:thridPerson) object(domain)?",
         //¿Quien actuó como el agente Smith?
         "interrogativePronounPerson verb(mainVerb:present:thridPerson) object(domain)?",
         "interrogativePronounPerson verb(mainVerb:past:thridPerson) object(domain)?"
   
     
        
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
      
       // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Cuanto costo Pulp Fiction?
        "interrogativeMuch verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeMuch verb(mainVerb:present3rd:thridPerson) object(domain)?",
        //How much did Pulp Fiction cost?
        "interrogativeMuch verb(mainVerb:past:thridPerson) object(domain)?"
              
        ),
       TransitiveFrame,
       HOW_MANY_TOTAL,
       activeTransitive
      )
    );
    
     // TransitiveFrame active
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //Which flim costed X?
        //"interrogativeDeterminer(domain:singular) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
           ),
       TransitiveFrame,
       HOW_MANY_TOTAL,
       passiveTransitive
      )
    );
    
      
       // TransitiveFrame passive amount
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //How many languages are spoken in Turkmenistan? 
        //"interrogativeAmount(domain:plural) verb(component_be:present:plural) verb(mainVerb:perfect:thridPerson) preposition adjunct(range)?" ,
        //How many ethnic groups live in Slovenia?
        //"interrogativeAmount(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?" ,
        ///¿Cuántas veces ha estado casada Jane Fonda?
        //"interrogativeOften verb(component_ha:present:singular) verb(component_estado:present:singular) verb(mainVerb:past:thridPerson) adjunct(range)?"         
                
            ),
        TransitiveFrame,
        HOW_MANY_THING,
        passiveTransitive
      )
    );
    
 
     //In welchem Museum ist der Schrei ausgestellt?
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(   
         //¿En que ciudad termina la ruta Chilena 68?
         "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Por qué ciudades pasa el río Zeravshan?
         "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Dónde empieza Piccadilly?
         "interrogativePlace verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿Donde están situadas las Casas del Parlamento?
         "interrogativePlace2 verb(mainVerb:past:thridPerson) adjunct(domain)?",
         //¿En qué lenguaje de programación esta programado GIMP?
          "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿En qué país nace el Ganges?
         "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿De que región es el vino Melon de Bourgogne?..De que regi�n es vino ($x | Grape_NP)?
          "interrogativeDeterminerDe(range:singular) verb(component_be:present:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿En qué países se habla japonés?
         "interrogativeDeterminerEn(range:singular) verb(component_se:present:singular) verb(mainVerb:present:thridPerson) object(domain)?"

          /*"preposition interrogativeDeterminer(preposition:range:plural) verb(mainVerb:present:thridPerson) adjunct(domain)?",
          //In welchem Museum ist Der Schrei ausgestellt?
         "preposition interrogativeDeterminer(preposition:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:range:plural) verb(component_be:present:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
          // Wo liegt Fort Knox?
          "interrogativePlace verb(mainVerb:present3rd:thridPerson) adjunct(domain)?"  */   

        ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        forward
      )
    );
    //Durch welches Land fließt der Rhein?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //¿Qué ingredientes son necesarios para una tarta de zanahorias?
          "interrogativeVariableDeterminer(range:singular) verb(component_be:present:plural) verb(mainVerb:past:thridPerson) preposition object(range)?"
        /* //Was fließt durch...?
        "interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Welcher Fluss fließt durch...?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Trenn
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(range) verb(TrennVerbPart2:past:thridPerson)? ",
          //Für was steht YiCM?
         "preposition interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) adjunct(domain)?",
         //In welcher Ländern wird Japanisch gesprochen?
         "preposition interrogativeDeterminer(preposition:domain:singular) verb(component_werden:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:domain:plural) verb(component_werden:present:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?" 
         //"preposition interrogativePronounThing(dativeCase:masculine:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         */
        ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_1,
        backward
      )
    );
      
       //In welchem Museum ist der Schrei ausgestellt?
    sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(   
         //¿Que tipo de uva crece en Oregon?
         "interrogativeVariableDeterminer(range:singular) verb(mainVerb:past:thridPerson) preposition object(domain)?"
       
        ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        forward
      )
    );
    //Durch welches Land fließt der Rhein?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        /* //Was fließt durch...?
        "interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Welcher Fluss fließt durch...?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Trenn
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(range) verb(TrennVerbPart2:past:thridPerson)? ",
          //Für was steht YiCM?
         "preposition interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present3rd:thridPerson) adjunct(range)?",
         //In welcher Ländern wird Japanisch gesprochen?
         "preposition interrogativeDeterminer(preposition:domain:singular) verb(component_werden:present:singular) adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "preposition interrogativeDeterminer(preposition:domain:plural) verb(component_werden:present:plural) adjunct(range) verb(mainVerb:perfect:thridPerson)?" 
         //"preposition interrogativePronounThing(dativeCase:masculine:singular) verb(component_be:present:singular) adjunct(range) verb(mainVerb:perfect:thridPerson)?",
       */
        ),
        IntransitivePPFrame,
        WHAT_WHICH_PRESENT_THING_2,
        backward
      )
    );
      
      
      
       sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(           
      //¿En que ciudad termina la ruta Chilena 68?
         "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Por qué ciudades pasa el río Zeravshan?
         "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
         //¿Dónde empieza Piccadilly?
          "interrogativePlace verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿En qué país se encuentra el lago Limerick?
           "interrogativeDeterminerEn(range:singular) verb(component_se:present:singular) verb(mainVerb:past:thridPerson) object(domain)?"
          //¿En qué estado american se encuentra Fort Knox?
                
                ),
        IntransitivePPFrame,
        WHAT_WHICH_LOCATION,
        forward
      )
    );
    
    
    
    
      sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
            //Was fließt durch...?
       /* "interrogativePronoun(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Welcher Fluss fließt durch...?
        "interrogativeDeterminer(nominativeCase:domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
        "interrogativeDeterminer(nominativeCase:domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         //Trenn
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(range) verb(TrennVerbPart2:past:thridPerson)? "
          */  ),
        IntransitivePPFrame,
        WHAT_WHICH_LOCATION,
        backward
      )
    );
      
      ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //"¿Cuando se completo el Titanic?
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:present:thridPerson) object(domain)?",
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:past:thridPerson) object(domain)?"
          //┬┐Cu├índo tuvo lugar la batalla de Gettysburg?
          //"interrogativeTemporal verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿Quién es el anfitrión de la American Idol?
          //"interrogativePronounPerson verb(component_be:present:singular) verb(mainVerb:past:thridPerson) object(domain)?"
        ),
        IntransitivePPFrame,
        WHEN_WHAT_PAST_THING,
        forward
      )
    );
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         /*//Was wurde 2010 fertiggestellt?
         "interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)",
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)",
         "interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)?",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)?",
         "interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)?"
         //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_werden:past:plural) preposition adjunct(range) verb(TrennVerb:perfect:thridPerson)",
         //"interrogativePronoun(nominativeCase:domain:singular) verb(component_werden:past:singular) preposition adjunct(range) verb(mainVerb:past:thridPerson)?"
        */),
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
          //"¿En qué ciudad murió John F. Kennedy?",
         "interrogativeDeterminerEn(range:plural) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         //"¿Cuándo murió la princesa Diana?
         "interrogativeTemporal verb(mainVerb:past:thridPerson) adjunct(domain)?",
         //¿Cúando se fundó Jack Wolfskin?
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:present:thridPerson) adjunct(domain)?",
          "interrogativeTemporal verb(component_se:present:singular) verb(mainVerb:past:thridPerson) adjunct(domain)?",
          //¿En que año nació Rachel Stevens?
          "interrogativeDeterminerEn(range:singular) verb(mainVerb:perfect:thridPerson) adjunct(domain)?",
          "interrogativeDeterminerEn(range:plural) verb(mainVerb:perfect:thridPerson) adjunct(domain)?"
        // "preposition interrogativeDeterminer(dativeCase:range:singular) verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
         //Wann wurde Abrham Lincon geboren?
        //"interrogativeTemporal verb(component_werden:past:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?",
        //Wann ist Draculas Sch├Âpfer gestorben?
        //"interrogativeTemporal verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"
        /* "interrogativeDeterminerEn(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerEn(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:singular) verb(mainVerb:past:thridPerson) object(domain)?",
        "interrogativeDeterminerPor(range:plural) verb(mainVerb:present:thridPerson) object(domain)?",*/
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
      
    
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //¿En qué ciudad murió John F. Kennedy?
         "interrogativeDeterminerEn(domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
         "interrogativeDeterminerEn(domain:plural) verb(mainVerb:past:thridPerson) preposition adjunct(range)?"
         
        ),
        IntransitivePPFrame,
        WHO_WHO_PERSON,
        forward
      )
    );
      
      
      
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
          //¿Qué surfistas profesionales nacieron en las Filipinas?
         "interrogativeVariableDeterminer(range:singular) verb(mainVerb:present:thridPerson) preposition adjunct(domain)?",   
         "interrogativeVariableDeterminer(range:plural) verb(mainVerb:present:thridPerson) preposition adjunct(domain)?",
         //¿Dónde está enterrado Syngman Rhee?
        "interrogativePlace verb(component_be:past:singular) verb(mainVerb:past:thridPerson) adjunct(domain)?",
         //¿Dónde nació Bach?
         "interrogativePlace verb(mainVerb:past:thridPerson) adjunct(domain)?"
                   ),
        IntransitivePPFrame,
        WHERE_WHO_PAST_PERSON,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
    
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(     
          //¿En qué ciudad murió John F. Kennedy?
        "interrogativeDeterminerEn(domain:singular) verb(mainVerb:past:thridPerson) adjunct(range)?",
        "interrogativeDeterminerEn(domain:singular) verb(mainVerb:present:thridPerson) adjunct(range)?",
        //¿Qué surfistas profesionales nacieron en las Filipinas?
        "interrogativeVariableDeterminer noun(domain:singular) verb(mainVerb:present:thridPerson) preposition adjunct(range)?",
        "interrogativeVariableDeterminer noun(domain:plural) verb(mainVerb:present:thridPerson) preposition adjunct(range)?"
        
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
        //¿De qué murió Abraham Lincoln?
        "interrogativeCause verb(mainVerb:past:thridPerson) adjunct(domain)?",
        //¿Quién era llamado Frank The Tank?
        "interrogativePronounPerson verb(component_be:present:singular) verb(mainVerb:past:thridPerson) adjunct(domain)?"
        //An was ist Bruce Carver gestorben?
        //"preposition interrogativePronoun(nominativeCase:range:singular) verb(component_be:present:singular) adjunct(domain) verb(mainVerb:perfect:thridPerson)?"     
               ),
        IntransitivePPFrame,
        PERSON_CAUSE,
        forward
      )
    );
    //Welche Person wurde 2010 geboren?
    //Wer ist 2010 geboren?
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
        //"Wer ist an Malaria gestorben?
        //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:present:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?",
        //"Wer war an Malaria gestorben?
        //"interrogativePronoun(nominativeCase:domain:singular) verb(component_be:past:singular) preposition adjunct(range) verb(mainVerb:perfect:thridPerson)?"
      
          ),
        IntransitivePPFrame,
        PERSON_CAUSE,
        backward
      )
    );
      
      
      
      
             ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
         //Wo f├ñngt Piccadilly an? Trenn example
         "interrogativePlace verb(TrennVerbPart1:present3rd:thridPerson) object(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         "interrogativePlace verb(TrennVerbPart1:past:thridPerson) object(domain) verb(TrennVerbPart2:past:thridPerson)?"
             ),
        IntransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        backward
      )
    );
      sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //Was beginnt in Piccadilly?      
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:present3rd:thridPerson) preposition adjunct(domain) verb(TrennVerbPart2:present3rd:thridPerson)?",
         "interrogativePronoun(nominativeCase:domain:singular) verb(TrennVerbPart1:past:thridPerson) preposition adjunct(domain) verb(TrennVerbPart2:past:thridPerson)?"
        ),
        IntransitivePPFrame,
        WHERE_WHAT_PRESENT_THING,
        forward
      )
    );
      
          ///////////////////////////////
       sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(
         //¿Cuántas personas viven en Polonia?
          "interrogativeAmount(range:plural) verb(mainVerb:present:thridPerson) preposition object(domain)?"  
         //was kostet der film?
          //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:present:thridPerson) determiner(component_the_nominative:domain) object(domain)?"
         //"Wieviele Sprachen werden in Turkmenistan gesprochen?"  
         //"interrogativeAmountDeterminer(nominativeCase:range:singular) verb(component_werden:present:singular) preposition determiner(component_the_nominative:domain) object(domain) verb(mainVerb:perfect:thridPerson)?"  

       
        //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      
       
    
        ),
        IntransitivePPFrame,
        HOW_MANY_TOTAL,
        forward
      )
    );
   
      sentenceTemplateRepository.add(
      createSentenceTemplate(language,
        List.of(  //Welches Buch kostet 10 Dollar?
        //"interrogativePronoun(nominativeCase:present:singular) verb(mainVerb:perfect:present) object(range)?",
        //"interrogativeDeterminer(nominativeCase:domain:singular) verb(component_haben:present:singular) object(range) verb(mainVerb:perfect:singular)?"      
        //¿Que ciudad tiene la mayor población?
           
        ),
        IntransitivePPFrame,
        HOW_MANY_TOTAL,
        backward
      )
    );
    
      // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Cuál es la montaña más alta de Australia? 
        "interrogativePronounThing verb(component_be:present:singular) noun(range:singular) adjective(superlative) preposition adjunct(domain)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativeCountry,
        forward
      )
    );
    
        // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Quién es el jugador mas jóven de dardos?
       "interrogativePronounPerson verb(component_be:present:singular) adjective(superlative) noun(range:singular)?",
        //Quien es el jugador de baloncesto mas alto?
       "interrogativePronounPerson verb(component_be:present:singular) noun(range:singular) adjective(superlative)?"
           
        ),
        AdjectiveSuperlativeFrame,
        superlativeWorld,
        forward
      )
    );
    
     // AdjectivePPFrame...superlative
    sentenceTemplateRepository.add(createSentenceTemplate(language,
        List.of(
        //¿Cuál es el hijo mas mayor de Meryl Streep? ¿Cuál es m�s viejo ni�o of ($x | Person_NP)?
        "interrogativePronounThing verb(component_be:present:singular) noun(range:singular) adjective(superlative) preposition adjunct(domain)?"
        ),
        AdjectiveSuperlativeFrame,
        superlativePerson,
        forward
      )
    );
    
         // AdjectivePPFrame...adjectiveBaseForm
    sentenceTemplateRepository.add(
      createSentenceTemplate(
        language,
        List.of(
        //¿Cómo de alta el faro de Colombo? How es adjectiveBaseForm XX faro?
         "interrogativeEvalution verb(component_be:present:singular) adjective(adjectiveBaseForm)  noun(range:singular) preposition adjunct(range)?",
         "interrogativeEvalution verb(component_be:present:singular) adjective(adjectiveBaseForm) noun(range:singular) preposition adjunct(range)?",
        //¿Cómo de alta es Claudia Schiffer?"¿Cómo adjectiveBaseForm es ($x | THING_NP)?
        "interrogativeEvalution preposition adjective(adjectiveBaseForm) verb(component_be:present:singular) adjunct(range)?",
        //¿Cómo de alta el faro de Colombo?
         "interrogativeEvalution preposition preposition adjective(adjectiveBaseForm) noun(range:singular) adjunct(range)?",
         //¿Cómo de alta el la torre Yokohama Marine?
          "interrogativeEvalution preposition preposition adjective(adjectiveBaseForm) adjunct(range)?"
      ),
        AdjectiveSuperlativeFrame,
        adjectiveBaseForm,
        forward
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
      
      
   }


}