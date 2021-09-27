package grammar.generator.helper;

import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.List;

public interface SentenceBuilder {
  List<String> generateFullSentences(String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;

  List<String> generateNP(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;
}
