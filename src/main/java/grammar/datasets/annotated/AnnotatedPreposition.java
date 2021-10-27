package grammar.datasets.annotated;

import grammar.structure.component.Language;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
public class AnnotatedPreposition extends AnnotatedWord {
  public AnnotatedPreposition(
    @NotEmpty String writtenRepValue,
    Language language
  ) {
    super("preposition", writtenRepValue, language);
  }
}
