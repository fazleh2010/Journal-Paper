/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.structure.component.Language;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class GenderUtils {

    private String article = null;
    private String[] articles =new String[2];
    private static Map<String,String[]> referenceArticleMap = new TreeMap<String,String[]>();
    
    static {
        referenceArticleMap.put("http://dbpedia.org/ontology/areaCode", new String[]{"il"});
        referenceArticleMap.put("http://dbpedia.org/ontology/author", new String[]{"il", "la"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Film", new String[]{"il"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Bridge", new String[]{"il"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Book", new String[]{"il"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Song", new String[]{"il"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Work", new String[]{"il"});
        referenceArticleMap.put("http://dbpedia.org/ontology/Person", new String[]{"il", "la"});
    }
    
   public GenderUtils(Language language, String reference) {
        
        if(referenceArticleMap.containsKey(reference)){
            this.articles=referenceArticleMap.get(reference);
        }
        else
            this.article= "XXXXX";
       
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

}
