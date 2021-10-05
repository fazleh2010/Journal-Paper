import com.fasterxml.jackson.databind.ObjectMapper;
import evaluation.Entry;
import evaluation.EntryComparison;
import evaluation.EvaluationResult;
import evaluation.QALD;
import evaluation.QALDImporter;
import grammar.sparql.SPARQLRequest;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
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
import util.sparql.RequestCompiler;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static grammar.generator.BindingResolver.insertBindingInSPARQL;
import static grammar.generator.BindingConstants.DEFAULT_BINDING_VARIABLE;
import java.io.FileNotFoundException;
import static java.lang.System.exit;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Objects.isNull;
import org.apache.commons.text.similarity.CosineDistance;
import static org.apache.jena.sparql.syntax.ElementWalker.walk;
import static util.io.ResourceHelper.loadResource;

public class EvaluateAgainstQALD {
  private static final Logger LOG = LogManager.getLogger(EvaluateAgainstQALD.class);
  private final Language language;
  

  EvaluateAgainstQALD(Language language) {
    this.language = language;
  }
  
   /*public static void main(String[] args) throws IOException {
   EvaluateAgainstQALD evaluateAgainstQALD = new EvaluateAgainstQALD(Language.EN);
   GrammarWrapper grammarWrapper;
    ObjectMapper objectMapper = new ObjectMapper();
    URL grammarEntriesFile = loadResource("grammar_FULL_DATASET_EN.json", this.getClass());
    URL grammarEntriesFile2 = loadResource("grammar_COMBINATIONS_EN.json", this.getClass());
    grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);
    GrammarWrapper gw2 = objectMapper.readValue(grammarEntriesFile2, GrammarWrapper.class);
    grammarWrapper.merge(gw2);
  
       
   }*/
  
  
   
   public static URL loadResource(String resource, Class<?> clazz) throws FileNotFoundException {
        
    URL res = clazz.getClassLoader().getResource(resource);
  
    if (isNull(res)) {
      throw new FileNotFoundException(String.format("FileNotFound: %s", resource));
    }
    return res;
  }

  public void evaluateAndOutput(GrammarWrapper grammarWrapper,String qaldOriginalFile,String qaldModifiedFile,String resultFileName,String qaldRaw) throws IOException {
    
 
    QALDImporter qaldImporter = new QALDImporter();
    //qaldImporter.qaldToCSV(qaldOriginalFile, "QALD-2017-dataset-raw.csv");
    qaldImporter.qaldToCSV(qaldOriginalFile,qaldRaw);
    QALD qaldModified = qaldImporter.readQald(qaldModifiedFile);
    //QALD qaldOriginal = qaldImporter.readQald(qaldOriginalFile);

    ZonedDateTime before = ZonedDateTime.now();
    EvaluationResult result = doEvaluation(qaldModified, grammarWrapper);
    ZonedDateTime after = ZonedDateTime.now();
    long duration = Duration.between(before, after).toSeconds();

    //qaldImporter.writeToCSV(resultToPrintableList(result, qaldOriginal), resultFileName);
    LOG.info(String.format("Evaluation was completed in %dmin %ds", duration / 60, duration % 60));
    LOG.info("Results are available here: " + resultFileName);
  }

