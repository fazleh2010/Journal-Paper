package lexicon;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.Lexicon;

import java.net.URI;

public class LexiconSearch {
  /**
   * The base uri of the user lexicon.
   */
  public static final String LEXICON_BASE_URI = "http://localhost:8080#";

  private final Lexicon lexicon;

  public LexiconSearch(Lexicon lexicon) {
    this.lexicon = lexicon;
  }

  /**
   * Get the specified {@link LexicalEntry} from the lexicon by string name.
   *
   * @param resource the string name of the lexical entry that should be retrieved
   * @return the lexical entry that matches the specified resource name or null if there was no match
   */
  public LexicalEntry getReferencedResource(String resource) {
    return lexicon.getEntrys().stream()
                  .filter(lexicalEntry1 -> lexicalEntry1.getURI().equals(createLexiconURI(resource)))
                  .findFirst()
                  .orElse(null);
  }

  /**
   * Create an URI for the specified lexical entry name string
   *
   * @param lexicalEntry the string name of the lexical entry inside the user lexicon
   * @return an URI with the {@link LexiconSearch#LEXICON_BASE_URI}
   */
  private URI createLexiconURI(String lexicalEntry) {
    return URI.create(LEXICON_BASE_URI + lexicalEntry);
  }

  /**
   * Get the specified {@link LexicalEntry} from the lexicon by URI.
   *
   * @param resource the URI reference of the lexical entry that should be retrieved
   * @return the lexical entry that matches the specified resource name or null if there was no match
   */
  public LexicalEntry getReferencedResource(URI resource) {
    return lexicon.getEntrys().stream()
                  .filter(lexicalEntry1 -> lexicalEntry1.getURI().equals(resource))
                  .findFirst()
                  .orElse(null);
  }
}
