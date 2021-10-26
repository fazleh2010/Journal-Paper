package grammar.generator;

import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.List;

public interface SentenceBuilder {
  List<String> generateFullSentencesForward(String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;

  List<String> generateFullSentencesBackward(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException;
}