  private EvaluationResult doEvaluation(QALD qaldFile, GrammarWrapper grammarWrapper) {

    EvaluationResult evaluationResult = new EvaluationResult();
    List<EntryComparison> entryComparisons = getAllSentenceMatches(qaldFile, grammarWrapper);
    for (EntryComparison entryComparison : entryComparisons) {
       compareEntriesNew(entryComparison);
       //compareEntries(entryComparison);
      
      //evaluationResult.getEntryComparisons().add(entryComparison);
      /*LOG.info("tp: {}, fp: {}, fn: {}", entryComparison.getTp(), entryComparison.getFp(), entryComparison.getFn());
      evaluationResult.setTp_global(evaluationResult.getTp_global() + entryComparison.getTp());
      evaluationResult.setFp_global(evaluationResult.getFp_global() + entryComparison.getFp());
      evaluationResult.setFn_global(evaluationResult.getFn_global() + entryComparison.getFn());*/
    }
    
    //System.out.println("entryComparisons::::"+entryComparisons.size());


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
      (2 *
        (
          calculateMeasure(
            evaluationResult.getPrecision_global() * evaluationResult.getRecall_global(),
            evaluationResult.getPrecision_global(),
            evaluationResult.getRecall_global()
          )
        )
      )
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

  private List<String[]> resultToPrintableList(EvaluationResult result, QALD qaldOriginal) {
    List<String[]> list = new ArrayList<>();
    list.add(new String[]{
      "QALD id",
      "QALD original question",
      "QALD original SPARQL query",
      "QALD reformulated question",
      "QueGG SPARQL query",
      "TP",
      "FP",
      "FN",
      "Precision",
      "Recall",
      "F-Measure",
      "Reformulated?"
    });
    int numberOfQueGGMatches = 0;
    for (EntryComparison entryComparison : result.getEntryComparisons()) {
      list.add(
        new String[]{
          entryComparison.getQaldEntry().getId(),
          QALDImporter.getQaldQuestionString(
            getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison),
            language.name().toLowerCase()
          ),
          getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison).query.sparql,
          entryComparison.getQaldEntry().getQuestions(),
          !isNull(entryComparison.getQueGGEntry()) ?
          entryComparison.getQueGGEntry().getSparql() :
          "", // entryComparison.getQueGGEntries().stream().filter(entry -> entry.).getSentences().stream().reduce((x, y) -> x + "\n" + y).orElse(""),
          String.valueOf(entryComparison.getTp()),
          String.valueOf(entryComparison.getFp()),
          String.valueOf(entryComparison.getFn()),
          String.valueOf(entryComparison.getPrecision()),
          String.valueOf(entryComparison.getRecall()),
          String.valueOf(entryComparison.getF_measure()),
          String.valueOf(checkReformulated(QALDImporter.getQaldQuestionString(getMatchingOriginalQaldQuestions(
            qaldOriginal,
            entryComparison
          ), language.name().toLowerCase()), entryComparison.getQaldEntry().getQuestions()))
        }
      );
      if (
        !isNull(entryComparison.getQueGGEntry()) &&
          !isNull(entryComparison.getQueGGEntry().getSparql()) &&
          !entryComparison.getQueGGEntry().getSparql().equals("")
      ) {
        numberOfQueGGMatches++;
      }
    }
    list.add(new String[]{
      "",                                           // "QALD id",
      "",                                           // "QALD original question",
      "",                                           // "QALD original SPARQL query",
      "",                                           // "QALD reformulated question",
      "",                                           // "QueGG SPARQL query",
      String.valueOf(result.getTp_global()),        // "TP",
      String.valueOf(result.getFp_global()),        // "FP",
      String.valueOf(result.getFn_global()),        // "FN",
      String.valueOf(result.getPrecision_global()), // "Precision",
      String.valueOf(result.getRecall_global()),    // "Recall",
      String.valueOf(result.getF_measure_global()), // "F-Measure",
      "",                                           // "Reformulated?"
    });
    LOG.info(String.format("Total matches: %d", numberOfQueGGMatches));
    LOG.info(String.format(
      "QALD coverage: %.2f%%",
      (float) (numberOfQueGGMatches) / (float) qaldOriginal.questions.size() * 100
    ));

