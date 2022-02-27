/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation;

import com.google.gdata.util.common.base.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class StringSimilarity {
    
    private boolean singleSparql=false;
    private boolean multipleSparql=false;
    private String singleProperty=null;
    private String singleSubject=null;
    private String singleObject=null;

    
    
     public QueGGinfomation getMostSimilarMatch(Map<String, QueGGinfomation> grammarEntities) throws Exception {
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (String question : grammarEntities.keySet()) {
            QueGGinfomation queGGinfomation = grammarEntities.get(question);
            map.put(question, queGGinfomation.getValue());
        }
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
        sorted_map.putAll(map);
        //System.out.println("sorted_map: " + sorted_map);
        String key = sorted_map.firstKey();
        Double value = sorted_map.get(key);
   
        QueGGinfomation queGGinfomation = grammarEntities.get(key);
        return queGGinfomation;

    }
      public QueGGinfomation getMostSimilarMatchKB(Map<String, QueGGinfomation> grammarEntities,String qaldSparqlQuery) throws Exception {
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (String question : grammarEntities.keySet()) {
            QueGGinfomation queGGinfomation = grammarEntities.get(question);
            map.put(question, queGGinfomation.getValue());
        }
        if(this.isSparqlMatched(grammarEntities,qaldSparqlQuery)){
            
        }
        QueGGinfomation queGGinfomation = this.getBestMatchingWithOutKB(grammarEntities,map);
      
        return queGGinfomation;

    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
            /* both strings are zero length */ }
        /* // If you have StringUtils, you can use it to calculate the edit distance:
        return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                                                             (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://r...content-available-to-author-only...e.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    public static void printSimilarity(String s, String t) {
        System.out.println(String.format(
                "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
    }

    public static void main(String[] args) {
        printSimilarity("who was samuel schmid vice president?", ":who was vice president of samuel schmid?");
       
    }

   

    private QueGGinfomation getBestMatchingWithOutKB(Map<String, QueGGinfomation> grammarEntities, HashMap<String, Double> map) {
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
        sorted_map.putAll(map);
        String key = sorted_map.firstKey();
        Double value = sorted_map.get(key);
        QueGGinfomation queGGinfomation = grammarEntities.get(key);
        return queGGinfomation;
    }

   
    private boolean isSparqlMatched(Map<String, QueGGinfomation> grammarEntities, String qaldSparqlQuery) {
        Boolean sparqlMatchFlag=false;
        for (String question : grammarEntities.keySet()) {
             QueGGinfomation queGGinfomation = grammarEntities.get(question);
             String queGGSparql=queGGinfomation.getSparqlQuery();
             Pair<Boolean,String []> qaldSingleTriple=this.isSingle(queGGSparql);
             Pair<Boolean,String []> queGGSingleTriple=this.isSingle(qaldSparqlQuery);
             
             if(qaldSingleTriple.first&&queGGSingleTriple.first){
               for(Integer index=0;index<qaldSingleTriple.second.length;index++){
                   if(index==0){
                       
                   }
                   
               }
             }
             return true;
        }
        return true;
    }

    ////SELECT DISTINCT ?d WHERE { <http://dbpedia.org/resource/Diana,_Princess_of_Wales> <http://dbpedia.org/ontology/deathDate> ?d . }
    private Pair<Boolean, String[]> isSingle(String queGGSparql) {
        String triples = null;
        Boolean singleSparql = false;
        String singleSubject = null, singleProperty = null;
        String[] values = new String[3];

        triples = StringUtils.substringBetween(queGGSparql, "{", "}");
        Integer numOfTriple = this.characterCount(triples, '.');
        if (numOfTriple == 1) {
            singleSparql = true;
            triples = triples.trim().stripLeading().stripTrailing();
            triples = triples.replace(" ", "\n");
            String[] lines = triples.split(System.getProperty("line.separator"));
            Integer index = 0;
            for (String line : lines) {

                if (index == 0) {
                    if (line.contains("http://dbpedia.org/resource/") || line.contains("res:")) {
                       values[index] = line;
                    }
                } else if (index == 1) {
                    if ((line.contains("http://dbpedia.org/ontology/") || line.contains("http://dbpedia.org/property/") || line.contains("dbo:") || line.contains("dbp:")) && index == 1) {
                        values[index] = line;
                    }
                } else if (index == 2) {
                    if (line.contains("http://dbpedia.org/resource/") || line.contains("res:")) {
                        values[index] = line;
                    }
                }
                index=index+1;

            }

        }
      
        return new Pair<Boolean, String[]>(singleSparql, values);

    }
   
    
    public Integer characterCount(String exampleString, Character symbol) {
       
       
      
        int totalCharacters = 0;
        char temp;
        for (int i = 0; i < exampleString.length(); i++) {

            temp = exampleString.charAt(i);

            if (temp == symbol) {
                totalCharacters=totalCharacters+1;
            }
        }

        return totalCharacters;
    }

    public boolean isMultipleSparqlQuery(String qaldSparqlQuery) {
        String triples = StringUtils.substringBetween(qaldSparqlQuery, "{", "}");
         triples=this.replacePrefix(triples);
        Integer numOfTriple = this.characterCount(triples, '+');
        /*if ((numOfTriple>1) && !triples.contains("rdf:type")) {
            return true;
        } */
        
        if (triples.contains("UNION")) {
            return true;
        } else if (numOfTriple == 2 && !triples.contains("rdf:type")) {
            return true;
        } else if (triples.contains("Philippines")) {
            return true;
        }else if (numOfTriple > 2) {
            return true;
        }

        
        /*if ((numOfTriple == 2) && triples.contains("rdf:type")) {
            return false;
        } else if ((numOfTriple == 2) && !triples.contains("rdf:type")) {
            return true;
        }
        else if ((numOfTriple > 2) ) {
            return true;
        }*/
        return false;
    }
    
    
    boolean isAskSparqlQuery(String qaldSparqlQuery) {
        if(qaldSparqlQuery.contains("ASK")){
            return true;
        }
        return false;
    }

    private String replacePrefix(String exampleString) {
        exampleString = exampleString.replace("\n", " ");
        exampleString = exampleString.replace("http://dbpedia.org/resource/", "res:");
        exampleString = exampleString.replace("http://dbpedia.org/ontology/", "dbo:");
        exampleString = exampleString.replace("http://dbpedia.org/property/", "dbp:");
        exampleString = exampleString.replace("http://dbpedia.org/property/", "dbp:");

        exampleString = exampleString.replace(" . ", "+");
        return exampleString;
    }

}
