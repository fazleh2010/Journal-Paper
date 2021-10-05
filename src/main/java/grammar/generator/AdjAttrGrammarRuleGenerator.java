package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalSense;
import eu.monnetproject.lemon.model.Lexicon;
import grammar.sparql.SPARQLRequest;
import grammar.sparql.SelectVariable;
import grammar.structure.component.Binding;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceBindings;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QueryType;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;
import util.logging.LoggerHelper;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static grammar.sparql.SPARQLRequest.SPARQL_ENDPOINT_URL;
import static java.util.Objects.isNull;

public class AdjAttrGrammarRuleGenerator extends GrammarRuleGeneratorRoot {
  private static final Logger LOG = LogManager.getLogger(AdjAttrGrammarRuleGenerator.class);

  public AdjAttrGrammarRuleGenerator(Language language) {
    super(FrameType.AA, language, BindingConstants.NONE);
  }

  @Override
  public List<String> generateSentences(
    LexicalEntryUtil lexicalEntryUtil
  ) {
    // another implementation of generate is used here...
    return null;
  }

  @Override
  public List<GrammarEntry> generate(Lexicon lexicon) {
    List<GrammarEntry> grammarEntries = new ArrayList<>();

    // filter lexicon for correct grammar type:
    List<LexicalEntry> entriesFilteredByLanguageAndGrammarType = getEntriesFilteredByGrammarType(lexicon);
    try {
      for (LexicalEntry lexicalEntry : entriesFilteredByLanguageAndGrammarType) {
        for (LexicalSense lexicalSense : lexicalEntry.getSenses()) {
          LexicalEntryUtil lexicalEntryUtil = new LexicalEntryUtil(lexicon, lexicalEntry, getFrameType(), lexicalSense);
          OWLRestriction owlRestriction = lexicalEntryUtil.getOwlRestriction();
          if (!lexicalSense.getReference().toString().contains(LexiconSearch.LEXICON_BASE_URI)) {
            LOG.error(
              "Lexical {} entry does not have a local reference. The reference is {}",
              lexicalEntry.getURI(),
              lexicalSense.getURI()
            );
          }
          SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
          List<Binding> propertiesOfMostFrequentClasses = getMostFrequentClass(selectVariable, owlRestriction);
          propertiesOfMostFrequentClasses = filterOutSameLabels(propertiesOfMostFrequentClasses);
          for (Binding properties : propertiesOfMostFrequentClasses) {
            GrammarEntry grammarEntry = new GrammarEntry();
            grammarEntry.setFrameType(getFrameType());
            grammarEntry.setLanguage(getLanguage());

            // generate SPARQL query
            grammarEntry.setQueryType(QueryType.SELECT);
            SPARQLRequest sparqlRequest = generateSPARQL(selectVariable, properties, lexicalEntryUtil);
            grammarEntry.setSparqlQuery(sparqlRequest.toString());

            // set return variable
            grammarEntry.setReturnVariable(selectVariable.getVariableName());

            // set return and binding type
            grammarEntry.setReturnType(DomainOrRangeType.getMatchingType(URI.create(properties.getUri())));
            grammarEntry.setBindingType(DomainOrRangeType.getMatchingType(URI.create("")));

            // generate bindings for result sentence
            SentenceBindings sentenceBindings = new SentenceBindings();
            sentenceBindings.setBindingVariableName(getBindingVariable()); // maybe retrieve from sentence generation

            grammarEntry.setSentenceBindings(sentenceBindings);

            // generate sentences
            List<String> sentences = generateSentences(properties, lexicalEntryUtil);
            grammarEntry.setSentences(sentences);

            grammarEntry.setType(SentenceType.SENTENCE);

            // generate a fragment, i.e. a single NP
            GrammarEntry fragmentEntry = generateFragmentEntry(
              grammarEntry,
              properties,
              lexicalEntryUtil
            );

            grammarEntries.add(grammarEntry);
            if (!isNull(fragmentEntry)) {
              grammarEntries.add(fragmentEntry);
            }
          }
        }
      }
    } catch (QueGGMissingFactoryClassException e) {
      LOG.error("QueGGMissingFieldDeclarationException: " + e.getMessage());
    }
    return grammarEntries;
  }

  public GrammarEntry generateFragmentEntry(
    GrammarEntry grammarEntry,
    Binding binding,
    LexicalEntryUtil lexicalEntryUtil
  ) throws QueGGMissingFactoryClassException {
    GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
    fragmentEntry.setType(SentenceType.NP);

    SentenceBuilder sentenceBuilder = new SentenceBuilderAdjectiveAttributiveEN(
      getLanguage(),
      lexicalEntryUtil
    );
    List<String> generatedSentences = sentenceBuilder.generateFullSentencesBackward(binding.getLabel(), new String[]{}, lexicalEntryUtil);
    generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
    fragmentEntry.setSentences(generatedSentences);

    return fragmentEntry;
  }

