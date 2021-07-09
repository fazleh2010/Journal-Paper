
import eu.monnetproject.lemon.LemonModel;
import grammar.generator.BindingResolver;
import grammar.generator.GrammarRuleGeneratorRoot;
import grammar.generator.GrammarRuleGeneratorRootImpl;
import grammar.read.questions.ReadAndWriteQuestions;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
import lexicon.LexiconImporter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.jena.sys.JenaSystem;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
import util.io.CsvFile;
import util.io.FileUtils;
import util.io.TurtleCreation;
import java.io.File;
import java.io.IOException;
import grammar.read.questions.SparqlQuery;
import grammar.sparql.SPARQLRequest;
import java.util.logging.Level;
import util.io.LinkedData;

@NoArgsConstructor
public class QueGG {

    private static final Logger LOG = LogManager.getLogger(QueGG.class);
    private static String BaseDir = "";
    private static String QUESTION_ANSWER_FILE = "questions";
    private static String QUESTION_SUMMARY_FILE = "summary";
    private static String entityLabelDir = "src/main/resources/entityLabels/";
    private static Boolean externalEntittyListflag = false;
    private static String outputFileName = "grammar_FULL_DATASET";
    //private static String srcDir = "src/main/resources/";
   // private static String endpoint = "https://dbpedia.org/sparql";

    public static void main(String[] args) throws Exception {
        JenaSystem.init();
        String questionAnswerFile = null, questionSummaryFile, languageStr = null;

        try {
            if (args.length < 6) {
                throw new IllegalArgumentException(String.format("Too few parameters (%s/%s)", args.length, 3));
            }
            QueGG queGG = new QueGG();
            Language language = Language.stringToLanguage(args[0]);
            LOG.info("Starting {} with language parameter '{}'", QueGG.class.getName(), language);
            LOG.info("Input directory: {}", Path.of(args[1]).toString());
            LOG.info("Output directory: {}", Path.of(args[2]).toString());
            language = Language.stringToLanguage(args[0]);
            String inputDir = Path.of(args[1]).toString();
            String outputDir = Path.of(args[2]).toString();
            String numberOfEntitiesString = Path.of(args[3]).toString();
            //setSparqlEndpoint(endpoint);
            Integer maxNumberOfEntities = Integer.parseInt(numberOfEntitiesString);
            String fileType = args[4];
            String dataSetConfFile=args[5];
            LinkedData linkedData=FileUtils.getLinkedDataConf(new File(dataSetConfFile));
            setDataSet(linkedData);
            
            if (fileType.contains("ttl")) {
                queGG.init(language, inputDir, outputDir);
            } else if (fileType.contains("csv")) {
                queGG.generateTurtle(inputDir,linkedData);
                queGG.init(language, inputDir, outputDir);
            } else {
                throw new Exception("No file type is mentioned!!");
            }
          

            List<File> fileList = FileUtils.getFiles(outputDir + "/", outputFileName + "_" + language, ".json");
            if (fileList.isEmpty()) {
                throw new Exception("No files to process for question answering system!!");
            }
            questionAnswerFile = outputDir + File.separator + QUESTION_ANSWER_FILE + "_" + language + ".csv";
            questionSummaryFile = outputDir + File.separator + QUESTION_SUMMARY_FILE + "_" + language + ".csv";
            ReadAndWriteQuestions readAndWriteQuestions = new ReadAndWriteQuestions(questionAnswerFile, questionSummaryFile, maxNumberOfEntities, args[0],linkedData.getEndpoint());
            readAndWriteQuestions.readQuestionAnswers(fileList, entityLabelDir, externalEntittyListflag);
            
            LOG.warn("To get optimal combinations of sentences please add the following types to {}\n{}",
                    DomainOrRangeType.class.getName(), DomainOrRangeType.MISSING_TYPES.toString()
            );
        } catch (IllegalArgumentException | IOException e) {
            System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
            System.err.printf("Usage: <%s> <input directory> <output directory>%n", Arrays.toString(Language.values()));
        }
    }

    public void init(Language language, String inputDir, String outputDir) throws IOException {
        try {
            loadInputAndGenerate(language, inputDir, outputDir);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            LOG.error("Could not create grammar: {}", e.getMessage());
        }
    }

