package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.structure.component.Language;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.List;

import static grammar.generator.SentenceTemplateParser.QUESTION_MARK;
import java.util.Collection;
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
  public List<String> generateFullSentencesForward(String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
    List<String> generatedSentences = new ArrayList<>();
    // get to be forms
    LexicalEntry entry = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource("component_be");
    List<AnnotatedVerb> toBeVerbs = this.lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs(entry.getOtherForms());
    String subject = this.lexicalEntryUtil.getSubjectBySubjectType(SubjectType.interrogativeDeterminerSingular, language, null);

    String separator = " ";
    for (AnnotatedVerb toBeForm : toBeVerbs) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(subject)
                   .append(separator)
                   .append(toBeForm.getWrittenRepValue())
                   .append(separator)
                   .append(generateFullSentencesBackward(bindingVar, new String[]{toBeForm.getNumber().getURI().getFragment()},
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
  public List<String> generateFullSentencesBackward(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) {
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
          this.lexicalEntryUtil.getLexicalEntry().getCanonicalForm().getWrittenRep().value
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

    @Override
    public List<String> generateBooleanQuestionDomainRange(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateBooleanQuestionsDomain(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateBackwardAmount(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateForwardAmount(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTemplate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
   
}

