package grammar.read.questions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import grammar.sparql.SparqlQuery;
import util.io.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andrewoma.dexx.collection.Pair;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import util.io.Statistics;

/**
 *
 * @author elahi
 */
public class ReadAndWriteQuestions {

    public String[] questionHeader = new String[]{ID, QUESTION, SPARQL, ANSWER_URI, ANSWER, FRAME};
    public String[] summaryHeader = new String[]{LEXICAL_ENTRY, NUMBER_OF_GRAMMAR_RULES, NUMBER_OF_QUESTIONS, FRAMETYPE, Status, Reason};
    public static String FRAMETYPE_NPP = "NPP";
    public static final String ID = "id";
    public static final String QUESTION = "question";
    public static final String SPARQL = "sparql";
    public static final String ANSWER_URI = "answer";
    public static final String ANSWER = "answer";
    public static final String FRAME = "frame";
    private static final String LEXICAL_ENTRY = "lexicalEntry";
    private static final String SENTENCETYPE = "sentenceType";
    private static final String FRAMETYPE = "frameType";
    private static final String NUMBER_OF_GRAMMAR_RULES = "numberOfGrammarRules";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String Status = "numberOfQuestions";
    private static final String Reason = "numberOfQuestions";
    private static String language = "en";

    public CSVWriter csvWriterQuestions;
    public CSVWriter csvWriterSummary;
    public String questionAnswerFile = null;
    public String questionSummaryFile = null;
    private Set<String> excludes = new HashSet<String>();
    private Map<String, Statistics> summary = new TreeMap<String, Statistics>();

