package grammar.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.GrammarWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.io.ResourceHelper.loadResource;

class BindingResolverTest {

  private static final String GRAMMAR_FILE_PATH = "grammar_FULL_DATASET_EN.json";
  private BindingResolver bindingResolver;

  @BeforeEach
  void init() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    URL grammarEntriesFile = loadResource(GRAMMAR_FILE_PATH, this.getClass());
    List<GrammarEntry> grammarEntries = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class)
                                                    .getGrammarEntries();
    this.bindingResolver = new BindingResolver(grammarEntries);
  }

  @Test
  void resolve() {
    GrammarWrapper grammarWrapper = bindingResolver.resolve();
    assertNotNull(grammarWrapper);
    assertNotEquals(0, grammarWrapper.getGrammarEntries().size());
    grammarWrapper.getGrammarEntries().stream().map(grammarEntry -> grammarEntry.getSentences().size() > 0 ? grammarEntry.getSentences().get(0) : grammarEntry.getSentences()).forEach(System.out::println);
  }
}
