package grammar.generator;

import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.SentenceBindings;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import lombok.NoArgsConstructor;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.sse.SSE;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.matcher.PatternMatchHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@NoArgsConstructor
public class GrammarEntryCombinationFactory {
  private static final Logger LOG = LogManager.getLogger(GrammarEntryCombinationFactory.class);


    public List<GrammarEntry> generateCombinations(List<GrammarEntry> grammarEntries, String bindingVariable) {
        final String newMappingForBindingVariable = "x";
        List<GrammarEntry> combinations = new ArrayList<GrammarEntry>();
        List<GrammarEntry> sentenceGrammarEntries
                = grammarEntries.stream()
                        .filter(grammarEntry -> grammarEntry.getType().equals(SentenceType.SENTENCE))
                        .collect(Collectors.toList());
                     

        for (GrammarEntry sentenceEntry : sentenceGrammarEntries) {
            // Parse first sentence to determine the target type to filter for
            if (isNull(sentenceEntry.getSentences()) || sentenceEntry.getSentences().isEmpty()) {
                LOG.error("There are no sentences for {}", sentenceEntry.getSparqlQuery());
                continue;
            }
            String targetType = getTargetTypeFromSentence(sentenceEntry.getSentences().get(0), bindingVariable);
            SentenceType sentenceType = SentenceType.getMatchingType(targetType);
            // Filter all grammar entries for matching types (sentenceEntry#bindingType = grammarEntry#returnType)
            /*System.out.println("sentenceEntry.getSentences().get(0)::::" + sentenceEntry.getSentences().get(0));
            System.out.println("bindingVariable::::" + bindingVariable);
            System.out.println("current targetType.size()::::" + targetType);
            System.out.println("sentenceType::::" + sentenceType);*/
            List<GrammarEntry> filteredEntries
                    = grammarEntries.stream()
                            .filter(grammarEntry -> grammarEntry.getType().equals(sentenceType))
                            .filter(grammarEntry -> matchSentenceBindingToNPReturn(sentenceEntry, grammarEntry))
                            .collect(Collectors.toList());
            //System.out.println("current sentenceGrammarEntries.size()::::" + sentenceGrammarEntries.toString());
            //System.out.println("current filteredEntries.size()::::" + filteredEntries.toString());
            //exit(1);

            for (GrammarEntry filteredEntry : filteredEntries) {
                GrammarEntry combinedEntry = new GrammarEntry();
                for (String sentence : sentenceEntry.getSentences()) {
                    for (String npSentence : filteredEntry.getSentences()) {

                        String matchToken = String.format(
                                "\\(%s \\| %s_%s\\)",
                                sentenceEntry.getSentenceBindings().getBindingVariableName(),
                                sentenceEntry.getBindingType(),
                                sentenceType
                        );
                        String combinedSentence = combineSentences(
                                sentence,
                                npSentence,
                                matchToken
                        );
                        combinedEntry.getSentences().add(combinedSentence);
                    }
                }
                String newQueryPattern = combineSPARQL(
                        sentenceEntry.getSparqlQuery(),
                        sentenceEntry.getBindingVariable(),
                        filteredEntry.getSparqlQuery(),
                        filteredEntry.getReturnVariable(),
                        newMappingForBindingVariable
                );
                combinedEntry.setSparqlQuery(newQueryPattern);

                // Copy needed entries from base entry (sentenceEntry)
                combinedEntry.setReturnVariable(sentenceEntry.getReturnVariable());
                combinedEntry.setLanguage(sentenceEntry.getLanguage());
                combinedEntry.setType(sentenceEntry.getType());
                combinedEntry.setFrameType(sentenceEntry.getFrameType());
                combinedEntry.setReturnType(sentenceEntry.getReturnType());
                combinedEntry.setQueryType(sentenceEntry.getQueryType());
                combinedEntry.setCombination(true);

                // Get binding-related properties from filteredEntry
                SentenceBindings combinedBindings = new SentenceBindings();
                combinedBindings.setBindingVariableName(filteredEntry.getSentenceBindings().getBindingVariableName());
                combinedEntry.setSentenceBindings(combinedBindings);
                combinedEntry.setBindingType(filteredEntry.getBindingType());

                if (!isNull(filteredEntry.getSentenceToSparqlParameterMapping())) {
                    Map<String, String> combinedMap = new HashMap<>();
                    combinedMap.put(filteredEntry.getSentenceBindings().getBindingVariableName(), filteredEntry.getBindingVariable() + newMappingForBindingVariable);
                    combinedEntry.setSentenceToSparqlParameterMapping(combinedMap);
                }

                combinations.add(combinedEntry);
            }
        }
        return combinations;
    }

