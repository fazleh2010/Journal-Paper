package evaluation;


import evaluation.EntryComparison;
import evaluation.EvaluationResult;
import evaluation.QALD;
import evaluation.QALD.QALDQuestions;
import evaluation.QALDImporter;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class EvalutionUtils {
    
     private static final Logger LOG = LogManager.getLogger(EvalutionUtils.class);

    public static List<String[]> resultToPrintableList(EvaluationResult result, QALD qaldOriginal,String languageCode){
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
            
            QALDQuestions QALDQuestions=getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison);
                        
            String qaldQuestion=QALDImporter.getQaldQuestionString(
                                getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison),
                                languageCode
                        );
            String qaldSparql=getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison).query.sparql;
            System.out.println(qaldSparql+"..!!!!.."+qaldQuestion);
          
            list.add(
                    new String[]{
                        entryComparison.getQaldEntry().getId(),
                        qaldQuestion,
                        qaldSparql,
                        entryComparison.getQaldEntry().getQuestions(),
                        !isNull(entryComparison.getQueGGEntry())
                        ? entryComparison.getQueGGEntry().getSparql()
                        : "", // entryComparison.getQueGGEntries().stream().filter(entry -> entry.).getSentences().stream().reduce((x, y) -> x + "\n" + y).orElse(""),
                        String.valueOf(entryComparison.getTp()),
                        String.valueOf(entryComparison.getFp()),
                        String.valueOf(entryComparison.getFn()),
                        String.valueOf(entryComparison.getPrecision()),
                        String.valueOf(entryComparison.getRecall()),
                        String.valueOf(entryComparison.getF_measure()),
                        /*String.valueOf(checkReformulated(QALDImporter.getQaldQuestionString(getMatchingOriginalQaldQuestions(
                                qaldOriginal,
                                entryComparison
                        ), languageCode), entryComparison.getQaldEntry().getQuestions()))*/
                        "modified question"
                    }
            );
            if (!isNull(entryComparison.getQueGGEntry())
                    && !isNull(entryComparison.getQueGGEntry().getSparql())
                    && !entryComparison.getQueGGEntry().getSparql().equals("")) {
                numberOfQueGGMatches++;
            }
        }
        list.add(new String[]{
            "", // "QALD id",
            "", // "QALD original question",
            "", // "QALD original SPARQL query",
            "", // "QALD reformulated question",
            "", // "QueGG SPARQL query",
            String.valueOf(result.getTp_global()), // "TP",
            String.valueOf(result.getFp_global()), // "FP",
            String.valueOf(result.getFn_global()), // "FN",
            String.valueOf(result.getPrecision_global()), // "Precision",
            String.valueOf(result.getRecall_global()), // "Recall",
            String.valueOf(result.getF_measure_global()), // "F-Measure",
            "", // "Reformulated?"
        });
        LOG.info(String.format("Total matches: %d", numberOfQueGGMatches));
        LOG.info(String.format(
                "QALD coverage: %.2f%%",
                (float) (numberOfQueGGMatches) / (float) qaldOriginal.questions.size() * 100
        ));

        return list;
    }

    private static QALD.QALDQuestions getMatchingOriginalQaldQuestions(QALD qaldOriginal, EntryComparison entryComparison) {
        return qaldOriginal.questions.stream()
                .filter(qaldQuestions -> qaldQuestions.id.equals(entryComparison.getQaldEntry()
                .getId()))
                .findAny()
                .orElseThrow();
    }
    
    private static boolean checkReformulated(String qaldQuestionString, String questions) {
    return !qaldQuestionString.equals(questions);
  }

}
