/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.sparql;

import static grammar.datasets.sentencetemplates.TempConstants.superlativePerson;
import grammar.generator.sentencebuilder.TemplateFinder;
import grammar.sparql.SPARQLRequest;
import grammar.structure.component.Binding;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryType;
import util.io.FileUtils;
import static grammar.datasets.sentencetemplates.TempConstants.superlativeCountry;
import static grammar.datasets.sentencetemplates.TempConstants.superlativeLocation;
import static java.lang.System.exit;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.linkeddatafragments.client.LinkedSparqlExecution;
import org.linkeddatafragments.model.LinkedDataFragmentGraph;
import util.io.OffLineResult;

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
    private String sparqlQueryID = null;
    public static String RETURN_TYPE_OBJECT = "objOfProp";
    public static String RETURN_TYPE_SUBJECT = "subjOfProp";
    public static String VARIABLE = "VARIABLE";
    public static String QUESTION_MARK = "?";
    private String resultSparql = null;
    private String command = null;

    private String type = null;
    private List<Binding> bindingList=new ArrayList<Binding>();

    private Boolean online=false;
    //private Model model =null;
    private  Map<String, OffLineResult> offLineResults =new TreeMap<String, OffLineResult> ();

    
    public SparqlQuery(String template, String rdfProperty, String className, String domainEntityUrl, String sparqlQueryOrg, String rangeEntityUrl, String type, String returnType, String language, String endpoint, Boolean online, QueryType queryType) {
        this.endpoint = endpoint;
        this.type = type;
        this.offLineResults = offLineResults;
        Integer index = isSimpleOrComposite(sparqlQueryOrg);
        /*LinkedDataFragmentGraph ldfg = new LinkedDataFragmentGraph("http://data.linkeddatafragments.org/dbpedia2014");
        model = ModelFactory.createModelForGraph(ldfg);*/
        String property = null;

        if (endpoint.contains("dbpedia")) {
            if (type.contains(FIND_ANY_ANSWER) && index == 1) {
                property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
                if (queryType.equals(QueryType.SELECT)) {
                    this.sparqlQuery = setSingleSelect(template, rdfProperty, className, domainEntityUrl, sparqlQueryOrg, rangeEntityUrl, type, returnType, language, endpoint, online, queryType);
                } else if (queryType.equals(QueryType.ASK)) {
                    this.sparqlQuery = PrepareSparqlQuery.setBooleanWikiPedia(domainEntityUrl, property, rangeEntityUrl);
                    this.sparqlQueryID = this.getSparqlID(property, domainEntityUrl, rangeEntityUrl);
                }

            } else if (type.contains(FIND_ANY_ANSWER) && index == 2) {
                if (queryType.equals(QueryType.SELECT)) {
                    if (returnType.contains("objOfProp") || returnType.contains("subjOfProp")) {
                        sparqlQuery = this.setObjectWikiPediaComposite(domainEntityUrl, sparqlQueryOrg, queryType, returnType);
                    }

                } else if (queryType.equals(QueryType.ASK)) {
                    sparqlQuery = PrepareSparqlQuery.setBooleanWikiPedia(domainEntityUrl, sparqlQueryOrg, rangeEntityUrl);

                }

            } else if (type.contains(FIND_ANY_ANSWER) && index == 0) {
                if (queryType.equals(QueryType.SELECT)) {
                    if (template.contains(superlativePerson) || template.contains(superlativeCountry)) {
                        sparqlQuery = sparqlQueryOrg.replace(QUESTION_MARK + VARIABLE, "<" + domainEntityUrl + ">");

                    }

                } else if (queryType.equals(QueryType.ASK)) {
                }
            } else if (type.contains(FIND_LABEL)) {
                sparqlQuery = PrepareSparqlQuery.setLabelWikipedia(domainEntityUrl, language);
            }

            if (sparqlQuery != null) {
                if (online) {
                    if (queryType.equals(QueryType.SELECT) || queryType.equals(QueryType.ASK)) {
                        if (this.endpoint.contains("https://dbpedia.org/")) {
                            this.resultSparql = executeSparqlQueryCurl(sparqlQuery);
                            this.parseResult(resultSparql);
                        }
                        /*else if (this.endpoint.contains("http://data.linkeddatafragments.org")) {
                           LinkedSparqlExecution main=  new LinkedSparqlExecution(model,endpoint,sparqlQuery); 
                           List<String> results=main.sparqlObjectAsVariable(sparqlQuery);
                           this.parseResultSingle(results);
                        }*/

                    }

                } else {
                    return;
                    /*if (queryType.equals(QueryType.SELECT) || queryType.equals(QueryType.ASK)) {
                        if (returnType.contains(RETURN_TYPE_OBJECT)) {
                            System.out.println("domainEntityUrl:"+domainEntityUrl);
                            this.objectOfProperty = this.offLineResults.get(domainEntityUrl).getObjectUri();
                            System.out.println(" returnType: " + returnType);
                             System.out.println(" domainEntityUrl: " + domainEntityUrl);
                            System.out.println("  answerUri: " + this.objectOfProperty);
                            exit(1);
                        } else if (returnType.contains(RETURN_TYPE_SUBJECT)) {
                            this.objectOfProperty = this.offLineResults.get(domainEntityUrl).getSubjectUri();
                        }
                        //this.objectOfProperty = this.getOfflineResult(this.sparqlQueryID, property);

                    }*/
 /*if (queryType.equals(QueryType.SELECT) || queryType.equals(QueryType.ASK)) {
                        this.objectOfProperty = this.getOfflineResult(this.sparqlQueryID, property);
                    } else {
                        return;
                    }*/
                }
            } else {
                System.out.println("sparql query is wrong!!!");
            }

        }

    }


    private String setSingleSelect(String template, String rdfProperty, String className, String subject, String sparqlQueryOrg, String object, String type, String returnType, String language, String endpoint, Boolean online, QueryType queryType) {
        String property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
        String sparqlQuery = null;
        
     

        if (template != null) {
            if (template.contains(TemplateFinder.HOW_MANY_THING)) {
                if (returnType.contains(RETURN_TYPE_OBJECT)) {
                    return sparqlQuery = PrepareSparqlQuery.setObjectWikiPediaCount(subject, property, returnType);
                } else if (returnType.contains(RETURN_TYPE_SUBJECT)) {
                    return sparqlQuery = PrepareSparqlQuery.setSubjectWikiPediaCount(subject, property, returnType);
                }
            }
            if (template.contains(superlativePerson) || template.equals(superlativeCountry) || template.equals(superlativeLocation)) {
                sparqlQuery = sparqlQueryOrg.replace(QUESTION_MARK + VARIABLE, "<" + subject + ">");
            } else if (template.equals(TemplateFinder.superlativeWorld)) {
                return sparqlQueryOrg;
            } else {
                   
                if (returnType.contains(RETURN_TYPE_OBJECT)) {
                    sparqlQuery = PrepareSparqlQuery.setObjectWikiPedia(subject, property, rdfProperty, className);
                } else if (returnType.contains(RETURN_TYPE_SUBJECT)) {
                    if (TemplateFinder.isRdfsLabel(property)) {
                        sparqlQuery = this.setSubjectLabelWikipedia(subject, property, language);
                    } else {
                        sparqlQuery = PrepareSparqlQuery.setSubjectWikipedia(subject, property, rdfProperty, className);
                    }
                }
            }

        } else {

            if (returnType.contains(RETURN_TYPE_OBJECT)) {
                sparqlQuery = PrepareSparqlQuery.setObjectWikiPedia(subject, property, rdfProperty, className);
            } else if (returnType.contains(RETURN_TYPE_SUBJECT)) {
                if (TemplateFinder.isRdfsLabel(property)) {
                    sparqlQuery = this.setSubjectLabelWikipedia(subject, property, language);
                } else {
                    sparqlQuery = PrepareSparqlQuery.setSubjectWikipedia(subject, property, rdfProperty, className);
                   
                }

            }
        }
        
        /*System.out.println("sparqlQueryOrg::" + sparqlQueryOrg);
                        System.out.println("sparqlQuery::" + sparqlQuery);
                        System.out.println("template::" + template);
                        System.out.println("className::" + className);*/
      
        
        return sparqlQuery;
    }

    
    private Integer isSimpleOrComposite(String sparql) {
        Integer index = 0;
        /*if (sparql != null) {
            String[] lines = sparql.split(System.getProperty("line.separator"));
            for (String line : lines) {
                if (line.contains("triple")) {
                    index = index + 1;
                }
            }
        }*/
        
        if (sparql != null) {
            String[] lines = sparql.split(System.getProperty("line.separator"));
            for (String line : lines) {
                if (line.contains(".")) {
                    index = index + 1;
                }
            }
        }

        return index;
    }
    
   
    public SparqlQuery(String sparqlQuery) {
        endpoint = SPARQLRequest.getSPARQL_ENDPOINT_URL();
        
        //The data set has a problem that it does not get results given language filter
        //This is a dirty solution for beniculturali dataset 
        if (endpoint.contains("beniculturali") ) {
           sparqlQuery=this.modifyBindingListSparql(sparqlQuery);
        }
        this.resultSparql = executeSparqlQueryCurl(sparqlQuery);
        this.parseResultBindingList(resultSparql);
    }

   

    public String executeSparqlQueryCurl(String query) {
        String result = null, resultUnicode = null;
        Process process = null;
      
           
            try {
                resultUnicode = this.stringToUrlUnicode(query);
                this.command = "curl " + endpoint + "?query=" + resultUnicode;
                process = Runtime.getRuntime().exec(command);
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
    
    public String setSubjectLabelWikipedia(String entityUrl, String property, String language) {
        String sparql = null;
        if (isEntity(entityUrl)) {
            sparql = PrepareSparqlQuery.setLabelWikipedia(entityUrl, language);
            String resultSparql = executeSparqlQueryCurl(sparql);
            this.parseResult(resultSparql);
            entityUrl = this.objectOfProperty;
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " + "'" + entityUrl + "'" + "\n"
                    + "    }";
        } else {
            sparql = "select  ?s\n"
                    + "    {\n"
                    + "   " + "?s" + " " + "<" + property + ">" + "  " +  entityUrl  + "\n"
                    + "    }";
        }
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
    
    
    /*
    
PREFIX dbo: <http://dbpedia.org/ontology/>
PREFIX res: <http://dbpedia.org/resource/>
SELECT DISTINCT ?uri WHERE { 
        res:French_Polynesia dbo:capital ?x .
        ?x dbo:mayor ?uri .
}
    
    
    
       sparqlQuery:::(bgp
  
  (triple ?subjOfPropx <http://dbpedia.org/ontology/capital> ?subjOfProp)
  (triple ?subjOfProp <http://dbpedia.org/ontology/mayor> ?objOfProp)
)
    "bindingType" : "Country",
    "returnType" : "Person",
    */

    private String setObjectWikiPediaComposite(String entityUrl, String sparqlOrg, QueryType queryType, String returnVariable) {
        String[] lines = sparqlOrg.split(System.getProperty("line.separator"));
        //entityUrl="http://dbpedia.org/resource/France";
        String sparql = "";
        for (String line : lines) {

            if (line.contains("bgp") && queryType.equals(QueryType.SELECT)) {
                line = "SELECT DISTINCT " + "?" + returnVariable + " WHERE { ";
            } /*else if (queryType.equals(QueryType.SELECT) && type.contains(RETURN_TYPE_SUBJECT)) {
                line = "SELECT DISTINCT " + "?" + RETURN_TYPE_SUBJECT + " WHERE { ";

            }*/ else if (line.contains("triple") || line.contains("?subjOfPropx") || line.contains("?objOfPropx")) {
                line = line.replace("triple", "");
                line = line.replace("?subjOfPropx", "<" + entityUrl + ">");
                line = line.replace("?objOfPropx", "<" + entityUrl + ">");
                line = line + " .";
            }

            sparql += line;

        }

        sparql = sparql.replace("(", "").replace(")", "") + "}";
        
        return sparql;
    }
    
    /*
    SELECT DISTINCT ?objOfProp WHERE { 
        <http://dbpedia.org/resource/France> <http://dbpedia.org/ontology/capital> ?subjOfProp .
        ?subjOfProp dbo:mayor ?objOfProp .
}
    */

    private boolean isPropertyLabel(String property) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean isEntity(String entityUrl) {
        if(entityUrl.contains("http:"))
            return true;
        return false;
    }

    private void parseResultSingle(List<String> results) {
        String result = null;
        if (results.size() > 0) {
            result = results.iterator().next();
            result = StringUtils.substringBetween(result, "<", ">");
        }
        this.objectOfProperty = result;
    }

    private String getSparqlID(String property,String domainEntityUrl,String rangeEntityUrl) {
        return property+domainEntityUrl+rangeEntityUrl;
    }

  

    /*private String getOfflineResult(String sparqlQueryID,String property) {
        property=property.replace("http://dbpedia.org/ontology", "");
        if(this.offLine.containsKey(sparqlQueryID)){
            this.objectOfProperty= this.offLine.get(sparqlQueryID).iterator().next();
        }
        return null;
    }*/
    
   


}
