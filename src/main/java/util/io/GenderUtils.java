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
    
    static{
        referenceArticleMap.put("http://dbpedia.org/ontology/areaCode", new String[]{"il"});
        referenceArticleMap.put("http://dbpedia.org/ontology/author", new String[]{"il","la"});
    }
    
    public GenderUtils(Language language, String reference) {
        
        if(referenceArticleMap.containsKey(reference)){
            this.articles=referenceArticleMap.get(reference);
        }
        else
            this.article= "XXXXX";
       
    }

    public GenderUtils(String noun, Language it) {
        if (noun.contains("Person")) {
            article = "IL";
        } else {
            article = "LA";
        }
    }

    public String getArticle() {
        return article;
    }

}
