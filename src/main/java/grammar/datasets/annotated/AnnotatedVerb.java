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
public class AnnotatedVerb extends AnnotatedWord {
  private final PropertyValue number;
  private final PropertyValue person;
  private final PropertyValue tense;
  private PropertyValue verbFormMood;
  private PropertyValue aspect;

  public AnnotatedVerb(
    @NotEmpty String writtenRepValue,
    Language language,
    PropertyValue number,
    PropertyValue person,
    PropertyValue tense
  ) {
    super("verb", writtenRepValue, language);
    this.number = number;
    this.person = person;
    this.tense = tense;
  }

  public AnnotatedVerb(
    @NotEmpty String writtenRepValue,
    Language language,
    @NotEmpty String number,
    @NotEmpty String person,
    @NotEmpty String tense
  ) {
    super("verb", writtenRepValue, language);
    this.number = lexInfo.getPropertyValue(number);
    this.person = lexInfo.getPropertyValue(person);
    this.tense = lexInfo.getPropertyValue(tense);
  }
}
