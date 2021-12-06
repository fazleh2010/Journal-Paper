package grammar.read.questions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import grammar.sparql.SparqlQuery;
import util.io.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import static grammar.generator.BindingConstants.DEFAULT_BINDING_VARIABLE;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jena.query.QueryType;
import org.apache.logging.log4j.LogManager;
import util.io.CsvFile;
import util.io.MatcherExample;
import util.io.Statistics;

/**
 *
 * @author elahi
 */
public class ReadAndWriteQuestions {

    public String[] questionHeader = new String[]{ID, QUESTION, SPARQL, ANSWER_URI, ANSWER_LABEL, FRAME};
    public String[] summaryHeader = new String[]{LEXICAL_ENTRY, NUMBER_OF_GRAMMAR_RULES, NUMBER_OF_QUESTIONS, FRAMETYPE, Status, Reason};
    public static String FRAMETYPE_NPP = "NPP";
    public static final String ID = "id";
    public static final String QUESTION = "question";
    public static final String SPARQL = "sparql";
    public static final String ANSWER_URI = "answer_uri";
    public static final String ANSWER_LABEL = "answer_label";
    public static final String FRAME = "frame";
    private static final String LEXICAL_ENTRY = "lexicalEntry";
    private static final String SENTENCETYPE = "sentenceType";
    private static final String FRAMETYPE = "frameType";
    private static final String NUMBER_OF_GRAMMAR_RULES = "numberOfGrammarRules";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String Status = "numberOfQuestions";
    private static final String Reason = "numberOfQuestions";
    private static String language = "en";
    private String entityDir = null;
    //public static String qaldFileBinding = "qaldEntities.csv";
    public static String qaldFileBinding = "qaldEntities.csv";


    public CSVWriter csvWriterQuestions;
    public CSVWriter csvWriterSummary;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    private Set<String> excludes = new HashSet<String>();
    private Map<String, Statistics> summary = new TreeMap<String, Statistics>();

    private Map<String, String> bindingLabels = new TreeMap<String, String>();
    private Map<String, String> answerLabels = new TreeMap<String, String>();

