/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkeddatafragments.client;

import com.google.common.base.Stopwatch;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import static org.fest.assertions.Assertions.assertThat;
import org.linkeddatafragments.model.LinkedDataFragmentGraph;

/**
 *
 * @author elahi
 */
public class LinkedSparqlExecution {
   
    protected static Model model;
    
    public LinkedSparqlExecution(){
        
    }
    
    public static void main(String[] args) {
        /*String endpoint = "http://data.linkeddatafragments.org/dbpedia2014";
        LinkedDataFragmentGraph ldfg = new LinkedDataFragmentGraph("http://data.linkeddatafragments.org/dbpedia2014");
        Model model = ModelFactory.createModelForGraph(ldfg);
        String queryString="select  ?o    {    <http://dbpedia.org/resource/Australia> <http://dbpedia.org/ontology/capital>  ?o .   ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://dbpedia.org/ontology/City> .    }";
        LinkedSparqlExecution main = new LinkedSparqlExecution(model, endpoint,queryString);*/
        
         //http://data.linkeddatafragments.org/dbpedia2014
        LinkedDataFragmentGraph ldfg = new LinkedDataFragmentGraph("http://data.linkeddatafragments.org/dbpedia2014");
        String queryString = "select  ?o    {    <http://dbpedia.org/resource/Canada> <http://dbpedia.org/ontology/capital>  ?o .   ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://dbpedia.org/ontology/City> .    }";
        model = ModelFactory.createModelForGraph(ldfg);
        LinkedSparqlExecution LinkedSparqlExecution=new LinkedSparqlExecution();
        LinkedSparqlExecution.sparqlObjectAsVariable(queryString);
        exit(1);
       
    }
    
   
    


    public LinkedSparqlExecution(Model modelT,String endpoint,String queryString) {
        model=modelT;
        this.sparqlObjectAsVariable(queryString);
    }

    public void testSize() {
        assertThat(model.size()).isEqualTo(377367913);
        System.out.println(model.size());
    }
    
    public List<String> sparqlObjectAsVariable(String queryString) {
        List<String> results = new ArrayList<String>();
        Query qry = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(qry, model);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            String result = rs.nextSolution().toString();
            results.add(result);
        }
        return results;
    }

    public List<String> parseResultList(List<String> results) {

        List<String> parsedResult = new ArrayList<String>();
        for (String result : results) {
            result = result.replace("(", "");
            result = result.replace(")", "");

            if (result.contains("=")) {
                if (result.contains("=")) {
                    String[] info = result.split("=");
                    result = info[1];
                    parsedResult.add(result);
                }

            }

        }
        return parsedResult;
    }

   

}
