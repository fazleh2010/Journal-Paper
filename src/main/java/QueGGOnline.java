
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.monnetproject.lemon.LemonModel;
import evaluation.EvaluateAgainstQALD;
import static evaluation.EvaluateAgainstQALD.PROTOTYPE_QUESTION;
import static evaluation.EvaluateAgainstQALD.REAL_QUESTION;
import evaluation.EvaluateAgainstQALD_1;
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
import turtle.GermanTurtle;
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
import turtle.EnglishTurtle;
import util.io.GenderUtils;
import linkeddata.LinkedData;
import turtle.ItalianTurtle;
import turtle.SpanishTurtle;
import static util.io.ResourceHelper.loadResource;
import turtle.TutleConverter;
import util.io.FileFolderUtils;
import util.io.InputCofiguration;

@NoArgsConstructor
public class QueGGOnline {

    private static final Logger LOG = LogManager.getLogger(QueGG.class);
    private static String questionsFile = "questions";
    private static String summaryFile = "summary";
    public static String propertyReport = "propertyReport";

    private static Boolean externalEntittyListflag = true;
    private static String grammar_FULL_DATASET = "grammar_FULL_DATASET";
    private static String grammar_COMBINATIONS = "grammar_COMBINATIONS";
    private static Boolean online = false;



    public static void main(String[] args) throws Exception {
        JenaSystem.init();
        QueGGOnline queGG = new QueGGOnline();
        String configFile = null, dataSetConfFile = null;        
       
        try {
            if (args.length < 2) {
                throw new IllegalArgumentException(String.format("Too few parameters (%s/%s)", args.length));
            } else if (args.length == 2) {
                configFile = args[0];
                InputCofiguration inputCofiguration = FileUtils.getInputConfig(new File(configFile));
                inputCofiguration.setLinkedData(args[1]);
                online=inputCofiguration.getOnline();
                if (inputCofiguration.isCsvToTurtle()) {
                    if (queGG.csvToProto(inputCofiguration)) {
                        queGG.turtleToProto(inputCofiguration);
                    }
                }
                if (inputCofiguration.isProtoTypeToQuestion()) {                   
                    queGG.protoToReal(inputCofiguration, grammar_FULL_DATASET, grammar_COMBINATIONS);
                }
                if (inputCofiguration.isEvalution()) {
                    Language language = inputCofiguration.getLanguage();
                    String qaldDir = inputCofiguration.getQaldDir();
                    String outputDir = inputCofiguration.getOutputDir();
                    LinkedData linkedData = inputCofiguration.getLinkedData();
                    Double similarity = inputCofiguration.getSimilarityThresold();
                    queGG.evalution(qaldDir, outputDir, language, linkedData.getEndpoint(), EvaluateAgainstQALD.REAL_QUESTION, similarity);
                }

            }

        } catch (IllegalArgumentException | IOException e) {
            System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
            System.err.printf("Usage: <%s> <input directory> <output directory>%n", Arrays.toString(Language.values()));
        }

    }

    public void evalution(String qaldDir, String outputDir, Language language, String endpoint, String questionType, Double similarityMeasure) throws IOException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String queGGJson = null, queGGJsonCombined = null, qaldFile = null, qaldModifiedFile = null;
        String languageCode = language.name().toLowerCase();
        String resultFileName = outputDir + File.separator + "QALD-QueGG-Comparison_" + languageCode + ".csv";
        String qaldRaw = outputDir + File.separator + "QALD-2017-dataset-raw.csv";
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

        //temporary code for qald entity creation...
        //System.out.println(entityLabelDir+File.separator+"qaldEntities.txt");
        //FileUtils.stringToFile(string, entityLabelDir+File.separator+"qaldEntities.txt");
        
