package evaluation;


import com.google.gdata.util.common.base.Pair;
import static grammar.generator.BindingConstants.DEFAULT_BINDING_VARIABLE;
import grammar.sparql.SPARQLRequest;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.lang.SPARQLParser;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.matcher.PatternMatchHelper;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import grammar.sparql.SparqlQuery;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import static java.lang.System.exit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.jena.query.QueryType;
import static org.apache.jena.sparql.syntax.ElementWalker.walk;

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

    public EvaluateAgainstQALD(String language,String endpoint) {
        this.language = language;
        this.endpoint=endpoint;
    }
    
    public String getQaldEntities( String qaldOriginalFile, String qaldModifiedFile, String qaldRaw, String languageCode) throws IOException {
        QALDImporter qaldImporter = new QALDImporter();
        qaldImporter.qaldToCSV(qaldOriginalFile, qaldRaw, languageCode);
        QALD qaldModified = qaldImporter.readQald(qaldModifiedFile);
        QALD qaldOriginal = qaldImporter.readQald(qaldOriginalFile);
        return findAllQaldEntities(qaldModified,languageCode);

    }

    public void evaluateAndOutput(GrammarWrapper grammarWrapper, String qaldOriginalFile, String qaldModifiedFile, String resultFileName, String qaldRaw, String languageCode, String questionType,Double similarityMeasure) throws IOException {
        QALDImporter qaldImporter = new QALDImporter();
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        qaldImporter.qaldToCSV(qaldOriginalFile, qaldRaw, languageCode);
        QALD qaldModified = qaldImporter.readQald(qaldModifiedFile);
        QALD qaldOriginal = qaldImporter.readQald(qaldOriginalFile);
        entryComparisons = getProtoTypeMatchedQuestions(qaldModified, grammarWrapper, languageCode, BOG, similarityMeasure);
        EvaluationResult result = doEvaluation(qaldModified, entryComparisons, languageCode,questionType);
        Writer.writeResult(qaldImporter, qaldOriginal, result, resultFileName, languageCode);

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
            /*if(entryComparison.getMatchedFlag()){
                //System.out.println();
                                System.out.println();

                 System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!Start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                  System.out.println("qald id: "+entryComparison.getQaldEntry().getId());
                 System.out.println((index)+"  QaldEntry::::"+entryComparison.getQaldEntry().getQuestions()+" "+"QaldEntry::::"+entryComparison.getQaldEntry().getSparql());
                 System.out.println((index)+"   QueGGEntry::::"+entryComparison.getQueGGEntry().getQuestions()+" "+entryComparison.getQueGGEntry().getSparql());
                 System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!End!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                 index=index+1;
            }*/
            if (questionType.contains(PROTOTYPE_QUESTION)) {
                protoTypeQuestionComperison(entryComparison);
            }
            else if(questionType.contains(REAL_QUESTION)){
                real(entryComparison);
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
    
    private List<EntryComparison> getProtoTypeMatchedQuestions(QALD qaldFile, GrammarWrapper grammarWrapper, String languageCode, String matchType, double similarityPercentage) {
        List<EntryComparison> matchingEntries = new ArrayList<EntryComparison>();
        List<String[]> realQuestions = new ArrayList<String[]>();
        List<String> qaldSentences
                = qaldFile.questions
                        .stream().parallel()
                        .map(qaldQuestions -> qaldQuestions.question)
                        .flatMap(qaldQuestions1
                                -> qaldQuestions1.stream().parallel()
                                .filter(qaldQuestion -> qaldQuestion.language.equals(languageCode))
                                .map(qaldQuestion -> qaldQuestion.string))
                        .collect(Collectors.toList());
        return this.getMatchProtoQuestion(qaldFile, grammarWrapper, languageCode, matchType, similarityPercentage);
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

    private void protoTypeQuestionComperison(EntryComparison entryComparison) {
        GrammarEntry grammarEntry = !isNull(entryComparison.getQueGGEntry()) ? (GrammarEntry) entryComparison.getQueGGEntry().getActualEntry() : null;
        String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
        String cleanQaldQuestion = cleanQALDString(qaldQuestion); //  make lower case
        String qaldSparql = entryComparison.getQaldEntry().getSparql();
        String queGGSparql = !isNull(entryComparison.getQueGGEntry()) ? entryComparison.getQueGGEntry().getSparql() : "";
        Boolean tpFlag = false;
        Query qaldPARQLQuery = new Query();
        SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);
        String matchedSentenceItem = "";
        String sentencePattern = null;
        String uriMatch = "";
        String bindingVarName = "";
        List<String> uriResultListQueGG = new ArrayList<String>();
        List<String> uriResultListQALD;
        String matchedPattern = "";

        sentencePattern = Matcher.getMatchPattern(entryComparison, cleanQaldQuestion);
        uriMatch = Matcher.getMatchedUri(sentencePattern,matchedSentenceItem,qaldPARQLQuery);
        uriResultListQALD = this.getResultForQaldSparqlQuery(qaldSparql);
        entryComparison.setQaldResults(uriResultListQALD);
        
      
        // get variable name (i.e. objOfProp, subjOfProp) from QueGG binding sparql to replace it with uriMatch
        bindingVarName = getVarNameFromQueGGBinding(grammarEntry);
         
        if (bindingVarName.isEmpty() || uriMatch.isEmpty()) {
            // Variable name to substitute in the bindings SPARQL or a valid uri substitute could not be found
            LOG.info("No match for {}", qaldQuestion);
            if (!isNull(entryComparison.getQueGGEntry())) {
                entryComparison.getQueGGEntry().setSparql(""); // delete SPARQL query because it was not executed anyway
            }
        } else {
            String queGGSparqlOrg=queGGSparql;
            queGGSparql=this.getQuGGResult(queGGSparql,uriMatch,bindingVarName,grammarEntry.getQueryType());
            uriResultListQueGG= this.getResultForQaldSparqlQuery(queGGSparql);
            entryComparison.getQueGGEntry().setSparql(queGGSparql);
            entryComparison.setQueGGResults(uriResultListQueGG);    
        
            if (entryComparison.getMatchedFlag()) {
                System.out.println("sentencePattern:::" + sentencePattern);
                System.out.println("uriMatch:::" + uriMatch);
                System.out.println("qaldSparql:::" + qaldSparql);
                System.out.println("uriResultListQALD:::" + uriResultListQALD);
                System.out.println("uriResultListQueGG:::" + uriResultListQueGG);
                System.out.println("queGG questions:::" + entryComparison.getQueGGEntry().getQuestionList());
                System.out.println("queGGSparqlOrg:::" + queGGSparqlOrg);
                System.out.println("queGGSparql:::" + queGGSparql);
                System.out.println("bindingVarName:::" + bindingVarName);
             
               
            }


         
            /*LOG.info(sentencePattern);
            LOG.info(uriMatch);
            LOG.info(qaldSparql);
            LOG.info(uriResultListQALD);
            LOG.info(uriResultListQueGG);*/
           
        }

        LOG.debug(
                "Comparing QueGG results to QALD results: #QueGG: {}, #QALD: {}",
                uriResultListQueGG.size(),
                uriResultListQALD.size()
        );
        LOG.debug("Comparing QueGG results to QALD results: QueGG: {}, QALD: {}", uriResultListQueGG, uriResultListQALD);
      
        /*if (sentencePattern != null) {
            System.out.println("sentencePattern............................................." + sentencePattern);
            System.out.println("uriMatch............................................." + uriMatch);
            System.out.println("qaldSparql............................................." + qaldSparql);
            System.out.println("uriResultListQALD............................................." + uriResultListQALD);
            System.out.println("binding............................................." + bindingVarName);
            System.out.println("queGGSparql............................................." + queGGSparql);
            System.out.println("uriResultListQueGG............................................." + uriResultListQueGG);
           
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
         /*if (entryComparison.getTp()==1) {
          System.out.println(":::::::::::::entryComparison.getTp():::::::::::::::::::::::::::" );
          System.out.println("Qald Question::" + qaldQuestion);
          System.out.println("Qald Sparql Query::" + qaldSparql);
          System.out.println("QALD Answer::" + uriResultListQALD);
          System.out.println("QueGG Sparql Query::" + queGGSparql);
          System.out.println("QueGG Answer::" + uriResultListQueGG);
          System.out.println("finalUriResultListQueGG::::"+finalUriResultListQueGG );
          System.out.println("getTp::" + entryComparison.getTp());
          System.out.println("getFp::" + entryComparison.getFp());
          System.out.println("getFn::" + entryComparison.getFn());
          System.out.println("getPrecision::" + entryComparison.getPrecision());
          System.out.println("getRecall::" + entryComparison.getRecall());
          System.out.println("getF_measure::" + entryComparison.getF_measure());
          System.out.println("::::::::::::::::::::::::::::::::::::::::" );
      }
        /*if (entryComparison.getFp()==1) {
           System.out.println(":::::::::::::::::::::(entryComparison.getFp():::::::::::::::::::" );
          System.out.println("Qald Question::" + qaldQuestion);
          System.out.println("Qald Sparql Query::" + qaldSparql);
          System.out.println("QALD Answer::" + uriResultListQALD);
          System.out.println("QueGG Sparql Query::" + queGGSparql);
          System.out.println("QueGG Answer::" + uriResultListQueGG);
          System.out.println("finalUriResultListQueGG::::"+finalUriResultListQueGG );
          System.out.println("getTp::" + entryComparison.getTp());
          System.out.println("getFp::" + entryComparison.getFp());
          System.out.println("getFn::" + entryComparison.getFn());
          System.out.println("getPrecision::" + entryComparison.getPrecision());
          System.out.println("getRecall::" + entryComparison.getRecall());
          System.out.println("getF_measure::" + entryComparison.getF_measure());
          System.out.println("::::::::::::::::::::::::::::::::::::::::" );
                   

            
      } if (entryComparison.getFn()==1) {
           System.out.println("::::::::::::::::entryComparison.getFn()::::::::::::::::::::::::" );
          System.out.println("Qald Question::" + qaldQuestion);
          System.out.println("Qald Sparql Query::" + qaldSparql);
          System.out.println("QALD Answer::" + uriResultListQALD);
          System.out.println("QueGG Sparql Query::" + queGGSparql);
          System.out.println("QueGG Answer::" + uriResultListQueGG);
          System.out.println("finalUriResultListQueGG::::"+finalUriResultListQueGG );
          System.out.println("getTp::" + entryComparison.getTp());
          System.out.println("getFp::" + entryComparison.getFp());
          System.out.println("getFn::" + entryComparison.getFn());
          System.out.println("getPrecision::" + entryComparison.getPrecision());
          System.out.println("getRecall::" + entryComparison.getRecall());
          System.out.println("getF_measure::" + entryComparison.getF_measure());
          System.out.println("::::::::::::::::::::::::::::::::::::::::" );
          
      }*/
    }
    
    private void real(EntryComparison entryComparison) {
        String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
        String cleanQaldQuestion = cleanQALDString(qaldQuestion); //  make lower case
        String qaldSparql = entryComparison.getQaldEntry().getSparql();
        String queGGSparql = !isNull(entryComparison.getQueGGEntry()) ? entryComparison.getQueGGEntry().getSparql() : "";
        Boolean tpFlag = false;
        Query qaldPARQLQuery = new Query();
        SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);
        List<String> uriResultListQueGG = new ArrayList<String>();
        List<String> uriResultListQALD;
    
        uriResultListQALD = this.getResultForQaldSparqlQuery(qaldSparql);
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


    private List<EntryComparison> getMatchProtoQuestion(QALD qaldFile, GrammarWrapper grammarWrapper, String languageCode, String matchType, double similarityPercentage) {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();

        List<String> list = new ArrayList<String>();
        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            //for (QALD.QALDQuestion qaldQuestion : qaldQuestions.question) {
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            List<GrammarEntry> grammarEntities = this.grammarMatchedEntities(qaldQuestion, grammarWrapper.getGrammarEntries(), similarityPercentage);
            EntryComparison entryComparison = new EntryComparison();
            String qaldSparql = qaldQuestions.query.sparql;
            Entry qaldEntry = new Entry();
            Entry queGGEntry = new Entry();
            qaldEntry.setActualEntry(qaldQuestions);
            qaldEntry.setId(qaldQuestions.id);
            qaldEntry.setQuestions(qaldQuestion);
            qaldEntry.setSparql(qaldSparql);

            if (!grammarEntities.isEmpty()) {
                GrammarEntry grammarEntry = grammarEntities.iterator().next();
                queGGEntry.setActualEntry(grammarEntry);
                queGGEntry.setId(grammarEntry.getId());
                queGGEntry.setQuestions(grammarEntry.getQaldMatchedQuestion());
                queGGEntry.setQuestionList(grammarEntry.getSentences());
                queGGEntry.setSparql(grammarEntry.getSparqlQuery());
                entryComparison.setMatchedFlag(Boolean.TRUE);
            } else {
                entryComparison.setMatchedFlag(Boolean.FALSE);
            }

            entryComparison.setQaldEntry(qaldEntry);
            entryComparison.setQueGGEntry(queGGEntry);
            entryComparisons.add(entryComparison);
            
            if (!grammarEntities.isEmpty()) {
                /*System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("getQaldEntry::::" + entryComparison.getQaldEntry().getQuestions());
                System.out.println("getQueGGEntry::::" + entryComparison.getQueGGEntry().getQuestionList());*/
              
            }

            //}
        }
        return entryComparisons;
    }
    
    private List<EntryComparison> getMatchRealQuestion(QALD qaldFile, Map<String, String[]> realQuestions, String languageCode, double similarityPercentage) throws Exception {
        List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
        //List<String> list = new ArrayList<String>();

        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            Map<String, QueGGinfomation> grammarEntities = this.matchedRealQuestions(qaldQuestion, realQuestions, similarityPercentage);
            EntryComparison entryComparison = new EntryComparison();
            String qaldSparql = qaldQuestions.query.sparql;
            Entry qaldEntry = new Entry();
            Entry queGGEntry = new Entry();
            qaldEntry.setActualEntry(qaldQuestions);
            qaldEntry.setId(qaldQuestions.id);
            qaldEntry.setQuestions(qaldQuestion);
            qaldEntry.setSparql(qaldSparql);

            if (!grammarEntities.isEmpty()) {
                QueGGinfomation queGGinfomation = this.getMostSimilarMatch(grammarEntities);
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
    
    private String findAllQaldEntities(QALD qaldFile, String languageCode) {
        List<String> entites = new ArrayList<String>();
        for (QALD.QALDQuestions qaldQuestions : qaldFile.questions) {
            String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, languageCode);
            String qaldSparql = qaldQuestions.query.sparql;
            qaldSparql = qaldSparql.replace("res:", '\n' + "http://dbpedia.org/resource/");
            qaldSparql = qaldSparql.replace("<http://dbpedia.org/resource/", '\n' + "http://dbpedia.org/resource/");
            qaldSparql = qaldSparql.replace("dbo:", '\n' + "http://dbpedia.org/resource/ontology/");
            qaldSparql = qaldSparql.replace("dbp:", '\n' + "http://dbpedia.org/resource/property/");
            qaldSparql = qaldSparql.replace("rdf:", '\n' + "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            qaldSparql = qaldSparql.replace("<", "\n");
            qaldSparql = qaldSparql.replace(">", "\n");
            qaldSparql = qaldSparql.replace("?uri", "");
            qaldSparql = qaldSparql.replace(".", "");
            qaldSparql = qaldSparql.replace(" ", "\n");
            entites.add(qaldSparql);
        }
        String content = "";
        for (String string : entites) {
            String[] lines = string.split(System.getProperty("line.separator"));
            for (String line : lines) {
                if (line.contains("resource")) {
                    line = line.trim().strip().stripLeading().stripTrailing();
                    if (line.equals("http://dbpediaorg/resource/")
                            ||line.contains("http://dbpediaorg/resource/ontology/")
                            || line.contains("http://dbpediaorg/resource/property/")) {
                        ;
                    } else {
                        String filterLine = line + "\n";
                        content += filterLine;
                    }

                }
            }

            /*if(line.contains("res:")){
                filter.add(line);
             }
             else if(line.contains("resource")){
                filter.add(line+" $$$");
                // System.out.println("line::"+line);
             }*/
        }

        return content;
    }

    public List<GrammarEntry> grammarMatchedEntities(String qaldsentence, List<GrammarEntry> grammarEntries, double similarityPercentage) {
        List<GrammarEntry> grammarEntities = new ArrayList<GrammarEntry>();
        qaldsentence = qaldsentence.toLowerCase().strip().trim();
        double cosineDistance = 0.0;
        double cosineDistancePercentage = 0.0;
        double cosineSimilarityPercentage = 0.0;
        for (GrammarEntry grammarEntry : grammarEntries) {
            for (String queGGquestionOrg : grammarEntry.getSentences()) {
                String queGGquestionWithPattern=Matcher.cleanString(queGGquestionOrg);
                //queGGquestionWithPattern=queGGquestionWithPattern.replace("^","");
                String queGGquestion=queGGquestionWithPattern.replace("([\\w\\s\\d-,.']+)$","");
                queGGquestion=queGGquestion.replace("([\\w\\s\\d-,.']+)\\?$","?");
                queGGquestion=queGGquestion.replace("([\\w\\s\\d-,.']+).$","?");
                queGGquestion=queGGquestion.replace("([\\w\\s\\d-,.']+)","");
                queGGquestion=queGGquestion.replace("^","");
                cosineDistance = new CosineDistance().apply(qaldsentence, queGGquestion);
                cosineDistancePercentage = Math.round(cosineDistance * 100);
                cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);

                if (cosineSimilarityPercentage > similarityPercentage) {
                    //System.out.println("qaldsentence2:::" + qaldsentence + " queGGquestion2:::" + queGGquestionWithPattern + " cosineSimilarityPercentage::" + cosineSimilarityPercentage);
                    grammarEntry.setQaldMatchedQuestion(queGGquestionWithPattern);
                    grammarEntities.add(grammarEntry);
                }
            }
        }
        return grammarEntities;
    }
    
    public Map<String, QueGGinfomation> matchedRealQuestions(String qaldsentence, Map<String, String[]> questions, double similarityPercentage) {
        Map<String, QueGGinfomation> matchedQuestions = new TreeMap<String, QueGGinfomation>();
        qaldsentence = qaldsentence.toLowerCase().strip().trim();
        HashMap<String, Double> sort = new HashMap<String, Double>();

        for (String queGGquestion : questions.keySet()) {
            String[] row = questions.get(queGGquestion);
            qaldsentence = qaldsentence.replace("\"", "");
            queGGquestion = queGGquestion.replace("\"", "");
            Double value = StringSimilarity.similarity(qaldsentence, queGGquestion);

            if (value > 0.7) {
                QueGGinfomation queGGinfomation = new QueGGinfomation(row, value);
                sort.put(queGGinfomation.getQuestion(), value);
                matchedQuestions.put(queGGinfomation.getQuestion(), queGGinfomation);
                System.out.println("MATCHED: " + qaldsentence + ":" + queGGquestion + " cosineSimilarityPercentage::" + value);
            }
            //System.out.println("MATCHED: "+qaldsentence + ":" + queGGquestion + " cosineSimilarityPercentage::" + value);
            //else
            //System.out.println("NOT MATCHED:" + qaldsentence + ":" + question + " cosineSimilarityPercentage::" + cosineSimilarityPercentage);
        }

        return matchedQuestions;
    }

    private List<String> getResultForQaldSparqlQuery(String qaldSparql) {
        LOG.debug("Executing QALD SPARQL Query:\n{}", qaldSparql);
        
        if (qaldSparql.length() >= 2 && qaldSparql.charAt(0) == '"' && qaldSparql.charAt(qaldSparql.length() - 1) == '"') {
            qaldSparql = qaldSparql.substring(1, qaldSparql.length() - 1);
        }
          

        List<String> uriResultList = new ArrayList<String>();
        SPARQLRequest sparqlRequest = SPARQLRequest.fromString(qaldSparql);
        uriResultList = sparqlRequest.getSparqlResultList();
        return uriResultList;
    }

    private String getQuGGResult(String queGGSparql,String uriMatch,String bindingVarName,QueryType queryType) {
        String replaceUri=null;
         SparqlQuery sparqlQuery =null;
        if(bindingVarName.contains("subjOfProp")){
           replaceUri="objOfProp" ;
        }
        else if(bindingVarName.contains("objOfProp")){
             replaceUri="subjOfProp" ;
        }
            
        
        if (queryType.equals(QueryType.SELECT)) {
            String property = StringUtils.substringBetween(queGGSparql, "<", ">");
            sparqlQuery = new SparqlQuery(uriMatch, property, null,SparqlQuery.FIND_ANY_ANSWER, replaceUri, language, endpoint, false,queryType);
        }
        else if (queryType.equals(QueryType.ASK)) {
            String property = StringUtils.substringBetween(queGGSparql, "<", ">");
            //change later..............
            String rangeUri = StringUtils.substringBetween(queGGSparql, "<", ">");
            sparqlQuery = new SparqlQuery(uriMatch, property,rangeUri,SparqlQuery.FIND_ANY_ANSWER, replaceUri, language, endpoint, false,queryType);
        }
        return  sparqlQuery.getSparqlQuery();
    }

  
    private QueGGinfomation getMostSimilarMatch(Map<String, QueGGinfomation> grammarEntities) throws Exception {
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (String question : grammarEntities.keySet()) {
            QueGGinfomation queGGinfomation = grammarEntities.get(question);
            map.put(question, queGGinfomation.getValue());
        }
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
        sorted_map.putAll(map);
        System.out.println("sorted_map: " + sorted_map);
        String key = sorted_map.firstKey();
        Double value = sorted_map.get(key);
        QueGGinfomation queGGinfomation = grammarEntities.get(key);
        return queGGinfomation;

    }

    

}
