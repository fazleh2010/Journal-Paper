import com.fasterxml.jackson.databind.ObjectMapper;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static util.io.ResourceHelper.loadResource;

class EvaluateAgainstQALDTest {

    /*private static final EvaluateAgainstQALD evaluateAgainstQALD = new EvaluateAgainstQALD(Language.EN);
    private GrammarWrapper grammarWrapper;
    private static final String RESOURCE = "/home/elahi/AHack/italian/question-grammar-generator/src/main/resources/QALD-2017/";
    private static final String DATASET_EN_JSON =RESOURCE +"grammar_FULL_DATASET_IT.json";
    private static final String COMBINATIONS_EN_JSON = RESOURCE +"grammar_COMBINATIONS_IT.json";
    public static final String QALD_FILE = RESOURCE + "QALD-2017/qald-7-train-multilingual.json";
    public static final String QALD_FILE_MODIFIED = RESOURCE + "QALD-2017/qald-7-train-multilingual_modified.json";
    */


    /*@BeforeEach
    void init() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL grammarEntriesFile = loadResource("grammar_FULL_DATASET_EN.json", this.getClass());
        URL grammarEntriesFile2 = loadResource("grammar_COMBINATIONS_EN.json", this.getClass());
        grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);
        GrammarWrapper gw2 = objectMapper.readValue(grammarEntriesFile2, GrammarWrapper.class);
        grammarWrapper.merge(gw2);
    }*/
    /**
     * This does not actually test anything it is only there to generate the
     * evaluation files.
     */
    /*@Test
    void testEvaluateAndOutput() throws IOException {
        evaluateAgainstQALD.evaluateAndOutput(grammarWrapper);
    }*/
    /*public static void main(String[] args) throws IOException {
        EvaluateAgainstQALDTest evaluateAgainstQALDTest=new EvaluateAgainstQALDTest();
        ObjectMapper objectMapper = new ObjectMapper();
        URL grammarEntriesFile = EvaluateAgainstQALD.loadResource(DATASET_EN_JSON, EvaluateAgainstQALDTest.class);
        URL grammarEntriesFile2 = loadResource(COMBINATIONS_EN_JSON, EvaluateAgainstQALDTest.class);
        evaluateAgainstQALDTest.grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);
        GrammarWrapper gw2 = objectMapper.readValue(grammarEntriesFile2, GrammarWrapper.class);
        evaluateAgainstQALDTest.grammarWrapper.merge(gw2);
        evaluateAgainstQALD.evaluateAndOutput(evaluateAgainstQALDTest.grammarWrapper);

    }*/
    
     //private static final String DATASET_EN_JSON ="/home/elahi/AHack/italian/question-grammar-generator/json/grammar_FULL_DATASET_IT.json";
    //private static final String COMBINATIONS_EN_JSON = "/home/elahi/AHack/italian/question-grammar-generator/json/grammar_COMBINATIONS_IT.json";
   
    
   
    
     //private static String srcDir = "src/main/resources/";
   // private static String endpoint = "https://dbpedia.org/sparql";
    
   
    
    /*private static String gravityCambridge = "the force that makes objects fall toward the earth, or toward some other large object such as a planet or a star";
    private static String gravityNasa = " the force by which a planet or other body draws objects toward its center";

    public static void main(String[] args) {

        String[][] inputStrings = new String[][]{
            // No similarity
            {"Its All Binary", "Java is great"},
            // One out of three word similar
            {"Hi All Its All Binary", "Hi Binary"},
            // Three out of 4 words similar
            {"Its All Binary", "Really Its All Binary"},
            // Completely exact similar
            {"Its All Binary", "Its All Binary"},
            // Completely exact similar but different sequence
            {"Its All Binary", "All Binary Its"},
            // Different case of same string. Its case sensitive.
            {"Its All Binary", "iTS aLL bINARY"}};

        for (String[] input : inputStrings) {

            // How dis-similar words are between both strings.
            double cosineDistance = new CosineDistance().apply(input[0], input[1]);
            double cosineDistancePercentage = Math.round(cosineDistance * 100);
            double cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);

            if(cosineSimilarityPercentage>50)
            System.out.println("CosineDistance of '" + input[0] + "' & '" + input[1] + "' | Words in strings are "
                    + cosineDistancePercentage + "% dis-similar or " + cosineSimilarityPercentage + "% similar.");
        }

        // Realistic example to match two documents & find hwo much similar they are
        double cosineDistanceOfGravitDefinitions = new CosineDistance().apply(gravityNasa, gravityCambridge);
        System.out.println("Gravity definitions from Nasa Website & Cambdrige Dictionary are "
                + Math.round((1 - cosineDistanceOfGravitDefinitions) * 100) + "% similar.");
    }*/
    
    
}