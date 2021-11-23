/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.sentencetemplates.TempConstants;
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
public class GenderUtils implements TempConstants{

    public static Map<String, String[]> referenceArticleMap = new TreeMap<String, String[]>();
    public static Map<String, String[]> writtenForms = new TreeMap<String, String[]>();
    public static Map<String,Pair<String,String>> trennVerbType = new TreeMap<String,Pair<String,String>>();
    public static Map<String,ParamterFinder> verbDetail = new TreeMap<String,ParamterFinder>();

    public static void setWrittenForms(String uri, String writtenSingular, String writtenPlural) {
        writtenForms.put(uri, new String[]{writtenSingular, writtenPlural});
    }

    public static void setArticles(String uri, String artile) {
        referenceArticleMap.put(uri, new String[]{artile});
    }
    
    
    public static void setVerbType(String verb, String type) {
        if (verb != null&&type.contains(separableVerb)) {
           if(verb.contains(" ")){
              String []info= verb.split(" ");
              trennVerbType.put(verb, new Pair<String,String>(info[0],info[1]));
           }
        }

    }

    

    /*public static void setArticles(Map<String, String[]> referenceArticleMapT) {
        referenceArticleMap = referenceArticleMapT;
    }*/

   
    static {

    }

    static {
        /*referenceArticleMap.put("http://dbpedia.org/ontology/capital", new String[]{"feminine"});
        referenceArticleMap.put("http://dbpedia.org/ontology/City", new String[]{"feminine"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Person", new String[]{"feminine"});
        referenceArticleMap.put("http://dbpedia.org/ontology/officialLanguage", new String[]{"feminine"});
        referenceArticleMap.put("http://dbpedia.org/ontology/populationTotal", new String[]{"feminine"});
        referenceArticleMap.put("http://dbpedia.org/ontology/locatedInArea", new String[]{"masculine"});
        referenceArticleMap.put("http://dbpedia.org/ontology/editor", new String[]{"masculine"});
        referenceArticleMap.put("http://dbpedia.org/ontology/child", new String[]{"neuter"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Mountain", new String[]{"masculine"});
        referenceArticleMap.put(" http://dbpedia.org/ontology/Place", new String[]{"masculine"});*/

    }

    /*public GenderUtils(Language language, String reference) {
        System.out.println("language::" + language + " reference:" + reference);

        if (referenceArticleMap.containsKey(reference)) {
            this.articles = referenceArticleMap.get(reference);
        } else {
            this.article = "XXXXX";
        }

    }*/

 /*public GenderUtils(String domain) {
        if (referenceArticleMap.containsKey(domain)) {
            article = referenceArticleMap.get(domain)[0];
        } else {
            article = "";
        }
    }*/
    public static String getArticle(String domain) {
        String article = "XX";
        if (referenceArticleMap.containsKey(domain)) {
            article = referenceArticleMap.get(domain)[0];
        }
        return article;
    }

    public static String getWrittenFormSingular(String uri) {
        return writtenForms.get(uri)[0];
    }

    public static String getWrittenFormPlural(String uri) {
        return writtenForms.get(uri)[1];
    }
    
    public static String getTrennVerbType(String verb, String type) {
        Pair<String, String> pair = trennVerbType.get(verb);
        if (type.contains(TrennVerbPart1)) {
            return pair.first;
        } else {
            return pair.second;
        }
    }
    
    public static Boolean isTrennVerbType(String verb) {
        if (trennVerbType.containsKey(verb)) {
            return true;
        }
        return false;
    }

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
        
        
    }

   
    

}
