
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
import java.io.File;
import lexicon.LexiconImporter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import util.io.CsvFile;
import util.io.FileUtils;
import util.io.TurtleCreation;
import util.io.ExecJar;






@NoArgsConstructor
public class QueGG {

    private static final Logger LOG = LogManager.getLogger(QueGG.class);
    private static String GENERATE_JSON = "generate";
    private static String CREATE_CSV = "CREATE_CSV";
    private static String BaseDir = "";
    private static String QUESTION_ANSWER_LOCATION = BaseDir + "questions/";
    //tmp location
    //private static String QUESTION_ANSWER_LOCATION =  "/tmp/";
    private static String QUESTION_ANSWER_CSV_FILE = "questions.csv";
    private static String entityLabelDir = "src/main/resources/entityLabels/";
    //private static String javaLoc="/home/elahi/grammar/hackthon/fork/QueGG-web/target/";
    private static String javaLoc="QueGG-webCopy/";
    private static String jarFile="quegg-web-0.0.1-SNAPSHOT.jar";
    

    /*public static void mainT(String[] args) throws Exception {
      queGG.callInterface(javaLoc,jarFile);
    }*/

   
    public static void main(String[] args) throws Exception {
        String search=GENERATE_JSON+CREATE_CSV;
        String questionAnswerFile = QUESTION_ANSWER_LOCATION + File.separator + QUESTION_ANSWER_CSV_FILE;

        try {
            if (args.length < 5) {
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
            String numberOfEntitiesString=Path.of(args[3]).toString();
            Integer maxNumberOfEntities=Integer.parseInt(numberOfEntitiesString);
            String fileType=args[4];
            if(fileType.contains("ttl")){
               queGG.init(language, inputDir, outputDir);
                }
            else if(fileType.contains("csv")){
               queGG.generateTurtle(inputDir);
            }
            else
              throw new Exception("No file type is mentioned!!");

                queGG.init(language, inputDir, outputDir);
                List<File> fileList = FileUtils.getFiles(outputDir+"/", "grammar_FULL_DATASET_EN", ".json");
                if (fileList.isEmpty()) {
                    throw new Exception("No files to process for question answering system!!");
                }
                ReadAndWriteQuestions readAndWriteQuestions = new ReadAndWriteQuestions(questionAnswerFile,maxNumberOfEntities);
                readAndWriteQuestions.readQuestionAnswers(fileList, entityLabelDir);

                //temporary close of QA system generation
                //ExecJar.callInterface(javaLoc,jarFile);
                //System.out.println("csv file generation successful!!");


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

    public void generateTurtle(String inputDir) throws IOException {
            //FileUtils.deleteFiles(inputDir,".ttl");
        
               String lemonEntry=null;
               File f = new File(inputDir);
               String[]pathnames = f.list();
               for (String pathname : pathnames) {
                    String[]files = new File(inputDir+File.separatorChar+pathname).list(); 

                    for (String file : files) {
                          if(!file.contains(".csv"))
                             continue;
                          CsvFile csvFile = new CsvFile();
                           System.out.println(inputDir+"/"+pathname+"/"+file);
                           String directory=inputDir+"/"+pathname+"/";
                          List<String[]> rows = csvFile.getRows(new File(directory+file));
                          Integer index = 0;


                        for (String[] row : rows) {
                            if (index == 0) {
                                index=index+1;
                                continue;
                            } 
                            TurtleCreation nounPPFrameXsl = new TurtleCreation(row);
                            FileUtils.stringToFile(nounPPFrameXsl.getTutleString(), directory + nounPPFrameXsl.getTutleFileName());
                               
                    
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

 

}
