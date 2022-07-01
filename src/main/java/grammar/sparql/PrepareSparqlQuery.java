/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.sparql;

import grammar.generator.sentencebuilder.TemplateFinder;
import static grammar.sparql.SparqlQuery.RETURN_TYPE_OBJECT;
import static grammar.sparql.SparqlQuery.RETURN_TYPE_SUBJECT;
import grammar.structure.component.DomainOrRangeTypeCheck;
import grammar.structure.component.Language;
import static java.lang.System.exit;
import java.util.Map;
import java.util.TreeMap;
import linkeddata.LinkedData;
import util.io.UrlUtils;

/**
 *
 * @author elahi
 */
public class PrepareSparqlQuery {

   
   
    
    private String rdfType = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
    private LinkedData linkedData=null;
    //"<http://www.w3.org/2000/01/rdf-schema#label>"
    
    public PrepareSparqlQuery(LinkedData linkedData){
       this.linkedData=linkedData;
    }

    public static String setSubjectWikipedia(String objectUri, String property, String rdfProperty, String objectClassUri) {
        String sparql = null;
       
        
        if (TemplateFinder.isOccupation(objectClassUri)) {
            sparql=  setSubjectWikipediaWithOccupation(objectUri, property, rdfProperty, objectClassUri);
        } else if (TemplateFinder.isClassTypeCheck(objectClassUri)) {
             sparql = setSubjectWikipediaWithClassName(objectUri, property, rdfProperty, objectClassUri);
        }
        else if(objectUri.contains("http")){
           sparql= "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + objectUri + "> ." + "\n"
                    + "    }";
        } 
        else {
           sparql= "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + objectUri +" ." + "\n"
                    + "    }";
        } 

        
        /*System.out.println(sparql);
        System.out.println(objectClassUri);
        exit(1);*/
        return sparql;

    }
    
    public static String setObjectWikiPedia(String subjectUrl, String property, String rdfProperty, String objectClassUri) {
        String sparql = null;

        if (TemplateFinder.isOccupation(objectClassUri)) {
            sparql = setObjectWikipediaWithOccupation(subjectUrl, property, rdfProperty, objectClassUri);
        } else if (TemplateFinder.isClassTypeCheck(objectClassUri)) {
            sparql = setObjectWikipediaWithClassName(subjectUrl, property, rdfProperty, objectClassUri);
        } else {
            sparql = "select  ?o\n"
                    + "    {\n"
                    + "    " + "<" + subjectUrl + ">" + " " + "<" + property + ">" + "  " + "?o ." + "\n"
                    + "    }";

        }

        return sparql;

    }

    public static String setSubjectWikipediaWithClassName(String entityUri, String property, String rdfProperty, String entityClass) {
        String sparql = null;
        //className //        ?uri rdf:type dbo:Country .
        if (entityUri.contains("http:")) {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + entityUri + "> ." + "\n"
                    + "   " + "?s" + " " + "<" + rdfProperty + ">" + "  " + "<" + entityClass + "> ." + "\n"
                    + "    }";
        } else {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + entityUri + "> ." + "\n"
                    + "   " + "?s" + " " + "<" + rdfProperty + ">" + "  " + "<" + entityClass + "> ." + "\n"
                    + "    }";

        }

        return sparql;

    }

    public static String setSubjectWikipediaWithOccupation(String entityUri, String property, String otherProperty, String otherClass) {
        String sparql = null;
        otherProperty = DomainOrRangeTypeCheck.getUri(DomainOrRangeTypeCheck.occupation);
        String lastSegment = UrlUtils.lastSegment(otherClass);
        otherClass = DomainOrRangeTypeCheck.getResourceUri(lastSegment);

        if (entityUri.contains("http:")) {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + entityUri + "> ." + "\n"
                    + "   " + "?s" + " " + "<" + otherProperty + ">" + "  " + "<" + otherClass + "> ." + "\n"
                    + "    }";
        } else {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + entityUri + "> ." + "\n"
                    + "   " + "?s" + " " + "<" + otherProperty + ">" + "  " + "<" + otherClass + "> ." + "\n"
                    + "    }";

        }

        return sparql;

    }

    public static String setObjectWikipediaWithClassName(String entityUri, String property, String rdfProperty, String entityClass) {
        String sparql = null;
        //className //        ?uri rdf:type dbo:Country .
        sparql = "select  ?o\n"
                + "    {\n"
                + "    " + "<" + entityUri + ">" + " " + "<" + property + ">" + "  " + "?o ." + "\n"
                + "   " + "?o" + " " + "<" + rdfProperty + ">" + "  " + "<" + entityClass + "> ." + "\n"
                + "    }";

        return sparql;

    }

