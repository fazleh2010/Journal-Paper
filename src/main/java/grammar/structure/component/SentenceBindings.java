package grammar.structure.component;

import lombok.Data;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

@Data
public class SentenceBindings {
  private String bindingVariableName;
  private List<Binding> bindingList;

  @Transient
  public SentenceBindings deepCopy() {
    SentenceBindings sentenceBindings = new SentenceBindings();
    sentenceBindings.bindingVariableName = this.bindingVariableName;
    List<Binding> bindingList = new ArrayList<>();
    this.bindingList.forEach(binding -> bindingList.add(binding.copy()));
    sentenceBindings.bindingList = bindingList;
    return sentenceBindings;
  }

  public String toString() {
    return "  SentenceBindings(\n" +
      "    bindingVariableName =\t" + this.getBindingVariableName() + ",\n" +
      "    bindingList =\n      " + (!isNull(this.getBindingList()) ?
                                     this.getBindingList().toString() :
                                     Collections.EMPTY_LIST) + "\n  )";
  }
}
