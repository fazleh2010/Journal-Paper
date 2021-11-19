package grammar.datasets.annotated;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.structure.component.Language;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
public class AnnotatedInterrogativeDeterminer extends AnnotatedDeterminer implements AnnotatedNounOrQuestionWord {

  public AnnotatedInterrogativeDeterminer(
    @NotEmpty String writtenRepValue,
    PropertyValue number,
    Language language
  ) {
    super(writtenRepValue, number, language);
    setPOSTag(lexInfo.getPropertyValue("interrogativeDeterminer"));
  }

  public AnnotatedInterrogativeDeterminer(
    @NotEmpty String writtenRepValue,
    String number,
    Language language
  ) {
    super(writtenRepValue, number, language);
    setPOSTag(lexInfo.getPropertyValue("interrogativeDeterminer"));
  }

  public AnnotatedInterrogativeDeterminer(
    @NotEmpty String writtenRepValue,
    @NotEmpty String number,
    @NotEmpty String gender,
    Language language
  ) {
    super(writtenRepValue, number, gender, language);
    setPOSTag(lexInfo.getPropertyValue("interrogativeDeterminer"));
  }

  public AnnotatedInterrogativeDeterminer(
    @NotEmpty String writtenRepValue,
    PropertyValue number,
    PropertyValue gender,
    Language language
  ) {
    super(writtenRepValue, number, gender, language);
    setPOSTag(lexInfo.getPropertyValue("interrogativeDeterminer"));
  }
}
