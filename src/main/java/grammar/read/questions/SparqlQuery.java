/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import grammar.sparql.SPARQLRequest;
import grammar.structure.component.Binding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class SparqlQuery {

    

  
    //https://www.w3.org/TR/rdf-sparql-query/

    private static String endpoint = null;
    private String objectOfProperty;
    public static String FIND_ANY_ANSWER = "FIND_ANY_ANSWER";
    public static String FIND_LABEL = "FIND_LABEL";
    public String sparqlQuery = null;
    public static String RETURN_TYPE_OBJECT = "objOfProp";
    public static String RETURN_TYPE_SUBJECT = "subjOfProp";
    private String resultSparql = null;
    private List<Binding> bindingList=new ArrayList<Binding>();

    public SparqlQuery(String entityUrl, String property, String type, String returnType,String language,String endpoint) {
       this.endpoint = endpoint;

        if (endpoint.contains("dbpedia.org")) {
            if (type.contains(FIND_ANY_ANSWER)) {
                if (returnType.contains("objOfProp")) {
                    sparqlQuery = this.setObjectWikiPedia(entityUrl, property);
                } else if (returnType.contains("subjOfProp")) {
                    sparqlQuery = this.setSubjectWikipedia(entityUrl, property);
                }

            } else if (type.contains(FIND_LABEL)) {
                sparqlQuery = this.setLabelWikipedia(entityUrl);
            }
            //System.out.print("sparqlQuery::"+sparqlQuery);
            this.resultSparql = executeSparqlQuery(sparqlQuery);
            parseResult(resultSparql);
        } else if (endpoint.contains("wikidata.org")) {
            if (type.contains(FIND_ANY_ANSWER)) {
                if (returnType.contains("objOfProp")) {
                    sparqlQuery = this.setObjectWikiData(entityUrl, property,language);
                } else if (returnType.contains("subjOfProp")) {
                    sparqlQuery = this.setSubjectWikiData(entityUrl, property,language);
                }

            } else if (type.contains(FIND_LABEL)) {
                sparqlQuery = this.setLabelWikiData(entityUrl,language);
            }
            //System.out.println("sparqlQuery::"+sparqlQuery);
            this.resultSparql = executeSparqlQuery(sparqlQuery);
            parseResult(resultSparql);

        }

    }

    public SparqlQuery(String sparqlQuery) {
        this.endpoint = SPARQLRequest.getSPARQL_ENDPOINT_URL();
        this.resultSparql = executeSparqlQuery(sparqlQuery);
        this.parseResultBindingList(resultSparql);
    }

    private String executeSparqlQuery(String query) {
        String result = null, resultUnicode = null, command = null;
        Process process = null;
        try {
            resultUnicode = this.stringToUrlUnicode(query);
            command = "curl " + endpoint + "?query=" + resultUnicode;
            process = Runtime.getRuntime().exec(command);
        } catch (Exception ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("error in unicode in sparql query!" + ex.getMessage());
            ex.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            result = builder.toString();
            //System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("error in reading sparql query!" + ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    public void parseResult(String xmlStr) {
        Document doc = convertStringToXMLDocument(xmlStr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            this.parseResult(builder, xmlStr);
        } catch (Exception ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("error in parsing sparql in XML!" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseResult(DocumentBuilder builder, String xmlStr) {

        try {
            Document document = builder.parse(new InputSource(new StringReader(
                    xmlStr)));
            NodeList results = document.getElementsByTagName("results");
            for (int i = 0; i < results.getLength(); i++) {
                NodeList childList = results.item(i).getChildNodes();
                for (int j = 0; j < childList.getLength(); j++) {
                    Node childNode = childList.item(j);
                    if ("result".equals(childNode.getNodeName())) {
                        //System.out.println("label!!!!!!!!!!!!!" + childList.item(j).getTextContent().trim());
                        this.objectOfProperty = childList.item(j).getTextContent().trim();
                    }
                }

            }
        } catch (SAXException ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("no result after sparql query!" + ex.getMessage());
            return;
        } catch (IOException ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("no result after sparql query!" + ex.getMessage());
            return;
        }

        //System.out.println("xmlStr!!!!!!!!!!!!!" + xmlStr);
        //System.out.println("xmlStr!!!!!!!!!!!!!" + xmlStr);
    }

    public String setObjectWikiPedia(String entityUrl, String property) {
        return "select  ?o\n"
                + "    {\n"
                + "    " + "<" + entityUrl + ">" + " " + "<" + property + ">" + "  " + "?o" + "\n"
                + "    }";

    }

    public String setObjectWikiData(String entityUrl, String propertyUrl,String language) {
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
                + "        FILTER (langMatches( lang(?label), \""+language+"\" ) )\n"
                + "}";

    }

    public String setSubjectWikipedia(String entityUrl, String property) {
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

    public String setSubjectWikiData(String entityUrl, String propertyUrl,String language) {
          return "SELECT ?subjectLabel WHERE {\n"
                + "    subject <" + propertyUrl + "> ?object.\n"
                + "   SERVICE wikibase:label {\n"
                + "     bd:serviceParam wikibase:language \""+language+"\" .\n"
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

    public static String setLabelWikipedia(String entityUrl) {
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

    public static String setLabelWikiData(String entityUrl,String language) {
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

    public String stringToUrlUnicode(String string) throws UnsupportedEncodingException {
        String encodedString = URLEncoder.encode(string, "UTF-8");
        return encodedString;
    }

    public String getObject() {
        return this.objectOfProperty;
    }

    public String getSparqlQuery() {
        return sparqlQuery;
    }

    public String getResultSparql() {
        return resultSparql;
    }

    @Override
    public String toString() {
        return "SparqlQuery{" + "objectOfProperty=" + objectOfProperty + ", sparqlQuery=" + sparqlQuery + '}';
    }

    public SparqlQuery() {

    }

    /*public static void main(String[] args) {

        String objectUrl = "http://dbpedia.org/ontology/largestCity";
        String propertyUrl = "http://www.wikidata.org/prop/direct/P26";
        String subject = "http://www.wikidata.org/entity/Q1744";
        String endpoint = "https://query.wikidata.org/sparql";
        endpoint = "https://dbpedia.org/sparql";
        SPARQLRequest.setEndpoint(endpoint);

        String language="en";
        //subject = "wd:Q1744";
        //propertyUrl="wdt:P26";
        SparqlQuery sparqlQuery = new SparqlQuery(subject, propertyUrl, FIND_ANY_ANSWER, RETURN_TYPE_OBJECT,language);
        //System.out.println(sparqlQuery.getSparqlQuery());
        System.out.println(sparqlQuery.getObject());

        //SparqlQuery sparqlQuery = new SparqlQuery();
        //String sparql=sparqlQuery.setSparqlQueryForLabelWikipedia(object);
        //System.out.println("sparql:"+sparql);
        //System.out.println(sparqlQuery.getResultSparql());
        //System.out.println(sparql);

        /*String entitieSparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
               + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
               + "SELECT ?subject ?label\n"
               + "WHERE {\n"
               + "    ?subject rdf:type <http://dbpedia.org/ontology/Country> .\n"
               + "    ?subject rdfs:label ?label .\n"
               + "       filter(langMatches(lang(?label),\"EN\"))         \n"
               + "} LIMIT 20000";*/
        ///<http://dbpedia.org/resource/Algeria> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Country> .
        /*SparqlQuery sparql=new SparqlQuery();
       String sparqlStr=SparqlQuery.setSparqlQueryPropertyWithSubjectFilter(objectUrl, propertyUrl);
       String resultSparql = sparql.executeSparqlQuery(entitieSparql);
       //System.out.println("sparql:"+resultSparql);
       //System.out.println("sparql:"+sparql.getObject());
         
    }*/
    
    public static void setEndpoint(String endpointT) {
        endpoint=endpointT;
    }

    public List<Binding> getBindingList() {
        return bindingList;
    }

    private void parseResultBindingList(String xmlStr) {
         Document doc = convertStringToXMLDocument(xmlStr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            this.parseResult(builder, xmlStr);
        } catch (Exception ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("error in parsing sparql in XML!" + ex.getMessage());
            ex.printStackTrace();
        }

        try {
            Document document = builder.parse(new InputSource(new StringReader(
                    xmlStr)));
            NodeList results = document.getElementsByTagName("results");
            for (int i = 0; i < results.getLength(); i++) {
                NodeList childList = results.item(i).getChildNodes();
                  //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                for (int j = 0; j < childList.getLength(); j++) {
                    Node childNode = childList.item(j);
                    if ("result".equals(childNode.getNodeName())) {
                        String[] lines = childList.item(j).getTextContent().strip().trim().split(System.getProperty("line.separator"));
                        Integer index=0;
                        String http="",label="";
                        Map<String,String> map=new TreeMap<String,String>();
                        for(String line:lines) {
                           
                            if(index==0)
                              http=line.strip().trim();
                            else if(index==3)
                              label=line.strip().trim();
                            index=index+1;
                        }
                       map.put(http,label);
                       Binding binding=new Binding(label,http);
                       this.bindingList.add(binding);
                    }
                   
                }

            }
        } catch (SAXException ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("no result after sparql query!" + ex.getMessage());
            return ;
        } catch (IOException ex) {
            Logger.getLogger(SparqlQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("no result after sparql query!" + ex.getMessage());
            return ;
        }
    }

}