    public static String setObjectWikipediaWithOccupation(String entityUri, String property, String otherProperty, String otherClass) {
        String sparql = null;
        otherProperty = DomainOrRangeTypeCheck.getUri(DomainOrRangeTypeCheck.occupation);
        String lastSegment = UrlUtils.lastSegment(otherClass);
        otherClass = DomainOrRangeTypeCheck.getResourceUri(lastSegment);

        sparql = "select  ?o\n"
                + "    {\n"
                + "    " + "<" + entityUri + ">" + " " + "<" + property + ">" + "  " + "?o ." + "\n"
                + "   " + "?o" + " " + "<" + otherProperty + ">" + "  " + "<" + otherClass + "> ." + "\n"
                + "    }";

        return sparql;

    }

    /*public static String setSubjectWikipedia(String objectUri, String property, String rdfProperty, String objectClassUri) {
        String sparql = null;
        //className //        ?uri rdf:type dbo:Country .

        if (objectClassUri.contains("Number") || objectClassUri.contains("THING") | objectClassUri.contains("date")) {
            if (objectUri.contains("http:")) {
                sparql = "select  ?s\n"
                        + "    {\n"
                        + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + objectUri + "> ." + "\n"
                        + "   " + "?s" + " " + "<" + rdfProperty + ">" + "  " + "<" + objectClassUri + "> ." + "\n"
                        + "    }";
            } else {
                sparql = "select  ?s\n"
                        + "    {\n"
                        + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + objectUri + "> ." + "\n"
                        + "   " + "?s" + " " + "<" + rdfProperty + ">" + "  " + "<" + objectClassUri + "> ." + "\n"
                        + "    }";

            }

        } else if (objectUri.contains("http:")) {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + objectUri + "> ." + "\n"
                    + "    }";
        } else {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + objectUri + "> ." + "\n"
                    + "    }";

        }

        return sparql;

    }*/

 /*public static String setObjectWikiPedia(String entityUrl, String property, String rdfProperty, String objectClassUri) {
        String sparql = null;
        if (objectClassUri.contains("Number") || objectClassUri.contains("THING") | objectClassUri.contains("date")) {
            sparql = "select  ?o\n"
                    + "    {\n"
                    + "    " + "<" + entityUrl + ">" + " " + "<" + property + ">" + "  " + "?o ." + "\n"
                    + "    }";

        } else {
            sparql = "select  ?o\n"
                    + "    {\n"
                    + "    " + "<" + entityUrl + ">" + " " + "<" + property + ">" + "  " + "?o ." + "\n"
                    + "   " + "?o" + " " + "<" + rdfProperty + ">" + "  " + "<" + objectClassUri + "> ." + "\n"
                    + "    }";

        }

        return sparql;

    }*/
   

    public static String setObjectWikiPediaCount(String entityUrl, String property, String variable) {
        //SELECT (COUNT(DISTINCT ?x) as ?c) WHERE {  <http://dbpedia.org/resource/Turkmenistan> <http://dbpedia.org/ontology/language> ?x . } 
        variable = "?" + variable;
        String sparql = "select " + "(COUNT(DISTINCT " + variable + ") as ?c) WHERE" + "\n"
                + "    {\n"
                + "    " + "<" + entityUrl + ">" + " " + "<" + property + ">" + "  " + variable + " ." + "\n"
                + "    }";
        return sparql;

    }

    public static String setSubjectWikiPediaCount(String objectUri, String property, String variable) {
        //SELECT (COUNT(DISTINCT ?x) as ?c) WHERE {  <http://dbpedia.org/resource/Turkmenistan> <http://dbpedia.org/ontology/language> ?x . } 
        variable = "?" + variable;
        String sparql = "select  " + "(COUNT(DISTINCT " + variable + ") as ?c) WHERE" + "\n"
                + "    {\n"
                + "   " + variable + " " + "<" + property + ">" + "  " + "<" + objectUri + "> ." + "\n"
                + "    }";
        return sparql;

    }

    public static String setBooleanWikiPedia(String domainEntityUrl, String property, String rangeEntityUrl) {
        String sparql
                = "ASK WHERE { "
                + "<" + domainEntityUrl + ">" + " " + "<" + property + ">" + " " + "<" + rangeEntityUrl + "> . "
                + "}";
        return sparql;
    }

