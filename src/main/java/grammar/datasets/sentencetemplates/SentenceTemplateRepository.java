package grammar.datasets.sentencetemplates;

import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;

import java.util.List;

public interface SentenceTemplateRepository {

  List<String> getAll();

  List<String> findOneByEntryTypeAndLanguageAndArguments(
    SentenceType entryType,
    Language language,
    String... arguments
  );

  void add(SentenceTemplate sentenceTemplate);
}
