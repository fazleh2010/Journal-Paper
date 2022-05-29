/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation;

import java.io.IOException;
import static java.lang.System.exit;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author elahi
 */
public class Writer {
        private static final Logger LOG = LogManager.getLogger(Writer.class);

    
     public static void writeResult(QALDImporter qaldImporter, QALD qaldOriginal, EvaluationResult result, String resultFileName, String languageCode) throws IOException {
        ZonedDateTime before = ZonedDateTime.now();
        ZonedDateTime after = ZonedDateTime.now();
        long duration = Duration.between(before, after).toSeconds();
        List<String[]> dataLines = resultToPrintableList(result, qaldOriginal, languageCode);
        qaldImporter.writeToCSV(dataLines, resultFileName);
        LOG.info(String.format("Evaluation was completed in %dmin %ds", duration / 60, duration % 60));
        LOG.info("Results are available here: " + resultFileName);
    }
     
      public static List<String[]> resultToPrintableList(EvaluationResult result, QALD qaldOriginal,String languageCode){
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{
            "QALD id",
            "match status",
            "QALD question",
            "QueGG question",
            "QALD SPARQL query",
            "QueGG SPARQL query",
            "TP",
            "FP",
            "FN",
            "Precision",
            "Recall",
            "F-Measure"
        });
        int numberOfQueGGMatches = 0;
        for (EntryComparison entryComparison : result.getEntryComparisons()) {   
            QALD.QALDQuestions QALDQuestions=Matcher.getMatchingOriginalQaldQuestions(qaldOriginal, entryComparison);   
            String qaldQuestion=entryComparison.getQaldEntry().getQuestions();
            String qaldSparql=entryComparison.getQaldEntry().getSparql();
            
            /*if (entryComparison.getMatchedFlag()) {
                System.out.println("qaldQuestion::::" + qaldQuestion);
                System.out.println("getQueGGEntry().getQuestions()::::" + entryComparison.getQueGGEntry().getQuestions());
                System.out.println("qaldQuestion::::" + qaldQuestion);
                System.out.println("qaldQuestion::::" + entryComparison.getQueGGEntry().getSparql());
            }*/
           
            //System.out.println("qaldSparql::::"+qaldSparql);
                         
                    
            list.add(
                    new String[]{
                        entryComparison.getQaldEntry().getId(),
                        entryComparison.getSimilarityCsore().toString(),
                        qaldQuestion,
                        entryComparison.getQueGGEntry().getQuestions(),
                        qaldSparql,
                        !isNull(entryComparison.getQueGGEntry())
                        ? entryComparison.getQueGGEntry().getSparql()
                        : "", // entryComparison.getQueGGEntries().stream().filter(entry -> entry.).getSentences().stream().reduce((x, y) -> x + "\n" + y).orElse(""),
                        String.valueOf(entryComparison.getTp()),
                        String.valueOf(entryComparison.getFp()),
                        String.valueOf(entryComparison.getFn()),
                        String.valueOf(entryComparison.getPrecision()),
                        String.valueOf(entryComparison.getRecall()),
                        String.valueOf(entryComparison.getF_measure())
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
            "", // "match status",
            "", // "QALD original question",
            "", // "QALD original SPARQL query",
            "", // "QALD reformulated question",
            "", // "QueGG SPARQL query",
            String.valueOf(result.getTp_global()), // "TP",
            String.valueOf(result.getFp_global()), // "FP",
            String.valueOf(result.getFn_global()), // "FN",
            String.valueOf(result.getPrecision_global()), // "Precision",
            String.valueOf(result.getRecall_global()), // "Recall",
            String.valueOf(result.getF_measure_global())
        });
        LOG.info(String.format("Total matches: %d", numberOfQueGGMatches));
        LOG.info(String.format(
                "QALD coverage: %.2f%%",
                (float) (numberOfQueGGMatches) / (float) qaldOriginal.questions.size() * 100
        ));

        return list;
    }
    
}
