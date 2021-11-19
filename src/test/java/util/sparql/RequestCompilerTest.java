package util.sparql;

import com.fasterxml.jackson.databind.ObjectMapper;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import org.apache.jena.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.io.ResourceHelper.loadResource;

class RequestCompilerTest {

  private static final String GRAMMAR_FILE_PATH = "grammar_FULL_DATASET_EN.json";
  private static final String GRAMMAR_FILE_COMBINATIONS_PATH = "grammar_COMBINATIONS_EN.json";
  private List<GrammarEntry> grammarEntries = new ArrayList<>();

  @BeforeEach
  void init() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    URL grammarEntriesFile = loadResource(GRAMMAR_FILE_PATH, this.getClass());
    GrammarWrapper grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);

    this.grammarEntries = grammarWrapper.getGrammarEntries();

    grammarEntriesFile = loadResource(GRAMMAR_FILE_COMBINATIONS_PATH, this.getClass());
    grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);
    this.grammarEntries.addAll(grammarWrapper.getGrammarEntries());
  }

  @Test
  void testCompileAnswerQuery() {
    for (GrammarEntry grammarEntry : this.grammarEntries) {
      Query sparqlQuery = RequestCompiler.compileAnswerQuery(grammarEntry);
      assertNotNull(sparqlQuery);
      assertNotEquals("", sparqlQuery.toString());
      assertEquals(grammarEntry.getQueryType(), sparqlQuery.queryType());
      assertNotNull(sparqlQuery.getResultVars());
      assertEquals(grammarEntry.getReturnVariable(), sparqlQuery.getResultVars().get(0));
    }
  }

  @Test
  void testCompileBindingQuery() {
    for (GrammarEntry grammarEntry : this.grammarEntries) {
      Query sparqlQuery = RequestCompiler.compileBindingQuery(grammarEntry);
      assertNotNull(sparqlQuery);
      if (!isNull(grammarEntry.getSentenceToSparqlParameterMapping())) {
        assertNotEquals("", sparqlQuery.toString());
        System.out.println(sparqlQuery.toString());
        assertEquals(grammarEntry.getQueryType(), sparqlQuery.queryType());
        assertNotNull(sparqlQuery.getResultVars());
        assertEquals(grammarEntry.getBindingVariable(), sparqlQuery.getResultVars().get(0));
      } else {
        assertEquals("", sparqlQuery.toString());
      }
    }
  }
}
