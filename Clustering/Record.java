import java.util.HashMap;

class Record {
  private HashMap<String, Double> values;
  private Integer label;

  public Record(HashMap<String, Double> record) {
    this.values = record;
  }

  public void setLabel(Integer label) {
    this.label = label;
  }

  public HashMap<String, Double> getValues() {
    return values;
  }

  public Integer getLabel() {
    return label;
  }
}
