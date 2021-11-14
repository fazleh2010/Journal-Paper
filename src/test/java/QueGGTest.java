
import eu.monnetproject.lemon.LemonModel;
import grammar.generator.BindingResolver;
import grammar.generator.GrammarRuleGeneratorRoot;
import grammar.generator.GrammarRuleGeneratorRootImpl;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
import lexicon.LexiconImporter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import java.util.logging.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@NoArgsConstructor
public class QueGGTest {

    private static String inputDir = "src/test/resources/lexicon/input/";
    private static String outputDir = "src/test/resources/";
    private static Language language = Language.stringToLanguage("EN");

   
    public static void main(String []agrs) {
        QueGG queGG = new QueGG();
        try {
            queGG.init(language, inputDir, outputDir);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(QueGGTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