    private Integer maxNumberOfEntities = 100;
    private String endpoint = null;
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ReadAndWriteQuestions.class);
    private Boolean online=false; 

    public ReadAndWriteQuestions(String questionAnswerFile, String questionSummaryFile, Integer maxNumberOfEntities, String language, String endpoint, Boolean online) {
        this.initialExcluded();
        this.endpoint = endpoint;
        this.language = language;
        this.questionAnswerFile = questionAnswerFile;
        this.questionSummaryFile = questionSummaryFile;
        this.maxNumberOfEntities = maxNumberOfEntities;
        this.online=online;
    }

    public void readQuestionAnswers(List<File> fileList, String entityDir, Boolean externalEntittyListflag) throws Exception {
        String sparql = null;
        Integer index = 0;

        this.csvWriterQuestions = new CSVWriter(new FileWriter(questionAnswerFile));
        this.csvWriterSummary = new CSVWriter(new FileWriter(questionSummaryFile));
        //this.csvWriter = new CSVWriter(new FileWriter(questionAnswerFile, true));
        this.csvWriterQuestions.writeNext(questionHeader);
        this.csvWriterSummary.writeNext(summaryHeader);

        LOG.info("Number of Files!!'", fileList.size());

        for (File file : fileList) {
            index = index + 1;
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries = mapper.readValue(file, GrammarEntries.class);
            Integer total = grammarEntries.getGrammarEntries().size();
            Integer idIndex = 0, noIndex = 0;
            LOG.info("running file'", file.getName());
            for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
                
                /*if (idIndex > 1) {
                    break;
                }*/
                /*if (grammarEntryUnit.getSentences().iterator().next().contains("Where is $x located?"))
                    continue;*/

                sparql = grammarEntryUnit.getSparqlQuery();
                String returnVairable = grammarEntryUnit.getReturnVariable();
                String retunrStr = grammarEntryUnit.getBindingType();
                String syntacticFrame = grammarEntryUnit.getFrameType();
                List<UriLabel> bindingList = new ArrayList<UriLabel>();
                
                if(grammarEntryUnit.getBindingType().contains("LOCATION")&&grammarEntryUnit.getReturnType().contains("LOCATION"))
                    continue;

                if (externalEntittyListflag) {
                    
                    String entityFileName = entityDir + "ENTITY_LABEL_LIST" + "_" + retunrStr.toLowerCase()+ ".txt";
                    //entityFileName="/home/elahi/AHack/italian/question-grammar-generator/src/main/resources/entityLabels/ENTITY_LABEL_LIST_Person.txt";
                    //System.out.println("entityFileName::"+entityFileName);
                    File entityFile = new File(entityFileName);
                    bindingList = this.getExtendedBindingList(grammarEntryUnit.getBindingList(), entityFile);
                    System.out.println(bindingList.size());
                } else {
                    bindingList = grammarEntryUnit.getBindingList();
                }
               
                noIndex = this.replaceVariables(bindingList, sparql, returnVairable, grammarEntryUnit.getSentences(), syntacticFrame, noIndex, "");
                noIndex = noIndex + 1;
                //LOG.info("index:" + index + " Id:" + grammarEntryUnit.getId() + " total:" + total + " example:" + grammarEntryUnit.getSentences().iterator().next());
                //System.out.println("index:" + index + " Id:" + grammarEntryUnit.getId() + " total:" + total + " example:" + grammarEntryUnit.getSentences().iterator().next());
                idIndex = idIndex + 1;

                if (grammarEntryUnit.getLexicalEntryUri() != null) {
                    String uri = grammarEntryUnit.getLexicalEntryUri().toString();

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
        this.writeSummary(this.summary);
        //System.out.println("this.summary::" + this.summary);
        this.csvWriterQuestions.close();
        this.csvWriterSummary.close();

    }

    private Integer replaceVariables(List<UriLabel> uriLabels, String sparqlOrg, String frameType, List<String> questions, String syntacticFrame, Integer rowIndex, String lexicalEntry) {
        Integer index = 0;
        List< String[]> rows = new ArrayList<String[]>();
        for (UriLabel uriLabel : uriLabels) {
            //System.out.println("Uri:"+uriLabel.getUri());
            //System.out.println("Label:"+uriLabel.getLabel());
            if (!isKbValid(uriLabel)) {
                continue;
            }
            //System.out.println("index: " + index + " size:" + uriLabels.size() + " uriLabel:::" + uriLabel.getUri() + " labe::" + uriLabel.getLabel());
            String questionForShow = questions.iterator().next();
            
            if (questionForShow.contains("Where is $x located?")) {
                continue;
            }

            String[] wikipediaAnswer = this.getAnswerFromWikipedia(uriLabel.getUri(), sparqlOrg, frameType, endpoint,online);
            String sparql = wikipediaAnswer[0];
            String answerUri = wikipediaAnswer[1];
            String answer = wikipediaAnswer[2];
            index = index + 1;
            sparql = this.modifySparql(sparql);

            System.out.println("index::" + index + " uriLabel::" + uriLabel.getLabel() + " questionForShow::" + questionForShow + " sparql::" + sparql + " answer::" + answer + " syntacticFrame:" + syntacticFrame);
            try {
                /*if (answer.isEmpty() || answer.contains("no answer found")) {
                    continue;
                } else*/ {
                    /*if (index >= this.maxNumberOfEntities) {
                        break;
                    }*/
                    
                if (answerUri.isEmpty() || answerUri.length()<2){//answer.contains("no answer found")) {
                    continue;
                }
                    
                    for (String question : questions) {
                        //System.out.println(question);
                        if (question.contains("(") && question.contains(")")) {
                            String result = StringUtils.substringBetween(question, "(", ")");
                            question = question.replace(result, "X");
                        } else if (question.contains("$x")) {
                            //System.out.println(question);

                        }

                        String id = rowIndex.toString();
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
                        if(!online){
                            answerUri="offline";
                            answer="offline";
                        }
                            

                        String[] record = {id, questionT, sparql, answerUri, answer, syntacticFrame};
                        this.csvWriterQuestions.writeNext(record);
                        rowIndex = rowIndex + 1;
                    }
                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " " + sparql + " " + answer);
            }
        }

        return rowIndex;
    }

    public String[] getAnswerFromWikipedia(String subjProp, String sparql, String returnType, String endpoint,Boolean online) {
        String answerUri = null;
        String answerLabel = "no answer found";
        SparqlQuery sparqlQuery = null;
        String property = StringUtils.substringBetween(sparql, "<", ">");
        sparqlQuery = new SparqlQuery(subjProp, property, SparqlQuery.FIND_ANY_ANSWER, returnType, language, endpoint,online);
        //System.out.println("original sparql:: "+sparql);
        //System.out.println("sparqlQuery:: "+sparqlQuery.getSparqlQuery());
        answerUri = sparqlQuery.getObject();
        if (answerUri != null) {
            if (answerUri.contains("http:")) {
                //System.out.println(answer);
                SparqlQuery sparqlQueryLabel = new SparqlQuery(answerUri, property, SparqlQuery.FIND_LABEL, null, language, endpoint,online);
                answerLabel = sparqlQueryLabel.getObject();
                //System.out.println(answer);

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

    private List<UriLabel> getExtendedBindingList(List<UriLabel> bindingList, File classFile) {
        List<UriLabel> modifyLabels = new ArrayList<UriLabel>();
        for (UriLabel uriLabel : bindingList) {
            if (isKbValid(uriLabel)) {
                modifyLabels.add(uriLabel);
            }
        }
        modifyLabels.addAll(FileUtils.getUriLabels(classFile));
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

}
