

import grammar.structure.component.Language;
import java.io.IOException;
import java.util.logging.Level;
import lombok.NoArgsConstructor;



@NoArgsConstructor
public class QueGGEnglishTest {

    private static String inputDir = "src/test/resources/lexicon/en/";
    //private static String inputDir = "src/test/resources/lexicon/en/nouns/";
    private static String outputDir = "src/test/resources/lexicon/z-output/";
    private static Language language = Language.stringToLanguage("EN");

   
    /*public static void main(String []agrs) {
        QueGG queGG = new QueGG();
        try {
            queGG.init(language, inputDir, outputDir);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(QueGGEnglishTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }*/
   
}
