package evaluation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Entry implements Serializable {
  public static final long serialVersioUID = 3L;
  private String id, questions, sparql,className;
  private List<String> questionList;
  private Object actualEntry;

  
}
