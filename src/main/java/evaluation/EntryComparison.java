package evaluation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EntryComparison implements Serializable {
  public static final long serialVersioUID = 2L;
  private Entry qaldEntry;
  private Entry queGGEntry;
  private Boolean matchedFlag=false;
  private List<String> qaldResults;
  private List<String> queGGResults;
  private Double SimilarityCsore;

  private float tp = 0, fp = 0, fn = 0, precision = 0, recall = 0, f_measure = 0;

  public void addTp(float newValue) {
    tp += newValue;
  }

  public void addFp(float newValue) {
    fp += newValue;
  }

  public void addFn(float newValue) {
    fn += newValue;
  }
  
}