    return list;
  }

  /**
   * QueGG Satz-Prototyp: Who wrote $x?
   * - $x ersetzen durch regex mit capture
   * - regex auf Qald anwenden
   * -> capture group in bindings labels von QueGG suchen
   * -> uri nehmen und in QueGG sparql einsetzen, Ergebnisse der sparql queries (QueGG <-> QALD) vergleichen
   */
  
    private void compareEntriesNew(EntryComparison entryComparison) {
        GrammarEntry grammarEntry = !isNull(entryComparison.getQueGGEntry()) ? (GrammarEntry) entryComparison.getQueGGEntry().getActualEntry() : null;
        String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
        String cleanQaldQuestion = cleanQALDString(qaldQuestion); //  make lower case
        String qaldSparql = entryComparison.getQaldEntry().getSparql();
        String queGGSparql = !isNull(entryComparison.getQueGGEntry()) ? entryComparison.getQueGGEntry().getSparql() : "";
        Query qaldPARQLQuery = new Query();
        SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);

        // Replace grammarEntry sentence DEFAULT_BINDING_VARIABLE with a regex capture.
        Boolean flag = false;
        String matchedSentenceItem = "";
        Pattern sentencePattern = null;
        
      
        
        if (!isNull(entryComparison.getQueGGEntry())) {
            List<Pattern> matchedPatterns
                    = entryComparison.getQueGGEntry().getQuestionList()
                            .stream()
                            .map(this::cleanString)
                            .map(Pattern::compile)
                            .filter(pattern -> !PatternMatchHelper.getPatternMatch(cleanQaldQuestion, pattern)
                            .isEmpty())
                            .collect(Collectors.toList());

            //System.out.println("matchedPatterns:::"+matchedPatterns.size());
            // We already know that there is at least one match in the sentences!
              System.out.println("matchedPatterns::"+matchedPatterns);
        }
        
        //cosineSimilarity(entryComparison,cleanQaldQuestion);
        
        /*System.out.println("qaldQuestion::"+qaldQuestion);
        System.out.println("cleanQaldQuestion::"+cleanQaldQuestion);
        System.out.println("qaldSparql::"+qaldSparql);
        System.out.println("queGGSparql::"+queGGSparql);*/

    }

  
   private void compareEntries(EntryComparison entryComparison) {
    GrammarEntry grammarEntry = !isNull(entryComparison.getQueGGEntry()) ? (GrammarEntry) entryComparison.getQueGGEntry().getActualEntry() : null;
    String qaldQuestion = entryComparison.getQaldEntry().getQuestions();
    String cleanQaldQuestion = cleanQALDString(qaldQuestion); //  make lower case
    String qaldSparql = entryComparison.getQaldEntry().getSparql();
    String queGGSparql = !isNull(entryComparison.getQueGGEntry()) ? entryComparison.getQueGGEntry().getSparql() : "";
    Query qaldPARQLQuery = new Query();
    SPARQLParser.createParser(Syntax.syntaxSPARQL_11).parse(qaldPARQLQuery, qaldSparql);

    // Replace grammarEntry sentence DEFAULT_BINDING_VARIABLE with a regex capture.
    Boolean flag=false;
    String matchedSentenceItem = "";
    Pattern sentencePattern = null;
  
    if (!isNull(entryComparison.getQueGGEntry())) {
      List<Pattern> matchedPatterns =
        entryComparison.getQueGGEntry().getQuestionList()
                       .stream()
                       .map(this::cleanString)
                       .map(Pattern::compile)
                       .filter(pattern -> !PatternMatchHelper.getPatternMatch(cleanQaldQuestion, pattern)
                                                                     .isEmpty())
                       .collect(Collectors.toList());
      
      // We already know that there is at least one match in the sentences!
      if (matchedPatterns.size() == 1) {
         sentencePattern = matchedPatterns.get(0);
        // Try to find an item to replace $x in the QueGG sentence
         matchedSentenceItem = PatternMatchHelper.getPatternMatch(cleanQaldQuestion, sentencePattern);
         System.out.println("sentencePattern::"+sentencePattern);
         System.out.println("matchedPatterns::"+matchedPatterns);
         System.out.println("matchedSentenceItem::"+matchedSentenceItem);
         System.out.println("cleanQaldQuestion::"+cleanQaldQuestion);
         exit(1);
        
        flag=true;
      } else {
        LOG.info("How can there be more than one match in one sentence?!");
      }
    }
    
    
    String uriMatch = "";
    String bindingVarName = "";
    List<String> uriResultListQueGG = new ArrayList<>();
    List<String> uriResultListQALD;
    String matchedPattern = "";

    String lowerCaseMatchedSentenceItem =null;
    if (!isNull(sentencePattern)) {
      matchedPattern = sentencePattern.toString();
      // Try to find the matching uri for the previously found item inside the QALD SPARQL query
      lowerCaseMatchedSentenceItem = matchedSentenceItem.toLowerCase().replace(" ", "_");
      uriMatch = findMatchingNodeToQALDSentenceMatchedItem(qaldPARQLQuery, lowerCaseMatchedSentenceItem);
       
     
    }

    LOG.debug("Executing QALD SPARQL Query:\n{}", qaldSparql);
    SPARQLRequest sparqlRequestQALD = SPARQLRequest.fromString(qaldSparql);
    uriResultListQALD = sparqlRequestQALD.getSparqlResultList();
    
       /*if (flag) {
           System.out.println("sentencePattern::" + sentencePattern);
           System.out.println("matchedSentenceItem::" + matchedSentenceItem);
           System.out.println("lowerCaseMatchedSentenceItem::" + lowerCaseMatchedSentenceItem);
           System.out.println("uriMatch::" + uriMatch);
           System.out.println("sparqlRequestQALD::" + sparqlRequestQALD);
           System.out.println("uriResultListQALD::" + uriResultListQALD);
           exit(1);

       }*/
    
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
      // Execute QueGG and QALD SPARQL queries and compare results
      // replace binding variable with the previously found QALD uri match and execute the query
      LOG.info("Match found with pattern '{}'", matchedPattern);
      // replace bindingVar in QueGG SPARQL
      queGGSparql = insertBindingInSPARQL(bindingVarName, uriMatch, queGGSparql);
      entryComparison.getQueGGEntry().setSparql(queGGSparql);
      

      LOG.info("Executing QueGG SPARQL Query:\n{}", queGGSparql);
      SPARQLRequest sparqlRequestQueGG = SPARQLRequest.fromString(queGGSparql.replace(bindingVarName, uriMatch));
      uriResultListQueGG = sparqlRequestQueGG.getSparqlResultList();
      LOG.info("QueGG Query results: {}", uriResultListQueGG);
      LOG.info("QALD Query results: {}", uriResultListQALD);
      
    }

    LOG.debug(
      "Comparing QueGG results to QALD results: #QueGG: {}, #QALD: {}",
      uriResultListQueGG.size(),
      uriResultListQALD.size()
    );
    LOG.debug("Comparing QueGG results to QALD results: QueGG: {}, QALD: {}", uriResultListQueGG, uriResultListQALD);
    /*
      Measures:

      True  Positive:  Number of results in QueGG SPARQL query that are also in QALD  query results
      False Positive:  Number of results in QueGG SPARQL query that are not  in QALD  query results
      False Negative:  Number of results in QALD  SPARQL query that are not  in QueGG query results
      //True  Negative:  Number of results missing from both datasets -> not relevant

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
        (2 *
          (
            calculateMeasure(
              entryComparison.getPrecision() * entryComparison.getRecall(),
              entryComparison.getPrecision(),
              entryComparison.getRecall()
            )
          )
        )
      );
    }
    
    
     if (flag) {
           System.out.println("sentencePattern::" + sentencePattern);
           System.out.println("matchedSentenceItem::" + matchedSentenceItem);
           System.out.println("lowerCaseMatchedSentenceItem::" + lowerCaseMatchedSentenceItem);
           System.out.println("uriMatch::" + uriMatch);
           System.out.println("sparqlRequestQALD::" + sparqlRequestQALD);
           System.out.println("entryComparison::" + entryComparison);
           exit(1);

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

  private QALD.QALDQuestions getMatchingOriginalQaldQuestions(QALD qaldOriginal, EntryComparison entryComparison) {
    return qaldOriginal.questions.stream()
                                 .filter(qaldQuestions -> qaldQuestions.id.equals(entryComparison.getQaldEntry()
                                                                                                 .getId()))
                                 .findAny()
                                 .orElseThrow();
  }

  private boolean checkReformulated(String qaldQuestionString, String questions) {
    return !qaldQuestionString.equals(questions);
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

  private List<EntryComparison> getAllSentenceMatches(QALD qaldFile, GrammarWrapper grammarWrapper) {
      //System.out.print("qaldFile.questions:"+qaldFile.questions.get(10).question.get(0).language.contains("it"));
      //exit(1);
    List<EntryComparison> matchingEntries = new ArrayList<>();
    List<String> qaldSentences =
      qaldFile.questions
        .stream().parallel()
        .map(qaldQuestions -> qaldQuestions.question)
        .flatMap(qaldQuestions1 ->
                   qaldQuestions1.stream().parallel()
                                 .filter(qaldQuestion -> qaldQuestion.language.equals("it"))
                                 .map(qaldQuestion -> qaldQuestion.string))
        .collect(Collectors.toList());

    List<Pattern> queGGPatterns =
      grammarWrapper.getGrammarEntries()
                    .stream().parallel()
                    .map(GrammarEntry::getSentences)
                    .flatMap(strings -> strings.stream().parallel().map(this::cleanString).map(Pattern::compile))
                    .collect(Collectors.toList());

    List<String> matchedQaldEntries =
      qaldSentences.stream().parallel()
                   .filter(qaldQuestion ->
                             queGGPatterns.stream().parallel()
                                          .anyMatch(queGGPattern -> !PatternMatchHelper.getPatternMatch(
                                            cleanQALDString(qaldQuestion),
                                            queGGPattern
                                          ).isEmpty())
                   )
                   .collect(Collectors.toList());

    List<QALD.QALDQuestions> potentialQALDMatches =
      qaldFile.questions.stream().parallel()
                        .filter(
                          qaldQuestion1 ->
                            matchedQaldEntries
                              .stream().parallel()
                              .anyMatch(matchedQaldEntry -> qaldQuestion1.question
                                .stream().parallel()
                                .anyMatch(
                                  qaldQuestion -> qaldQuestion.string.equals(matchedQaldEntry)
                                )
                              )
                        )
                        .collect(Collectors.toList());

    List<GrammarEntry> matchedQueGGEntries =
      grammarWrapper.getGrammarEntries()
                    .stream().parallel()
                    .filter(queGGEntry ->
                              qaldSentences.stream().parallel()
                                           .anyMatch(qaldQuestion ->
                                                       isQaldQuestionMatchAnyQueGGSentenceInGrammarEntry(
                                                         queGGEntry,
                                                         qaldQuestion
                                                       )
                                           )
                    )
                    .collect(Collectors.toList());

    potentialQALDMatches
      .forEach(
        qaldQuestions ->
        {
          EntryComparison entryComparison = new EntryComparison();
          String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, "it");
          String qaldSparql = qaldQuestions.query.sparql;
          matchedQueGGEntries
            .forEach(
              grammarEntry ->
              {
                if (isQaldQuestionMatchAnyQueGGSentenceInGrammarEntry(grammarEntry, qaldQuestion)) {
                  Entry qaldEntry = new Entry();
                  qaldEntry.setActualEntry(qaldQuestions);
                  qaldEntry.setId(qaldQuestions.id);
                  qaldEntry.setQuestions(qaldQuestion);
                  qaldEntry.setSparql(qaldSparql);
                  if (isNull(entryComparison.getQaldEntry())) {
                    entryComparison.setQaldEntry(qaldEntry);
                  }
                  Query queGGSparqlQuery = RequestCompiler.compileAnswerQuery(grammarEntry);
                  String queGGSparql = queGGSparqlQuery.toString();

                  Entry queGGEntry = new Entry();
                  queGGEntry.setActualEntry(grammarEntry);
                  queGGEntry.setId(grammarEntry.getId());
                  queGGEntry.setQuestions(grammarEntry.getSentences()
                                                      .stream()
                                                      .reduce((x, y) -> x + "\n" + y)
                                                      .orElse(""));
                  queGGEntry.setQuestionList(grammarEntry.getSentences());
                  queGGEntry.setSparql(queGGSparql);
                  // Only set QueGG entry if it is null, otherwise check for more properties
                  if (isNull(entryComparison.getQueGGEntry())) {
                    entryComparison.setQueGGEntry(queGGEntry);
                  } else {
                    // Overwrite QueGG entry only if the SPARQL query is longer (so probably more complex)
                    if (entryComparison.getQueGGEntry().getSparql().length() < queGGEntry.getSparql().length()) {
                      entryComparison.setQueGGEntry(queGGEntry);
                    }
                  }
                  if (!matchingEntries.contains(entryComparison)) {
                    matchingEntries.add(entryComparison);
                  }
                }
              }
            );
        }
      );

    List<EntryComparison> notMatchedQALD = qaldFile.questions.stream().parallel()
                      .filter(
                        qaldQuestion1 ->
                          matchingEntries
                            .stream().parallel()
                            .noneMatch(entryComparison -> qaldQuestion1.id.equals(entryComparison.getQaldEntry().getId())
                            )
                      )
//                      .filter(qaldQuestions -> !qaldQuestions.answertype.equals("boolean")) // removes ASK queries from result set
                      .map(qaldQuestions -> {
                        EntryComparison entryComparison = new EntryComparison();
                        String qaldQuestion = QALDImporter.getQaldQuestionString(qaldQuestions, "it");
                        String qaldSparql = qaldQuestions.query.sparql;
                        Entry qaldEntry = new Entry();
                        qaldEntry.setActualEntry(qaldQuestions);
                        qaldEntry.setId(qaldQuestions.id);
                        qaldEntry.setQuestions(qaldQuestion);
                        qaldEntry.setSparql(qaldSparql);
                        entryComparison.setQaldEntry(qaldEntry);
                        return entryComparison;
                      })
                      .collect(Collectors.toList());
    matchingEntries.addAll(notMatchedQALD);
    return sortMatches(matchingEntries);
  }

  private List<EntryComparison> sortMatches(List<EntryComparison> matchingEntries) {
    return matchingEntries.stream().parallel()
                          .sorted(Comparator.comparing(
                            entryComparison -> Integer.valueOf(entryComparison.getQaldEntry().getId())
                          ))
                          .collect(Collectors.toList());
  }

  private boolean isQaldQuestionMatchAnyQueGGSentenceInGrammarEntry(GrammarEntry queGGEntry, String qaldQuestion) {
    return queGGEntry.getSentences()
                     .stream().parallel()
                     .map(this::cleanString)
                     .map(Pattern::compile)
                     .anyMatch(
                       queGGPattern ->
                         !PatternMatchHelper.getPatternMatch(
                           cleanQALDString(qaldQuestion),
                           queGGPattern
                         ).isEmpty()
                     );
  }

    private List<Pattern> findMatchPatterns(EntryComparison entryComparison, String cleanQaldQuestion) {
        List<Pattern> patterns=new ArrayList<Pattern>();
        patterns= entryComparison.getQueGGEntry().getQuestionList()
                .stream()
                .map(this::cleanString)
                .map(Pattern::compile)
                .filter(pattern -> !PatternMatchHelper.getPatternMatch(cleanQaldQuestion, pattern)
                .isEmpty())
                .collect(Collectors.toList());
        
        for(Pattern pattern:patterns){
            System.out.println("pattern:::"+pattern);
        }
        return patterns;
    }
    
    private Boolean cosineSimilarity(EntryComparison entryComparison, String cleanQaldQuestion) {
        double cosineDistance = 0.0;
        double cosineDistancePercentage = 0.0;
        double cosineSimilarityPercentage = 0.0;

        for (String question : entryComparison.getQueGGEntry().getQuestionList()) {
            System.out.println("question::" + question);
            cosineDistance = new CosineDistance().apply(question, cleanQaldQuestion);
            cosineDistancePercentage = Math.round(cosineDistance * 100);
            cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);
            if (cosineSimilarityPercentage > 40) {
                System.out.println("cosineSimilarityPercentage");
                return true;
            }
        }
        return false;
    }
    
  
}