  /**
   * Filter bindings for same label, return only one binding per label
   */
  private List<Binding> filterOutSameLabels(List<Binding> propertiesOfMostFrequentClasses) {
    return new ArrayList<>(
      propertiesOfMostFrequentClasses
        .stream()
        .collect(
          Collectors.toMap(
            Binding::getLabel,
            Function.identity(),
            BinaryOperator.minBy(Comparator.comparing(Binding::getUri))
          )
        ).values());
  }

  public SPARQLRequest generateSPARQL(
    SelectVariable selectVariable,
    Binding binding,
    LexicalEntryUtil lexicalEntryUtil
  ) {
    OWLRestriction owlRestriction = lexicalEntryUtil.getOwlRestriction();
    String property = owlRestriction.getProperty();
    String value = owlRestriction.getValue();

    SPARQLRequest sparqlRequest = new SPARQLRequest();
    sparqlRequest.setSearchProperty(property);
    sparqlRequest.setSelectVariable(selectVariable);

    Triple adjTriple =
      Triple.create(
        Var.alloc(selectVariable.getVariableName()),
        NodeFactory.createURI(property),
        NodeFactory.createURI(value)
      );
    sparqlRequest.addTriple(adjTriple);

    //-------------------------------------------
    String argUri = binding.getUri();
    // ---------------------------------------

    Triple argTriple =
      Triple.create(
        Var.alloc(selectVariable.getVariableName()),
        RDF.type.asNode(),
        NodeFactory.createURI(argUri)
      );
    sparqlRequest.addTriple(argTriple);

    sparqlRequest.createQuery();

    return sparqlRequest;
  }

  private List<Binding> getMostFrequentClass(SelectVariable selectVariable, OWLRestriction owlRestriction) {
    SPARQLRequest innerRequest = new SPARQLRequest();
    innerRequest.setSearchProperty(owlRestriction.getProperty());
    innerRequest.setSelectVariable(selectVariable);

    innerRequest.getParameterizedSparqlString().setCommandText(
      String.format(
        "SELECT ?y (count(?y) as ?f) ?%s WHERE\n" +
          "{\n" +
          "  ?x <%s> <%s> ;\n" +
          "  <%s> ?y .\n" +
          "  ?y  <%s> ?%s .\n" +
          "  FILTER ( lang(?%s)=\"%s\" ) .\n" +
          "}\n" +
          "group by ?y ?%s\n" +
          "having (count(?y) > 9)" +
          "order by desc(?f)\n",
        RDFS.label.getLocalName(),
        owlRestriction.getProperty(), owlRestriction.getValue(),
        RDF.type,
        RDFS.label, RDFS.label.getLocalName(),
        RDFS.label.getLocalName(), getLanguage().toString().toLowerCase(),
        RDFS.label.getLocalName()
      )
    );
    QueryExecution exec = QueryExecutionFactory.sparqlService(
      SPARQL_ENDPOINT_URL,
      innerRequest.getParameterizedSparqlString().asQuery()
    );
    ResultSet resultSet = exec.execSelect();
    QuerySolution querySolution;
    List<Binding> bindings = new ArrayList<>();
    while (resultSet.hasNext()) {
      querySolution = resultSet.next();
      bindings.add(
        new Binding(
          querySolution.getLiteral(RDFS.label.getLocalName()).getLexicalForm(),
          querySolution.get("y").asResource().getURI()
        )
      );
    }
    if (bindings.isEmpty()) {
      LOG.error(
        "No results for binding query\n{}",
        LoggerHelper.sepString(
          owlRestriction.getProperty(),
          innerRequest.getParameterizedSparqlString().toString()
        )
      );
    }
    return bindings;
  }

  /**
   * Generates a list of sentences that will later be saved as part of the grammar rule.
   * For example: ["What is the capital of $x?"]
   *
   * @param lexicalEntryUtil a utility class for a specific combination of LexicalEntry, LexicalSense and FrameType
   * @return a list of possible sentences
   */
  private List<String> generateSentences(
    Binding binding,
    LexicalEntryUtil lexicalEntryUtil
  ) throws QueGGMissingFactoryClassException {
    String argUri = binding.getLabel();

    SentenceBuilderAdjectiveAttributiveEN sentenceBuilderAdjectiveAttributiveEN =
      new SentenceBuilderAdjectiveAttributiveEN(getLanguage(), lexicalEntryUtil);
    return sentenceBuilderAdjectiveAttributiveEN.generateFullSentencesForward(argUri, lexicalEntryUtil);
  }

}
