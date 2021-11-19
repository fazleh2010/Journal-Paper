package grammar.structure.component;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
public class GrammarWrapper {
  private List<GrammarEntry> grammarEntries = new ArrayList<>();

  /**
   * Merge another GrammarWrapper with this one.
   *
   * @param grammarWrapper the GrammarWrapper to merge
   */
  public void merge(GrammarWrapper grammarWrapper) {
    if (!isNull(grammarWrapper)) {
      grammarEntries.addAll(grammarWrapper.getGrammarEntries());
    }
  }
}
