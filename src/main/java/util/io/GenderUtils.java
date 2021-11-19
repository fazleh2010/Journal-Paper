/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.structure.component.Language;
import static java.lang.System.exit;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class GenderUtils {

    private String article = null;
    private String[] articles = new String[2];
    public static Map<String, String[]> referenceArticleMap = new TreeMap<String, String[]>();
    public static Map<String, String[]> dbpediaClassMap = new TreeMap<String, String[]>();

    public static void setArticles(Map<String, String[]> referenceArticleMapT) {
        referenceArticleMap = referenceArticleMapT;
    }

    public static Map<String, String[]> getArticles() {
        return referenceArticleMap;
    }

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

    public GenderUtils(Language language, String reference) {
        System.out.println("language::" + language + " reference:" + reference);

        if (referenceArticleMap.containsKey(reference)) {
            this.articles = referenceArticleMap.get(reference);
        } else {
            this.article = "XXXXX";
        }

    }

    public GenderUtils(String domain) {
        if (referenceArticleMap.containsKey(domain)) {
            article = referenceArticleMap.get(domain)[0];
        } else {
            article = "";
        }
    }

    GenderUtils(String noun, Language language) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getArticle() {
        return article;
    }

    public static String getManuallyCreatedLabel(String uri) {
        if (dbpediaClassMap.containsKey(uri)) {
            return dbpediaClassMap.get(uri)[0];
        }
        return null;
    }
    public static void display() {
        for (String key : GenderUtils.referenceArticleMap.keySet()) {
            System.out.println("key::" + key);
            String[] articles = GenderUtils.referenceArticleMap.get(key);
            for (String article : articles) {
                System.out.println("article::" + article);
            }
        }
    }
    

}
