

import grammar.structure.component.Language;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.logging.Level;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QueGGGermanTest {

    private static String inputDir = "src/test/resources/lexicon/input/";
    private static String outputDir = "src/test/resources/lexicon/output/";
    private static Language language = Language.stringToLanguage("DE");

   
    /*public static void main(String []agrs) {
        QueGG queGG = new QueGG();
        try {
            queGG.init(language, inputDir, outputDir);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(QueGGGermanTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }*/
}
