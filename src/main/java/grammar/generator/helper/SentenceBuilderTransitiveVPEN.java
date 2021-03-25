package grammar.generator.helper;

public class SentenceBuilderTransitiveVPEN {
  private final String subject;
  private final String verb;
  private final String object;
  private final String particle;

  public SentenceBuilderTransitiveVPEN(String subject, String verb, String object, String particle) {
    this.subject = subject;
    this.verb = verb;
    this.object = object;
    this.particle = particle;
  }

  public String getSentence() {
    String sentence;
    sentence = sentenceSubjOfPropActive();
    return sentence;
  }

  // Who writes $x?
  private String sentenceSubjOfPropActive() {
    if (particle == null) {
      return String.format("%s %s %s?", subject, verb, object);
    } else {
      return String.format("%s %s %s %s?", subject, verb, object, particle);
    }
  }
}