    public String setSubjectWikiData(String entityUrl, String propertyUrl, String language) {
        return "SELECT ?subjectLabel WHERE {\n"
                + "    subject <" + propertyUrl + "> ?object.\n"
                + "   SERVICE wikibase:label {\n"
                + "     bd:serviceParam wikibase:language \"" + language + "\" .\n"
                + "   }\n"
                + "}\n"
                + "";

    }

    public static String setSparqlQueryPropertyWithSubjectFilterWikipedia(String entityUrl, String property) {
        String sparql = null;
        if (entityUrl.contains("http:")) {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "<" + entityUrl + ">" + "\n"
                    + "    }";
        } else {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + entityUrl + "\n"
                    + "    }";
        }
        return sparql;

    }

    public static String setLabelWikipedia(String entityUrl, String language,String rdfsLabel) {

        /*System.out.println(entityUrl);
        String classNameOfInstances = findClassName(entityUrl);
        classNameOfInstances = "Publication";
        String rdfsLabel = this.linkedData.getRdfPropertyLabel(classNameOfInstances);
        System.out.println("rdfsPropertyLabel::" + rdfsLabel);
        exit(1);*/

        String sparql
                = "   SELECT DISTINCT ?label \n"
                + "   WHERE {  \n"
                + "       <" + entityUrl + "> " + rdfsLabel + " ?label .     \n"
                + "       filter(langMatches(lang(?label),\"" + language + "\"))         \n"
                + "   }";

        return sparql;

    }


    public static String setLabelWikiData(String entityUrl, String language) {
        String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "   PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "   PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "\n"
                + "   SELECT DISTINCT ?label \n"
                + "   WHERE {  \n"
                + "       <" + entityUrl + "> rdfs:label ?label .     \n"
                + "       filter(langMatches(lang(?label),\"EN\"))         \n"
                + "   }";

        return sparql;

    }

    public static String setSparqlQueryForTypesWikipedia(String propertyUrl, String objectUrl) {
        String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "   PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "   PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "\n"
                + "   SELECT DISTINCT ?label \n"
                + "   WHERE {  \n"
                + "   " + "?label" + " " + "<" + propertyUrl + ">" + " " + "<" + objectUrl + ">" + " .     \n"
                + "       filter(langMatches(lang(?label),\"EN\"))         \n"
                + "   }";

        return sparql;

    }

    //SELECT DISTINCT ?uri WHERE { 
    //?uri a <http://dbpedia.org/ontology/Country> . ?uri <http://dbpedia.org/ontology/areaTotal> ?n . } 
    //ORDER BY DESC(?n) OFFSET 0 LIMIT 1
    public static String desc(String domain, String reference) {
        String newSparqlQuery = "SELECT DISTINCT ?" + RETURN_TYPE_SUBJECT + " "
                + "WHERE {" + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>" + " " + "<" + domain + ">" + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY DESC(?num) LIMIT 1";
        return newSparqlQuery;
    }

    public static String descObj(String className, String locationProperty, String reference, String variable) {
        String type = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
        //String locationProperty = "<http://dbpedia.org/ontology/locatedInArea>";
        //locationProperty = "<http://dbpedia.org/ontology/location>";
        //locationProperty = "<http://dbpedia.org/ontology/country>";

        String newSparqlQuery = "SELECT DISTINCT ?" + RETURN_TYPE_SUBJECT + " "
                + "WHERE {" + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + type + " " + "<" + className + ">" + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + locationProperty + " " + "?" + variable + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY DESC(?num) LIMIT 1";
        return newSparqlQuery;
    }

    public static String descObjOfPropPerson(String className, String property, String reference, String variable) {
        return "SELECT DISTINCT ?" + RETURN_TYPE_OBJECT + " "
                + "WHERE {" + " "
                + "?" + variable + " " + property + " " + "?" + RETURN_TYPE_OBJECT + " ." + " "
                + "?" + RETURN_TYPE_OBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY DESC(?num) LIMIT 1";
    }

    public static String ascObjOfPropPerson(String className, String property, String reference, String variable) {
        return "SELECT DISTINCT ?" + RETURN_TYPE_OBJECT + " "
                + "WHERE {" + " "
                + "?" + variable + " " + property + " " + "?" + RETURN_TYPE_OBJECT + " ." + " "
                + "?" + RETURN_TYPE_OBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY ASC(?num) LIMIT 1";
    }

