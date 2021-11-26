package grammar.sparql;

import grammar.structure.component.Language;
import lombok.Getter;
import lombok.Setter;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QueryType;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.core.ResultBinding;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.E_Lang;
import org.apache.jena.sparql.expr.E_LangMatches;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementOptional;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFieldDeclarationException;
import util.matcher.PatternMatchHelper;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static util.validation.NullCheck.notNull;

@Setter
@Getter
public class SPARQLRequest {
  public static  String SPARQL_ENDPOINT_URL = "http://dbpedia.org/sparql";
  public static final long DEFAULT_LIMIT = 100;
  private static final Logger LOG = LogManager.getLogger(SPARQLRequest.class);
  private final String labelString = RDFS.label.getLocalName();
  private final ElementTriplesBlock block;
  private ElementGroup body;
  private Query query;
  private ParameterizedSparqlString parameterizedSparqlString;
  private SelectVariable selectVariable;
  private String searchProperty;

  /**
   * Constructs a full SPARQL query. <br>
   * Label search and language filtering can be added later.
   */
  public SPARQLRequest() {
    this.body = new ElementGroup();
    this.block = new ElementTriplesBlock();
    this.parameterizedSparqlString = new ParameterizedSparqlString();
    this.query = initQuery();
  }

  private Query initQuery() {
    return QueryFactory.make();
  }

  public void addTriple(Triple triple) {
    block.addTriple(triple);
  }

  /**
   * Add ?subjOfProp < searchProperty_uri > ?objOfProp
   */
  public void addGenericTriple() throws QueGGMissingFieldDeclarationException {
    notNull("searchProperty", searchProperty, this.getClass());
    Triple triple =
      Triple.create(
        Var.alloc(SelectVariable.subjOfProp.getVariableName()),
        NodeFactory.createURI(searchProperty),
        Var.alloc(SelectVariable.objOfProp.getVariableName())
      );
    block.addTriple(triple);
  }

  /**
   * Add ?<selectVariable> < rdf:type > <conditionUri>
   */
  public void addConditionTriple(SelectVariable selectVariable, String conditionUri) {
    Triple triple =
      Triple.create(
        Var.alloc(selectVariable.getVariableName()),
        RDF.type.asNode(),
        NodeFactory.createURI(conditionUri)
      );
    block.addTriple(triple);
  }

  public void createQuery() {
    this.parameterizedSparqlString = initParameterizedSparqlString();
  }

  private ParameterizedSparqlString initParameterizedSparqlString() {
    // do not forget to add at least generic triple!

    body.addElement(block); // Group pattern match and filter
/*

    query.setDistinct(true);
    query.setQueryPattern(body);                               // Set the body of the query to our group
    query.addResultVar(selectVariable.getVariableName());      // Select ___
*/
    parameterizedSparqlString.setCommandText(Algebra.compile(body).toString());
    return parameterizedSparqlString;
  }

  /**
   * Adds the previously filled {@link ElementTriplesBlock} and {@link ElementGroup} to the {@link Query}.
   *
   * @param queryType the {@link QueryType} of the query.
   * @return a valid query if {@link #body} and/or {@link #block} were filled before.
   */
  public Query initQuery(QueryType queryType) {
    Query query = QueryFactory.make();
    if (queryType == QueryType.ASK) {
      query.setQueryAskType();
    } else if (queryType == QueryType.SELECT) {
      query.setQuerySelectType();
    }
    body.addElement(block); // Group pattern match and filter
    query.setQueryPattern(body);                               // Set the body of the query to our group
    return query;
  }

  /**
   * This adds a label query to {@link #block} and a language filter to the {@link #body} and the {@link #query} result vars. <br>
   * {@code
   * select ... ?label
   * ...
   * ?<selectVariable> rdfs:label ?label .
   * FILTER langMatches( lang(?label),"<filterLanguage>" ) .
   * } <br>
   * <selectVariable> will be replaced by the {@link #selectVariable} value. <br>
   * <filterLanguage> will be replaced by the filterLanguage value. <br>
   *
   * @param filterLanguage the language for the label filter
   */
  public void addLabelQueryWithFilter(Language filterLanguage) throws QueGGMissingFieldDeclarationException {
    notNull("selectVariable", selectVariable, this.getClass());
    addLabelQueryWithFilter(filterLanguage, selectVariable.getVariableName());
  }

