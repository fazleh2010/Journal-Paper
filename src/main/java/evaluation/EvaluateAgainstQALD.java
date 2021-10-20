package evaluation;


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
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.CosineDistance;
import static org.apache.jena.sparql.syntax.ElementWalker.walk;

public class EvaluateAgainstQALD {

    private static final Logger LOG = LogManager.getLogger(EvaluateAgainstQALD.class);
    private final String language;
    private final String ORIGINAL = "ORIGINAL";
    private final String BOG = "BOG";
    private final double similarityPercentage = 65;
    private Map<String, GrammarEntry> matchedQueGGEntriesIds = new TreeMap<String, GrammarEntry>();
    private Set<String> qaldQuestions = new TreeSet<String>();
    private String endpoint=null;

    public EvaluateAgainstQALD(String language,String endpoint) {
        this.language = language;
        this.endpoint=endpoint;
    }

    public void evaluateAndOutput(GrammarWrapper grammarWrapper, String qaldOriginalFile, String qaldModifiedFile, String resultFileName, String qaldRaw, String languageCode) throws IOException {
        QALDImporter qaldImporter = new QALDImporter();
        qaldImporter.qaldToCSV(qaldOriginalFile, qaldRaw,languageCode);
        //qaldImporter.qaldToCSV(qaldOriginalFile, qaldRaw);
        QALD qaldModified = qaldImporter.readQald(qaldModifiedFile);
        QALD qaldOriginal = qaldImporter.readQald(qaldOriginalFile);
        EvaluationResult result = doEvaluation(qaldModified, grammarWrapper, languageCode);
        //System.out.println("f_measure_global:::" + result.f_measure_global);
        //System.out.println("precision_global:::" + result.precision_global);
        //System.out.println("recall_global:::" + result.recall_global);
        //System.out.println("tp_global:::" + result.tp_global);
        //System.out.println("fp_global:::" + result.fp_global);
        //System.out.println("fn_global:::" + result.fn_global);

        //this.writeResult(qaldImporter,qaldOriginal,result,resultFileName,languageCode);
    }

    private EvaluationResult doEvaluation(QALD qaldFile, GrammarWrapper grammarWrapper, String languageCode) {

        EvaluationResult evaluationResult = new EvaluationResult();
        List<EntryComparison> entryComparisons = getAllSentenceMatches(qaldFile, grammarWrapper, languageCode, BOG, similarityPercentage);
        for (EntryComparison entryComparison : entryComparisons) {
            compareEntries(entryComparison);
            /*evaluationResult.getEntryComparisons().add(entryComparison);
            LOG.info("tp: {}, fp: {}, fn: {}", entryComparison.getTp(), entryComparison.getFp(), entryComparison.getFn());
            evaluationResult.setTp_global(evaluationResult.getTp_global() + entryComparison.getTp());
            evaluationResult.setFp_global(evaluationResult.getFp_global() + entryComparison.getFp());
            evaluationResult.setFn_global(evaluationResult.getFn_global() + entryComparison.getFn());*/
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
                "tp_global: {}, fp_global: {}, fn_global: {}",
                evaluationResult.getTp_global(),
                evaluationResult.getFp_global(),
                evaluationResult.getFn_global()
        );
        LOG.info(
                "precision_global: {}, recall_global: {}, f_measure_global: {}",
                evaluationResult.getPrecision_global(),
                evaluationResult.getRecall_global(),
                evaluationResult.getF_measure_global()
        );
        return evaluationResult;
    }

    /**
     * QueGG Satz-Prototyp: Who wrote $x? - $x ersetzen durch regex mit capture
     * - regex auf Qald anwenden -> capture group in bindings labels von QueGG
     * suchen -> uri nehmen und in QueGG sparql einsetzen, Ergebnisse der sparql
     * queries (QueGG <-> QALD) vergleichen
     */
    private void compareEntries(EntryComparison entryComparison) {
        GrammarEntry grammarEntry = !isNull(entryComparison.getQueGGEntry()) ? (GrammarEntry) entryComparison.getQueGGEntry().getActualEntry() : null;
        String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
        String cleanQaldQuestion = cleanQALDString(qaldQuestion); //  make lower case
        String qaldSparql = entryComparison.getQaldEntry().getSparql();
        String queGGSparql = !isNull(entryComparison.getQueGGEntry()) ? entryComparison.getQueGGEntry().getSparql() : "";
        Boolean tpFlag = false;
        Query qaldPARQLQuery = new Query();
        SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);
        String matchedSentenceItem = "";
        Pattern sentencePattern = null;
        String uriMatch = "";
        String bindingVarName = "";
        List<String> uriResultListQueGG = new ArrayList<String>();
        List<String> uriResultListQALD;
        String matchedPattern = "";

