package grammar.generator;

import eu.monnetproject.lemon.model.Lexicon;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceBindings;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QueryType;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.syntax.ElementUnion;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static grammar.sparql.Prefix.DBO;

public class GrammarRuleGeneratorRootImpl extends GrammarRuleGeneratorRoot {
  private static final Logger LOG = LogManager.getLogger(GrammarRuleGeneratorRootImpl.class);

  public GrammarRuleGeneratorRootImpl(Language language) {
    super(FrameType.FULL_DATASET, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
  }

  /**
   * Not used here.
   */
  @Override
  public List<String> generateSentences(
    LexicalEntryUtil lexicalEntryUtil
  ) {
    return null;
  }

  @Override
  public List<GrammarEntry> generate(Lexicon lexicon) {
    List<GrammarEntry> grammarEntries;

    grammarEntries = buildHardCodedGrammarEntries();

    return grammarEntries;
  }

  private List<GrammarEntry> buildHardCodedGrammarEntries() {
    List<GrammarEntry> grammarEntries = new ArrayList<>();
    grammarEntries.add(createWhereIsXLocatedEntry());
    return grammarEntries;
  }

  private GrammarEntry createWhereIsXLocatedEntry() {
    GrammarEntry grammarEntry = new GrammarEntry();
    grammarEntry.setLanguage(getLanguage());
    grammarEntry.setType(SentenceType.SENTENCE);
    grammarEntry.setFrameType(getFrameType());
    grammarEntry.setBindingType(DomainOrRangeType.Location);
    grammarEntry.setReturnType(DomainOrRangeType.Location);
    grammarEntry.getSentences().add(
      String.format(
        "Where is %s located?",
        getBindingVariable()
      )
    );
    grammarEntry.setQueryType(QueryType.SELECT);
    String bindingVariableName = SelectVariable.subjOfProp.getVariableName();
    String returnVariableName = SelectVariable.objOfProp.getVariableName();
    Map<String, String> sentenceSparqlMap = new HashMap<>();
    sentenceSparqlMap.put(getBindingVariable(), bindingVariableName);
    grammarEntry.setSentenceToSparqlParameterMapping(sentenceSparqlMap);
    grammarEntry.setReturnVariable(returnVariableName);
    SentenceBindings sentenceBindings = new SentenceBindings();
    sentenceBindings.setBindingVariableName(getBindingVariable());
    grammarEntry.setSentenceBindings(sentenceBindings);
    String sparqlQuery = makeUnionQuery(
      bindingVariableName,
      returnVariableName,
      grammarEntry.getBindingType(),
      grammarEntry.getReturnType(),
      DBO.getUri() + "country",
      DBO.getUri() + "location"
    );
    grammarEntry.setSparqlQuery(sparqlQuery);

    return grammarEntry;
  }

  private String makeUnionQuery(
    String bindingVariableName,
    String returnVariableName,
    DomainOrRangeType bindingType,
    DomainOrRangeType returnType,
    String... unionParams
  ) {
    ElementGroup elementGroup = new ElementGroup();
    ElementUnion elementUnion = new ElementUnion();
    for (String param : unionParams) {
      ElementTriplesBlock block = new ElementTriplesBlock();
      block.addTriple(Triple.create(
        Var.alloc(bindingVariableName),
        NodeFactory.createURI(param),
        Var.alloc(returnVariableName)
      ));
      elementUnion.addElement(block);
    }
    elementGroup.addElement(elementUnion);
    // Add conditions
    Triple returnCondition =
      Triple.create(
        Var.alloc(returnVariableName),
        RDF.type.asNode(),
        NodeFactory.createURI(returnType.getReferences().get(0).toString())
      );
    elementGroup.addTriplePattern(returnCondition);
    Triple bindingCondition =
      Triple.create(
        Var.alloc(bindingVariableName),
        RDF.type.asNode(),
        NodeFactory.createURI(bindingType.getReferences().get(0).toString())
      );
    elementGroup.addTriplePattern(bindingCondition);

    return Algebra.compile(elementGroup).toString();
  }
}
