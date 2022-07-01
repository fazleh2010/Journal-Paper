package grammar.sparql;

public enum Prefix {
  RDFS("http://www.w3.org/2000/01/rdf-schema#"),
  DBPEDIA("http://dbpedia.org/"),
  DBO(DBPEDIA.getUri() + "ontology/"),
  DBP(DBPEDIA.getUri() + "property/"),
  DBR(DBPEDIA.getUri() + "resource/"),
  LEXINFO("http://www.lexinfo.net/ontology/2.0/lexinfo#"),
  LEMON("http://lemon-model.net/lemon#"),
  AIFD("http://swrc.ontoware.org/ontology#"),
  OWL("http://www.w3.org/2002/07/owl#");

  private final String uri;

  Prefix(String uri) {
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }
}
