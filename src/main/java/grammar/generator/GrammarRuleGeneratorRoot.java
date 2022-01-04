package grammar.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.monnetproject.lemon.model.Frame;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalSense;
import eu.monnetproject.lemon.model.Lexicon;
import grammar.datasets.sentencetemplates.SentenceTemplateFactory;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import static grammar.datasets.sentencetemplates.TempConstants.superlativePerson;
import grammar.sparql.SparqlQuery;
import grammar.sparql.Prefix;
import grammar.sparql.PrepareSparqlQuery;
import grammar.sparql.SPARQLRequest;
import grammar.sparql.SelectVariable;
import grammar.structure.component.Binding;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceBindings;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryType;
import org.apache.jena.vocabulary.RDFS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;
import util.exceptions.QueGGMissingFieldDeclarationException;
import util.sparql.RequestCompiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static grammar.sparql.SPARQLRequest.DEFAULT_LIMIT;
import static java.util.Objects.isNull;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import grammar.generator.sentencebuilder.TemplateFinder;
import static grammar.sparql.SparqlQuery.VARIABLE;
import static java.lang.System.exit;
import static grammar.datasets.sentencetemplates.TempConstants.superlativeCountry;
import static grammar.datasets.sentencetemplates.TempConstants.superlativeLocation;

@Getter
@Setter
public abstract class GrammarRuleGeneratorRoot implements GrammarRuleGenerator {

    private static final Logger LOG = LogManager.getLogger(GrammarRuleGeneratorRoot.class);
    private static String endpoint;


    /**
     * The frameType is the name of the LexInfo frame that the generated
     * sentence will be based on.
     */
    final FrameType frameType;
    final Language language;
    /**
     * The bindingValue is the variable that will be used for bindings in a
     * sentence and for the bindings list (i.e. "$x").
     */
    private final String bindingVariable;
    private Lexicon lexicon;

    private final SentenceTemplateRepository sentenceTemplateRepository;
    private final SentenceTemplateParser sentenceTemplateParser;

    @SneakyThrows
    public GrammarRuleGeneratorRoot(FrameType frameType, Language language, String bindingVariable) {
        this.frameType = frameType;
        this.language = language;
        this.bindingVariable = bindingVariable;
        this.sentenceTemplateRepository = new SentenceTemplateFactory(this.language).init();
        this.sentenceTemplateParser = new SentenceTemplateParser(this.language, this.sentenceTemplateRepository);
    }

    public List<GrammarEntry> generateCombinations(List<GrammarEntry> grammarEntries) {
        GrammarEntryCombinationFactory grammarEntryCombinationFactory = new GrammarEntryCombinationFactory();
        return grammarEntryCombinationFactory.generateCombinations(grammarEntries, bindingVariable);
    }

