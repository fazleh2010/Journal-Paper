package grammar.datasets.annotated;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.structure.component.Language;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AnnotatedNoun extends AnnotatedWord implements AnnotatedNounOrQuestionWord {
  private final PropertyValue number;
  private PropertyValue gender;
  private PropertyValue grammaticalCase;
  /**
   * Definiteness of determiners.<br>
   * Can also be used for some languages that express definiteness using noun inflections.<br>
   * Values: indefinite, shortArticle, fullArticle, definite
   */
  private PropertyValue definiteness;

  public AnnotatedNoun(
    @NotEmpty String writtenRepValue,
    PropertyValue number,
    Language language
  ) {
    super("noun", writtenRepValue, language);
    this.number = number;
  }

  public AnnotatedNoun(
    @NotEmpty String writtenRepValue,
    String number,
    Language language
  ) {
    super("noun", writtenRepValue, language);
    this.number = lexInfo.getPropertyValue(number);
  }

  public AnnotatedNoun(
    @NotEmpty String writtenRepValue,
    @NotEmpty String number,
    @NotEmpty String gender,
    Language language
  ) {
    super("noun", writtenRepValue, language);
    this.number = lexInfo.getPropertyValue(number);
    this.gender = lexInfo.getPropertyValue(gender);
  }

  public AnnotatedNoun(
    @NotEmpty String writtenRepValue,
    PropertyValue number,
    PropertyValue gender,
    Language language
  ) {
    super("noun", writtenRepValue, language);
    this.number = number;
    this.gender = gender;
  }
}
