/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.sparql;

import grammar.sparql.SPARQLRequest;
import grammar.structure.component.Binding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import static java.lang.System.exit;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.query.QueryType;
import util.io.FileUtils;

/**
 *
 * @author elahi
 */
public class SparqlQuery {
    private static String endpoint = "https://dbpedia.org/sparql";
    private String objectOfProperty;
    public static String FIND_ANY_ANSWER = "FIND_ANY_ANSWER";
    public static String FIND_LABEL = "FIND_LABEL";
    private String sparqlQuery = null;
    public static String RETURN_TYPE_OBJECT = "objOfProp";
    public static String RETURN_TYPE_SUBJECT = "subjOfProp";
    private String resultSparql = null;
    private String type = null;
    private List<Binding> bindingList=new ArrayList<Binding>();

    public SparqlQuery(String domainEntityUrl, String property, String rangeEntityUrl, String type, String returnType, String language, String endpoint, Boolean online, QueryType queryType) {
        this.endpoint = endpoint;
        this.type=type;
        if (endpoint.contains("dbpedia.org")) {
            if (type.contains(FIND_ANY_ANSWER)) {
                if (queryType.equals(QueryType.SELECT)) {
                    if (returnType.contains("objOfProp")) {
                        sparqlQuery = this.setObjectWikiPedia(domainEntityUrl, property);
                    } else if (returnType.contains("subjOfProp")) {
                        sparqlQuery = this.setSubjectWikipedia(domainEntityUrl, property);
                    }

                } else if (queryType.equals(QueryType.ASK)) {
                    sparqlQuery = this.setBooleanWikiPedia(domainEntityUrl, property, rangeEntityUrl);
                    
                }

            } else if (type.contains(FIND_LABEL)) {
                sparqlQuery = this.setLabelWikipedia(domainEntityUrl, language);
            }
            //System.out.print("sparqlQuery::"+sparqlQuery);
            if (online) {
                if (queryType.equals(QueryType.SELECT)) {
                    this.resultSparql = executeSparqlQuery(sparqlQuery);
                    parseResult(resultSparql);
                }else if(queryType.equals(QueryType.ASK)){
                     this.resultSparql = executeSparqlQuery(sparqlQuery);
                     this.objectOfProperty=this.resultSparql;
                }
              
                
            } else {
                return;
            }

        } else if (endpoint.contains("wikidata.org")) {
            if (type.contains(FIND_ANY_ANSWER)) {
                if (returnType.contains("objOfProp")) {
                    sparqlQuery = this.setObjectWikiData(domainEntityUrl, property, language);
                } else if (returnType.contains("subjOfProp")) {
                    sparqlQuery = this.setSubjectWikiData(domainEntityUrl, property, language);
                }

            } else if (type.contains(FIND_LABEL)) {
                sparqlQuery = this.setLabelWikiData(domainEntityUrl, language);
            }
            this.resultSparql = executeSparqlQuery(sparqlQuery);
            parseResult(resultSparql);

        } else if (endpoint.contains("beniculturali")) {
            if (type.contains(FIND_ANY_ANSWER)) {
                if (returnType.contains("objOfProp")) {
                    sparqlQuery = this.setObjectBen(domainEntityUrl, property, language);
                } else if (returnType.contains("subjOfProp")) {
                    sparqlQuery = this.setSubjectWikiData(domainEntityUrl, property, language);

                }

            } else if (type.contains(FIND_LABEL)) {
                sparqlQuery = this.setLabelWikiData(domainEntityUrl, language);
            }
            this.resultSparql = executeSparqlQuery(sparqlQuery);
            parseResult(resultSparql);

        }

    }
    
    
    public SparqlQuery(String sparqlQuery) {
        endpoint = SPARQLRequest.getSPARQL_ENDPOINT_URL();
        
        //The data set has a problem that it does not get results given language filter
        //This is a dirty solution for beniculturali dataset 
        if (endpoint.contains("beniculturali") ) {
           sparqlQuery=this.modifyBindingListSparql(sparqlQuery);
        }
        this.resultSparql = executeSparqlQuery(sparqlQuery);
        this.parseResultBindingList(resultSparql);
    }

   

    public String executeSparqlQuery(String query) {
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
                    /*String[] lines = childList.item(j).getTextContent().strip().trim().split(System.getProperty("line.separator"));
                    for(String line:lines){
                        System.out.println(" line " +line);
                    }*/

                    if ("result".equals(childNode.getNodeName())) {
                        String answer= childList.item(j).getTextContent().trim();
                        if(endpoint.contains("dbpedia")&&type.contains(FIND_ANY_ANSWER)&&answer.contains("--")){
                          continue;
                        }
                        else 
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
    
    public String setObjectBen(String entityUrl, String propertyUrl,String language) {
      
        return "SELECT ?object WHERE {\n"
                + "    <" + entityUrl + "> <" + propertyUrl + "> ?object.\n"
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
    
    public String setBooleanWikiPedia(String domainEntityUrl, String property, String rangeEntityUrl) {
        String sparql=
                      "ASK WHERE { "
                     + "<"+domainEntityUrl+">"+" "+"<"+property+">"+" "+"<"+rangeEntityUrl+"> . "
                     + "}";
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

    public static String setLabelWikipedia(String entityUrl,String language) {
        String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "   PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "   PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "\n"
                + "   SELECT DISTINCT ?label \n"
                + "   WHERE {  \n"
                + "       <" + entityUrl + "> rdfs:label ?label .     \n"
                + "       filter(langMatches(lang(?label),\""+language+"\"))         \n"
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
                for (int j = 0; j < childList.getLength(); j++) {
                    Node childNode = childList.item(j);
                    if ("result".equals(childNode.getNodeName())) {
                        String[] lines = childList.item(j).getTextContent().strip().trim().split(System.getProperty("line.separator"));
                        Integer index=0;
                        String http="",label="";
                        Map<String,String> map=new TreeMap<String,String>();
                        for(String line:lines) {
                            
                           
                            if(index==0){
                              http=line.strip().trim();    
                            }
                            else if(endpoint.contains("wikidata")&&index==3){
                              label=line.strip().trim();  
                            }
                            else if(endpoint.contains("beniculturali")&&index==1){
                              label=line.strip().trim();  
                            }
                            
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

    private String  modifyBindingListSparql(String sparqlQuery) {
        String[] lines =  sparqlQuery.strip().trim().split(System.getProperty("line.separator"));
        String str="";
        for(String line :lines){
            if(line.contains("FILTER"))
               continue;
            else
            str+=line+"\n";

        }
        return str;
    }
    
    /*private String  modifyObjectAnswerSparql(String sparqlQuery) {
        String[] lines =  sparqlQuery.strip().trim().split(System.getProperty("line.separator"));
        String str="";
        for(String line :lines){
            if(line.contains("FILTER"))
               continue;
            else if(line.contains("rdfs:label"))
               continue;
            else
            str+=line+"\n";

        }
        return str;
    }*/
    
   

   

}
