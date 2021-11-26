/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.datasets.sentencetemplates;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import grammar.generator.SubjectType;
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
    public static String FULL_DATASET = "FULL_DATASET";

    public static String subject = "subject";
    public static String directObject = "directObject";
    public static String object = "object";
    public static String interrogativePronoun = "interrogativePronoun";
    public static String interrogativeDeterminer = "interrogativeDeterminer";
    public static String mainVerb = "mainVerb";
     public static String TrennVerb = "TrennVerb";
    public static String TrennVerbPart2 = "TrennVerbPart2";
    public static String TrennVerbPart1 = "TrennVerbPart1";
    public static final String verb = "verb";

    public static String questionMark = "?";
    public static String space = " ";
    public static String DETERMINER = "determiner";

    public static String active = "active";
    public static String passive = "passive";

    public static String caseType = "case";
    public static String gender = "gender";

    public static String PREPOSITION = "preposition";
    public static String QUESTION_MARK = "?";
    public static String FULL_STOP = ".";
    public static String variableIndicator = "X";
    public static String colon = ":";
    public static String present = "present";
    public static String past = "past";
     public static String infinitive="infinitive";

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
    public static String defaultGender = "masculine";
    public static String defaultNumber =singular;

    public static String article = "article";
    public static String thirdPerson = "thirdPerson";
    public static String secondPerson = "secondPerson";

    public static final String whQuestion = "whQuestion";
    public static final String booleanQuestionDomainRange = "booleanQuestion";
    public static final String booleanQuestionDomain = "booleanQuestionWithoutReference";
    public static final String nounPhrase = "nounPhrase";
    public static final String noun = "noun";
    public static final String location = "location";

    public static Set<String> tenses = new HashSet<String>(Arrays.asList(present, past, perfect, future));
    public static Set<String> numbers = new HashSet<String>(Arrays.asList(singular, plural));
    public static Set<String> persons = new HashSet<String>(Arrays.asList(secondPerson, thirdPerson));

    public static final String WHEN_WHAT_PAST_THING = "WHEN_WHAT_PAST_THING";
    public static final String WHEN_WHO_PAST_PERSON = "WHEN_WHO_PAST_PERSON";
    public static final String WHERE_WHO_PAST_PERSON = "WHERE_WHO_PAST_PERSON";
    public static final String WHAT_WHICH_PRESENT_THING = "WHAT_WHICH_PRESENT_THING";
    public static final String HOW_MANY_PRESENT_THING = "HOW_MANY_PRESENT_THING";


    public static final String Adjunct = "adjunct";
    public static final String determiner = "determiner";
    public static final String question = "question";

    public static final String forward = "forward";
    public static final String backward = "backward";
    public static final String preposition = "preposition";
    public static final String adjunct = "adjunct";
    public static final String QuestionMark = "?";
    public static final String component_be = "component_be";
    public static final String component_aux_object_past = "component_aux_object_past";

    public static final String DIRECT_OBJECT = "directObject";
    public static String pronoun = "pronoun";
        
 

}
