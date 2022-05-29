/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.datasets.sentencetemplates;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import grammar.generator.SubjectType;
import static grammar.sparql.SelectVariable.reference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public interface TempConstants {

    public static String NounPPFrame = "NounPPFrame";
    public static String TransitiveFrame = "TransitiveFrame";
    public static String IntransitivePPFrame = "IntransitivePPFrame";
    public static String AdjectiveAttributiveFrame = "AdjectiveAttributiveFrame";
    public static String AdjectivePPFrame = "AdjectivePPFrame";
    public static String AdjectiveSuperlativeFrame = "AdjectiveSuperlativeFrame";
    public static String FULL_DATASET = "FULL_DATASET";

    public static String subject = "subject";
    public static String directObject = "directObject";
    public static String object = "object";
    public static String mainVerb = "mainVerb";
    public static String TrennVerb = "TrennVerb";
     public static String imperative="imperative";
    public static String auxVerb = "auxVerb";
    public static String RefVerb = "RefVerb";
    public static String TrennVerbPart2 = "TrennVerbPart2";
    public static String TrennVerbPart1 = "TrennVerbPart1";
    public static final String verb = "verb";
    public static final String pronounReflexive = "reflexive_pronoun";
    
    public static final String dativeCase = "dativeCase";
    public static final String accusativeCase = "accusativeCase";
    public static final String appos = "appos";
    
    

    public static String questionMark = "?";
    public static String space = " ";
    public static String DETERMINER = "determiner";

    public static String activeTransitive = "active";
    public static String activeBoolean = "activeBoolean";
    public static String passiveTransitive = "passive";
    public static String amount = "amount";

    public static String caseType = "case";
    public static String gender = "gender";

    public static String PREPOSITION = "preposition";
    public static String QUESTION_MARK = "?";
    public static String FULL_STOP = ".";
    public static String variableIndicator = "X";
    public static String colon = ":";
    public static String present = "present";
    public static String present3rd = "present3rd";
    public static String past = "past";
    public static String infinitive = "infinitive";

    public static String perfect = "perfect";
    public static String future = "future";
    public static String tense = "tense";
    public static String number = "number";
    public static String person = "person";
    public static String SLASH = "#";
    public static String singular = "singular";
    public static String plural = "plural";
    public static String range = "range";
    public static String domain = "domain";
    public static String property = "property";
    public static String defaultGender = "masculine";
    public static String defaultNumber = singular;

    public static String article = "article";
    public static String thirdPerson = "thirdPerson";
    public static String secondPerson = "secondPerson";

    public static final String whQuestion = "whQuestion";
    public static final String booleanQuestionDomainRange = "booleanQuestion";
    public static final String booleanQuestionDomain = "booleanQuestionWithoutReference";
    public static final String nounPhrase = "nounPhrase";
    public static final String noun = "noun";
    public static final String location = "location";

    public static Set<String> TENSES = new HashSet<String>(Arrays.asList(infinitive,present, present3rd,past, perfect, future));
    public static Set<String> NUMBERS = new HashSet<String>(Arrays.asList(singular, plural));
    public static Set<String> PERSONS = new HashSet<String>(Arrays.asList(secondPerson, thirdPerson));

    public static final String WHEN_WHAT_PAST_THING = "WHEN_WHAT_PAST_THING";
    public static final String WHAT_WHICH_PRESENT_THING_1 = "WHAT_WHICH_PRESENT_THING_1";
    public static final String WHAT_WHICH_PRESENT_THING_2 = "WHAT_WHICH_PRESENT_THING_2";
    public static final String HOW_MANY_TOTAL = "HOW_MANY_TOTAL";
    public static final String HOW_MANY_THING = "HOW_MANY_THING";
    public static final String WHERE_WHAT_PRESENT_THING = "WHERE_WHAT_PRESENT_THING";
    public static final String WHAT_WHICH_LOCATION = "WHAT_WHICH_LOCATION";
    public static final String WHAT_WHICH_LOCATION_COMPANY = "WHAT_WHICH_LOCATION_COMPANY";

    
    public static final String PERSON_PERSON = "PERSON_PERSON";
    
    
     
    public static final String WHO_WHO_PERSON = "WHO_WHO_PERSON";
     public static final String WHEN_WHO_PAST_PERSON = "WHEN_WHO_PAST_PERSON";
     public static final String WHERE_WHO_PAST_PERSON = "WHERE_WHO_PAST_PERSON";
     public static final String PERSON_CAUSE = "PERSON_CAUSE";
     public static final String PERSON_CAUSE_SUBJECT  = "PERSON_CAUSE_SUBJECT";
     public static final String PERSON_CAUSE_SUBJECT_PREPOSITION  = "PERSON_CAUSE_SUBJECT_PREPOSITION";
     
     public static final String PERSON_ACTIVITY = "PERSON_ACTIVITY";

    
    //public static final String WHAT_PERSON_THING = "WHAT_PERSON_THING";
    //public static final String WHO_PERSON_THING = "WHO_PERSON_THING";

    public static final String Adjunct = "adjunct";
    public static final String determiner = "determiner";
    public static final String question = "question";

    public static final String forward = "forward";
    public static final String backward = "backward";
    public static final String preposition = "preposition";
    public static final String Apostrophe = "Apostrophe";
    public static final String particleLocation="particleLocation";
    public static final String adjunct = "adjunct";
    public static final String QuestionMark = "?";
    public static final String component_be = "component_be";
    public static final String component_do ="component_do";
    public static final String component_se = "component_se";
    public static final String component_ha = "component_ha";
    public static final String component_estado = "component_estado";
    public static final String component_esta = "component_esta";
    
    public static final String component_aux_object_past = "component_aux_object_past";
    public static final String componentVerb = "componentVerb";

    public static final String DIRECT_OBJECT = "directObject";
    public static String pronoun = "pronoun";
    public static String adjective = "adjective";
    public static String superlative = "superlative";

    public static String comperative = "comparative";
    public static String superlativeCountry = "superlativeCountry";
    public static String superlativePerson = "superlativePerson";
    public static String superlativeLocation="superlativeLocation";

    public static String superlativeWorld = "superlativeWorld";
    public static String adjectiveBaseForm="adjectiveBaseForm";
    public static String degree = "degree";
    
    public static String past_imperfect="past_imperfect";
    public static String past_indefinite="past_indefinite";
    
    public static String interrogativePronounPerson = "interrogativePronounPerson";
    public static String interrogativePronounThing = "interrogativePronounThing";
    public static String interrogativePronoun = "interrogativePronoun";
    public static String interrogativeDeterminer = "interrogativeDeterminer";
    public static String interrogativePronounThingPlural="interrogativePronounThingPlural";

}
