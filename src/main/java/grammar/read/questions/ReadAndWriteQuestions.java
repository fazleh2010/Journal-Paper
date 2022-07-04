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
import static grammar.datasets.sentencetemplates.TempConstants.superlative;
import grammar.sparql.PrepareSparqlQuery;
import static grammar.sparql.SparqlQuery.RETURN_TYPE_SUBJECT;
import grammar.structure.component.FrameType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
import linkeddata.LinkedData;
import util.io.MatcherExample;
import util.io.OffLineResult;
import util.io.Statistics;
import util.io.StringMatcher;
import util.io.ReferenceManagement;

/**
 *
 * @author elahi
 */
public class ReadAndWriteQuestions implements ReadWriteConstants {

    private static String language = "en";
    private String propertyDir = null;
    private String classDir = null;

    public CSVWriter csvWriterQuestions;
    public CSVWriter csvWriterSummary;
    public CSVWriter csvWriterPropertyReport;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    private Set<String> excludes = new HashSet<String>();
    private Map<String, Statistics> summary = new TreeMap<String, Statistics>();
    private Integer maxNumberOfEntities = 100;
    private String endpoint = null;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ReadAndWriteQuestions.class);
    private Boolean online = false;
    private Boolean externalEntittyListflag = false;
    private LinkedData linkedData = null;
    private Map<String, OffLineResult> entityLabels = new TreeMap<String, OffLineResult>();

    public ReadAndWriteQuestions(String questionAnswerFile, String questionSummaryFile, Integer maxNumberOfEntities, String language, String endpoint, Boolean online, Boolean externalEntittyListflag, String entityDir, String classDir, LinkedData linkedData) {
        this.endpoint = endpoint;
        this.language = language;
        this.questionAnswerFile = questionAnswerFile;
        this.questionSummaryFile = questionSummaryFile;
        this.maxNumberOfEntities = maxNumberOfEntities;
        this.online = online;
        this.externalEntittyListflag = externalEntittyListflag;
        this.propertyDir = entityDir;
        this.classDir = classDir;
        this.linkedData = linkedData;
    }

    public void generateFullQuestionOnline(List<File> protoSimpleQFiles) throws Exception {
        Integer index = 0;

        if (protoSimpleQFiles.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        this.csvWriterQuestions = new CSVWriter(new FileWriter(this.questionAnswerFile, true));
        this.csvWriterSummary = new CSVWriter(new FileWriter(questionSummaryFile, true));
        this.writeInCSV();

        Set<String> existingEntries = this.getExistingLexicalEntries(questionSummaryFile);

        for (File file : protoSimpleQFiles) {
            index = index + 1;
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries = mapper.readValue(file, GrammarEntries.class);
            Integer total = grammarEntries.getGrammarEntries().size();
            Integer idIndex = 0, noIndex = 0;
            Integer fileIndex = 1;
            eachGrammarUnit(grammarEntries, existingEntries, noIndex, idIndex);
        }

        this.csvWriterQuestions.close();
        this.writeSummary(this.summary);
        this.csvWriterSummary.close();

    }

    private void eachGrammarUnit(GrammarEntries grammarEntries, Set<String> existingEntries, Integer noIndex, Integer idIndex) throws IOException, Exception {
        List<String> questions;
        for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
            String uri = null, className;

            if (grammarEntryUnit.getLexicalEntryUri() != null) {
                uri = grammarEntryUnit.getLexicalEntryUri().toString();
                if (existingEntries.isEmpty() && existingEntries.contains(uri)) {
                    continue;
                }
            } else {
                continue;
            }
            questions = this.validQuestion(grammarEntryUnit);

            if (questions.isEmpty()) {
                continue;
            }

            List<UriLabel> bindingList = this.getBindingList(grammarEntryUnit);
            String sparql = this.getSparql(grammarEntryUnit);

            if (grammarEntryUnit.getQueryType().equals(QueryType.SELECT)) {
                noIndex = this.replaceVariablesOnline(uri, sparql, bindingList, questions, noIndex, "", grammarEntryUnit, entityLabels);
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
    }

    private Integer replaceVariablesOnline(String uri, String sparqlQuery, List<UriLabel> uriLabels, List<String> questions, Integer rowIndex, String lexicalEntry, GrammarEntryUnit grammarEntryUnit, Map<String, OffLineResult> entityLabels) throws IOException {
        Integer index = 0;
        String returnSubjOrObj = grammarEntryUnit.getReturnVariable();
        String syntacticFrame = grammarEntryUnit.getFrameType();
        QueryType queryType = grammarEntryUnit.getQueryType();
        String rdfTypeProperty = linkedData.getRdfPropertyType();
        String className = linkedData.getRdfPropertyClass(grammarEntryUnit.getReturnType());
        String template = grammarEntryUnit.getSentenceTemplate();

        List< String[]> rows = new ArrayList<String[]>();

        if (questions.isEmpty()) {
            return rowIndex;
        }

        for (UriLabel uriLabel : uriLabels) {
            String questionUri = uriLabel.getUri(), questionLabel = uriLabel.getLabel();

            if (questionUri != null && !questionLabel.isEmpty()) {
                questionUri = questionUri.trim().strip().stripLeading().stripTrailing();
                questionLabel = uriLabel.getLabel().replaceAll("\'", "").replaceAll("\"", "");

            } else if (questionLabel.isEmpty()) {
                //find labels for the specific database
                if (this.online) {
                    String classType = this.linkedData.getClassProperty();
                    questionLabel = getUriLabel(questionUri, classType);
                }
            }

            if (!isKbValid(uriLabel)) {
                continue;
            }
            String questionForShow = questions.iterator().next();

            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }
            String[] wikipediaAnswer = new String[3];
            String sparql = null, answerUri = null, answerLabel = null;
            wikipediaAnswer = this.getAnswerFromWikipedia(template, rdfTypeProperty, className, questionUri, sparqlQuery, null, returnSubjOrObj, endpoint, online, queryType);
            sparql = wikipediaAnswer[0];

            if (this.online) {
                answerUri = wikipediaAnswer[1];
                answerLabel = wikipediaAnswer[2];
            } else {
                answerUri = uriLabel.getAnswerUri();
                answerLabel = uriLabel.getAnswerLabel();

            }
            System.out.println(lexicalEntry + " questionForShow:" + questionForShow + " questionUri::" + questionUri + " questionLabel:" + questionLabel + " answerUri:" + answerUri + " answerLabel" + answerLabel);

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

                        String id = uri + "_" + rowIndex.toString();
                        //question = modifyQuestion(question, uriLabel);
                        String questionT = question.replaceAll("(X)", questionLabel);
                        questionT = questionT.replace("(", "");
                        questionT = questionT.replace(")", "");
                        questionT = questionT.replace("$x", questionLabel);
                        questionT = questionT.replace(",", "");
                        questionT = questionT.stripLeading().trim();
                        if (questionLabel.isEmpty()) {
                            continue;
                        }

                        String[] record = {id, questionT, sparql, answerUri, answerLabel, syntacticFrame};
                        String[] newRecord = doubleQuote(record);
                        System.out.println("index::" + index + " questionT::" + questionT);

                        if (this.online) {
                            if (answerUri != null) {
                                this.csvWriterQuestions.writeNext(newRecord);
                                System.out.println("index::" + index + " questionT::" + questionT + " sparql::" + sparql + " answerUri::" + answerUri + " answerLabel::" + answerLabel + " syntacticFrame:" + syntacticFrame);
                                rowIndex = rowIndex + 1;
                            } else {
                                continue;
                            }
                        }
                        if (!this.online) {
                            this.csvWriterQuestions.writeNext(newRecord);
                            rowIndex = rowIndex + 1;
                        }

                        //}
                    }
                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " " + sparql + " " + answerLabel);
            }
        }

        return rowIndex;
    }

    private Integer replaceVariables(String template, String rdfPropertyType, String className, String uri, String sparqlQuery, List<UriLabel> domainList, List<UriLabel> rangeList, String returnSubjOrObj, List<String> questions, String syntacticFrame, Integer rowIndex, String lexicalEntry, QueryType queryType, Map<String, OffLineResult> entityLabels) throws IOException {
        Integer index = 0;
        for (UriLabel domainUriLabel : domainList) {
            String domainUri = "";
            if (!isKbValid(domainUriLabel)) {
                continue;
            } else {
                domainUri = domainUriLabel.getUri();
            }

            String questionForShow = questions.iterator().next();

            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }
            for (UriLabel rangeUriLabel : rangeList) {
                String rangeUri = "";
                if (!isKbValid(rangeUriLabel)) {
                    continue;
                } else {
                    rangeUri = rangeUriLabel.getUri();
                }

                if (domainUri.contains(rangeUri)) {
                    continue;
                }

                String[] wikipediaAnswer = new String[3];
                if (this.online) {
                    this.getAnswerFromWikipedia(template, rdfPropertyType, className, domainUriLabel.getUri(), sparqlQuery, rangeUriLabel.getUri(), returnSubjOrObj, endpoint, online, queryType);
                }
                String sparql = wikipediaAnswer[0];
                String answerUri = wikipediaAnswer[1];
                String answer = wikipediaAnswer[2];
                index = index + 1;
                sparql = this.modifySparql(sparql);
                rowIndex = this.makeQuestions(uri, answerUri, questions, index, rowIndex, rangeUriLabel.getLabel(), domainUriLabel.getLabel(), sparql, answer, syntacticFrame);
            }

        }
        return rowIndex;
    }

    public String[] getAnswerFromWikipedia(String template, String rdfPropertyType, String className, String domainEntityUri,
            String sparql, String rangeEntityUri, String returnSubjOrObj, String endpoint,
            Boolean online, QueryType queryType) throws IOException {
        String answerLabel = null, answerUri = null;
        SparqlQuery sparqlQuery = new SparqlQuery(template, rdfPropertyType, className, domainEntityUri, sparql, rangeEntityUri, SparqlQuery.FIND_ANY_ANSWER, returnSubjOrObj, language, endpoint, online, queryType, this.linkedData);
        answerUri = sparqlQuery.getObject();

        if (answerUri != null) {
            if (queryType.equals(QueryType.ASK)) {
                answerLabel = answerUri;
            } else if (queryType.equals(QueryType.SELECT)) {
                if (answerUri.contains("http:")) {
                    SparqlQuery sparqlQueryLabel = new SparqlQuery(template, rdfPropertyType, className, answerUri, sparql, rangeEntityUri, SparqlQuery.FIND_LABEL, null, language, endpoint, online, queryType, this.linkedData);
                    answerLabel = sparqlQueryLabel.getObject();
                    answerLabel = StringMatcher.modifyLabels(answerLabel);
                }

            }
        }
        return new String[]{sparqlQuery.getSparqlQuery(), answerUri, answerLabel};

    }

    private boolean isKbValid(UriLabel uriLabel) {
        if (uriLabel.getLabel() != null)
            ; else {
            return false;
        }

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

    private Integer makeQuestions(String uri, String answerUri, List<String> questions, Integer index, Integer rowIndex, String rangeLabel, String domainLabel, String sparql, String answer, String syntacticFrame) {
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
                String questionT = MatcherExample.replaceQuestion(question, new String[]{rangeLabel, domainLabel});
                String id = uri + "_" + rowIndex.toString();

                System.out.println("index::" + index + " questionT::" + questionT + " sparql::" + sparql + " answer::" + answer + " syntacticFrame:" + syntacticFrame);
                String[] record = {id, questionT, sparql, answerUri, answer, syntacticFrame};
                String[] newRecord = doubleQuote(record);
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

    private String[] doubleQuote(String[] row) {
        String[] newRow = new String[row.length];
        Integer index = 0;
        for (String string : row) {
            string = doubleQuote(string);
            newRow[index] = string;
            index = index + 1;
        }
        return newRow;
    }

    private String doubleQuote(String string) {
        return "\"" + string + "\"";
    }

    private String getProperty(String entityDir, String sparqlQueryOrg) {
        String property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
        property = property.replace("http://dbpedia.org/ontology/", "dbo_");
        property = property.replace("http://dbpedia.org/property/", "dbp_");
        return entityDir + property + ".txt";
    }

    private String getEntity(String entityDir, String bindingType) {
        return entityDir + bindingType + ".txt";
    }

    private String getProperty(String sparqlQueryOrg) {
        String property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
        property = property.replace("http://dbpedia.org/ontology/", "dbo_");
        property = property.replace("http://dbpedia.org/property/", "dbp_");
        return property;
    }

    private List<UriLabel> getOffLineBindingList(Map<String, OffLineResult> entityLabels, String returnType) {
        List<UriLabel> uriLabels = new ArrayList<UriLabel>();

        for (String uri : entityLabels.keySet()) {
            OffLineResult offLineResult = entityLabels.get(uri);
            UriLabel uriLabel = null;
            if (returnType.contains(RETURN_TYPE_SUBJECT)) {
                uriLabel = new UriLabel(offLineResult.getObjectUri(), offLineResult.getObjectLabel(), offLineResult.getSubjectUri(), offLineResult.getSubjectLabel());

            } else {
                uriLabel = new UriLabel(offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getObjectUri(), offLineResult.getObjectLabel());
            }
            uriLabels.add(uriLabel);

        }

        return uriLabels;
    }

    private void writeInCSV() {
        //this.csvWriterQuestions.writeNext(questionHeader);
        this.csvWriterSummary.writeNext(summaryHeader);
    }

    private List<UriLabel> getBIndingList(Map<String, OffLineResult> entityLabels) {
        List<UriLabel> urlLabels = new ArrayList<UriLabel>();
        for (String key : entityLabels.keySet()) {
            OffLineResult offLineResult = entityLabels.get(key);
            UriLabel uriLabel = new UriLabel(offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getObjectUri(), offLineResult.getObjectLabel());
            urlLabels.add(uriLabel);
        }
        return urlLabels;
    }

    public void offline(List<File> protoSimpleQFiles) throws Exception {
        Integer index = 0;

        if (protoSimpleQFiles.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        this.csvWriterSummary = new CSVWriter(new FileWriter(questionSummaryFile, true));
        this.writeInCSV();

        Set<String> existingEntries = this.getExistingLexicalEntries(questionSummaryFile);
        ReferenceManagement referenceManagement = new ReferenceManagement(this.propertyDir, this.classDir, protoSimpleQFiles, GENERATED);
        Set<String> offlineMatchedProperties = referenceManagement.getMatchedProperties();
        Set<String> offlineEntities = referenceManagement.getOfflineGeneratedEntities();

        if (offlineMatchedProperties.isEmpty()) {
            throw new Exception("No off line properties to process!!");
        }

        for (File file : protoSimpleQFiles) {
            index = index + 1;
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries = mapper.readValue(file, GrammarEntries.class);
            Integer total = grammarEntries.getGrammarEntries().size();
            Integer idIndex = 0, noIndex = 0;
            LOG.info("running file'", file.getName());
            List<String> questions = new ArrayList<String>();
            Integer fileIndex = 1;

            for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
                String uri = null, className;
                className = linkedData.getRdfPropertyClass(grammarEntryUnit.getReturnType());
                String template = grammarEntryUnit.getSentenceTemplate();

                if (grammarEntryUnit.getLexicalEntryUri() != null) {
                    uri = grammarEntryUnit.getLexicalEntryUri().toString();
                    if (existingEntries.isEmpty() && existingEntries.contains(uri)) {
                        continue;
                    }
                } else {
                    continue;
                }

                questions = grammarEntryUnit.getSentences();
                if (questions.contains("Where is $x located?")) {
                    continue;
                }
                String sparql = grammarEntryUnit.getSparqlQuery();
                String returnSubjOrObj = grammarEntryUnit.getReturnVariable();
                String bindingType = grammarEntryUnit.getBindingType();
                String returnType = grammarEntryUnit.getReturnType();
                String syntacticFrame = grammarEntryUnit.getFrameType();
                List<UriLabel> bindingList = grammarEntryUnit.getBindingList();
                String property = this.getProperty(grammarEntryUnit.getSparqlQuery());
                //String fileId = grammarEntryUnit.getLexicalEntryUri().toString().replace("http://localhost:8080#", "") + "-" + "dbo_" + bindingType + "-" + property + "-" + "dbo_" + returnType + "-";
                //fileId = fileId + grammarEntryUnit.getFrameType() + "-" + grammarEntryUnit.getReturnVariable();
                //String questionAnswerFileTemp = this.questionAnswerFile.replace(".csv", "") + "-" + fileId + "-" + (fileIndex++).toString() + ".csv";

                if (!offlineMatchedProperties.contains(property)) {
                    continue;
                }
                try {
                    //String fileId = bindingType + "_" + grammarEntryUnit.getLexicalEntryUri().toString().replace("http://localhost:8080#", "") + "_" + returnType + "_";
                    String fileId = grammarEntryUnit.getLexicalEntryUri().toString().replace("http://localhost:8080#", "") + "-" + "dbo_" + bindingType + "-" + property + "-" + "dbo_" + returnType + "-";
                    fileId = fileId + grammarEntryUnit.getFrameType() + "-" + grammarEntryUnit.getReturnVariable();
                    String questionAnswerFileTemp = this.questionAnswerFile.replace(".csv", "") + "-" + fileId + "-" + (fileIndex++).toString() + ".csv";
                    this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFileTemp, true));
                    String propertyFile = this.getProperty(this.propertyDir, grammarEntryUnit.getSparqlQuery());
                    this.entityLabels = FileUtils.getEntityLabels(propertyFile, this.classDir, returnSubjOrObj, bindingType, returnType);
                    /*System.out.println("entityLabels::" + entityLabels.size());
                    System.out.println("property::" + property);
                    System.out.println("offlineProperties::" + offlineMatchedProperties);
                    exit(1);*/
                } catch (Exception ex) {
                    continue;
                }

                if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {
                    continue;
                }

                bindingList = this.getOffLineBindingList(entityLabels, returnSubjOrObj);

                if (grammarEntryUnit.getBindingType().contains("date")) {
                    bindingList = grammarEntryUnit.getBindingList();
                }

                try {
                    if (grammarEntryUnit.getFrameType().contains(FrameType.AG.toString()) && grammarEntryUnit.getSentenceTemplate().contains(superlative)) {
                        sparql = grammarEntryUnit.getExecutable();
                    } else {
                        sparql = grammarEntryUnit.getSparqlQuery();

                    }
                } catch (Exception ex) {
                    continue;
                }

                if (grammarEntryUnit.getQueryType().equals(QueryType.SELECT)) {
                    noIndex = this.replaceVariablesOnline(uri, sparql, bindingList, questions, noIndex, "", grammarEntryUnit, entityLabels);
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
                if (!this.online) {
                    this.csvWriterQuestions.close();
                }

            }
            questions = new ArrayList<String>();
        }
        this.writeSummary(this.summary);
        this.csvWriterSummary.close();

    }

    /*private Integer replaceVariablesOffline(String template, String className, String uri, String sparqlQuery, List<UriLabel> uriLabels, List<String> questions, Integer rowIndex, String lexicalEntry, GrammarEntryUnit grammarEntryUnit, Map<String, OffLineResult> entityLabels) throws IOException {
        Integer index = 0;
        String returnSubjOrObj = grammarEntryUnit.getReturnVariable();
        String syntacticFrame = grammarEntryUnit.getFrameType();
        QueryType queryType = grammarEntryUnit.getQueryType();
        String rdfTypeProperty = linkedData.getRdfPropertyType();

        List< String[]> rows = new ArrayList<String[]>();

        if (questions.isEmpty()) {
            return rowIndex;
        }

        for (UriLabel uriLabel : uriLabels) {
            String questionUri = uriLabel.getUri(), questionLabel = uriLabel.getLabel();

            if (questionUri != null && questionLabel != null) {
                questionUri = questionUri.trim().strip().stripLeading().stripTrailing();
                questionLabel = uriLabel.getLabel().replaceAll("\'", "").replaceAll("\"", "");

            } else {
                continue;
            }

            if (!isKbValid(uriLabel)) {
                continue;
            }
            String questionForShow = questions.iterator().next();

            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }
            String[] wikipediaAnswer = new String[3];
            String sparql = null, answerUri = null, answerLabel = null;
            wikipediaAnswer = this.getAnswerFromWikipedia(template, rdfTypeProperty, className, questionUri, sparqlQuery, null, returnSubjOrObj, endpoint, online, queryType);
            sparql = wikipediaAnswer[0];

            if (this.online) {
                answerUri = wikipediaAnswer[1];
                answerLabel = wikipediaAnswer[2];
            } else {
                answerUri = uriLabel.getAnswerUri();
                answerLabel = uriLabel.getAnswerLabel();

            }
            System.out.println(lexicalEntry + " questionForShow:" + questionForShow + " questionUri::" + questionUri + " questionLabel:" + questionLabel + " answerUri:" + answerUri + " answerLabel" + answerLabel);

            index = index + 1;
            sparql = this.modifySparql(sparql);

            try {
             {

                    if (online) {
                       
                    }

                    for (String question : questions) {
                        if (question.contains("(") && question.contains(")")) {
                            String result = StringUtils.substringBetween(question, "(", ")");
                            question = question.replace(result, "X");
                        } else if (question.contains("$x")) {

                        }

                        String id = uri + "_" + rowIndex.toString();
                        //question = modifyQuestion(question, uriLabel);
                        String questionT = question.replaceAll("(X)", questionLabel);
                        questionT = questionT.replace("(", "");
                        questionT = questionT.replace(")", "");
                        questionT = questionT.replace("$x", questionLabel);
                        questionT = questionT.replace(",", "");
                        questionT = questionT.stripLeading().trim();
                        if (questionLabel.isEmpty()) {
                            continue;
                        }

                        String[] record = {id, questionT, sparql, answerUri, answerLabel, syntacticFrame};
                        String[] newRecord = doubleQuote(record);
                        System.out.println("index::" + index + " questionT::" + questionT);

                        if (this.online) {
                            if (answerUri != null) {
                                this.csvWriterQuestions.writeNext(newRecord);
                                System.out.println("index::" + index + " questionT::" + questionT + " sparql::" + sparql + " answerUri::" + answerUri + " answerLabel::" + answerLabel + " syntacticFrame:" + syntacticFrame);
                                rowIndex = rowIndex + 1;
                            } else {
                                continue;
                            }
                        }
                        if (!this.online) {
                            this.csvWriterQuestions.writeNext(newRecord);
                            rowIndex = rowIndex + 1;
                        }

                        //}
                    }
                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " " + sparql + " " + answerLabel);
            }
        }

        return rowIndex;
    }*/

    private List<UriLabel> getBindingList(GrammarEntryUnit grammarEntryUnit) {
        List<UriLabel> bindingList = grammarEntryUnit.getBindingList();

        if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {
            return new ArrayList<UriLabel>();
        }

        //If it is an external binding list
        if (externalEntittyListflag) {
            bindingList = this.getBIndingList(this.entityLabels);
        } else {
            bindingList = grammarEntryUnit.getBindingList();

        }

        if (grammarEntryUnit.getBindingType().contains("date")) {
            bindingList = grammarEntryUnit.getBindingList();
        }
        return bindingList;
    }

    private String getSparql(GrammarEntryUnit grammarEntryUnit) throws Exception {
        String sparql = grammarEntryUnit.getSparqlQuery();

        try {
            if (grammarEntryUnit.getFrameType().contains(FrameType.AG.toString()) && grammarEntryUnit.getSentenceTemplate().contains(superlative)) {
                sparql = grammarEntryUnit.getExecutable();
            } else {
                sparql = grammarEntryUnit.getSparqlQuery();

            }
        } catch (Exception ex) {
            throw new Exception("no sparlq query!!!");
        }
        return sparql;
    }

    private List<String> validQuestion(GrammarEntryUnit grammarEntryUnit) {
        if (!grammarEntryUnit.getSentences().contains("Where is $x located?")) {
            return grammarEntryUnit.getSentences();
        }
        return new ArrayList<String>();
    }

    private String getUriLabel(String uri, String property) {
        String classSparql = PrepareSparqlQuery.findClassGivenInstance(uri, property);
        SparqlQuery sparqlQueryForClass = new SparqlQuery(this.linkedData, classSparql);
        String labelProperty = this.linkedData.getRdfPropertyLabel(sparqlQueryForClass.getObject());
        String labelSparql = PrepareSparqlQuery.findClassGivenInstance(uri, labelProperty);
        sparqlQueryForClass = new SparqlQuery(this.linkedData, labelSparql);
        return sparqlQueryForClass.getObject();
    }

}
