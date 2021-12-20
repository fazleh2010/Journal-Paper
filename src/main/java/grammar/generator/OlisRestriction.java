/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.Lexicon;
import static java.lang.System.exit;
import java.net.URI;
import static java.util.Objects.isNull;
import lexicon.LexiconSearch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author elahi
 */
public class OlisRestriction {
  private static final Logger LOG = LogManager.getLogger(OlisRestriction.class);

  private final Lexicon lexicon;
  private final URI lexicalSenseReference;
  private String property;
  private String value;

  public OlisRestriction(Lexicon lexicon, URI lexicalSenseReference) {
    this.lexicon = lexicon;
    this.lexicalSenseReference = lexicalSenseReference;
  }

  public String getProperty() {
    return property;
  }

  public String getValue() {
    return value;
  }

  public OlisRestriction invoke() {
    property = "";
    value = "";
    LexicalEntry lexicalEntryAdj = new LexiconSearch(lexicon).getReferencedResource(lexicalSenseReference);
       
    if (!isNull(lexicalEntryAdj)) {
      property = lexicalEntryAdj.getAnnotations().get(URI.create(Olis.onProperty.getURI())).iterator().next().toString();
      value = lexicalEntryAdj.getAnnotations().get(URI.create(Olis.hasValue.getURI())).iterator().next().toString();
    } else if (lexicalSenseReference.toString().contains(LexiconSearch.LEXICON_BASE_URI)) {
      // lexicalSenseReference is a reference to a local lexicalEntry, but it could not be found.
      LOG.error("Could not find lexical entry {}", lexicalSenseReference);
    }
    
    
    return this;
  }

    @Override
    public String toString() {
        return "OlisRestriction{" + "lexicon=" + lexicon + ", lexicalSenseReference=" + lexicalSenseReference + ", property=" + property + ", value=" + value + '}';
    }

}