        if (questionType.contains(PROTOTYPE_QUESTION)) {
            EvaluateAgainstQALD_1 evaluateAgainstQALD_1 = new EvaluateAgainstQALD_1(languageCode, endpoint);
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
        
            evaluateAgainstQALD_1.evaluateAndOutput(grammarWrapper, qaldFile, qaldModifiedFile, resultFileName, qaldRaw, languageCode, questionType, similarityMeasure);

        } else if (questionType.contains(REAL_QUESTION)) {
             
            Map<String, String[]> queGGQuestions = new HashMap<String, String[]>();
            List<String[]> rows = new ArrayList<String[]>();
            String[] files = new File(outputDir).list();
            for (String fileName : files) {
                if (fileName.contains(questionsFile) && fileName.contains(".csv")) {
                    File file = new File(outputDir + File.separator + fileName);
                    CsvFile csvFile = new CsvFile(file);
                    rows = csvFile.getRows(file);
                    for (String[] row : rows) {
                        String question = row[1];
                        String cleanQuestion = question.toLowerCase().trim().strip().stripLeading().stripTrailing();
                        queGGQuestions.put(cleanQuestion, row);
                    }
                }
            }

            evaluateAgainstQALD.evaluateAndOutput(queGGQuestions, qaldFile, qaldModifiedFile, resultFileName, qaldRaw, languageCode, questionType, similarityMeasure);
        }

    }

    private Boolean csvToProto(InputCofiguration inputCofiguration) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getInputDir();
        String outputDir = inputCofiguration.getOutputDir();
        Integer numberOfEntitiesString = inputCofiguration.getNumberOfEntities();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        setDataSet(linkedData);
        TutleConverter tutleConverter = null;
        FileFolderUtils.deleteFiles(inputDir, ".ttl");
        if (language.equals(Language.DE)) {
            tutleConverter = new GermanTurtle(inputDir, linkedData, language);
        } else if (language.equals(Language.EN)) {
            tutleConverter = new EnglishTurtle(inputDir, linkedData, language);
        }else if (language.equals(Language.ES)) {
            tutleConverter = new SpanishTurtle(inputDir, linkedData, language);
        }else if (language.equals(Language.IT)) {
            tutleConverter = new ItalianTurtle(inputDir, linkedData, language);
        }
        return tutleConverter.getConversionFlag();
    }

    private void turtleToProto(InputCofiguration inputCofiguration) throws IOException {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getInputDir();
        String outputDir = inputCofiguration.getOutputDir();
        this.init(language, inputDir, outputDir);
    }

    private void protoToReal(InputCofiguration inputCofiguration, String grammar_FULL_DATASET, String grammar_COMBINATIONS) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String inputDir = inputCofiguration.getOutputDir();
        String entityDir=inputCofiguration.getEntityDir();
        String questionDir=inputCofiguration.getQuestionDir();
        String classDir=inputCofiguration.getClassDir();
        Integer maxNumberOfEntities = inputCofiguration.getNumberOfEntities();
        Boolean combinedFlag=inputCofiguration.getCompositeFlag();
        Boolean singleFlag=inputCofiguration.getSingleFlag();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        List<File> protoToQuestions=new ArrayList<File>();
        setDataSet(linkedData);

        if (singleFlag) {
            protoToQuestions.addAll(FileUtils.getFiles(inputDir + "/", grammar_FULL_DATASET + "_" + language.name(), ".json"));
        }
        if (combinedFlag) {
            protoToQuestions.addAll(FileUtils.getFiles(inputDir + "/", grammar_COMBINATIONS + "_" + language.name(), ".json"));
        }

        String langCode = language.name().toLowerCase().trim();
        String questionAnswerFile = questionDir + File.separator + questionsFile + "_" + langCode + ".csv";
        String questionSummaryFile = questionDir + File.separator + summaryFile + "_" + langCode + ".csv";
        ReadAndWriteQuestions readAndWriteQuestions = new ReadAndWriteQuestions(questionAnswerFile, questionSummaryFile,maxNumberOfEntities, langCode, linkedData.getEndpoint(), online,externalEntittyListflag,entityDir,classDir,linkedData);
        
        if (online)
            readAndWriteQuestions.generateFullQuestionOnline(protoToQuestions);
        else
            readAndWriteQuestions.readQuestionAnswersOffline(protoToQuestions);

    }

    private void questionEvaluation(InputCofiguration inputCofiguration) throws Exception {
        Language language = inputCofiguration.getLanguage();
        String qaldDir = inputCofiguration.getQaldDir();
        String outputDir = inputCofiguration.getOutputDir();
        LinkedData linkedData = inputCofiguration.getLinkedData();
        Double similarityMeasure = inputCofiguration.getSimilarityThresold();
        Boolean combinedFlag=inputCofiguration.getCompositeFlag();
        evalution(qaldDir, outputDir, language, linkedData.getEndpoint(), EvaluateAgainstQALD.REAL_QUESTION, similarityMeasure);

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
