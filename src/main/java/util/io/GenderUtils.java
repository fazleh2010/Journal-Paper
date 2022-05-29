/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.sparql.SelectVariable;
import static grammar.sparql.SelectVariable.reference;
import grammar.structure.component.Language;
import static java.lang.System.exit;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class GenderUtils implements TempConstants {

    public static Map<String, String[]> referenceArticleMap = new TreeMap<String, String[]>();
    public static Map<String, String[]> nounWrittenForms = new TreeMap<String, String[]>();
    public static Map<String, Map<String, String>> trennVerb = new TreeMap<String, Map<String, String>>();
    public static Map<String, Map<String, String>> perfectVerb = new TreeMap<String, Map<String, String>>();
    public static Map<String, Map<String, String>> refVerb = new TreeMap<String, Map<String, String>>();
    public static Map<String, String> prepositionCase = new TreeMap<String, String>();

    static {
        prepositionCase.put("durch", accusativeCase);
        prepositionCase.put("fuer", accusativeCase);
        prepositionCase.put("in", dativeCase);
    }

    public static void setPrepositionCase(String preposition, String genderCase) {
        prepositionCase.put(preposition, genderCase);
    }

    public static void setWrittenForms(String uri, String writtenSingular, String writtenPlural) {
        nounWrittenForms.put(uri, new String[]{writtenSingular, writtenPlural});
    }

    public static void setArticles(String uri, String artile) {
        referenceArticleMap.put(uri, new String[]{artile});
    }

    public static void setVerbTypes(String partOfSpeech, String[] verbs, Map<String, String> verbTypes) {
        for (String key : verbTypes.keySet()) {
            if (partOfSpeech.contains(RefVerb)) {
                setRefVerb(verbs, verbTypes);
            } else if (partOfSpeech.contains(TrennVerb)) {
                setTrennVerb(verbs, verbTypes);
            }
            setPerfectVerb(verbs, verbTypes);

        }

    }

    public static String getArticle(String domain) {
        String article = "XX";
        if (referenceArticleMap.containsKey(domain)) {
            article = referenceArticleMap.get(domain)[0];
        }
        return article;
    }

    public static String getWrittenFormSingular(String uri) {
        if (nounWrittenForms.containsKey(uri)) {
            return nounWrittenForms.get(uri)[0];
        }
        return "XX";
    }

    public static String getWrittenFormPlural(String uri) {
        if (nounWrittenForms.containsKey(uri)) {
            return nounWrittenForms.get(uri)[1];
        }
        return "XX";
    }

    public static Pair<Boolean, String> getTrennVerbType(String key, String tense, String type) {
        Map<String, String> verb = trennVerb.get(key);
        if (verb.containsKey(tense)) {
            String trennVerb = verb.get(tense);
            if (trennVerb.contains(" ")) {
                String[] info = trennVerb.split(" ");
                if (type.contains(TrennVerbPart1)) {
                    return new Pair<Boolean, String>(Boolean.TRUE, info[0]);
                } else if (type.contains(TrennVerbPart2)) {
                    return new Pair<Boolean, String>(Boolean.TRUE, info[1]);
                }
            } else {
                return new Pair<Boolean, String>(Boolean.TRUE, trennVerb);
            }
        }
        return new Pair<Boolean, String>(Boolean.FALSE, key);

    }

    public static Pair<Boolean, String> getPerfecterbType(String key, String tense) {
        Pair pair = new Pair<Boolean, String>(Boolean.FALSE, key);
        if (perfectVerb.containsKey(key)) {
            Map<String, String> verb = perfectVerb.get(key);
            if (verb.containsKey(tense)) {
                pair = new Pair<Boolean, String>(Boolean.TRUE, verb.get(tense));
            }
        }
        return pair;
    }

    public static String getConditionLabelManually(String domainOrRange, String numberType, String subjectUri, String objectUri) {
        String word = "XX";
        if (domainOrRange.contains(domain) && numberType.contains(singular)) {
            word = getWrittenFormSingular(subjectUri);
        } else if (domainOrRange.contains(domain) && numberType.contains(plural)) {
            word = getWrittenFormPlural(subjectUri);
        } else if (domainOrRange.contains(range) && numberType.contains(singular)) {
            word = getWrittenFormSingular(objectUri);
        } else if (domainOrRange.contains(range) && numberType.contains(plural)) {
            word = getWrittenFormPlural(objectUri);
        }

        return word;
    }

    /*public static Boolean isTrennVerbType(String verb) {
        if (trennVerbType.containsKey(verb)) {
            return true;
        }
        return false;
    }*/

 /*public static String getManuallyCreatedLabel(String uri) {
        if (dbpediaClassMap.containsKey(uri)) {
            return dbpediaClassMap.get(uri)[0];
        }
        return null;
    }*/
    public static void display() {
        for (String key : referenceArticleMap.keySet()) {
            System.out.println("key::" + key);
            String[] articles = referenceArticleMap.get(key);
            for (String article : articles) {
                System.out.println("article::" + article);
            }
        }

        for (String key : nounWrittenForms.keySet()) {
            String[] values = nounWrittenForms.get(key);
            System.out.println(key);
            for (String value : values) {
                System.out.println(value);
            }
        }

    }

    private static void setRefVerb(String[] verbs, Map<String, String> verbTypes) {
        for (String verb : verbs) {
            refVerb.put(verb, verbTypes);
        }
    }

    private static void setTrennVerb(String[] verbs, Map<String, String> verbTypes) {
        for (String verb : verbs) {
            trennVerb.put(verb, verbTypes);
        }
    }

    private static void setPerfectVerb(String[] verbs, Map<String, String> verbTypes) {
        for (String verb : verbs) {
            perfectVerb.put(verb, verbTypes);
        }
    }

    public static Map<String, String> getPrepositionCase() {
        return prepositionCase;
    }

    public static String getPrepositionCase(String key) {
        if (prepositionCase.containsKey(key)) {
            return prepositionCase.get(key);
        }
        return null;
    }

}
