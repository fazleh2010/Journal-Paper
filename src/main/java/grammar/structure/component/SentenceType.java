package grammar.structure.component;

import java.util.stream.Stream;

public enum SentenceType {
  SENTENCE,
  NP,
  VP,
  AP;

  public static SentenceType getMatchingType(String name) {
    return Stream.of(SentenceType.values())
                 .filter(enumValue -> enumValue.name().equals(name))
                 .findFirst()
                 .orElse(null);
  }
}
