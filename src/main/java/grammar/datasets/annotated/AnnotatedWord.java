package grammar.datasets.annotated;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.structure.component.Language;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.lexinfo.LexInfo;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@EqualsAndHashCode
public abstract class AnnotatedWord {
  protected final LexInfo lexInfo;
  private final Language language;
  private PropertyValue POSTag;
  private final String writtenRepValue;

  AnnotatedWord(@NotEmpty String POSTag, @NotEmpty String writtenRepValue, Language language) {
    this.lexInfo = new LexInfo();
    this.POSTag = lexInfo.getPropertyValue(POSTag);
    this.writtenRepValue = writtenRepValue;
    this.language = language;
  }

  protected void setPOSTag(PropertyValue POSTag) {this.POSTag = POSTag; }
}