        sentencePattern = this.getMatchPattern(entryComparison, cleanQaldQuestion);
        uriMatch = this.getMatchedUri(sentencePattern,matchedSentenceItem,qaldPARQLQuery);
        uriResultListQALD = this.getResultForQaldSparqlQuery(qaldSparql);
        entryComparison.setQaldResults(uriResultListQALD);

        // get variable name (i.e. objOfProp, subjOfProp) from QueGG binding sparql to replace it with uriMatch
        bindingVarName = getVarNameFromQueGGBinding(grammarEntry);
        System.out.println("uriMatch:::"+uriMatch);
        
        if (bindingVarName.isEmpty() || uriMatch.isEmpty()) {
            // Variable name to substitute in the bindings SPARQL or a valid uri substitute could not be found
            LOG.info("No match for {}", qaldQuestion);
            if (!isNull(entryComparison.getQueGGEntry())) {
                entryComparison.getQueGGEntry().setSparql(""); // delete SPARQL query because it was not executed anyway
            }
        } else {
            queGGSparql=this.getQuGGResult(queGGSparql,uriMatch,bindingVarName);
            uriResultListQueGG= this.getResultForQaldSparqlQuery(queGGSparql);
            //uriResultListQueGG=new ArrayList<String>();
            //uriResultListQueGG.add("http://dbpedia.org/ontology/Donald_Trump");
            entryComparison.getQueGGEntry().setSparql(queGGSparql);
            entryComparison.setQueGGResults(uriResultListQueGG);    
         
            LOG.info(sentencePattern);
            LOG.info(uriMatch);
            LOG.info(qaldSparql);
            LOG.info(uriResultListQALD);
            LOG.info(uriResultListQueGG);
           
        }

        LOG.debug(
                "Comparing QueGG results to QALD results: #QueGG: {}, #QALD: {}",
                uriResultListQueGG.size(),
                uriResultListQALD.size()
        );
        LOG.debug("Comparing QueGG results to QALD results: QueGG: {}, QALD: {}", uriResultListQueGG, uriResultListQALD);
      
        if (sentencePattern != null) {
            System.out.println("sentencePattern............................................." + sentencePattern);
            System.out.println("uriMatch............................................." + uriMatch);
            System.out.println("qaldSparql............................................." + qaldSparql);
            System.out.println("uriResultListQALD............................................." + uriResultListQALD);
            System.out.println("binding............................................." + bindingVarName);
            System.out.println("queGGSparql............................................." + queGGSparql);
            System.out.println("uriResultListQueGG............................................." + uriResultListQueGG);

           
        }


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

        if (entryComparison.getTp()==1) {
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
        if (entryComparison.getFp()==1) {
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
      }

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

    private String findMatchingNodeToQALDSentenceMatchedItem(Query query, String lowerCasePattern) {
        final String[] match = {""};
        walk(
                query.getQueryPattern(), // ElementGroup
                new ElementVisitorBase() {
            // Go through blocks of triples
            public void visit(ElementPathBlock el) {
                // Go through all triples
                Iterator<TriplePath> triples = el.patternElts();
                while (triples.hasNext()) {
                    TriplePath triplePath = triples.next();
                    // Check for match in subject or object
                    if (triplePath.getSubject().isURI()) {
                        if (triplePath.getSubject().getURI().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getSubject().getURI();
                        }
                    } else if (triplePath.getSubject().isLiteral()) {
                        if (triplePath.getSubject().getLiteral().getValue().toString().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getSubject().getLiteral().getValue().toString();
                        }
                    }
                    if (triplePath.getObject().isURI()) {
                        if (triplePath.getObject().getURI().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getObject().getURI();
                        }
                    } else if (triplePath.getObject().isLiteral()) {
                        if (triplePath.getObject().getLiteral().getValue().toString().toLowerCase().contains(lowerCasePattern)) {
                            match[0] = triplePath.getObject().getLiteral().getValue().toString();
                        }
                    }
                }
            }
        }
        );
        return match[0];
    }

    private String cleanQALDString(String sentence) {
        return sentence.toLowerCase().trim();
    }

    /**
     * Make lower case, add regex capture for $x and (... | ...)
     */
    protected String cleanString(String sentence) {
        return String.format("^%s$", sentence
                .replace(DEFAULT_BINDING_VARIABLE, "([\\w\\s\\d-,.']+)")
                .replaceAll("\\((.+)\\s\\|\\s(.+)\\)", "([\\\\w\\\\s\\\\d-,.']+)")
                .replace("?", "\\?")
                .toLowerCase()
                .trim());
    }

    private String getVarNameFromQueGGBinding(GrammarEntry grammarEntry) {
        return !isNull(grammarEntry) ? Var.alloc(grammarEntry.getBindingVariable()).toString() : "";
    }

    private List<EntryComparison> getAllSentenceMatches(QALD qaldFile, GrammarWrapper grammarWrapper, String languageCode, String matchType, double similarityPercentage) {
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
        //List<String> matchedQaldEntries =this.getOriginalMatch(qaldSentences,queGGPatterns,matchType,similarityPercentage);
        return this.getOriginalMatch(qaldFile, grammarWrapper, languageCode, matchType, similarityPercentage);
    }

    private List<EntryComparison> sortMatches(List<EntryComparison> matchingEntries) {
        return matchingEntries.stream().parallel()
                .sorted(Comparator.comparing(
                        entryComparison -> Integer.valueOf(entryComparison.getQaldEntry().getId())
                ))
                .collect(Collectors.toList());
    }

    private List<Pattern> findMatchPatterns(EntryComparison entryComparison, String cleanQaldQuestion) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        patterns = entryComparison.getQueGGEntry().getQuestionList()
                .stream()
                .map(this::cleanString)
                .map(Pattern::compile)
                .filter(pattern -> !PatternMatchHelper.getPatternMatch(cleanQaldQuestion, pattern)
                .isEmpty())
                .collect(Collectors.toList());

        for (Pattern pattern : patterns) {
            //System.out.println("pattern:::"+pattern);
        }
        return patterns;
    }