    /*public static String objDesc(String className, String reference, String variable) {
        String type = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
        String locationProperty = "<http://dbpedia.org/ontology/locatedInArea>";
        //locationProperty = "<http://dbpedia.org/ontology/location>";
        //locationProperty = "<http://dbpedia.org/ontology/country>";

        String newSparqlQuery = "SELECT DISTINCT ?" + RETURN_TYPE_SUBJECT + " "
                + "WHERE {" + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + type + " " + "<" + className + ">" + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + locationProperty + " " + variable + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY DESC(?num) LIMIT 1";
        return newSparqlQuery;
    }*/
    public static String asc(String domain, String reference) {
        String newSparqlQuery = "SELECT DISTINCT ?" + RETURN_TYPE_SUBJECT + " "
                + "WHERE {" + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>" + " " + "<" + domain + ">" + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY ASC(?num) LIMIT 1";
        return newSparqlQuery;
    }

    /*public static String objAsc(String domain, String reference) {
        String locationProperty = "<http://dbpedia.org/ontology/locatedInArea>";
        //String locationObject = "<http://dbpedia.org/resource/Australia>";

        String newSparqlQuery = "SELECT DISTINCT ?" + "num" + " "
                + "WHERE {" + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>" + " " + "<" + domain + ">" + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + locationProperty + " ?" + RETURN_TYPE_OBJECT + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY ASC(?num) LIMIT 1";
        return newSparqlQuery;
    }*/
    public static String ascObj(String domain, String locationProperty, String reference, String variable) {
        //String locationProperty = "<http://dbpedia.org/ontology/locatedInArea>";
        //String locationObject = "<http://dbpedia.org/resource/Australia>";

        String newSparqlQuery = "SELECT DISTINCT ?" + "num" + " "
                + "WHERE {" + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>" + " " + "<" + domain + ">" + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + locationProperty + " ?" + RETURN_TYPE_OBJECT + " ." + " "
                + "?" + RETURN_TYPE_SUBJECT + " " + "<" + reference + ">" + " ?" + "num" + " ." + " "
                + "} " + "ORDER BY ASC(?num) LIMIT 1";
        return newSparqlQuery;
    }

    public static String getBindingList(String className, Long limit, Language language) {
        String sparqlQuery = "SELECT DISTINCT  ?subjOfProp ?label\n"
                + "WHERE "
                + "  {  "
                + "   ?subjOfProp  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  " + className + " "
                + "    OPTIONAL"
                + "      { ?subjOfProp  <http://www.w3.org/2000/01/rdf-schema#label>  ?label "
                + "        FILTER langMatches(lang(?label), \"" + language.toString() + "\") "
                + "      } "
                + "  } "
                + "LIMIT   " + limit.toString();
        return sparqlQuery;

    }

    public static String setObjectWikiPedia2(String entityUrl, String property) {
        return " PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX res: <http://dbpedia.org/resource/>\n"
                + "SELECT DISTINCT ?uri \n"
                + "WHERE { \n"
                + "        res:French_Polynesia dbo:capital ?x .\n"
                + "        ?x dbo:mayor ?uri .\n"
                + "}";

    }

    public static String setObjectWikiData(String entityUrl, String propertyUrl, String language) {
        /*return "SELECT ?object ?objectLabel WHERE {\n"
                + "   "+"<"+entityUrl+">"+" "+"<"+property+">"+" ?object.\n"
                + "   SERVICE wikibase:label {\n"
                + "     bd:serviceParam wikibase:language \"en\" .\n"
                + "   }\n"
                + "}";*/

 /*return "SELECT ?objectLabel WHERE {\n"
                + "    <" + entityUrl + "> <" + propertyUrl + "> ?object.\n"
                + "   SERVICE wikibase:label {\n"
                + "     bd:serviceParam wikibase:language \""+language+"\" .\n"
                + "   }\n"
                + "}\n"
                + "";*/
        return "SELECT ?label WHERE {\n"
                + "    <" + entityUrl + "> <" + propertyUrl + "> ?object.\n"
                + "  ?object rdfs:label ?label \n"
                + "        FILTER (langMatches( lang(?label), \"" + language + "\" ) )\n"
                + "}";

    }

    public static String setObjectBen(String entityUrl, String propertyUrl, String language) {

        return "SELECT ?object WHERE {\n"
                + "    <" + entityUrl + "> <" + propertyUrl + "> ?object.\n"
                + "}";

    }
    
    

    public static String findClassGivenInstance(String uri,String classProperty) {
        String classSparql = "select  ?class {     \n"
                + " " + "<" + uri + ">" + " " + "<" + classProperty + ">" + "   " + "?class" + ".\n"
                + "}";
        System.out.println(classSparql);
        return classSparql;
    }



}
