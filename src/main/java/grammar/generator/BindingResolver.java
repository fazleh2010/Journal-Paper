package grammar.generator;

import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import grammar.structure.component.SentenceType;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static util.matcher.PatternMatchHelper.cleanPattern;

public class BindingResolver {
  private static final Logger LOG = LogManager.getLogger(BindingResolver.class);
  private final List<GrammarEntry> grammarEntries;

  public BindingResolver(List<GrammarEntry> grammarEntries) {
    this.grammarEntries = grammarEntries;
  }

  public GrammarWrapper resolve() {
    GrammarWrapper grammarWrapper = new GrammarWrapper();

    // only include full sentences into the final dataset
    List<GrammarEntry> sentenceEntries = grammarEntries
      .parallelStream()
      .filter(grammarEntry -> grammarEntry.getType().equals(SentenceType.SENTENCE))
      .collect(Collectors.toList());
    grammarWrapper
      .getGrammarEntries()
      .addAll(
        sentenceEntries
          .parallelStream()
          .filter(grammarEntry -> grammarEntry.getSentenceBindings()
                                              .getBindingVariableName()
                                              .equals(BindingConstants.NONE))
          .collect(Collectors.toList())
      );
    List<GrammarEntry> eligibleSentenceEntries = sentenceEntries
      .parallelStream()
      .filter(grammarEntry -> !grammarEntry.getSentenceBindings()
                                           .getBindingVariableName()
                                           .equals(BindingConstants.NONE))
      .collect(Collectors.toList());
    eligibleSentenceEntries
      .forEach(grammarEntry -> {
        // Only insert into grammarEntries that have a binding variable for their sentences
        if (!isNull(grammarEntry.getSentenceBindings()) &&
          !isNull(grammarEntry.getSentenceBindings().getBindingList())) {
          grammarEntry.getSentenceBindings()
                      .getBindingList()
                      .forEach(binding -> {
                        GrammarEntry grammarEntryNew = grammarEntry.deepCopy();
                        // remove binding list, it will only take up space
                        grammarEntryNew.getSentenceBindings().setBindingList(null);
                        // insert binding into all sentences
                        List<String> sentences = insertBinding(
                          grammarEntryNew.getSentenceBindings().getBindingVariableName(),
                          binding.getLabel(),
                          grammarEntryNew.getSentences()
                        );
                        grammarEntryNew.setSentences(sentences);
                        grammarWrapper.getGrammarEntries().add(grammarEntryNew);

                        //insert binding into SPARQL query
                        grammarEntryNew.setSparqlQuery(insertBindingInSPARQL(
                          grammarEntryNew.getBindingVariable(),
                          binding.getUri(),
                          grammarEntryNew.getSparqlQuery()
                        ));

                      });
        } else {
          LOG.warn("There are no bindings for grammarEntry id {}", grammarEntry.getId());
        }
      });
    return grammarWrapper;
  }

  public static String insertBindingInSPARQL(String bindingVariable, String uri, String sparqlQuery) {
    ParameterizedSparqlString parameterizedSparqlString = new ParameterizedSparqlString(sparqlQuery);
    Node node;
    if (uri.startsWith("http")) {
      node = NodeFactory.createURI(uri);
    } else {
      node = NodeFactory.createLiteral(uri);
    }
    parameterizedSparqlString.setParam(bindingVariable, node);
    return parameterizedSparqlString.toString();
  }

  private List<String> insertBinding(String oldBinding, String newBinding, List<String> sentences) {
    return sentences
      .parallelStream()
      .map(s -> s.replaceAll("\\(" + cleanPattern(oldBinding) + "\\s\\|\\s.+\\)", cleanPattern(newBinding))
                 .replace(oldBinding, newBinding)
      )
      .collect(Collectors.toList());
  }
}
