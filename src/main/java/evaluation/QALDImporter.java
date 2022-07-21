package evaluation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import evaluation.QALD.QALDQuestions;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import util.io.LcQuad;
import util.io.LcQuadElement;

public class QALDImporter {
    private static final Logger LOG = LogManager.getLogger(QALDImporter.class);

    public QALDImporter() {
    }

    public void qaldToCSV(String qaldFile, String outputFile, String languageCode) throws IOException {
        QALD qald = null;
        if (qaldFile.contains("qald")) {
            qald = readQald(qaldFile);
        } else {
            qald = lcQuad(qaldFile);
        }
        writeToCSV(qaldJsonToCSVTemplate(qald, languageCode), outputFile);
    }

    public QALD readQald(String qaldFile) throws IOException {
        if (qaldFile.contains("qald")) {
            return new ObjectMapper().readValue(new File(qaldFile), QALD.class);
        } else {
            return lcQuad(qaldFile);
        }
    }

    public QALD lcQuad(String file) throws IOException {
        LcQuad lcQuad=new LcQuad(file);
        return  new QALD(lcQuad.getDatasetName(),lcQuad.getLcQuadElements());
    }

    public void writeToCSV(List<String[]> dataLines, String fileName) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(fileName), '\t', '"', '"', "\n");
        dataLines.forEach(writer::writeNext);
        writer.close();
    }

    private List<String[]> qaldJsonToCSVTemplate(QALD qaldFile, String languageCode) {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"id", "answertype", "question", "sparql"});
        qaldFile.questions.forEach(qaldQuestions -> {
            list.add(new String[]{qaldQuestions.id, qaldQuestions.answertype, getQaldQuestionString(qaldQuestions,
                languageCode
                ), qaldQuestions.query.sparql});
        });
        return list;
    }

    public static String getQaldQuestionString(QALD.QALDQuestions qaldQuestions, String languageAbbreviation) {
        return qaldQuestions.question.stream()
                .filter(qaldQuestion -> qaldQuestion.language.startsWith(languageAbbreviation))
                .findFirst()
                .orElseThrow().string;
    }

    public static String getQaldSparqlQuery(QALD.QALDQuestions qaldQuestions) {
        String sparql = qaldQuestions.query.sparql;
        return sparql;

    }

    public static void main(String[] args) throws IOException {

        QALDImporter qaldImporter = new QALDImporter();

    }

}