    private Integer maxNumberOfEntities = 100;
    private String endpoint = null;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ReadAndWriteQuestions.class);
    private Boolean online = false;

    public ReadAndWriteQuestions(String questionAnswerFile, String questionSummaryFile, Integer maxNumberOfEntities, String language, String endpoint, Boolean online) {
        this.initialExcluded();
        this.endpoint = endpoint;
        this.language = language;
        this.questionAnswerFile = questionAnswerFile;
        this.questionSummaryFile = questionSummaryFile;
        this.maxNumberOfEntities = maxNumberOfEntities;
        this.online = online;
    }

    public void readQuestionAnswers(List<File> fileList, String entityDir, Boolean externalEntittyListflag) throws Exception {
        String sparql = null;
        Integer index = 0;
        this.entityDir = entityDir+File.separator+ this.language;
        System.out.println("this.entityDir ::"+this.entityDir );
       

        this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFile,true));
        this.csvWriterSummary = new CSVWriter(new FileWriter(questionSummaryFile,true));
        //this.csvWriterQuestions.writeNext(questionHeader);
        //this.csvWriterSummary.writeNext(summaryHeader);

        LOG.info("Number of Files!!'", fileList.size());
        Set<URI> lexicalEntries = new HashSet<URI>();
        Set<String> existingEntries=this.getExistingLexicalEntries(questionSummaryFile);
       

        for (File file : fileList) {
            index = index + 1;
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries = mapper.readValue(file, GrammarEntries.class);
            Integer total = grammarEntries.getGrammarEntries().size();
            Integer idIndex = 0, noIndex = 0;
            LOG.info("running file'", file.getName());
            List<String> questions = new ArrayList<String>();

            for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
                String uri = null;
                
                
                if (grammarEntryUnit.getLexicalEntryUri() != null) {
                    uri = grammarEntryUnit.getLexicalEntryUri().toString();
                    if (existingEntries.isEmpty()&&existingEntries.contains(uri)) {
                        continue;
                    }
                   
                }
                
                /* if(!grammarEntryUnit.getBindingType().contains("date")){
                   continue; 
                }*/
                 
                

                /*if (idIndex > 1) {
                    break;
                }*/
                /*if (grammarEntryUnit.getSentences().iterator().next().contains("Where is $x located?"))
                    continue;*/
                //this code is giving some errors so block this code...
                /*if (!lexicalEntries.contains(grammarEntryUnit.getLexicalEntryUri()) && grammarEntryUnit.getFrameType().equals("NPP")) {
                    lexicalEntries.add(grammarEntryUnit.getLexicalEntryUri());
                    questions.addAll(grammarEntryUnit.getSentences());
                    continue;
                } else if (lexicalEntries.contains(grammarEntryUnit.getLexicalEntryUri()) && grammarEntryUnit.getFrameType().equals("NP")) {
                    questions.addAll(grammarEntryUnit.getSentences());
                } else if (grammarEntryUnit.getFrameType().equals("FULL_DATASET")) {
                    continue;
                } else {
                    questions.addAll(grammarEntryUnit.getSentences());
                } */
                questions = grammarEntryUnit.getSentences();
                sparql = grammarEntryUnit.getSparqlQuery();
                String returnSubjOrObj = grammarEntryUnit.getReturnVariable();
                String bindingType = grammarEntryUnit.getBindingType();
                String returnType = grammarEntryUnit.getReturnType();
                String syntacticFrame = grammarEntryUnit.getFrameType();
                String property = StringUtils.substringBetween(sparql, "<", ">");
                List<UriLabel> bindingList = new ArrayList<UriLabel>();
                List<UriLabel> returnList=new ArrayList<UriLabel>();
                Map<String, String[]> resultsOffline = new TreeMap<String, String[]>();
                
                if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {
                    continue;
                }

                /*if(grammarEntryUnit.getBindingType().contains("LOCATION")&&grammarEntryUnit.getReturnType().contains("LOCATION"))
                    continue;*/
                /*String qaldBindingListFileName = entityDir + File.separator + qaldFileBinding;
                File qaldBindingListFile = new File(qaldBindingListFileName);
                List<UriLabel> qaldBindingList = this.getExtendedBindingList(grammarEntryUnit.getBindingList(), qaldBindingListFile, 0, 2, bindingType.toLowerCase());
                bindingList.addAll(qaldBindingList);
                List<UriLabel> qaldReturnList = this.getExtendedBindingList(grammarEntryUnit.getBindingList(), qaldBindingListFile, 0, 2, returnType.toLowerCase());
                returnList.addAll(qaldReturnList);
                */

     
                if (externalEntittyListflag) {
                    String entityFileName = this.entityDir + File.separator + qaldFileBinding;
                    File entityFile = new File(entityFileName);
                    List<UriLabel>  qaldBindingList = this.getExtendedBindingList(grammarEntryUnit.getBindingList(), entityFile, 0, 2, bindingType.toLowerCase());
                    bindingList.addAll(qaldBindingList);
                    List<UriLabel>  qaldReturnList = this.getExtendedBindingList(grammarEntryUnit.getBindingList(), entityFile, 0, 2, returnType.toLowerCase()); 
                    returnList.addAll(qaldReturnList);
                    /*if(grammarEntryUnit.getQueryType().equals(QueryType.ASK)){
                       returnList = this.getExtendedBindingList(grammarEntryUnit.getBindingList(), entityFile, 0, 2, returnType.toLowerCase()); 
                       System.out.println("bindingList::"+bindingList);   
                       System.out.println("returnList::"+returnList);    
                    }*/
                } else {
                    bindingList = grammarEntryUnit.getBindingList();       

                }
                
                if(grammarEntryUnit.getBindingType().contains("date")){
                    bindingList = grammarEntryUnit.getBindingList();       
                }
                
             

                /*if (!this.online) {
                    String propertyFileName = new File(new URL(property).getPath()).getName();
                    propertyFileName = entityDir + propertyFileName + "_" + bindingType.toLowerCase() + "_" + returnSubjOrObj+ ".csv";       
                    System.out.println("lastPartOfProperty::"+propertyFileName);         
                    File propertyNameFile = new File(propertyFileName);
                    //resultsOffline = getDataFromFile(propertyNameFile, 0, 100000);
                }*/
                if (grammarEntryUnit.getQueryType().equals(QueryType.SELECT)) {
                    noIndex = this.replaceVariables(uri,sparql, bindingList, property, returnSubjOrObj, questions, syntacticFrame, noIndex, "", resultsOffline, grammarEntryUnit.getQueryType());
                    noIndex = noIndex + 1;
                    idIndex = idIndex + 1;
                }
                else if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {                    
                    noIndex = this.replaceVariables(uri,sparql, bindingList, property, returnList,returnSubjOrObj, questions, syntacticFrame, noIndex, "", resultsOffline, grammarEntryUnit.getQueryType());
                    noIndex = noIndex + 1;
                    idIndex = idIndex + 1;
                }
                
                if (grammarEntryUnit.getLexicalEntryUri() != null) {
                   uri = grammarEntryUnit.getLexicalEntryUri().toString();

                    if (this.summary.containsKey(uri)) {
                        Statistics summary = this.summary.get(uri);
                        this.summary.put(uri, new Statistics(grammarEntryUnit.getFrameType(), summary.getNumberOfGrammarRules() + 1, noIndex, bindingList.size()));
                    } else {
                        Statistics summary = new Statistics(grammarEntryUnit.getFrameType(), 1, noIndex, bindingList.size());
                        this.summary.put(uri, summary);
                    }
                }

            }
            questions = new ArrayList<String>();
        }
        this.writeSummary(this.summary);
        this.csvWriterQuestions.close();
        this.csvWriterSummary.close();

    }

    private Integer replaceVariables(String uri,String sparqlQuery, List<UriLabel> uriLabels, String property, String returnSubjOrObj, List<String> questions, String syntacticFrame, Integer rowIndex, String lexicalEntry, Map<String, String[]> resultsOffline, QueryType queryType) throws IOException {
        Integer index = 0;
        List< String[]> rows = new ArrayList<String[]>();
        //if(queryType.equals(QueryType.ASK)){
        //   String[] wikipediaAnswer = this.getAnswerFromWikipedia(uriLabel.getUri(), property, returnSubjOrObj, endpoint,online,resultsOffline);

        //}
        
        if(questions.isEmpty())
            return rowIndex;
        
        for (UriLabel uriLabel : uriLabels) {
             System.out.println(uriLabel+ " uriLabel:" );
            
            if (!isKbValid(uriLabel)) {
                continue;
            }
            String questionForShow = questions.iterator().next();
            System.out.println(questions.get(0) + " sparqlQuery:" + sparqlQuery + " property:" + property);

            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }

            String[] wikipediaAnswer = this.getAnswerFromWikipedia(uriLabel.getUri(), property, null,returnSubjOrObj, endpoint, online, resultsOffline,queryType);
            String sparql = wikipediaAnswer[0];
            String answerUri = wikipediaAnswer[1];
            String answer = wikipediaAnswer[2];
            index = index + 1;
            sparql = this.modifySparql(sparql);

            try {
                /*if (answer.isEmpty() || answer.contains("no answer found")) {
                    continue;
                } else*/ {
                    /*if (index >= this.maxNumberOfEntities) {
                        break;
                    }*/

                    if (online) {
                        /*if (answerUri.isEmpty() || answerUri.length() < 2) {
                            continue;
                        }*/
                    }

                    for (String question : questions) {
                        if (question.contains("(") && question.contains(")")) {
                            String result = StringUtils.substringBetween(question, "(", ")");
                            question = question.replace(result, "X");
                        } else if (question.contains("$x")) {

                        }

                        String id = uri+"_"+rowIndex.toString();
                        //question = modifyQuestion(question, uriLabel);
                        String questionT = question.replaceAll("(X)", uriLabel.getLabel());
                        questionT = questionT.replace("(", "");
                        questionT = questionT.replace(")", "");
                        questionT = questionT.replace("$x", uriLabel.getLabel());
                        questionT = questionT.replace(",", "");
                        questionT = questionT.stripLeading().trim();
                        if (uriLabel.getLabel().isEmpty()) {
                            continue;
                        }

                        // System.out.println("index::" + index + " uriLabel::" + uriLabel.getLabel() + " questionForShow::" + questionForShow + " sparql::" + sparql + " answer::" + answer + " syntacticFrame:" + syntacticFrame);
                        String[] record = {id, questionT, sparql, answerUri, answer, syntacticFrame};
                        String[]newRecord= doubleQuote(record);
                        this.csvWriterQuestions.writeNext(newRecord);
                        rowIndex = rowIndex + 1;
                    }
                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " " + sparql + " " + answer);
            }
        }
        

        return rowIndex;
    }
    
    private Integer replaceVariables(String uri,String sparqlQuery, List<UriLabel> domainList, String property, List<UriLabel> rangeList, String returnSubjOrObj, List<String> questions, String syntacticFrame, Integer rowIndex, String lexicalEntry, Map<String, String[]> resultsOffline, QueryType queryType) throws IOException {
        Integer index = 0;
        for (UriLabel domainUriLabel : domainList) {
            String domainUri="";
            if (!isKbValid(domainUriLabel)) 
                continue;
            else
                domainUri=domainUriLabel.getUri();
            
            String questionForShow = questions.iterator().next();
            System.out.println(questions.get(0) + " sparqlQuery:" + sparqlQuery + " property:" + property);

            if (questionForShow.contains("Where is $x located?")) 
                continue;
            System.out.println("domainUri:::" + domainUri);
            for (UriLabel rangeUriLabel : rangeList) {
                String rangeUri="";
                if (!isKbValid(rangeUriLabel)) {
                    continue;
                } 
                else
                rangeUri=rangeUriLabel.getUri();
                
                if (domainUri.contains(rangeUri)) {
                    continue;
                }
             
           
            System.out.println("rangeUri:::" + rangeUri);
            
            String[] wikipediaAnswer = this.getAnswerFromWikipedia(domainUriLabel.getUri(), property, rangeUriLabel.getUri(),returnSubjOrObj, endpoint, online, resultsOffline,queryType);
            String sparql = wikipediaAnswer[0];
            String answerUri = wikipediaAnswer[1];
            String answer = wikipediaAnswer[2];
            index = index + 1;
            sparql = this.modifySparql(sparql);
            rowIndex=this.makeQuestions(uri,answerUri,questions,index,rowIndex,rangeUriLabel.getLabel(),domainUriLabel.getLabel(),sparql,answer,syntacticFrame);
            }

        }
        return rowIndex;
    }


    public String[] getAnswerFromWikipedia(String domainEntityUri, String property, String rangeEntityUri, String returnSubjOrObj, String endpoint, Boolean online, Map<String, String[]> resultsOffline,QueryType queryType) throws IOException {
        String answerUri = "";
        String answerLabel = "";
        SparqlQuery sparqlQuery = null;

        /*if (!this.online && resultsOffline.containsKey(entityUri)) {
            answerUri = resultsOffline.get(entityUri)[1];
            answerLabel = resultsOffline.get(entityUri)[2];
            sparqlQuery = new SparqlQuery(entityUri, property, SparqlQuery.FIND_ANY_ANSWER, returnSubjOrObj, language, endpoint, false);
            //if (returnSubjOrObj.contains("subjOfProp")) {
            //    System.out.println("subjProp:::" + entityUri);
            //    System.out.println("property:::" + property);
            //    System.out.println("returnType:::" + returnSubjOrObj);
            //    System.out.println("answerUri:::" + answerUri);
            //    System.out.println("answerLabel:::" + answerLabel);
            //    System.out.println("sparqlQuery.getSparqlQuery():::" + sparqlQuery.getSparqlQuery());
            //}

            return new String[]{sparqlQuery.getSparqlQuery(), answerUri, answerLabel};
        }*/
       
        sparqlQuery = new SparqlQuery(domainEntityUri, property, rangeEntityUri, SparqlQuery.FIND_ANY_ANSWER, returnSubjOrObj, language, endpoint, online,queryType);
        answerUri = sparqlQuery.getObject();
        if (answerUri != null) {
            if (queryType.equals(QueryType.ASK)) {
                answerLabel = answerUri;
            } else if (queryType.equals(QueryType.SELECT)) {
                if (answerUri.contains("http:")) {
                    SparqlQuery sparqlQueryLabel = new SparqlQuery(answerUri, property, rangeEntityUri, SparqlQuery.FIND_LABEL, null, language, endpoint, online, queryType);
                    answerLabel = sparqlQueryLabel.getObject();
                }
            }

        }
        return new String[]{sparqlQuery.getSparqlQuery(), answerUri, answerLabel};
    }
    
   

    private void initialExcluded() {
        this.excludes.add("2013_Santa_Monica_shooting");
        this.excludes.add("2014_Fort_Hood_shooting");
        this.excludes.add("2014_killings_of_NYPD_officers");
        this.excludes.add("2015_Chattanooga_shootings");
        this.excludes.add("2015_Lafayette_shooting");
        this.excludes.add("2015_Parramatta_shooting");
        this.excludes.add("2015_Sousse_attacks");
        this.excludes.add("2016_Berlin_truck_attack");
        this.excludes.add("2016_New_York_and_New_Jersey_bombings");
        this.excludes.add("2016_UCLA_shooting");
        this.excludes.add("2016_shooting_of_Almaty_police_officers");
        this.excludes.add("2016_shooting_of_Dallas_police_officers");
        this.excludes.add("2017_Fresno_shooting_spree");
        this.excludes.add("7669_(group)");
        this.excludes.add("2013_Hialeah_shooting");
        this.excludes.add("2014_Isla_Vista_killings");
        this.excludes.add("2014_Las_Vegas_shootings");
        this.excludes.add("2014_shootings_at_Parliament_Hill,_Ottawa");
        this.excludes.add("2016_Munich_shooting");
        this.excludes.add("2016_shooting_of_Baton_Rouge_police_officers");
        this.excludes.add("2017_New_York_City_truck_attack");
    }

    private boolean isKbValid(UriLabel uriLabel) {
        String kb = uriLabel.getUri().replace("http://dbpedia.org/resource/", "");
        if (this.excludes.contains(kb)) {
            return false;
        }
        return true;
    }

    private List<UriLabel> getExtendedBindingList(List<UriLabel> bindingList, File classFile, Integer keyindex, Integer classIntex, String bindingType) throws IOException {
        List<UriLabel> modifyLabels = new ArrayList<UriLabel>();
        Map<String, String> map = new TreeMap<String, String>();
        for (UriLabel uriLabel : bindingList) {
            if (isKbValid(uriLabel)) {
                map.put(uriLabel.getUri(), uriLabel.getLabel());
            }
        }
        Map<String, String[]> temp = FileUtils.getDataFromFile(classFile, keyindex, classIntex, bindingType);
        for (String key : temp.keySet()) {
            String[] values = temp.get(key);
            String value = values[1];
            UriLabel uriLabel = new UriLabel(key, value);
            modifyLabels.add(uriLabel);
        }
        return modifyLabels;
    }

    private String modifySparql(String sparql) {
        sparql = sparql.stripLeading().trim();
        sparql = sparql.replace("\n", "");
        sparql = sparql.replace(" ", "+");
        sparql = sparql.replace("+", " ");
        return sparql;
    }

    private void writeSummary(Map<String, Statistics> summary) {
        if (summary.isEmpty()) {
            return;
        }
        for (String key : summary.keySet()) {
            Statistics element = summary.get(key);
            String[] record = {key, element.getNumberOfGrammarRules().toString(), element.getNumberOfQuestions().toString(), element.getFrameType(), element.getSuccess_Fail(), element.getReason()};
            this.csvWriterSummary.writeNext(record);

        }
    }

    private Integer makeQuestions(String uri,String answerUri, List<String> questions, Integer index, Integer rowIndex, String rangeLabel, String domainLabel, String sparql, String answer, String syntacticFrame) {
        if (rangeLabel.isEmpty() || domainLabel.isEmpty()) {
            return rowIndex;
        }

        try {

            if (online) {
                if (answerUri.isEmpty() || answerUri.length() < 2) {
                    return rowIndex;
                }
            }

            for (String question : questions) {
                //System.out.println("question::" + question);
                String questionT = MatcherExample.replaceQuestion(question, new String[]{rangeLabel,domainLabel});
                //System.out.println("questionT::" + questionT);
                //String questionT=this.replaceQuestion(question,rangeUriLabel,domainUriLabel);
                String id = uri+"_"+rowIndex.toString();

                System.out.println("index::" + index + " questionT::" + questionT + " sparql::" + sparql + " answer::" + answer + " syntacticFrame:" + syntacticFrame);
                String[] record = {id, questionT, sparql, answerUri, answer, syntacticFrame};
                String[]newRecord= doubleQuote(record);
                this.csvWriterQuestions.writeNext(newRecord);
                rowIndex = rowIndex + 1;
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage() + " " + sparql + " " + answer);
        }
        return rowIndex;
    }
    
  
    
    private String replaceQuestion(String question, String rangeLabel, String domainLabel) {
        
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(question);
        while (m.find()) {
            System.out.println(m.group(1));
        }
        /*if (question.contains("(") && question.contains(")")) {
            String result = StringUtils.substringBetween(question, "(", ")");
            question = question.replace(result, "X");
        } else if (question.contains("$x")) {

        }
        String questionT = question.replaceAll("(X)", label);
        questionT = questionT.replace("(", "");
        questionT = questionT.replace(")", "");
        questionT = questionT.replace("$x", label);
        questionT = questionT.replace(",", "");
        questionT = questionT.stripLeading().trim();*/
        return question;
    }

    public static String cleanString(String sentence) {
        return String.format("^%s$", sentence
                .replace(DEFAULT_BINDING_VARIABLE, "([\\w\\s\\d-,.']+)")
                .replaceAll("\\((.+)\\s\\|\\s(.+)\\)", "([\\\\w\\\\s\\\\d-,.']+)")
                .replace("?", "\\?")
                .toLowerCase()
                .trim());
    }

    private Set<String> getExistingLexicalEntries(String questionSummaryFile) {
        Integer index = 0;
        CsvFile csvFile = new CsvFile();
        Set<String> lexicalEntries = new HashSet<String>();
        List<String[]> rows = csvFile.getRows(new File(questionSummaryFile));
        for (String[] row : rows) {
            String column = row[0];
            column = column.trim().strip().stripLeading().stripTrailing();
            lexicalEntries.add(column);
        }
        return lexicalEntries;
    }

   private String[] doubleQuote(String []row) {
       String []newRow=new String[row.length];
       Integer index=0;
        for(String string:row){
            string=doubleQuote(string);
            newRow[index]=string;
            index=index+1;
        }
        return newRow;
    }
   
    private String doubleQuote(String string) {
        return "\""+string+"\"";
    }

    
}
