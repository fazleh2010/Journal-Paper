package util.sparql;

import grammar.structure.component.GrammarEntry;
import lombok.Data;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryType;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.OpAsQuery;
import org.apache.jena.sparql.syntax.ElementGroup;

import static java.util.Objects.isNull;

@Data
public class RequestCompiler {

  /**
   * Compile the SPARQL query of a grammar entry to an executable query.
   *
   * @param grammarEntry the grammar entry that is used for query compilation
   * @return an executable SPARQL query string
   */
  public static Query compileAnswerQuery(GrammarEntry grammarEntry) {
    return compile(grammarEntry, grammarEntry.getReturnVariable());
  }

    /*
    Query q = OpAsQuery.asQuery(Algebra.parse(Algebra.compile(elementGroup).toString()));
    q.setQueryResultStar(false);
    q.getQueryPattern()
    -> will get ElementGroup
     */

  private static Query compile(GrammarEntry grammarEntry, String resultVar) {
    Query query = new Query();
    if (resultVar.isEmpty()) {
      return query;
    }


    query.addResultVar(resultVar);
    if (grammarEntry.getQueryType().equals(QueryType.SELECT)) {
      query.setQuerySelectType();
    } else if (grammarEntry.getQueryType().equals(QueryType.ASK)) {
      query.setQueryAskType();
    }

    ElementGroup body = (ElementGroup) OpAsQuery.asQuery(Algebra.parse(grammarEntry.getSparqlQuery()))
                                                .getQueryPattern();

    query.setQueryPattern(body);

    return query;
  }

  public static Query compileBindingQuery(GrammarEntry grammarEntry) {
    return compile(
      grammarEntry,
      !isNull(grammarEntry.getSentenceToSparqlParameterMapping()) ?
      grammarEntry.getBindingVariable() :
      ""
    );
  }
}