    private List<Pattern> cosineSimilarity(EntryComparison entryComparison, String cleanQaldQuestion) {
        double cosineDistance = 0.0;
        double cosineDistancePercentage = 0.0;
        double cosineSimilarityPercentage = 0.0;
        List<Pattern> matchedPatterns = new ArrayList<Pattern>();

        for (String question : entryComparison.getQueGGEntry().getQuestionList()) {
            //System.out.println("question::" + question);
            cosineDistance = new CosineDistance().apply(question, cleanQaldQuestion);
            cosineDistancePercentage = Math.round(cosineDistance * 100);
            cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);
            if (cosineSimilarityPercentage > 40) {
                //System.out.println("cosineSimilarityPercentage");
                return matchedPatterns;
            }
        }
        return new ArrayList<Pattern>();
    }

    /*private List<String> getOriginalMatch(List<String> qaldSentences, List<Pattern> queGGPatterns, String matchType,double similarityPercentage) {

        List<String> list = new ArrayList<String>();
        if (matchType.contains(ORIGINAL)) {
            list = qaldSentences.stream().parallel()
                    .filter(qaldQuestion
                            -> queGGPatterns.stream().parallel()
                            .anyMatch(queGGPattern -> !PatternMatchHelper.getPatternMatch(
                            cleanQALDString(qaldQuestion),
                            queGGPattern
                    ).isEmpty())
                    )
                    .collect(Collectors.toList());
        } else if (matchType.contains(BOG)) {
            double cosineDistance = 0.0;
            double cosineDistancePercentage = 0.0;
            double cosineSimilarityPercentage = 0.0;
            for (String qaldsentence : qaldSentences) {
                String qaldsentenceOrg=qaldsentence;
                qaldsentence=qaldsentence.toLowerCase();
                for (Pattern pattern : queGGPatterns) {
                    String queGGquestion = pattern.pattern().toLowerCase().replace("^", "");
                    queGGquestion = queGGquestion.replace("([\\w\\s\\d-,.']+)\\", "");
                    queGGquestion = queGGquestion.replace("$", "");
                    cosineDistance = new CosineDistance().apply(qaldsentence, queGGquestion);
                    cosineDistancePercentage = Math.round(cosineDistance * 100);
                    cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);
                      //if (qaldsentence.contains("chi era la moglie del abraham lincoln?"))
                       // System.out.println("qaldsentence:" + qaldsentence + "  queGGquestion:" + queGGquestion + " cosine:" + cosineSimilarityPercentage);
                    if (cosineSimilarityPercentage > similarityPercentage) {
                        //if (pattern.pattern().contains("marito")||pattern.pattern().contains("moglie")) {
                            //System.out.println("qaldsentence:" + qaldsentence + "  queGGquestion:" + queGGquestion + " cosine:" + cosineSimilarityPercentage);
                            list.add(qaldsentenceOrg);
                        //}
                       
                    }
                }
            }

        }
        
        return list;

    }*/
    private List<String> getOriginalMatch(List<String> qaldSentences, List<Pattern> queGGPatterns, String matchType, double similarityPercentage) {

        List<String> list = new ArrayList<String>();
        if (matchType.contains(ORIGINAL)) {
            list = qaldSentences.stream().parallel()
                    .filter(qaldQuestion
                            -> queGGPatterns.stream().parallel()
                            .anyMatch(queGGPattern -> !PatternMatchHelper.getPatternMatch(
                            cleanQALDString(qaldQuestion),
                            queGGPattern
                    ).isEmpty())
                    )
                    .collect(Collectors.toList());
        } else if (matchType.contains(BOG)) {
            double cosineDistance = 0.0;
            double cosineDistancePercentage = 0.0;
            double cosineSimilarityPercentage = 0.0;
            for (String qaldsentence : qaldSentences) {
                String qaldsentenceOrg = qaldsentence;
                qaldsentence = qaldsentence.toLowerCase();
                for (Pattern pattern : queGGPatterns) {
                    String queGGquestion = pattern.pattern().toLowerCase().replace("^", "");
                    queGGquestion = queGGquestion.replace("([\\w\\s\\d-,.']+)\\", "");
                    queGGquestion = queGGquestion.replace("$", "");
                    cosineDistance = new CosineDistance().apply(qaldsentence, queGGquestion);
                    cosineDistancePercentage = Math.round(cosineDistance * 100);
                    cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);
                    //if (qaldsentence.contains("chi era la moglie del abraham lincoln?"))
                    // System.out.println("qaldsentence:" + qaldsentence + "  queGGquestion:" + queGGquestion + " cosine:" + cosineSimilarityPercentage);
                    if (cosineSimilarityPercentage > similarityPercentage) {
                        //if (pattern.pattern().contains("marito")||pattern.pattern().contains("moglie")) {
                        //System.out.println("qaldsentence:" + qaldsentence + "  queGGquestion:" + queGGquestion + " cosine:" + cosineSimilarityPercentage);
                        list.add(qaldsentenceOrg);
                        //}

                    }
                }
            }

        }
        return list;

    }

    private List<EntryComparison> getOriginalMatch(QALD qaldFile, GrammarWrapper grammarWrapper, String languageCode, String matchType, double similarityPercentage) {
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
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("getQaldEntry::::" + entryComparison.getQaldEntry());
                System.out.println("getQueGGEntry::::" + entryComparison.getQueGGEntry());

            }

            //}
        }
        return entryComparisons;
    }

    public List<GrammarEntry> grammarMatchedEntities(String qaldsentence, List<GrammarEntry> grammarEntries, double similarityPercentage) {
        List<GrammarEntry> grammarEntities = new ArrayList<GrammarEntry>();
        qaldsentence = qaldsentence.toLowerCase().strip().trim();
        double cosineDistance = 0.0;
        double cosineDistancePercentage = 0.0;
        double cosineSimilarityPercentage = 0.0;
        for (GrammarEntry grammarEntry : grammarEntries) {
            for (String queGGquestion : grammarEntry.getSentences()) {
                cosineDistance = new CosineDistance().apply(qaldsentence, queGGquestion);
                cosineDistancePercentage = Math.round(cosineDistance * 100);
                cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);
                if (cosineSimilarityPercentage > 50.0) {
                    //System.out.println("queGGquestion:::" + queGGquestion + " queGGquestion:::" + queGGquestion + " cosineSimilarityPercentage::" + cosineSimilarityPercentage);
                    grammarEntities.add(grammarEntry);
                }
            }
        }
        return grammarEntities;
    }

    /*private void getOriginalMatch(List<String> qaldSentences, List<GrammarEntry> grammarEntries, String matchType, double similarityPercentage) {

      
        if (matchType.contains(BOG)) {
            double cosineDistance = 0.0;
            double cosineDistancePercentage = 0.0;
            double cosineSimilarityPercentage = 0.0;
            for (String qaldsentence : qaldSentences) {
                String qaldsentenceOrg = qaldsentence;
                qaldsentence = qaldsentence.toLowerCase();
                for (GrammarEntry grammarEntry : grammarEntries) {
                    Boolean match=false;
                    String id = grammarEntry.getId();
                    List<String> sentences = grammarEntry.getSentences();
                    for (String queGGquestion : sentences) {
                        cosineDistance = new CosineDistance().apply(qaldsentence, queGGquestion);
                        cosineDistancePercentage = Math.round(cosineDistance * 100);
                        cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);
                        queGGquestion=queGGquestion.replace("($x | PERSON_NP)", "");
                        queGGquestion=queGGquestion.toLowerCase().strip();
                        //if (qaldsentence.contains("chi era la moglie del abraham lincoln?"))
                        //System.out.println("qaldsentence:" + qaldsentence + "  queGGquestion:" + queGGquestion + " cosine:" + cosineSimilarityPercentage);
                        if (cosineSimilarityPercentage > similarityPercentage) {
                            //if (pattern.pattern().contains("marito")||pattern.pattern().contains("moglie")) {
                            System.out.println("qaldsentence:" + qaldsentence + "  queGGquestion:" + queGGquestion + " cosine:" + cosineSimilarityPercentage);
                            this.qaldQuestions.add(qaldsentenceOrg);
                             match=true;
                             break;
                            //}

                        }
                    }
                    if(match){
                       this.matchedQueGGEntriesIds.put(grammarEntry.getId(),grammarEntry);
                    }

                }
            }

        }
        
       //System.out.println("this.grammarEntryIds:"+ this.matchedQueGGEntries);
       //exit(1);

    }*/
 /*private List<GrammarEntry> getMatchedGrammarEntries(List<GrammarEntry> grammarEntries) {
         List<GrammarEntry> matchedEntities=new ArrayList<GrammarEntry>();
         for (GrammarEntry grammarEntry : grammarEntries) {
             String id=grammarEntry.getId().trim().strip();
             System.out.println(id);
              System.out.println(this.matchedQueGGEntriesIds.toString());
             
             if(this.matchedQueGGEntriesIds.equals(id)){
                 matchedEntities.add(grammarEntry);
             }
         }
         return matchedEntities;
    }*/
    private void writeResult(QALDImporter qaldImporter, QALD qaldOriginal, EvaluationResult result, String resultFileName, String languageCode) throws IOException {
        ZonedDateTime before = ZonedDateTime.now();
        ZonedDateTime after = ZonedDateTime.now();
        long duration = Duration.between(before, after).toSeconds();
        List<String[]> dataLines = EvalutionUtils.resultToPrintableList(result, qaldOriginal, languageCode);
        //System.out.println(dataLines);
        qaldImporter.writeToCSV(dataLines, resultFileName);
        LOG.info(String.format("Evaluation was completed in %dmin %ds", duration / 60, duration % 60));
        LOG.info("Results are available here: " + resultFileName);
    }

    private Pattern getMatchPattern(EntryComparison entryComparison, String cleanQaldQuestion) {
        if (entryComparison.getMatchedFlag()) {
            List<Pattern> matchedPatterns
                    = entryComparison.getQueGGEntry().getQuestionList()
                            .stream()
                            .map(this::cleanString)
                            .map(Pattern::compile)
                            .filter(pattern -> !PatternMatchHelper.getPatternMatch(cleanQaldQuestion, pattern)
                            .isEmpty())
                            .collect(Collectors.toList());

            if (matchedPatterns.size() == 1) {
                return matchedPatterns.get(0);
            }

        }
        return null;
    }

    private String getMatchedUri(Pattern sentencePattern,String matchedSentenceItem,Query qaldPARQLQuery) {
        String uriMatch="";
        if (!isNull(sentencePattern)) {
            String matchedPattern = sentencePattern.toString();
            // Try to find the matching uri for the previously found item inside the QALD SPARQL query
            String lowerCaseMatchedSentenceItem = matchedSentenceItem.toLowerCase().replace(" ", "_");
            uriMatch = findMatchingNodeToQALDSentenceMatchedItem(qaldPARQLQuery, lowerCaseMatchedSentenceItem);
            System.out.println("lowerCaseMatchedSentenceItem::" + lowerCaseMatchedSentenceItem);
            System.out.println("uriMatch::" + uriMatch);
        }
        return uriMatch;
    }

    private List<String> getResultForQaldSparqlQuery(String qaldSparql) {
        LOG.debug("Executing QALD SPARQL Query:\n{}", qaldSparql);
        List<String> uriResultList = new ArrayList<String>();
        SPARQLRequest sparqlRequest = SPARQLRequest.fromString(qaldSparql);
        uriResultList = sparqlRequest.getSparqlResultList();
        return uriResultList;
    }

    private String getQuGGResult(String queGGSparql,String uriMatch,String bindingVarName) {
        String property = StringUtils.substringBetween(queGGSparql, "<", ">");
        SparqlQuery sparqlQuery = new SparqlQuery(uriMatch, property, SparqlQuery.FIND_ANY_ANSWER, bindingVarName, language, endpoint, false);
        return  sparqlQuery.getSparqlQuery();
    }

}