    public void generateTurtle(String inputDir,LinkedData linkedData) throws IOException {
        String lemonEntry = null;
        File f = new File(inputDir);
        String[] pathnames = f.list();
        for (String pathname : pathnames) {
            String[] files = new File(inputDir + File.separatorChar + pathname).list();

            for (String file : files) {
                if (!file.contains(".csv")) {
                    continue;
                }
                CsvFile csvFile = new CsvFile();
                String directory = inputDir + "/" + pathname + "/";
                List<String[]> rows = csvFile.getRows(new File(directory + file));
                Integer index = 0;

                for (String[] row : rows) {
                    if (index == 0) {
                        index = index + 1;
                        continue;
                    }
                    TurtleCreation turtleCreation;
                    try {
                        turtleCreation = new TurtleCreation(row,linkedData);
                        FileUtils.stringToFile(turtleCreation.getTutleString(), directory + turtleCreation.getTutleFileName());
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(QueGG.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        }
    }

    private void loadInputAndGenerate(Language lang, String inputDir, String outputDir) throws
            IOException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        LexiconImporter lexiconImporter = new LexiconImporter();
        LemonModel lemonModel = lexiconImporter.loadModelFromDir(inputDir, lang.toString().toLowerCase());
        printInputSummary(lemonModel);
        generateByFrameType(lang, lemonModel, outputDir);
    }

    private void printInputSummary(LemonModel lemonModel) {
        lemonModel
                .getLexica()
                .forEach(
                        lexicon
                        -> {
                    LOG.info("The input lexicon contains the following grammar frames:");
                    Arrays.stream(FrameType.values()).forEach(
                            frameType -> {
                                LOG.info(
                                        "{}: {}",
                                        frameType.getName(),
                                        // count of elements that have that frame
                                        lexicon.getEntrys()
                                                .stream()
                                                .filter(lexicalEntry
                                                        -> lexicalEntry.getSynBehaviors()
                                                        .stream()
                                                        .anyMatch(frame
                                                                -> frame.getTypes()
                                                                .stream()
                                                                .anyMatch(
                                                                        uri -> uri.getFragment().equals(frameType.getName())
                                                                )
                                                        )
                                                )
                                                .count()
                                );
                            });
                }
                );
    }

    private void generateByFrameType(Language language, LemonModel lemonModel, String outputDir) throws
            IOException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        GrammarWrapper grammarWrapper = new GrammarWrapper();
        for (FrameType frameType : FrameType.values()) {
            if (!isNull(frameType.getImplementingClass())) {
                GrammarWrapper gw = generateGrammarGeneric(
                        lemonModel,
                        (GrammarRuleGeneratorRoot) frameType.getImplementingClass()
                                .getDeclaredConstructor(Language.class)
                                .newInstance(language)
                );
                grammarWrapper.merge(gw);
            }
        }
        // Make a GrammarRuleGeneratorRoot instance to use the combination function
        GrammarRuleGeneratorRoot generatorRoot = new GrammarRuleGeneratorRootImpl(language);
        LOG.info("Start generation of combined entries");
        grammarWrapper.getGrammarEntries().addAll(generatorRoot.generateCombinations(grammarWrapper.getGrammarEntries()));

        for (GrammarEntry grammarEntry : grammarWrapper.getGrammarEntries()) {
            //System.out.println("grammarEntry::"+grammarEntry);
            grammarEntry.setId(String.valueOf(grammarWrapper.getGrammarEntries().indexOf(grammarEntry) + 1));
        }

        // Output file is too big, make two files
        GrammarWrapper regularEntries = new GrammarWrapper();
        regularEntries.setGrammarEntries(
                grammarWrapper.getGrammarEntries()
                        .stream()
                        .filter(grammarEntry -> !grammarEntry.isCombination())
                        .collect(Collectors.toList())
        );
        GrammarWrapper combinedEntries = new GrammarWrapper();
        combinedEntries.setGrammarEntries(
                grammarWrapper.getGrammarEntries().stream().filter(GrammarEntry::isCombination).collect(Collectors.toList())
        );

        // Generate bindings
        LOG.info("Start generation of bindings");
        grammarWrapper.getGrammarEntries().forEach(generatorRoot::generateBindings);

        generatorRoot.dumpToJSON(
                Path.of(
                        outputDir,
                        "grammar_" + generatorRoot.getFrameType().getName() + "_" + language + ".json"
                ).toString(),
                regularEntries
        );
        generatorRoot.dumpToJSON(
                Path.of(outputDir, "grammar_COMBINATIONS" + "_" + language + ".json").toString(),
                combinedEntries
        );

        // Insert those bindings and write new files
        LOG.info("Start resolving bindings");
        BindingResolver bindingResolver = new BindingResolver(grammarWrapper.getGrammarEntries());
        grammarWrapper = bindingResolver.resolve();
        generatorRoot.dumpToJSON(
                Path.of(outputDir, "grammar_FULL_WITH_BINDINGS_" + language + ".json").toString(),
                grammarWrapper
        );

    }

    private GrammarWrapper generateGrammarGeneric(LemonModel lemonModel, GrammarRuleGeneratorRoot grammarRuleGenerator) {
        GrammarWrapper grammarWrapper = new GrammarWrapper();
        lemonModel.getLexica().forEach(lexicon -> {
            LOG.info("Start generation for FrameType {}", grammarRuleGenerator.getFrameType().getName());
            grammarRuleGenerator.setLexicon(lexicon);
            grammarWrapper.setGrammarEntries(grammarRuleGenerator.generate(lexicon));
        });
        return grammarWrapper;
    }

    private static void setDataSet(LinkedData linkedData) throws Exception {
        String endpoint = linkedData.getEndpoint();
        if (linkedData.getEndpoint().contains("dbpedia")) {
            SPARQLRequest.setEndpoint(endpoint);
        }
        GrammarRuleGeneratorRoot.setEndpoint(endpoint);

    }

}
