package grammar.datasets.annotated;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.structure.component.Language;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@ToString
@EqualsAndHashCode(callSuper = true)
public class AnnotatedAdjective extends AnnotatedDeterminer implements AnnotatedNounOrQuestionWord {

  public AnnotatedAdjective(
    @NotEmpty String writtenRepValue,
    PropertyValue number,
    Language language
  ) {
    super(writtenRepValue, number, language);
    setPOSTag(lexInfo.getPropertyValue("adjective"));
  }

  public AnnotatedAdjective(
    @NotEmpty String writtenRepValue,
    String number,
    Language language
  ) {
    super(writtenRepValue, number, language);
    setPOSTag(lexInfo.getPropertyValue("adjective"));
  }

  public AnnotatedAdjective(
    @NotEmpty String writtenRepValue,
    @NotEmpty String number,
    @NotEmpty String gender,
    Language language
  ) {
    super(writtenRepValue, number, gender, language);
    setPOSTag(lexInfo.getPropertyValue("adjective"));
  }

  public AnnotatedAdjective(
    @NotEmpty String writtenRepValue,
    PropertyValue number,
    PropertyValue gender,
    Language language
  ) {
    super(writtenRepValue, number, gender, language);
    setPOSTag(lexInfo.getPropertyValue("adjective"));
  }
}
