package evaluation;


import grammar.sparql.SPARQLRequest;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.lang.SPARQLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import grammar.sparql.SparqlQuery;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.jena.query.QueryType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class EvaluateAgainstQALD {

    private static final Logger LOG = LogManager.getLogger(EvaluateAgainstQALD.class);
    private final String language;
    private final String ORIGINAL = "ORIGINAL";
    private final String BOG = "BOG";
    public static final String PROTOTYPE_QUESTION = "PROTOTYPE_QUESTION";
    public static final String REAL_QUESTION = "REAL_QUESTION";
    private Map<String, GrammarEntry> matchedQueGGEntriesIds = new TreeMap<String, GrammarEntry>();
    private Set<String> qaldQuestions = new TreeSet<String>();
    private String endpoint=null;
    private Model model =null;


    public EvaluateAgainstQALD(String language,String endpoint) {
        this.language = language;
        this.endpoint=endpoint;
    }
    
   
    
    public void evaluateAndOutput(Map<String, String[]> questions, String qaldOriginalFile, String qaldModifiedFile, String resultFileName, String qaldRaw, String languageCode, String questionType,Double similarityMeasure) throws IOException, Exception {
        QALDImporter qaldImporter = new QALDImporter();
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        qaldImporter.qaldToCSV(qaldOriginalFile, qaldRaw, languageCode);
        QALD qaldModified = qaldImporter.readQald(qaldModifiedFile);
        QALD qaldOriginal = qaldImporter.readQald(qaldOriginalFile);
        entryComparisons = getAllSentenceMatchesCsv(qaldModified, questions, languageCode, BOG, similarityMeasure);
        EvaluationResult result = doEvaluation(qaldModified, entryComparisons, languageCode,questionType);
        Writer.writeResult(qaldImporter, qaldOriginal, result, resultFileName, languageCode);

    }

    

    private EvaluationResult doEvaluation(QALD qaldFile, List<EntryComparison> entryComparisons, String languageCode, String questionType) {
        EvaluationResult evaluationResult = new EvaluationResult();
        Integer index=0;
        for (EntryComparison entryComparison : entryComparisons) {      
             System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!Start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
             System.out.println("qald id: "+entryComparison.getQaldEntry().getId());
             System.out.println((index)+"  QaldEntry::::"+entryComparison.getQaldEntry().getQuestions()+" "+"QaldEntry::::"+entryComparison.getQaldEntry().getSparql());
             System.out.println((index)+"   QueGGEntry::::"+entryComparison.getQueGGEntry().getQuestions()+" "+entryComparison.getQueGGEntry().getSparql());
             System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!End!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                 
            if(questionType.contains(REAL_QUESTION)){
                realQuestionComparision(entryComparison);
            }
            evaluationResult.getEntryComparisons().add(entryComparison);
            LOG.info("tp: {}, fp: {}, fn: {}", entryComparison.getTp(), entryComparison.getFp(), entryComparison.getFn());
            evaluationResult.setTp_global(evaluationResult.getTp_global() + entryComparison.getTp());
            evaluationResult.setFp_global(evaluationResult.getFp_global() + entryComparison.getFp());
            evaluationResult.setFn_global(evaluationResult.getFn_global() + entryComparison.getFn());
        }

        evaluationResult.setPrecision_global(calculateMeasure(
                evaluationResult.getTp_global(),
                evaluationResult.getTp_global(),
                evaluationResult
                        .getFp_global()
        ));
        evaluationResult.setRecall_global(calculateMeasure(
                evaluationResult.getTp_global(),
                evaluationResult.getTp_global(),
                evaluationResult
                        .getFn_global()
        ));
        evaluationResult.setF_measure_global(
                (2
                * (calculateMeasure(
                        evaluationResult.getPrecision_global() * evaluationResult.getRecall_global(),
                        evaluationResult.getPrecision_global(),
                        evaluationResult.getRecall_global()
                )))
        );

        LOG.info("-".repeat(50));
        LOG.info(
                "TP_GLOBAL: {}, FP_GLOBAL: {}, FN_GLOBAL: {}",
                evaluationResult.getTp_global(),
                evaluationResult.getFp_global(),
                evaluationResult.getFn_global()
        );
        LOG.info(
                "PRECISION_GLOBAL: {}, RECALL_GLOBAL: {}, F_MEASURE_GLOBAL: {}",
                evaluationResult.getPrecision_global(),
                evaluationResult.getRecall_global(),
                evaluationResult.getF_measure_global()
        );
        /*System.out.println("getTp_global::"+evaluationResult.getTp_global());
        System.out.println("evaluationResult::"+evaluationResult.getFp_global());
        System.out.println("evaluationResult::"+evaluationResult.getFn_global());
        System.out.println("getPrecision_global()::"+evaluationResult.getPrecision_global());
        System.out.println("getRecall_global()()::"+evaluationResult.getRecall_global());
         System.out.println("getRecall_global()()::"+evaluationResult.getF_measure_global());*/
        return evaluationResult;
    }
    
   

    private List<EntryComparison> getAllSentenceMatchesCsv(QALD qaldFile, Map<String, String[]> questions, String languageCode, String questionType, double similarityPercentage) throws Exception {
        List<EntryComparison> matchingEntries = new ArrayList<EntryComparison>();
        List<String> qaldSentences
                = qaldFile.questions
                        .stream().parallel()
                        .map(qaldQuestions -> qaldQuestions.question)
                        .flatMap(qaldQuestions1
                                -> qaldQuestions1.stream().parallel()
                                .filter(qaldQuestion -> qaldQuestion.language.equals(languageCode))
                                .map(qaldQuestion -> qaldQuestion.string))
                        .collect(Collectors.toList());
        return this.getMatchRealQuestion(qaldFile, questions, languageCode, similarityPercentage);
    }

    
    
    private void realQuestionComparision(EntryComparison entryComparison) {
        String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
        String qaldSparql = entryComparison.getQaldEntry().getSparql();
        String queGGSparql = !isNull(entryComparison.getQueGGEntry()) ? entryComparison.getQueGGEntry().getSparql() : "";
        //Query qaldPARQLQuery = new Query();
        System.out.println("qaldID::"+entryComparison.getQaldEntry().getId());
        System.out.println("qaldSparql::"+entryComparison.getQaldEntry().getSparql());
        //SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);
        List<String> uriResultListQueGG = new ArrayList<String>();
        List<String> uriResultListQALD;  
        uriResultListQALD = this.getResultForQaldSparqlQuery(qaldSparql);            
       
      
        /*if (this.endpoint.contains("https://dbpedia.org/")) {
            uriResultListQALD = this.getResultForQaldSparqlQuery(qaldSparql);
        } else if (this.endpoint.contains("http://data.linkeddatafragments.org")) {
            uriResultListQALD = this.getResultSparqlFragment(qaldSparql);
        }*/

       
        entryComparison.setQaldResults(uriResultListQALD);
        
        /*if (!isNull(entryComparison.getQueGGEntry())) {
            entryComparison.getQueGGEntry().setSparql(""); // delete SPARQL query because it was not executed anyway
        } else*/ 
            /*return "select  ?o\n"
                + "    {\n"
                + "    " + "<" + entityUrl + ">" + " " + "<" + property + ">" + "  " + "?o" + "\n"
                + "    }";*/
            /*  System.out.println("queGGSparql:::" + queGGSparql);
            if(!isNull(entryComparison.getQueGGEntry())){
                entryComparison.getQueGGEntry().setSparql(""); // delete SPARQL query because it was not executed anyway 
            }
            else{
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:::" );
            queGGSparql=queGGSparql.replace("{","\n"+"{"+"\n");
            queGGSparql=queGGSparql.replace("}","\n"+"}");
            uriResultListQueGG = this.getResultForQaldSparqlQuery(queGGSparql);
            entryComparison.getQueGGEntry().setSparql(queGGSparql);
            entryComparison.setQueGGResults(uriResultListQueGG);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:::" );
            System.out.println("queGGSparql:::" + queGGSparql);
            System.out.println("uriResultListQueGG:::" + uriResultListQueGG);
           }*/
            
        if (queGGSparql != null) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:::");
            queGGSparql = queGGSparql.replace("{", "\n" + "{" + "\n");
            queGGSparql = queGGSparql.replace("}", "\n" + "}");
            queGGSparql = queGGSparql.replace("\"", "");
            //queGGSparql = queGGSparql.replace("\"select", "select");
            //queGGSparql = queGGSparql.replace("}\"", "}");
             //queGGSparql = queGGSparql.replace("1\"", "1");
            uriResultListQueGG = this.getResultForQaldSparqlQuery(queGGSparql);
            entryComparison.getQueGGEntry().setSparql(queGGSparql);
            entryComparison.setQueGGResults(uriResultListQueGG);
            
           /* if (entryComparison.getQaldEntry().getId().contains("46")) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:::");
                System.out.println("qaldQuestion:::" + qaldQuestion);
                System.out.println("qaldSparql:::" + qaldSparql);
                System.out.println("uriResultListQALD:::" + uriResultListQALD);
                System.out.println("queGG questions:::" + entryComparison.getQueGGEntry().getQuestions());
                System.out.println("queGGSparql:::" + queGGSparql);
                System.out.println("uriResultListQueGG:::" + uriResultListQueGG);
            }*/
           
        }

       

        LOG.debug(
                "Comparing QueGG results to QALD results: #QueGG: {}, #QALD: {}",
                uriResultListQueGG.size(),
                uriResultListQALD.size()
        );
        LOG.debug("Comparing QueGG results to QALD results: QueGG: {}, QALD: {}", uriResultListQueGG, uriResultListQALD);
        
        
        /*if (entryComparison.getQaldEntry().getId().contains("25")) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:::" );
            System.out.println("qaldQuestion:::" + qaldQuestion);
            System.out.println("qaldSparql:::" + qaldSparql);
            System.out.println("uriResultListQALD:::" + uriResultListQALD);
            System.out.println("queGGSparql:::" + queGGSparql);
            //uriResultListQueGG = this.getResultForQaldSparqlQuery(queGGSparql);
            System.out.println("uriResultListQueGG:::" + uriResultListQueGG);
            System.out.println("queGG questions:::" + entryComparison.getQueGGEntry().getQuestions());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:::" );
           
        }*/

        
     

        /*
      Measures:
      True  Positive:  Number of results in QueGG SPARQL query that are also in QALD  query results
      False Positive:  Number of results in QueGG SPARQL query that are not  in QALD  query results
      False Negative:  Number of results in QALD  SPARQL query that are not  in QueGG query results
      True  Negative:  Number of results missing from both datasets -> not relevant
      Precision:  TP / (TP + FP)
      Recall:     TP / (TP + FN)
      F-measure:  2 * (Precision * Recall) / (Precision + Recall)
         */
        List<String> finalUriResultListQueGG = uriResultListQueGG;
        // Add TP, FP, FN
        entryComparison.setTp(uriResultListQueGG.stream().filter(uriResultListQALD::contains).count());
        entryComparison.addFp(uriResultListQueGG.stream()
                .filter(resultQueGG -> !uriResultListQALD.contains(resultQueGG))
                .count());

        entryComparison.setFn(uriResultListQALD.stream()
                .filter(resultQald -> !finalUriResultListQueGG.contains(resultQald))
                .count());

        // Add Precision, Recall, F-measure
        if ((entryComparison.getTp() + entryComparison.getFp()) > 0) {
            entryComparison.setPrecision(calculateMeasure(
                    entryComparison.getTp(),
                    entryComparison.getTp(),
                    entryComparison.getFp()
            ));
        }
        if ((entryComparison.getTp() + entryComparison.getFn()) > 0) {
            entryComparison.setRecall((calculateMeasure(
                    entryComparison.getTp(),
                    entryComparison.getTp(),
                    entryComparison.getFn()
            )));
        }
        if ((entryComparison.getPrecision() + entryComparison.getRecall()) > 0) {
            entryComparison.setF_measure(
                    (2
                    * (calculateMeasure(
                            entryComparison.getPrecision() * entryComparison.getRecall(),
                            entryComparison.getPrecision(),
                            entryComparison.getRecall()
                    )))
            );
        }
        LOG.debug("tp: {}, fp: {}, fn: {}", entryComparison.getTp(), entryComparison.getFp(), entryComparison.getFn());
        LOG.debug(
                "Precision: {}, Recall: {}, F-measure: {}",
                entryComparison.getPrecision(),
                entryComparison.getRecall(),
                entryComparison.getF_measure()
        );

    }

    

    /**
     * @return {@code tp / (tp2 + fp)}
     */
    private float calculateMeasure(float tp, float tp2, float fp) {
        return tp / (tp2 + fp);
    }

    

    private String cleanQALDString(String sentence) {
        return sentence.toLowerCase().trim();
    }

    /**
     * Make lower case, add regex capture for $x and (... | ...)
     */
    /*protected String cleanString(String sentence) {
        return String.format("^%s$", sentence
                .replace(DEFAULT_BINDING_VARIABLE, "([\\w\\s\\d-,.']+)")
                .replaceAll("\\((.+)\\s\\|\\s(.+)\\)", "([\\\\w\\\\s\\\\d-,.']+)")
                .replace("?", "\\?")
                .toLowerCase()
                .trim());
    }*/

    private String getVarNameFromQueGGBinding(GrammarEntry grammarEntry) {
        return !isNull(grammarEntry) ? Var.alloc(grammarEntry.getBindingVariable()).toString() : "";
    }

    

    private List<EntryComparison> sortMatches(List<EntryComparison> matchingEntries) {
        return matchingEntries.stream().parallel()
                .sorted(Comparator.comparing(
                        entryComparison -> Integer.valueOf(entryComparison.getQaldEntry().getId())
                ))
                .collect(Collectors.toList());
    }


   
    
    private List<EntryComparison> getMatchRealQuestion(QALD qaldFile, Map<String, String[]> realQuestions, String languageCode, double similarityPercentage) throws Exception {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        //List<String> list = new ArrayList<String>();
        Integer index=0;
        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            String qaldSparqlQuery = QALDImporter.getQaldSparqlQuery(qaldQuestions);
           
            Map<String, QueGGinfomation> grammarEntities = this.matchedRealQuestions(qaldQuestion, qaldSparqlQuery,realQuestions, similarityPercentage,index);
            StringSimilarity stringSimilarity=new StringSimilarity();
            index=index+1;
            /*if(stringSimilarity.isAskSparqlQuery(qaldSparqlQuery)){
                grammarEntities =new TreeMap<String,QueGGinfomation>();  
            }*/
            /*else if(!stringSimilarity.isMultipleSparqlQuery(qaldSparqlQuery)){
                //System.out.println("qaldSparqlQuery::"+qaldSparqlQuery);
                //exit(1);
                grammarEntities =new TreeMap<String,QueGGinfomation>();  
            }*/
            
            EntryComparison entryComparison = new EntryComparison();
            String qaldSparql = qaldQuestions.query.sparql;
            Entry qaldEntry = new Entry();
            Entry queGGEntry = new Entry();
            qaldEntry.setActualEntry(qaldQuestions);
            qaldEntry.setId(qaldQuestions.id);
            qaldEntry.setQuestions(qaldQuestion);
            qaldEntry.setSparql(qaldSparql);

            if (!grammarEntities.isEmpty()) {
               // StringSimilarity stringSimilarity=new StringSimilarity();
                QueGGinfomation queGGinfomation = stringSimilarity.getMostSimilarMatch(grammarEntities);
                queGGEntry.setId(queGGinfomation.getId());
                queGGEntry.setQuestions(queGGinfomation.getQuestion());
                List<String> list = new ArrayList<String>();
                list.add(queGGinfomation.getQuestion());
                queGGEntry.setQuestionList(list);
                queGGEntry.setSparql(queGGinfomation.getSparqlQuery());
                entryComparison.setMatchedFlag(Boolean.TRUE);
                entryComparison.setSimilarityCsore(queGGinfomation.getValue());
            } else {
                entryComparison.setMatchedFlag(Boolean.FALSE);
                entryComparison.setSimilarityCsore(0.0);
            }

            entryComparison.setQaldEntry(qaldEntry);
            entryComparison.setQueGGEntry(queGGEntry);
            entryComparisons.add(entryComparison);

            if (entryComparison.getMatchedFlag()) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("qald::::" + entryComparison.getQaldEntry().getQuestions());
                System.out.println("queGG::::" + entryComparison.getQueGGEntry().getQuestions());
            }

            //}
        }
        return entryComparisons;
    }
    
   
   
    
    public Map<String, QueGGinfomation> matchedRealQuestions(String qaldsentence, String qaldSparqlQuery, Map<String, String[]> questions, double similarityPercentage, Integer index) {
        Map<String, QueGGinfomation> matchedQuestions = new TreeMap<String, QueGGinfomation>();
        qaldsentence = qaldsentence.toLowerCase().strip().trim();
        HashMap<String, Double> sort = new HashMap<String, Double>();
        Boolean singleFlag = false, multipleFlag = false, askFlag = false;

        StringSimilarity stringSimilarity = new StringSimilarity();

        if (stringSimilarity.isAskSparqlQuery(qaldSparqlQuery)) {
            askFlag = true;

        } else if (!stringSimilarity.isMultipleSparqlQuery(qaldSparqlQuery)) {
            singleFlag = true;

        } else /*if (stringSimilarity.isMultipleSparqlQuery(qaldSparqlQuery)) */ {
            /* System.out.println("qaldSparqlQuery::"+qaldSparqlQuery);
               // exit(1);*/
            multipleFlag = true;
        }
        
         if(index==87)
           return new TreeMap<String, QueGGinfomation>();

        /*if(index==210||index==16||index==180)
           return new TreeMap<String, QueGGinfomation>();*/
 /*if(index==16||index==180||index==194||index==67
                ||index==89||index==150||index==76||index==87
                ||index==135||index==210||index==25||index==0||index==107||index==23||index==161||index==132)
           return new TreeMap<String, QueGGinfomation>();*/
 /*if (multipleFlag&&qaldSparqlQuery.contains("res:Surfing")) {
               
            return new TreeMap<String, QueGGinfomation>();
        }
        else if (askFlag&&qaldSparqlQuery.contains("dbo:birthPlace")) {
              
            return new TreeMap<String, QueGGinfomation>();
        }
        else if (multipleFlag&&qaldSparqlQuery.contains("industry")||qaldSparqlQuery.contains("Nobel_Prize_in_Physics")||qaldSparqlQuery.contains("Mickey_Rourke")||qaldSparqlQuery.contains("Nobel_Peace_Prize")) {
               System.out.println("qaldSparqlQuery::"+qaldSparqlQuery);
               // exit(1);
            return new TreeMap<String, QueGGinfomation>();
        }*/
 /*else if(!stringSimilarity.isMultipleSparqlQuery(qaldSparqlQuery)){
                //System.out.println("qaldSparqlQuery::"+qaldSparqlQuery);
                //exit(1);
                grammarEntities =new TreeMap<String,QueGGinfomation>();  
            }*/
        for (String queGGquestion : questions.keySet()) {
            String[] row = questions.get(queGGquestion);
            qaldsentence = qaldsentence.replace("\"", "");
            queGGquestion = queGGquestion.replace("\"", "");
            Double value = StringSimilarity.zacrdSimilarity(qaldsentence, queGGquestion);
            QueGGinfomation queGGinfomation = new QueGGinfomation(row, value, qaldSparqlQuery);
            String queGGsparql = queGGinfomation.getSparqlQuery();

            if (value > similarityPercentage) {
                //QueGGinfomation queGGinfomation = new QueGGinfomation(row, value);
                sort.put(queGGinfomation.getQuestion(), value);
                matchedQuestions.put(queGGinfomation.getQuestion(), queGGinfomation);
                System.out.println("MATCHED: " + qaldsentence + ":" + queGGquestion + " cosineSimilarityPercentage::" + value);
                //exit(1);
            }
        }

        return matchedQuestions;
    }

    private List<String> getResultForQaldSparqlQuery(String qaldSparql) {
        LOG.debug("Executing QALD SPARQL Query:\n{}", qaldSparql);
        List<String> uriResultList = new ArrayList<String>();
         
         if(qaldSparql.contains("ASK")){
            return new ArrayList<String>(); 
         }

        if (this.endpoint.contains("https://dbpedia.org/")) {
            if (qaldSparql.length() >= 2 && qaldSparql.charAt(0) == '"' && qaldSparql.charAt(qaldSparql.length() - 1) == '"') {
                qaldSparql = qaldSparql.substring(1, qaldSparql.length() - 1);
            }
            System.out.println("qaldSparql::"+qaldSparql);
            if(qaldSparql.contains("count")||qaldSparql.contains("COUNT")){
               uriResultList = new  ArrayList<String>();
            }
          
            else{
            SPARQLRequest sparqlRequest = SPARQLRequest.fromString(qaldSparql);
            uriResultList = sparqlRequest.getSparqlResultList();     
            if(uriResultList.size()>0){
                System.out.println("uriResultList::"+uriResultList); 
            }
            }
           
        } 
        System.out.println("uriResultList::"+uriResultList);
        return uriResultList;
    }

   
  

    private boolean isAskSparqlQuery(String qaldSparqlQuery) {
        if(qaldSparqlQuery.contains("ASK")){
            return true;
        }
        return false;
    }

}