    /**
     * Generates the SPARQL query that will later be saved as part of the
     * grammar rule. <br>
     * For a sentence like "What is the capital of $x?": <br>      {@code
   * select distinct ?y where {
     * ?x dbo:capital ?y .
     * }
     * }
     *
     * @param lexicalEntryUtil a utility class for a specific combination of
     * LexicalEntry, LexicalSense and FrameType
     * @return a SPARQLRequest that contains a full SPARQL query string.
     */
    @Override
    public SPARQLRequest generateSPARQL(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFieldDeclarationException {
        return createLabelAndUriForPropertyRequest(lexicalEntryUtil);
    }

    /**
     * Generates a list of sentences that will later be saved as part of the
     * grammar rule. For example: ["What is the capital of $x?"]
     *
     * @return a list of possible sentences
     */
    @Override
    public abstract List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException;

    @Override
    public List<GrammarEntry> generateFragmentEntry(
            GrammarEntry grammarEntry,
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        return null;
    }

    @Override
    public void generateBindings(GrammarEntry grammarEntry) {
        if (endpoint.contains("dbpedia")) {
            generateBindingsDBpedia(grammarEntry);
        } else  {
            generateBindingsFromDataSet(grammarEntry);
        }
    }

    private void generateBindingsDBpedia(GrammarEntry grammarEntry) {
        SentenceBindings newSentenceBindings = new SentenceBindings();
        newSentenceBindings.setBindingVariableName(grammarEntry.getSentenceBindings().getBindingVariableName());
        List<Binding> bindingList = new ArrayList<Binding>();
        Query bindingQuery = RequestCompiler.compileBindingQuery(grammarEntry);
        if ((isNull(bindingQuery.getQueryPattern())
                || bindingQuery.getQueryPattern().toString().isEmpty())
                && newSentenceBindings.getBindingVariableName().equals(BindingConstants.NONE)) {
            // This will happen for all attributive adjective entries, so they are ignored here!
            // It will also happen to all combinations with AA frame
            if (!grammarEntry.getFrameType().equals(FrameType.AA)
                    && !newSentenceBindings.getBindingVariableName().equals(BindingConstants.NONE)) {
                LOG.warn("generateBindings failed for GrammarEntry with SPARQL Query {}", grammarEntry.getSparqlQuery());
            }
        } else {
            // limited to 1000 distinct bindings
            if (grammarEntry.getQueryType().equals(QueryType.ASK)) {
                bindingList = new ArrayList<Binding>();
            } else {
                /*SPARQLRequest sparqlRequest = getBindingSparqlRequest(grammarEntry, bindingQuery);
                List<Map<String, String>> sparqlSelectResultList = sparqlRequest.getSparqlSelectResultList();
                bindingList = makeBindingsFromSPARQLRequestResult(sparqlSelectResultList);*/
            }


        }
        newSentenceBindings.setBindingList(bindingList);
        grammarEntry.setSentenceBindings(newSentenceBindings);
    }

    private void generateBindingsFromDataSet(GrammarEntry grammarEntry) {
        //System.out.println("Inside into Wikidata!!!!");
        SentenceBindings newSentenceBindings = new SentenceBindings();
        newSentenceBindings.setBindingVariableName(grammarEntry.getSentenceBindings().getBindingVariableName());
        List<Binding> bindingList = new ArrayList<>();
        Query bindingQuery = RequestCompiler.compileBindingQuery(grammarEntry);
        if ((isNull(bindingQuery.getQueryPattern())
                || bindingQuery.getQueryPattern().toString().isEmpty())
                && newSentenceBindings.getBindingVariableName().equals(BindingConstants.NONE)) {
            // This will happen for all attributive adjective entries, so they are ignored here!
            // It will also happen to all combinations with AA frame
            if (!grammarEntry.getFrameType().equals(FrameType.AA)
                    && !newSentenceBindings.getBindingVariableName().equals(BindingConstants.NONE)) {
                LOG.warn("generateBindings failed for GrammarEntry with SPARQL Query {}", grammarEntry.getSparqlQuery());
            }
        } else {
            // limited to 1000 distinct bindings
            SPARQLRequest sparqlRequest = getBindingSparqlRequest(grammarEntry, bindingQuery);
            SPARQLRequest.setEndpoint(endpoint);
            SparqlQuery sparqlQuery = new SparqlQuery(bindingQuery.toString());
            bindingList  = sparqlQuery.getBindingList();
            //System.out.println(bindingList);
        }
        newSentenceBindings.setBindingList(bindingList);
        grammarEntry.setSentenceBindings(newSentenceBindings);
    }

    @Override
    public void dumpToJSON(String fileName, GrammarWrapper grammarWrapper) throws IOException {
        //ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        objectMapper.writeValue(new File(fileName), grammarWrapper);
        LOG.info("New file created: {}", fileName);
    }

    /**
     * Returns the whole grammar rule object for this GrammarRuleGenerator.
     *
     * @return a list of GrammarEntries for the specified lexicon, based on
     * GrammarRuleGenerator's language.
     */
    @Override
    public List<GrammarEntry> generate(Lexicon lexicon) {
        List<GrammarEntry> grammarEntries = new ArrayList<>();

        // filter lexicon for correct grammar type:
        List<LexicalEntry> entriesFilteredByLanguageAndGrammarType = getEntriesFilteredByGrammarType(lexicon);
        try {

            for (LexicalEntry lexicalEntry : entriesFilteredByLanguageAndGrammarType) {

                for (LexicalSense lexicalSense : lexicalEntry.getSenses()) {
                    LexicalEntryUtil lexicalEntryUtil = new LexicalEntryUtil(lexicon, lexicalEntry, frameType, lexicalSense);
                    // the subject or object of this lexical entry's reference property that is bound to the subject of this frame
                    SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
                    GrammarEntry grammarEntry = new GrammarEntry();
                    grammarEntry.setFrameType(frameType);
                    grammarEntry.setLanguage(getLanguage());
                    grammarEntry.setLexicalEntryUri(lexicalEntryUtil.getLexicalEntry().getURI());

                    // generate SPARQL query
                    grammarEntry.setQueryType(QueryType.SELECT);
                    SPARQLRequest sparqlRequest = generateSPARQL(lexicalEntryUtil);
                    String sparql=sparqlRequest.toString();
                    grammarEntry.setSparqlQuery(sparqlRequest.toString());
                    //System.out.println(grammarEntry);
                    
                    if (grammarEntry.getFrameType().equals(FrameType.AG)) {
                        //if (grammarEntry.getSentenceTemplate().contains(superlative)) {
                        String domain = LexicalEntryUtil.getDomain(lexicalEntryUtil);
                        String range = LexicalEntryUtil.getRange(lexicalEntryUtil);
                        String reference = lexicalEntryUtil.getOlisRestriction().getProperty();
                        sparql=this.generateSparql(reference);
                        grammarEntry.setSparqlQuery(sparql);
                        String executableSparql = generateExecutableSparql(lexicalEntryUtil, grammarEntry.getFrameType());
                        
                        //System.out.println(executableSparql);
                        //exit(1);
                        if(executableSparql!=null)
                           grammarEntry.setExecutable(executableSparql);
                        else
                            grammarEntry.setExecutable(sparql);
                       
                       //System.out.println(executableSparql);
                       //exit(1);

                        //String sparqlBinding=this.generateBindingQueryL(lexicalEntryUtil,DEFAULT_LIMIT);
                        grammarEntry.setBindingListType(domain);
                        grammarEntry.setReturnVariable(SparqlQuery.RETURN_TYPE_OBJECT);
                        //grammarEntry.setSparqlQuery(executableSparql);
                        // }
                    }
                    else 
                          grammarEntry.setReturnVariable(selectVariable.getVariableName());

                    // set return variable
                  

                    // generate bindings for result sentence
                    SentenceBindings sentenceBindings = new SentenceBindings();
                    sentenceBindings.setBindingVariableName(getBindingVariable()); // maybe retrieve from sentence generation

                    Map<String, String> sparqlParameterMapping = new HashMap<>();
                    sparqlParameterMapping.put(
                            this.bindingVariable,
                            LexicalEntryUtil.getOppositeSelectVariable(selectVariable).getVariableName()
                    );
                    grammarEntry.setSentenceToSparqlParameterMapping(sparqlParameterMapping);

                    grammarEntry.setSentenceBindings(sentenceBindings);

                    // generate sentences
                    List<String> sentences = generateSentences(lexicalEntryUtil);
                    grammarEntry.setSentences(sentences);

                    grammarEntry.setReturnType(DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                            selectVariable
                    )));
                    grammarEntry.setBindingType(DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                            LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                    )));

                    grammarEntry.setType(SentenceType.SENTENCE);

                    // generate a fragment, i.e. a single NP
                    List<GrammarEntry> fragmentEntries = generateFragmentEntry(
                            grammarEntry,
                            lexicalEntryUtil
                    );

                    grammarEntries.add(grammarEntry);
                    if (!fragmentEntries.isEmpty()) {
                        grammarEntries.addAll(fragmentEntries);
                    }
                }
            }
        } catch (QueGGMissingFieldDeclarationException | QueGGMissingFactoryClassException e) {
            LOG.error(e.getMessage());
        }
        return grammarEntries;
    }

    private SPARQLRequest createLabelAndUriForPropertyRequest(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFieldDeclarationException {
        SPARQLRequest sparqlRequest = new SPARQLRequest();
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(selectVariable);

        sparqlRequest.setSelectVariable(selectVariable);
        sparqlRequest.setSearchProperty(lexicalEntryUtil.getReferenceUri());
        sparqlRequest.addGenericTriple();
        sparqlRequest.createQuery();

        return sparqlRequest;
    }

    /**
     * An example query:<br>      {@code
   * SELECT  ?subjOfProp ?label
     * WHERE
     * { ?subjOfProp  <http://dbpedia.org/ontology/nationality>  ?objOfProp
     * OPTIONAL
     * { ?subjOfProp  <http://www.w3.org/2000/01/rdf-schema#label>  ?label
     * FILTER langMatches(lang(?label), "EN")
     * }
     * }
     * }
     */
    private SPARQLRequest getBindingSparqlRequest(GrammarEntry grammarEntry, Query bindingQuery) {
        SPARQLRequest sparqlRequest = new SPARQLRequest();
        bindingQuery = sparqlRequest.addOptionalLabelQueryWithFilter(
                getLanguage(),
                grammarEntry.getBindingVariable(),
                bindingQuery
        );
        bindingQuery.setDistinct(true);
        bindingQuery.setLimit(DEFAULT_LIMIT);
        sparqlRequest.setParameterizedSparqlString(new ParameterizedSparqlString(bindingQuery.toString()));
        return sparqlRequest;
    }

    private List<Binding> makeBindingsFromSPARQLRequestResult(List<Map<String, String>> sparqlSelectResultList) {
        List<Binding> bindingsList = new ArrayList<>();
        for (Map<String, String> sparqlSelectResult : sparqlSelectResultList) {
            Binding binding = new Binding();
            for (String key : sparqlSelectResult.keySet()) {
                if (key.equals(RDFS.label.getLocalName())) {
                    binding.setLabel(sparqlSelectResult.get(key));
                } else {
                    binding.setUri(sparqlSelectResult.get(key));
                }
            }
            // If optional label was empty then the uri is probably a literal or number
            if (isNull(binding.getLabel()) || binding.getLabel().isEmpty()) {
                binding.setLabel(binding.getUri());
            }
            bindingsList.add(binding);
        }
        return bindingsList;
    }

    protected List<LexicalEntry> getEntriesFilteredByGrammarType(Lexicon lexicon) {
        List<LexicalEntry> list = new ArrayList<>();
        for (LexicalEntry entry : lexicon.getEntrys()) {
            for (Frame frame : entry.getSynBehaviors()) {
                frame.getTypes().stream()
                        // will probably still only be one entry!
                        .filter(uri -> uri.toString().equals(Prefix.LEXINFO.getUri() + frameType.getName()))
                        .forEach(uri -> list.add(entry));
            }
        }
        return list;
    }

    protected GrammarEntry copyGrammarEntry(GrammarEntry grammarEntry) {
        GrammarEntry fragmentEntry = new GrammarEntry();
        fragmentEntry.setLexicalEntryUri(grammarEntry.getLexicalEntryUri());
        fragmentEntry.setBindingType(grammarEntry.getBindingType());
        fragmentEntry.setReturnType(grammarEntry.getReturnType());
        fragmentEntry.setLanguage(grammarEntry.getLanguage());
        fragmentEntry.setFrameType(grammarEntry.getFrameType());
        fragmentEntry.setSentenceBindings(grammarEntry.getSentenceBindings());
        fragmentEntry.setQueryType(grammarEntry.getQueryType());
        fragmentEntry.setSparqlQuery(grammarEntry.getSparqlQuery());
        fragmentEntry.setReturnVariable(grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceToSparqlParameterMapping(grammarEntry.getSentenceToSparqlParameterMapping());
        return fragmentEntry;
    }

    private List<String> modify(List<String> sentences) {
        List<String> firstWordSentenceCaseSentences = new ArrayList<String>();
        for (String sentence : sentences) {
            String modifySentence = Stream.of(sentence.trim().split("\\s"))
                    .filter(word -> word.length() > 0)
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));
            firstWordSentenceCaseSentences.add(modifySentence);
        }
        return firstWordSentenceCaseSentences;
    }
   
    public static void setEndpoint(String endpointT) {
        endpoint = endpointT;
    }

    private String generateExecutableSparql(LexicalEntryUtil lexicalEntryUtil, FrameType frameType) {
        String domain = LexicalEntryUtil.getDomain(lexicalEntryUtil);
        String range = LexicalEntryUtil.getRange(lexicalEntryUtil);
        String reference = lexicalEntryUtil.getOlisRestriction().getProperty();
        String sparql=null;
       

        TemplateFinder templateFinder = new TemplateFinder(lexicalEntryUtil, frameType);
        String sentenceTemplate=templateFinder.getSelectedTemplate();
        String propertyReference=templateFinder.getPropertyReference();
        if(propertyReference!=null)
            propertyReference="<"+propertyReference+">";
        
        if (this.isHigh(lexicalEntryUtil)) {
            if (sentenceTemplate.equals(superlativeCountry)||sentenceTemplate.equals(superlativeLocation)) {
                sparql= PrepareSparqlQuery.descObj(range, propertyReference, reference, VARIABLE);
            } else if (sentenceTemplate.equals(superlativePerson)) {
                sparql= PrepareSparqlQuery.descObjOfPropPerson(range, propertyReference, reference, VARIABLE);
            } else if (sentenceTemplate.equals(TemplateFinder.superlativeWorld)) {
               sparql= PrepareSparqlQuery.desc(range, reference);
            }

        } else if (this.isLow(lexicalEntryUtil)) {
            if (sentenceTemplate.equals(superlativeCountry)||sentenceTemplate.equals(superlativeLocation)) {
                sparql= PrepareSparqlQuery.ascObj(range, propertyReference, reference, VARIABLE);
            } else if (sentenceTemplate.contains(superlativePerson)) {
                sparql= PrepareSparqlQuery.ascObjOfPropPerson(range, propertyReference, reference, VARIABLE);

            } else if (sentenceTemplate.equals(TemplateFinder.superlativeWorld)) {
               sparql= PrepareSparqlQuery.asc(range, reference);
            }
        }
        
         /*System.out.println("this.isLow(lexicalEntryUtil)::"+this.isLow(lexicalEntryUtil));
         System.out.println("templateFinder.getSelectedTemplate()::"+templateFinder.getSelectedTemplate());
         System.out.println("propertyReference::"+propertyReference);
         System.out.println("sparql::"+sparql);
         exit(1);*/

       
     
        

        /*if (this.isHigh(lexicalEntryUtil) && 
                (templateFinder.getSelectedTemplate().equals(superlativePlace))) {
            return PrepareSparqlQuery.objDesc(range, propertyReference,reference, "?VARIABLE");
        }
        else if (this.isHigh(lexicalEntryUtil) && 
                (templateFinder.getSelectedTemplate().equals(superlativePerson))) {
            return PrepareSparqlQuery.objOfPropPersonDESC(range, propertyReference,reference, "?VARIABLE");
        } else if (isLow(lexicalEntryUtil) && 
                (templateFinder.getSelectedTemplate().equals(superlativePerson)
                ||templateFinder.getSelectedTemplate().equals(TemplateFinder.superlativePlace))) {
            return PrepareSparqlQuery.objAsc(range, propertyReference,reference);
        } else if (this.isHigh(lexicalEntryUtil) && templateFinder.getSelectedTemplate().equals(TemplateFinder.superlativeWorld)) {
            return PrepareSparqlQuery.desc(range, reference);
        } else if (isLow(lexicalEntryUtil) && templateFinder.getSelectedTemplate().equals(TemplateFinder.superlativeWorld)) {
            return PrepareSparqlQuery.asc(range, reference);
        }*/
        return sparql;
    }

    private String generateSparql(String reference) {
        return "(bgp (triple ?subjOfProp <"+reference+"> ?objOfProp))\n";
    }

    private boolean isHigh(LexicalEntryUtil lexicalEntryUtil) {
         if (lexicalEntryUtil.getOlisRestriction().getValue().equals("http://lemon-model.net/oils/strong")
                || lexicalEntryUtil.getOlisRestriction().getValue().equals("http://lemon-model.net/oils/high")) {
            return true;
        } 
      
       return false;
    }
    
    private boolean isLow(LexicalEntryUtil lexicalEntryUtil) {
        if (lexicalEntryUtil.getOlisRestriction().getValue().equals("http://lemon-model.net/oils/weak")
                || lexicalEntryUtil.getOlisRestriction().getValue().equals("http://lemon-model.net/oils/low")) {
            return true;
        } 
        return false;
    }
}
