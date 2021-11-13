
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.monnetproject.lemon.LemonModel;
import evaluation.EvaluateAgainstQALD;
import static evaluation.EvaluateAgainstQALD.PROTOTYPE_QUESTION;
import static evaluation.EvaluateAgainstQALD.REAL_QUESTION;
import evaluation.QALD;
import evaluation.QALDImporter;
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
import grammar.sparql.SparqlQuery;
import grammar.sparql.SPARQLRequest;
import static java.lang.System.exit;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import org.apache.commons.text.similarity.CosineDistance;
import util.io.LinkedData;
import static util.io.ResourceHelper.loadResource;

@NoArgsConstructor
public class QueGG {

    private static final Logger LOG = LogManager.getLogger(QueGG.class);
    private static String BaseDir = "";
    private static String QUESTION_ANSWER_FILE = "questions";
    private static String QUESTION_SUMMARY_FILE = "summary";
    private static String entityLabelDir = "src/main/resources/entityLabels/";
    private static Boolean externalEntittyListflag = true;
    private static String outputFileName = "grammar_FULL_DATASET";
    private static Boolean online = false;

    public static void main(String[] args) throws Exception {
        JenaSystem.init();
        QueGG queGG = new QueGG();
        String questionAnswerFile = null, questionSummaryFile, languageStr = null, outputDir = null, qaldDir = null, dataSetConfFile = null;
        Language language = null;
        LinkedData linkedData = null;
         String numberOfEntitiesString =null;
      
         try {
            if (args.length < 4) {
                throw new IllegalArgumentException(String.format("Too few parameters (%s/%s)", args.length, 3));
            }  //if (args.length >4 && args.length <6) {
            if (args.length == 4) {
                language = Language.stringToLanguage(args[0]);
                qaldDir = args[1];
                outputDir = args[2];
                dataSetConfFile = args[3];
                linkedData = FileUtils.getLinkedDataConf(new File(dataSetConfFile));
                //System.out.println("outputDir: " + outputDir + " qaldDir::" + qaldDir + " " + language + " linkedData.getEndpoint():" + linkedData.getEndpoint());
                evalution(qaldDir, outputDir, language, linkedData.getEndpoint(),EvaluateAgainstQALD.REAL_QUESTION);
            } else if (args.length == 6) {
                language = Language.stringToLanguage(args[0]);
                LOG.info("Starting {} with language parameter '{}'", QueGG.class.getName(), language);
                LOG.info("Input directory: {}", Path.of(args[1]).toString());
                LOG.info("Output directory: {}", Path.of(args[2]).toString());
                language = Language.stringToLanguage(args[0]);
                String inputDir = Path.of(args[1]).toString();
                outputDir = Path.of(args[2]).toString();
                numberOfEntitiesString = Path.of(args[3]).toString();
                //setSparqlEndpoint(endpoint);
                String fileType = args[4];
                dataSetConfFile = args[5];
                linkedData = FileUtils.getLinkedDataConf(new File(dataSetConfFile));
                setDataSet(linkedData);

                if (fileType.equals("ttl")) {
                    queGG.init(language, inputDir, outputDir);
                } else if (fileType.equals("csv")) {
                    if (TurtleCreation.generateTurtle(inputDir, linkedData, language.toString().toLowerCase())) {
                        queGG.init(language, inputDir, outputDir);
                    } else {
                        throw new Exception("no turle file is found to process!!");
                    }

                } else if (fileType.equals("qa")) {
                    Integer maxNumberOfEntities = Integer.parseInt(numberOfEntitiesString);
                    List<File> fileList = FileUtils.getFiles(outputDir + "/", outputFileName + "_" + language, ".json");
                    if (fileList.isEmpty()) {
                        throw new Exception("No files to process for question answering system!!");
                    }

                    questionAnswerFile = outputDir + File.separator + QUESTION_ANSWER_FILE + "_" + language + ".csv";
                    questionSummaryFile = outputDir + File.separator + QUESTION_SUMMARY_FILE + "_" + language + ".csv";
                    ReadAndWriteQuestions readAndWriteQuestions = new ReadAndWriteQuestions(questionAnswerFile, questionSummaryFile, maxNumberOfEntities, args[0], linkedData.getEndpoint(), online);
                    readAndWriteQuestions.readQuestionAnswers(fileList, entityLabelDir, externalEntittyListflag);

                } else {
                    throw new Exception("No file type is mentioned!!");
                }

            }

                   LOG.warn("To get optimal combinations of sentences please add the following types to {}\n{}",
                    DomainOrRangeType.class.getName(), DomainOrRangeType.MISSING_TYPES.toString()
            );
        } catch (IllegalArgumentException | IOException e) {
            System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
            System.err.printf("Usage: <%s> <input directory> <output directory>%n", Arrays.toString(Language.values()));
        }
    }

