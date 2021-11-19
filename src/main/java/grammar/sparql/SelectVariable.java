package grammar.sparql;

public enum SelectVariable {
  subjOfProp("subjOfProp"),
  objOfProp("objOfProp"),
  reference("reference"),
  IS_A("isA");

  private final String name;

  SelectVariable(String name) {
    this.name = name;
  }

  public String getVariableName() {
    return name;
  }

  public String mapDomainOrRange() {
    return equals(subjOfProp) ? "domain" : "range";
  }
}

