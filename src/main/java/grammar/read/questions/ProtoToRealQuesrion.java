package grammar.read.questions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import grammar.sparql.SparqlQuery;
import util.io.FileProcessUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import static grammar.datasets.sentencetemplates.TempConstants.superlative;
import grammar.sparql.PrepareSparqlQuery;
import static grammar.sparql.SparqlQuery.RETURN_TYPE_SUBJECT;
import grammar.structure.component.FrameType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.jena.query.QueryType;
import org.apache.logging.log4j.LogManager;
import util.io.CsvFile;
import linkeddata.LinkedData;
import util.io.AddQuote;
import static util.io.BashScript.FIND_ABSTRACT;
import static util.io.BashScript.FIND_IMAGE_LINK;
import static util.io.BashScript.FIND_WIKI_LINK;
import util.io.InputCofiguration;
import util.io.OffLineResult;
import util.io.Statistics;
import util.io.StringMatcher;
import util.io.ReferenceManagement;

/**
 *
 * @author elahi
 */
public class ProtoToRealQuesrion implements ReadWriteConstants {

    private static String language = "en";
    private String propertyDir = null;
    public CSVWriter csvWriterQuestions;
    public CSVWriter csvWriterSummary;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    private Map<String, Statistics> summary = new TreeMap<String, Statistics>();
    private Integer maxNumberOfEntities = 100;
    private String endpoint = null;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ProtoToRealQuesrion.class);
    private LinkedData linkedData = null;
    private Map<String, OffLineResult> entityLabels = new TreeMap<String, OffLineResult>();
    private InputCofiguration inputCofiguration = null;
    private Boolean online = false;
    private List<String> menus = Stream.of(FIND_WIKI_LINK, FIND_ABSTRACT, FIND_IMAGE_LINK).collect(Collectors.toCollection(ArrayList::new));

    public ProtoToRealQuesrion(LinkedData linkedData, InputCofiguration inputCofiguration) {
        this.maxNumberOfEntities = maxNumberOfEntities;
        this.linkedData = linkedData;
        this.inputCofiguration = inputCofiguration;
        this.online = this.inputCofiguration.getOnline();
        this.language = this.inputCofiguration.getLanguageCode();
        this.propertyDir = this.inputCofiguration.getEntityDir();
        this.endpoint = this.linkedData.getEndpoint();
        this.questionAnswerFile = this.inputCofiguration.getQuestionDir() + File.separator + questionsFile + "_" + language + ".csv";
        this.questionSummaryFile = this.inputCofiguration.getQuestionDir() + File.separator + summaryFile + "_" + language + ".csv";

    }

    public void onlineQaGeneration(List<File> protoSimpleQFiles) throws Exception {
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
            processEachGrammarUnit(grammarEntries, existingEntries, noIndex, idIndex);
        }

        this.csvWriterQuestions.close();
        this.writeSummary(this.summary);
        this.csvWriterSummary.close();

    }
    
     private Integer onlineQaGeneration(String uri, String sparqlQuery, List<UriLabel> uriLabels, List<String> questions, Integer rowIndex, String lexicalEntry, GrammarEntryUnit grammarEntryUnit, Map<String, OffLineResult> entityLabels) throws IOException {
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
            String answerWiki = null, answerThumb = null, answerAbstract = null;

            if (questionUri != null && !questionLabel.isEmpty()) {
                questionUri = questionUri.trim().strip().stripLeading().stripTrailing();
                questionLabel = uriLabel.getLabel().replaceAll("\'", "").replaceAll("\"", "");
                //bashScript=  new BashScript(menus, inputCofiguration.getWikiFile(), inputCofiguration.getAbstractFile(),inputCofiguration.getWikiFile(),  questionUri);

            } else if (questionLabel.isEmpty()) {
                //find labels for the specific database
                if (this.online) {
                    String classType = this.linkedData.getClassProperty();
                    questionLabel = getUriLabel(questionUri, classType);
                }
            }

            if (!AddQuote.isKbValid(uriLabel)) {
                continue;
            }
            String questionForShow = questions.iterator().next();

            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }
            String[] wikipediaAnswer = new String[3];
            String sparql = null, answerUri = null, answerLabel = null;
            wikipediaAnswer = this.getAnswerFromWikipedia(template, rdfTypeProperty, className, questionUri, sparqlQuery, null, returnSubjOrObj, endpoint, queryType);
            sparql = wikipediaAnswer[0];

            if (this.online) {
                answerUri = wikipediaAnswer[1];
                answerLabel = wikipediaAnswer[2];
            } else {
                answerUri = uriLabel.getAnswerUri();
                answerLabel = uriLabel.getAnswerLabel();
                /*answerWiki =bashScript.getWikiLink();
                answerThumb =bashScript.getImageLink();
                answerAbstract = bashScript.getAbstractText();*/
                //System.out.println(lexicalEntry + " questionForShow:" + questionForShow + " questionUri::" + questionUri + " questionLabel:" + questionLabel + " answerUri:" + answerUri + " answerLabel" + answerLabel);
            }

            index = index + 1;

            sparql = AddQuote.modifySparql(sparql);

            try {
                /*if (answer.isEmpty() || answer.contains("no answer found")) {
                    continue;
                } else*/ {
                    /*if (index >= this.maxNumberOfEntities) {
                        break;
                    }*/

                    if (this.online) {
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
                        String questionT = getQuestion(question, questionLabel);

                        if (questionLabel.isEmpty()) {
                            continue;
                        }

                        String[] record = {id, questionT, sparql, answerUri, answerLabel, syntacticFrame, "single"};
                        String[] newRecord = AddQuote.doubleQuote(record);
                        System.out.println("index::" + index + " questionT::" + questionT + " answerUri:" + answerUri + " answerLabel:" + answerLabel + " sparql:" + sparql);
                        //System.out.println("answerWiki::" + answerWiki + " answerThumb::" + answerThumb+" answerAbstract::"+answerAbstract);

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

                        //if(rowIndex>100)
                        //  return rowIndex;
                        //}
                    }
                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " " + sparql + " " + answerLabel);
            }
        }

        return rowIndex;
    }

    public void offlineQaGeneration(List<File> protoSimpleQFiles) throws Exception {
        Integer index = 0;
        String classDir = this.inputCofiguration.getClassDir();

        if (protoSimpleQFiles.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        this.csvWriterSummary = new CSVWriter(new FileWriter(questionSummaryFile, true));
        this.writeInCSV();

        Set<String> existingEntries = this.getExistingLexicalEntries(questionSummaryFile);
        Set<String> offlineMatchedProperties = new TreeSet<String>();

        if (this.inputCofiguration.getOfflineQuestion()) {
            ReferenceManagement referenceManagement = new ReferenceManagement(this.propertyDir, classDir, protoSimpleQFiles, GENERATED);
            offlineMatchedProperties = referenceManagement.getMatchedProperties();
        } else if (this.inputCofiguration.getEvalutionQuestion()) {
            String questionFile = this.inputCofiguration.getQuestionDir() + questionsCsv;
            this.csvWriterQuestions = new CSVWriter(new FileWriter(questionFile, true));
        }

        /*//Map<String, String> wikiLink=FileUtils.tripleFileToHash(inputCofiguration.getWikiFile(),-1);
        Map<String, String> abstracts=FileUtils.tripleFileToHash(inputCofiguration.getAbstractFile(),-1);
        Map<String, String> wikiLink=new TreeMap<String, String>();
        Map<String, String> abstracts=new TreeMap<String, String>();
        Map<String, String>thumb= new TreeMap<String, String>();*/
        if (offlineMatchedProperties.isEmpty() && this.inputCofiguration.getOfflineQuestion()) {
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
                questions = grammarEntryUnit.getSentences();
                String sparql = grammarEntryUnit.getSparqlQuery();
                String returnSubjOrObj = grammarEntryUnit.getReturnVariable();
                String bindingType = grammarEntryUnit.getBindingType();
                String returnType = grammarEntryUnit.getReturnType();
                List<UriLabel> bindingList = grammarEntryUnit.getBindingList();
                String property = AddQuote.getProperty(grammarEntryUnit.getSparqlQuery());

                if (grammarEntryUnit.getLexicalEntryUri() != null) {
                    uri = grammarEntryUnit.getLexicalEntryUri().toString();
                    if (existingEntries.isEmpty() && existingEntries.contains(uri)) {
                        continue;
                    }
                } else {
                    continue;
                }

                if (questions.contains("Where is $x located?")) {
                    continue;
                }

                if (!offlineMatchedProperties.contains(property) && this.inputCofiguration.getOfflineQuestion()) {
                    continue;
                }
                if (this.inputCofiguration.getOfflineQuestion()) {
                    try {
                        String fileId = grammarEntryUnit.getLexicalEntryUri().toString().replace("http://localhost:8080#", "") + "-" + "dbo_" + bindingType + "-" + property + "-" + "dbo_" + returnType + "-";
                        fileId = fileId + grammarEntryUnit.getFrameType() + "-" + grammarEntryUnit.getReturnVariable();
                        String questionAnswerFileTemp = this.questionAnswerFile.replace(".csv", "") + "-" + fileId + "-" + (fileIndex++).toString() + ".csv";
                        this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFileTemp, true));
                        String propertyFile = AddQuote.getProperty(this.propertyDir, grammarEntryUnit.getSparqlQuery());
                        this.entityLabels = FileProcessUtils.getEntityLabels(propertyFile, classDir, returnSubjOrObj, bindingType, returnType);
                        bindingList = this.getOffLineBindingList(entityLabels, returnSubjOrObj);
                    } catch (Exception ex) {
                        continue;
                    }
                } else if (this.inputCofiguration.getEvalutionQuestion()) {
                    String entityFileName = this.inputCofiguration.getEvalutionBindingFile();
                    File entityFile = new File(entityFileName);
                    List<UriLabel> qaldBindingList = this.getExtendedOffline(grammarEntryUnit.getBindingList(), entityFile, 0, 2, bindingType.toLowerCase());
                    bindingList.addAll(qaldBindingList);
                }

                if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {
                    continue;
                }

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
                    noIndex = this.onlineQaGeneration(uri, sparql, bindingList, questions, noIndex, "", grammarEntryUnit, entityLabels);
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
                if (this.inputCofiguration.getOfflineQuestion()) {
                    this.csvWriterQuestions.close();
                }

            }
            questions = new ArrayList<String>();
        }
        if (this.inputCofiguration.getEvalutionQuestion()) {
            this.csvWriterQuestions.close();
        }
        this.writeSummary(this.summary);
        this.csvWriterSummary.close();

    }

    private void processEachGrammarUnit(GrammarEntries grammarEntries, Set<String> existingEntries, Integer noIndex, Integer idIndex) throws IOException, Exception {
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
                noIndex = this.onlineQaGeneration(uri, sparql, bindingList, questions, noIndex, "", grammarEntryUnit, entityLabels);
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

   

    public String[] getAnswerFromWikipedia(String template, String rdfPropertyType, String className, String domainEntityUri,
            String sparql, String rangeEntityUri, String returnSubjOrObj, String endpoint,
            QueryType queryType) throws IOException {
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

    private List<UriLabel> getExtendedOnline(List<UriLabel> bindingList, File classFile, Integer keyindex, Integer classIntex, String bindingType) throws IOException {
        List<UriLabel> modifyLabels = new ArrayList<UriLabel>();
        Map<String, String> map = new TreeMap<String, String>();
        for (UriLabel uriLabel : bindingList) {
            if (AddQuote.isKbValid(uriLabel)) {
                map.put(uriLabel.getUri(), uriLabel.getLabel());
            }
        }
        Map<String, String[]> temp = FileProcessUtils.getDataFromFile(classFile, keyindex, classIntex, bindingType);
        for (String key : temp.keySet()) {
            String[] values = temp.get(key);
            String value = values[1];
            UriLabel uriLabel = new UriLabel(key, value);
            modifyLabels.add(uriLabel);
        }
        return modifyLabels;
    }

    private List<UriLabel> getExtendedOffline(List<UriLabel> bindingList, File classFile, Integer keyindex, Integer classIntex, String bindingType) throws IOException {
        List<UriLabel> modifyLabels = new ArrayList<UriLabel>();
        Map<String, String> map = new TreeMap<String, String>();
        for (UriLabel uriLabel : bindingList) {
            if (AddQuote.isKbValid(uriLabel)) {
                map.put(uriLabel.getUri(), uriLabel.getLabel());
            }
        }
        Map<String, String[]> row = FileProcessUtils.getDataFromFile(classFile, keyindex, classIntex, bindingType);
        for (String bindingUri : row.keySet()) {
            String[] values = row.get(bindingUri);
            String bindingValue = values[1];
            String className = values[2];
            String answerUri = values[3];
            String answerLabel = values[4];
            String returnSubjOrObj = values[5];
            //System.out.println("key:"+key+" value:"+value+" className:"+className+" answerUri:"+answerUri+" answerLable:"+answerLable+" returnSubjOrObj:"+returnSubjOrObj);
            UriLabel uriLabel = new UriLabel(bindingUri, bindingValue, answerUri, answerLabel, returnSubjOrObj);
            modifyLabels.add(uriLabel);
        }
        return modifyLabels;
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

    private List<UriLabel> getOffLineBindingList(Map<String, OffLineResult> entityLabels, String returnType) {
        List<UriLabel> uriLabels = new ArrayList<UriLabel>();

        for (String uri : entityLabels.keySet()) {
            OffLineResult offLineResult = entityLabels.get(uri);
            UriLabel uriLabel = null;
            if (returnType.contains(RETURN_TYPE_SUBJECT)) {
                uriLabel = new UriLabel(offLineResult.getObjectUri(), offLineResult.getObjectLabel(), offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getSubjectWikiLink(), offLineResult.getSubjectThumbnail(), offLineResult.getSubjectAbstractText());

            } else {
                uriLabel = new UriLabel(offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getObjectUri(), offLineResult.getObjectLabel(), offLineResult.getObjectWikiLink(), offLineResult.getObjectThumbnail(), offLineResult.getObjectAbstractText());
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
            UriLabel uriLabel = new UriLabel(offLineResult.getSubjectUri(), offLineResult.getSubjectLabel(), offLineResult.getObjectUri(), offLineResult.getObjectLabel(), null, null, null);
            urlLabels.add(uriLabel);
        }
        return urlLabels;
    }

    private List<UriLabel> getBindingList(GrammarEntryUnit grammarEntryUnit) {
        List<UriLabel> bindingList = grammarEntryUnit.getBindingList();

        if (grammarEntryUnit.getQueryType().equals(QueryType.ASK)) {
            return new ArrayList<UriLabel>();
        }

        //If it is an external binding list
        if (this.inputCofiguration.getExternalEntittyList()) {
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

    private String getQuestion(String question, String questionLabel) {
        String questionT = question.replaceAll("(X)", questionLabel);
        questionT = questionT.replace("(", "");
        questionT = questionT.replace(")", "");
        questionT = questionT.replace("$x", questionLabel);
        questionT = questionT.replace(",", "");
        questionT = questionT.stripLeading().trim();
        return questionT;
    }

}
