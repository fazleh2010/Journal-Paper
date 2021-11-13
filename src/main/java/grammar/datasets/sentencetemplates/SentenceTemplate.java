package grammar.datasets.sentencetemplates;

import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lombok.Getter;

import java.util.List;

/**
 * A full sentence grammar rule string representation can look like this:<br>
 * {@code
 * SentenceTemplate(copulativeArg, prepositionalAdjunct) = "NP(copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
 * NP(copulativeArg) = interrogativeDeterminer noun(copulativeArg:condition)
 * NP(prepositionalAdjunct) = "determiner noun preposition prepositionalAdjunct"
 * }
 */
@Getter
class SentenceTemplate {

  private final SentenceType entryType;
  private final Language language;
  private final List<String> templates;
  
  /**
   * These are the arguments of the respective sentence template.<br>
   * Valid values are:
   * <ul>
   *   <li>copulativeArg</li>
   *   <li>subject</li>
   *   <li>attributiveArg</li>
   *   <li>copulativeSubject</li>
   *   <li>directObject</li>
   *   <li>prepositionalAdjunct</li>
   * </ul>
   */
  private final String[] arguments;

  /**
   * Create a SentenceTemplate.
   *
   * @param entryType the {@link SentenceType} of this template
   * @param language  the language of this template (sentence structure can depend on language...)
   * @param templates a list of plain sentence template strings. Such a string can look like this: "NP(copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
   * @param arguments the arguments of the frame of this sentence template
   */
  SentenceTemplate(
    SentenceType entryType,
    Language language,
    List<String> templates,
    String... arguments
  ) {
    this.entryType = entryType;
    this.language = language;
    this.templates = templates;
    this.arguments = arguments;
  }

  /**
   * Create a SentenceTemplate with the default value SentenceType.SENTENCE.
   *
   * @param language  the language of this template (sentence structure can depend on language...)
   * @param templates a list of plain sentence template strings. Such a string can look like this: "NP(copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
   * @param arguments the arguments of the frame of this sentence template
   * @return an instance of SentenceTemplate
   */
  public static SentenceTemplate createSentenceTemplate(
    Language language,
    List<String> templates,
    String... arguments
  ) {
    return new SentenceTemplate(SentenceType.SENTENCE, language, templates, arguments);
  }

  /**
   * Create a SentenceTemplate with the default value SentenceType.NP.
   *
   * @param language  the language of this template (sentence structure can depend on language...)
   * @param templates a list of plain sentence template strings. Such a string can look like this: "NP(copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
   * @param arguments the arguments of the frame of this sentence template
   * @return an instance of SentenceTemplate
   */
  public static SentenceTemplate createNPTemplate(
    Language language,
    List<String> templates,
    String... arguments
  ) {
    return new SentenceTemplate(SentenceType.NP, language, templates, arguments);
  }

  /**
   * Create a SentenceTemplate with the default value SentenceType.VP.
   *
   * @param language  the language of this template (sentence structure can depend on language...)
   * @param templates a list of plain sentence template strings. Such a string can look like this: "NP(copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
   * @param arguments the arguments of the frame of this sentence template
   * @return an instance of SentenceTemplate
   */
  public static SentenceTemplate createVPTemplate(
    Language language,
    List<String> templates,
    String... arguments
  ) {
    return new SentenceTemplate(SentenceType.VP, language, templates, arguments);
  }

  /**
   * Create a SentenceTemplate with the default value SentenceType.AP.
   *
   * @param language  the language of this template (sentence structure can depend on language...)
   * @param templates a list of plain sentence template strings. Such a string can look like this: "NP(copulativeArg) verb(reference:component_be) NP(prepositionalAdjunct)?"
   * @param arguments the arguments of the frame of this sentence template
   * @return an instance of SentenceTemplate
   */
  public static SentenceTemplate createAPTemplate(
    Language language,
    List<String> templates,
    String... arguments
  ) {
    return new SentenceTemplate(SentenceType.AP, language, templates, arguments);
  }
}