  protected boolean matchSentenceBindingToNPReturn(GrammarEntry sentenceEntry, GrammarEntry grammarEntry) {
    // E.g. sentenceEntry.getBindingType() = LOCATION
    // -> Also consider grammarEntry.getReturnType() = City as location because DomainOrRangeType.LOCATION also contains City!
    List<DomainOrRangeType> alternativeTypes = DomainOrRangeType.getAllAlternativeTypes(grammarEntry.getReturnType());
      /*System.out.println("sentenceEntry::" + sentenceEntry.getSentences());
      System.out.println("grammarEntry::" + grammarEntry.getSentences());

      System.out.println("alternativeTypes::" + alternativeTypes);
      System.out.println("sentenceEntry.getBindingType()::" + sentenceEntry.getBindingType());
      System.out.println("alternativeTypes.contains(sentenceEntry.getBindingType())::" + alternativeTypes.contains(sentenceEntry.getBindingType()));
      */
      return alternativeTypes.contains(sentenceEntry.getBindingType());
  }

  private String getTargetTypeFromSentence(String sentence, String bindingVariable) {
    String targetType = "";
    String matchPattern = String.format("\\(%s \\| \\w+_(\\w+)\\)", bindingVariable);
    matchPattern = PatternMatchHelper.cleanPattern(matchPattern);
    Pattern pattern = Pattern.compile(matchPattern);
    String expressionToReplace = PatternMatchHelper.getPatternMatch(sentence, pattern);
    if (!expressionToReplace.isEmpty()) {
      targetType = PatternMatchHelper.cleanPattern(expressionToReplace);
    }
     /*System.out.println("sentence::" + sentence);
     System.out.println("bindingVariable::" +bindingVariable);
     System.out.println("matchPattern::" + matchPattern);
     System.out.println("expressionToReplace::" +expressionToReplace);
     System.out.println("targetType::" + targetType);*/
   
    return targetType;
  }

  private String combineSentences(
    String baseSentence,
    String matchedSentence,
    String matchPattern
  ) {
    String combinedSentence = "";

    // Try to find the matching uri for the previously found item inside the QALD SPARQL query
    matchPattern = PatternMatchHelper.cleanPattern(matchPattern);
    Pattern pattern = Pattern.compile(matchPattern);
    String expressionToReplace = PatternMatchHelper.getPatternMatch(baseSentence, pattern, 0);
    if (!expressionToReplace.isEmpty()) {
      combinedSentence = baseSentence.replaceAll(matchPattern, PatternMatchHelper.cleanPattern(matchedSentence));
    }
    return combinedSentence;
  }

  /**
   * Combines two basic graph patterns.
   *
   * @param baseSparql                   <p>The basic graph pattern that is used as base pattern for this query.
   *                                     This queries' returnVariable will be used for the resulting SELECT query.</p>
   * @param baseMappedSparqlParameter    <p>The name of the subject or object of the baseSparql query
   *                                     that is used for matching the matchedSparql.</p>
   * @param matchedSparql                <p>The basic graph pattern that will be joined with the baseSparql.</p>
   * @param matchedSparqlReturnVariable  <p>The returnVariable of the matchedSparql query</p>
   * @param newMappingForBindingVariable <p>The new SPARQL variable that will be the mapped to the bindingVariable</p>
   * @return the new SPARQL parameter that will be mapped by the bindingVariable of the baseSparql
   */
  private String combineSPARQL(
    String baseSparql,
    String baseMappedSparqlParameter,
    String matchedSparql,
    String matchedSparqlReturnVariable,
    String newMappingForBindingVariable
  ) {
    ElementTriplesBlock block = new ElementTriplesBlock();
    List<Triple> baseTriples = SSE.parseBGP(baseSparql).getList();
    List<Triple> matchedTriples = SSE.parseBGP(matchedSparql).getList();
    for (Triple baseTriple : baseTriples) {
      block.addTriple(baseTriple);
      for (Triple matchedTriple : matchedTriples) {
        // Check which variable needs to be renamed and will be the new binding of the base sparql query
        if (matchedTriple.getSubject().isVariable()) {
          if (matchedTriple.getSubject().getName().equals(matchedSparqlReturnVariable)) {
            // subject matched to matchedSparqlReturnVariable
            //  -> subject needs to be renamed to baseMappedSparqlParameter
            matchedTriple = Triple.create(
              Var.alloc(baseMappedSparqlParameter),
              matchedTriple.getPredicate(),
              matchedTriple.getObject()
            );
          } else {
            //  -> subject needs to be extended by newMappingForBindingVariable
            matchedTriple = Triple.create(
              Var.alloc(matchedTriple.getSubject().getName() + newMappingForBindingVariable),
              matchedTriple.getPredicate(),
              matchedTriple.getObject()
            );
          }
        }
        if (matchedTriple.getObject().isVariable()) {
          if (matchedTriple.getObject().getName().equals(matchedSparqlReturnVariable)) {
            // object matched to matchedSparqlReturnVariable
            //  -> object needs to be renamed to baseMappedSparqlParameter
            matchedTriple = Triple.create(
              matchedTriple.getSubject(),
              matchedTriple.getPredicate(),
              Var.alloc(baseMappedSparqlParameter)
            );
          } else {
            //  -> object needs to be extended by newMappingForBindingVariable
            matchedTriple = Triple.create(
              matchedTriple.getSubject(),
              matchedTriple.getPredicate(),
              Var.alloc(matchedTriple.getObject().getName() + newMappingForBindingVariable)
            );
          }
        }
        block.addTriple(matchedTriple);
      }
    }
    return Algebra.compile(block).toString();
  }
}
