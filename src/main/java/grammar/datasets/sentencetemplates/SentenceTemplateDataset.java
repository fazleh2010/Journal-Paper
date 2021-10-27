package grammar.datasets.sentencetemplates;

import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SentenceTemplateDataset implements SentenceTemplateRepository {

  private final List<SentenceTemplate> sentenceTemplates;

  SentenceTemplateDataset() {
    this.sentenceTemplates = new ArrayList<>();
  }

  @Override
  public List<String> getAll() {
    return this.sentenceTemplates
      .stream()
      .map(SentenceTemplate::getTemplates)
      .findAny()
      .orElse(new ArrayList<>());
  }

  @Override
  public List<String> findOneByEntryTypeAndLanguageAndArguments(
    SentenceType entryType, Language language, String... arguments
  ) {
    return this.sentenceTemplates
      .stream()
      .filter(sentenceTemplate -> sentenceTemplate.getEntryType().equals(entryType))
      .filter(sentenceTemplate -> sentenceTemplate.getLanguage().equals(language))
      .filter(sentenceTemplate -> Arrays.equals(sentenceTemplate.getArguments(), arguments))
      .map(SentenceTemplate::getTemplates)
      .findAny()
      .orElse(new ArrayList<>());
  }

  @Override
  public void add(SentenceTemplate sentenceTemplate) {
    this.sentenceTemplates.add(sentenceTemplate);
  }
}
