/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
public class Matcher {
    
    public static String clean(String value,String language) {
        value = value.replace("<", "");
        value = value.replace(">", "");
        if(language.contains("it")){
        value = value.replace("http://it.dbpedia.org/resource/", "");
        value = value.replace("http://it.dbpedia.org/resource//", "");      
        }
      
        value = value.replace("^^<http://www.w3.org/2001/XMLSchema#date>", "");
        value = value.trim().strip().stripLeading().stripTrailing();
        return value;
    }
    
}
