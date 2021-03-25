package grammar.generator.helper;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.structure.component.Language;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.List;

import static grammar.generator.helper.parser.SentenceTemplateParser.QUESTION_MARK;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;

public class SentenceBuilderAdjectiveAttributiveEN implements SentenceBuilder {
  //  private String questionWord;
  private final Language language;
  private final LexicalEntryUtil lexicalEntryUtil;

  public SentenceBuilderAdjectiveAttributiveEN(Language language, LexicalEntryUtil lexicalEntryUtil) {
    this.language = language;
    this.lexicalEntryUtil = lexicalEntryUtil;
  }

  @Override
  public List<String> generateFullSentences(String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
    List<String> generatedSentences = new ArrayList<>();
    // get to be forms
    LexicalEntry entry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource("component_be");
    List<AnnotatedVerb> toBeVerbs = this.lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs(entry.getOtherForms());

    String separator = " ";
    for (AnnotatedVerb toBeForm : toBeVerbs) {
      String subject = this.lexicalEntryUtil.getSubjectBySubjectTypeAndNumber(SubjectType.INTERROGATIVE_DETERMINER, language, toBeForm.getNumber(), null);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(subject)
                   .append(separator)
                   .append(toBeForm.getWrittenRepValue())
                   .append(separator)
                   .append(generateNP(bindingVar, new String[]{toBeForm.getNumber().getURI().getFragment()},
                                      lexicalEntryUtil).get(0))
                   .append(QUESTION_MARK);

      String sentence = stringBuilder.toString();
      if (!generatedSentences.contains(sentence)) {
        generatedSentences.add(stringBuilder.toString());
      }
    }
    generatedSentences.sort(String::compareToIgnoreCase);
    return generatedSentences;
  }

  @Override
  public List<String> generateNP(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) {
    List<String> generatedSentences = new ArrayList<>();
    List<PropertyValue> numberList = new ArrayList<>();
    // Get determiner "a"
    LexicalEntry determinerA = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource("component_a");
    numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
    numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
    String separator = " ";
    for (PropertyValue number : numberList) {
      StringBuilder stringBuilder = new StringBuilder();
      if (argument.length > 0 && !argument[0].isBlank() && !argument[0].equals(number.getURI().getFragment())) {
        continue;
      } else {
        if (number.equals(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"))) {
          stringBuilder.append(determinerA.getCanonicalForm().getWrittenRep().value)
                       .append(separator);
        }
        stringBuilder.append(getDeterminerTokenByNumber(
          number,
          bindingVar,
          this.lexicalEntryUtil.getLexicalEntry().getCanonicalForm().getWrittenRep().value,
          language
        ));
      }
      String sentence = stringBuilder.toString();
      if (!generatedSentences.contains(sentence)) {
        generatedSentences.add(sentence);
      }
    }
    generatedSentences.sort(String::compareToIgnoreCase);
    return generatedSentences;
  }
}