  /**
   * This adds a label query to {@link #block} and a language filter to the {@link #body} and the {@link #query} result vars. <br>
   * {@code
   * select ... ?label
   * ...
   * <property> rdfs:label ?label .
   * FILTER langMatches( lang(?label),"<filterLanguage>" ) .
   * } <br>
   * <property> will be replaced by the parameter 'property' value. <br>
   * <filterLanguage> will be replaced by the parameter 'filterLanguage' value. <br>
   *
   * @param filterLanguage the language for the label filter
   * @param property       a property URI or a variable string (URI: http...., variable: simple string)
   */
  public void addLabelQueryWithFilter(Language filterLanguage, String property) {
    Triple labelPattern =
      Triple.create(
        property.startsWith("http") ? NodeFactory.createURI(property) : Var.alloc(property),
        RDFS.label.asNode(),
        Var.alloc(labelString)
      );
    block.addTriple(labelPattern);

    // FILTER langMatches( lang(?label), \"DE\" )
    ElementFilter filter = createLanguageFilterForLabel(filterLanguage);
    body.addElement(filter);
    query.addResultVar(Var.alloc(labelString));
  }

  /**
   * Add a label query with a language filter on the label to an existing {@link Query}.
   *
   * @param filterLanguage the language filter for the label
   * @param query          the query that the filter should be attached to
   * @return the query with a language and label filter attached
   */
  public Query addOptionalLabelQueryWithFilter(Language filterLanguage, String property, Query query) {
    ElementGroup body = (ElementGroup) query.getQueryPattern();
    ElementGroup optionalBody = new ElementGroup();
    Triple labelPattern =
      Triple.create(
        property.startsWith("http") ? NodeFactory.createURI(property) : Var.alloc(property),
        RDFS.label.asNode(),
        Var.alloc(labelString)
      );
    ElementFilter filter = createLanguageFilterForLabel(filterLanguage);
    optionalBody.addTriplePattern(labelPattern);
    optionalBody.addElementFilter(filter);
    ElementOptional elementOptional = new ElementOptional(optionalBody);
    body.addElement(elementOptional);

    query.addResultVar(Var.alloc(labelString));
    query.setQueryPattern(body);
    return query;
  }

  private ElementFilter createLanguageFilterForLabel(Language filterLanguage) {
    // FILTER langMatches( lang(?label), \"DE\" )
    Expr labelExpr = new ExprVar(labelString);            // ?label
    Expr langExpr = new E_Lang(labelExpr);                // lang(?label)
    Expr langMatchesExpr = new E_LangMatches(
      langExpr,
      new NodeValueString(filterLanguage.toString())      // langMatches(lang(?label), \"DE\")
    );
    return new ElementFilter(langMatchesExpr);
  }

  /**
   * Adds a limit to the query.
   * <b>Needs to be called last before executing the query.</b>
   *
   * @param limit a limit > 0.
   */
  public void addLimit(int limit) {
    if (limit > 0) {
      this.query.setLimit(limit);
    }
  }

  public List<String> getSparqlAskResultList() {
    List<String> resultList = new ArrayList<>();
    String match = PatternMatchHelper.getPatternMatch(
      getParameterizedSparqlString().getCommandText(),
      Pattern.compile("(ASK\\s+)"),
      0
    );
    if (!match.isEmpty()) {
      resultList.add(String.valueOf(executeAskQuery()));
    }
    return resultList;
  }

  /**
   * Execute a select query (executes {@link #parameterizedSparqlString}).<br>
   *
   * @return a list of result mappings for the query (e.g. a list of entries like
   * {@code {"label" -> "actualLabel","objOfProp" -> "http://....."} }).
   */
  public List<Map<String, String>> getSparqlSelectResultList() {
    List<Map<String, String>> resultList = new ArrayList<>();
    try {

      ResultSet results = executeSelectQuery();
      while (results.hasNext()) {
        QuerySolution qs = results.next();

        try {
          Iterator<Var> varIterator = (((ResultBinding) qs).getBinding()).vars();
          Map<String, String> resultMap = new HashMap<>();
          while (varIterator.hasNext()) {
            Var var = varIterator.next();
            if (qs.get(var.getVarName()).isURIResource()) {
              resultMap.put(var.getVarName(), qs.get(var.getVarName()).asResource().getURI());
            } else if (qs.get(var.getVarName()).isLiteral()) {
              if (!isNull(qs.get(var.getVarName()).asLiteral().getValue())) {
                resultMap.put(var.getVarName(), qs.get(var.getVarName()).asLiteral().getValue().toString());
              } else {
                resultMap.put(var.getVarName(), qs.get(var.getVarName()).asLiteral().toString());
              }
            } else if (qs.get(var.getVarName()).isAnon()) {
              resultMap.put(var.getVarName(), qs.get(var.getVarName()).asNode().toString());
            } else if (qs.get(var.getVarName()).isResource()) {
              resultMap.put(var.getVarName(), qs.get(var.getVarName()).asResource().toString());
            }
          }
          resultList.add(resultMap);
        } catch (Exception e) {
          LOG.error(e.getMessage());
        }
      }
    } catch (SocketTimeoutException exception) {
      LOG.error("SocketTimeoutException:\n{}", parameterizedSparqlString.toString());
    }
    return resultList;
  }

