package evaluation;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EvaluationResult implements Serializable {
  public static final long serialVersioUID = 1L;
  float tp_global = 0, fp_global = 0, fn_global = 0, precision_global = 0, recall_global = 0, f_measure_global = 0;
  List<EntryComparison> entryComparisons = new ArrayList<EntryComparison>();
}
