package grammar.generator.helper;

public class SentenceBuilderTransitiveVPEN {
  private final String subject;
  private final String verb;
  private final String object;

  public SentenceBuilderTransitiveVPEN(String subject, String verb, String object) {
    this.subject = subject;
    this.verb = verb;
    this.object = object;
  }

  public String getSentence() {
    String sentence;
    sentence = sentenceSubjOfPropActive();
    return sentence;
  }

  // Who writes $x?
  private String sentenceSubjOfPropActive() {
    return String.format("%s %s %s?", subject, verb, object);
  }
}