  /**
   * Execute an ask or a select query (executes {@link #parameterizedSparqlString}).<br>
   * This will skip all label results, so if you need those use {@link #getSparqlSelectResultList()} instead.
   *
   * @return a list of results for the query, skips label.
   */
  public List<String> getSparqlResultList() {
    List<String> resultList = new ArrayList<>();
    String match = PatternMatchHelper.getPatternMatch(
      getParameterizedSparqlString().getCommandText(),
      Pattern.compile("(ASK\\s+)"),
      0
    );
    if (!match.isEmpty()) {
      resultList.add(String.valueOf(executeAskQuery()));
      return resultList;
    }
    try {

      ResultSet results = executeSelectQuery();
      while (results.hasNext()) {
        QuerySolution qs = results.next();

        try {
          Iterator<Var> varIterator = (((ResultBinding) qs).getBinding()).vars();
          while (varIterator.hasNext()) {
            Var var = varIterator.next();
            if (var.getVarName().equals(RDFS.label.getLocalName())) {
              continue;
            }
            if (qs.get(var.getVarName()).isURIResource()) {
              resultList.add(qs.get(var.getVarName()).asResource().getURI());
            } else if (qs.get(var.getVarName()).isLiteral()) {
              resultList.add(qs.get(var.getVarName()).asLiteral().toString());
            } else if (qs.get(var.getVarName()).isAnon()) {
              resultList.add(qs.get(var.getVarName()).asNode().toString());
            } else if (qs.get(var.getVarName()).isResource()) {
              resultList.add(qs.get(var.getVarName()).asResource().toString());
            }
          }
        } catch (Exception e) {
          LOG.error(e.getMessage());
        }
      }
    } catch (SocketTimeoutException e) {
      LOG.error("SocketTimeoutException:\n{}", parameterizedSparqlString.toString());
    }
    return resultList;
  }

  /**
   * Needs a valid <b>SPARQLEndpointUrl</b> and a valid <b>parameterizedSparqlString</b>.
   *
   * @return a ResultSet after execution.
   */
  public ResultSet executeSelectQuery() throws SocketTimeoutException {
    ResultSet resultSet;
    QueryExecution exec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT_URL, parameterizedSparqlString.asQuery());
    exec.setTimeout(30000);
    try {
      resultSet = exec.execSelect();
    } catch (Exception e) {
      throw new SocketTimeoutException(e.getMessage());
    }
    return resultSet;
  }

  /**
   * Needs a valid <b>SPARQLEndpointUrl</b> and a valid <b>parameterizedSparqlString</b>.
   *
   * @return true or false as answer to the yes/no question
   */
  public boolean executeAskQuery() {
    QueryExecution exec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT_URL, parameterizedSparqlString.asQuery());
    exec.setTimeout(30000);
    return exec.execAsk();
  }

  public static SPARQLRequest fromQuery(Query query) {
    SPARQLRequest sparqlRequest = new SPARQLRequest();
    ParameterizedSparqlString ps = new ParameterizedSparqlString(query.toString());
    sparqlRequest.setParameterizedSparqlString(ps);
    sparqlRequest.setBody((ElementGroup) query.getQueryPattern());
    sparqlRequest.setQuery(query);
    return sparqlRequest;
  }

  public static SPARQLRequest fromString(String sparqlString) {
    SPARQLRequest sparqlRequest = new SPARQLRequest();
    ParameterizedSparqlString ps = new ParameterizedSparqlString(sparqlString);
    sparqlRequest.setParameterizedSparqlString(ps);
    return sparqlRequest;
  }

  public String toString() {
    return parameterizedSparqlString.toString();
  }

    public static void setEndpoint(String endpoint) {
        SPARQL_ENDPOINT_URL=endpoint;
    }

    public static String getSPARQL_ENDPOINT_URL() {
        return SPARQL_ENDPOINT_URL;
    }
    
    

}
