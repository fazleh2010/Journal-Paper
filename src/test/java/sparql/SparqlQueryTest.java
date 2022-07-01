/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparql;

import grammar.sparql.PrepareSparqlQuery;
import grammar.sparql.SparqlQuery;
import org.apache.jena.query.QueryType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author elahi
 */
public class SparqlQueryTest {

    public static void main(String[] args) {
        //labelTest();
        //answerTest();
           }

    /*private static void labelTest() {
        SparqlQuery sparqlQuery = new SparqlQuery();
        String uri = "http://dbpedia.org/resource/Heineken";
        String language = "de";
        String labelSprql = PrepareSparqlQuery.setLabelWikipedia(uri, language);
        System.out.println("labelSprql::" + labelSprql);
        String resultSparql = sparqlQuery.executeSparqlQueryCurl(labelSprql);
        System.out.println(uri);
    }*/

    /*@Test
    private static void answerTest() {
        String domainEntityUrl = "http://dbpedia.org/resource/Battle_of_Gettysburg";
        String property = "http://dbpedia.org/property/date";
        String language = "de";
        String endpoint = "https://dbpedia.org/sparql";
        Boolean online = true;
        SparqlQuery sparqlQuery = new SparqlQuery(domainEntityUrl, property, null, SparqlQuery.FIND_ANY_ANSWER, SparqlQuery.RETURN_TYPE_OBJECT, language, endpoint, online, QueryType.SELECT);
        System.out.println("query::" + sparqlQuery.getSparqlQuery());
        System.out.println("sparqlQuery.getObject()::" + sparqlQuery.getObject());
        assertEquals(sparqlQuery.getObject(), "2008-12-26");

       
    }*/
    
       /*
Gib mir den B�rgermeister von die Hauptstadt von ($x | Country_NP)?
            
PREFIX dbo: <http://dbpedia.org/ontology/>
PREFIX res: <http://dbpedia.org/resource/>
SELECT DISTINCT ?uri 
WHERE { 
        res:French_Polynesia dbo:capital ?x .
        ?x dbo:mayor ?uri .
}
            
            
            sparqlQuery:::(bgp
  
  (triple ?subjOfPropx <http://dbpedia.org/ontology/capital> ?subjOfProp)
  (triple ?subjOfProp <http://dbpedia.org/ontology/mayor> ?objOfProp)
)
    "bindingType" : "Country",
    "returnType" : "Person",
"(bgp\n  (triple ?subjOfProp <http://dbpedia.org/ontology/mayor> ?objOfProp)\n  (triple ?subjOfPropx <http://dbpedia.org/ontology/capital> ?subjOfProp)\n)\n"

             */
          

}
