package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.Lexicon;
import lexicon.LexiconSearch;
import org.apache.jena.vocabulary.OWL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

import static java.util.Objects.isNull;

public class OWLRestriction {
  private static final Logger LOG = LogManager.getLogger(OWLRestriction.class);

  private final Lexicon lexicon;
  private final URI lexicalSenseReference;
  private String property;
  private String value;

  public OWLRestriction(Lexicon lexicon, URI lexicalSenseReference) {
    this.lexicon = lexicon;
    this.lexicalSenseReference = lexicalSenseReference;
  }

  public String getProperty() {
    return property;
  }

  public String getValue() {
    return value;
  }

  public OWLRestriction invoke() {
    property = "";
    value = "";
    LexicalEntry lexicalEntryAdj = new LexiconSearch(lexicon).getReferencedResource(lexicalSenseReference);
    if (!isNull(lexicalEntryAdj)) {
      property = lexicalEntryAdj.getAnnotations().get(URI.create(OWL.onProperty.getURI())).iterator().next().toString();
      value = lexicalEntryAdj.getAnnotations().get(URI.create(OWL.hasValue.getURI())).iterator().next().toString();
    } else if (lexicalSenseReference.toString().contains(LexiconSearch.LEXICON_BASE_URI)) {
      // lexicalSenseReference is a reference to a local lexicalEntry, but it could not be found.
      LOG.error("Could not find lexical entry {}", lexicalSenseReference);
    }
    return this;
  }

    @Override
    public String toString() {
        return "OWLRestriction{" + "lexicon=" + lexicon + ", lexicalSenseReference=" + lexicalSenseReference + ", property=" + property + ", value=" + value + '}';
    }

}