    public static void evalution(String qaldDir, String outputDir, Language language, String endpoint, String questionType) throws IOException, Exception {
        QueGG evaluateAgainstQALDTest = new QueGG();
        ObjectMapper objectMapper = new ObjectMapper();
        String[] fileList = new File(qaldDir).list();
        String queGGJson = null, queGGJsonCombined = null, qaldFile = null, qaldModifiedFile = null;
        String languageCode = language.name().toLowerCase();
        String resultFileName = qaldDir + File.separator + "QALD-QueGG-Comparison_" + languageCode + ".csv";
        String qaldRaw = qaldDir + File.separator + "QALD-2017-dataset-raw.csv";
        List<String[]> questions = new ArrayList<String[]>();
        QALDImporter qaldImporter = new QALDImporter();
        EvaluateAgainstQALD evaluateAgainstQALD = new EvaluateAgainstQALD(languageCode, endpoint);
        

        for (String fileName : new File(qaldDir).list()) {
            if (fileName.contains("qald")) {
                if (fileName.contains("train-multilingual_modified.json")) {
                    qaldModifiedFile = qaldDir + File.separator + fileName;
                } else if (fileName.contains("train-multilingual.json")) {
                    qaldFile = qaldDir + File.separator + fileName;
                }
            }
        }
        
       String string=evaluateAgainstQALD.getQaldEntities( qaldFile, qaldModifiedFile,  qaldRaw, languageCode);
       
       //temporary code for qald entity creation...
       //System.out.println(entityLabelDir+File.separator+"qaldEntities.txt");
       //FileUtils.stringToFile(string, entityLabelDir+File.separator+"qaldEntities.txt");
    
        if (questionType.contains(PROTOTYPE_QUESTION)) {
            for (String fileName : new File(outputDir).list()) {
                if (fileName.contains("grammar_FULL_DATASET") && fileName.contains(language.name())) {
                    queGGJson = outputDir + File.separator + fileName;
                } else if (fileName.contains("grammar_COMBINATIONS") && fileName.contains(language.name())) {
                    queGGJsonCombined = outputDir + File.separator + fileName;
                }

            }
            File grammarEntriesFile = new File(queGGJson);
            File grammarEntriesFile2 = new File(queGGJsonCombined);
            GrammarWrapper grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);
            GrammarWrapper gw2 = objectMapper.readValue(grammarEntriesFile2, GrammarWrapper.class);
            grammarWrapper.merge(gw2);
            evaluateAgainstQALD.evaluateAndOutput(grammarWrapper, qaldFile, qaldModifiedFile, resultFileName, qaldRaw, languageCode,questionType);

        } else if (questionType.contains(REAL_QUESTION)) {
            Map<String, String[]> queGGQuestions = new HashMap<String, String[]>();
            List<String[]> rows = new ArrayList<String[]>();
            String[] files = new File(outputDir).list();
            for (String fileName : files) {
                if (fileName.contains(".csv")) {
                    File file = new File(outputDir + File.separator + fileName);
                    CsvFile csvFile = new CsvFile(file);
                    rows = csvFile.getRows(file);
                    for (String[] row : rows) {
                        String question = row[1];
                        String cleanQuestion=question.toLowerCase().trim().strip().stripLeading().stripTrailing();
                        queGGQuestions.put(cleanQuestion, row);
                    }
                }
            }
           
            evaluateAgainstQALD.evaluateAndOutput(queGGQuestions, qaldFile, qaldModifiedFile, resultFileName, qaldRaw, languageCode, questionType);
        }

    }

    public void init(Language language, String inputDir, String outputDir) throws IOException {
        try {
            loadInputAndGenerate(language, inputDir, outputDir);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            LOG.error("Could not create grammar: {}", e.getMessage());
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
