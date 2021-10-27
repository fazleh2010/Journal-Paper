package grammar.datasets.annotated;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.structure.component.Language;
import net.lexinfo.LexInfo;

public interface AnnotatedNounOrQuestionWord {
  PropertyValue getNumber();

  PropertyValue getGender();

  void setGender(PropertyValue gender);

  PropertyValue getGrammaticalCase();

  void setGrammaticalCase(PropertyValue grammaticalCase);

  PropertyValue getDefiniteness();

  void setDefiniteness(PropertyValue definiteness);

  LexInfo getLexInfo();

  Language getLanguage();

  PropertyValue getPOSTag();

  String getWrittenRepValue();
}
