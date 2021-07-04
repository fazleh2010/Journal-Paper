package grammar.generator;

import grammar.generator.helper.BindingConstants;
import grammar.generator.helper.SentenceBuilderTransitiveVPEN;
import grammar.generator.helper.SubjectType;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import net.lexinfo.LexInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.List;

import static grammar.generator.helper.BindingConstants.BINDING_TOKEN_TEMPLATE;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;

public class TransitiveVPGrammarRuleGenerator extends GrammarRuleGeneratorRoot {
  private static final Logger LOG = LogManager.getLogger(TransitiveVPGrammarRuleGenerator.class);

  public TransitiveVPGrammarRuleGenerator(Language language) {
    super(FrameType.VP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
  }

  @Override
  public List<String> generateSentences(LexicalEntryUtil lexicalEntryUtil) throws
                                                                                                          QueGGMissingFactoryClassException {
    List<String> generatedSentences = new ArrayList<>();

    //SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(),DomainOrRangeType.PERSON);
    
    SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable());

    String qWord = lexicalEntryUtil.getSubjectBySubjectType(subjectType, getLanguage(), null);

    List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
    for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
      // skip infinitive forms
      if (new LexInfo().getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
        continue;
      }
      // Make simple sentence (who develops $x?)
      SentenceBuilderTransitiveVPEN sentenceBuilder = new SentenceBuilderTransitiveVPEN(
        qWord,
        annotatedVerb.getWrittenRepValue(),
        String.format(
          BINDING_TOKEN_TEMPLATE,
          getBindingVariable(),
          DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
            LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
          )).name(),
          SentenceType.NP
        )
      );
      String sentence = sentenceBuilder.getSentence();
      generatedSentences.add(sentence);
      // Make sentence using the specified domain or range property (Which museum exhibits $x?)
      String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());
      // Only generate "Which <condition-label>" if condition label is a DBPedia entity
      if (lexicalEntryUtil.hasInvalidDeterminerToken(lexicalEntryUtil.getSelectVariable())) {
        continue;
      }
      String determiner = lexicalEntryUtil.getSubjectBySubjectType(
        SubjectType.INTERROGATIVE_DETERMINER,
        getLanguage(),
        null
      );
      String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
      SentenceBuilderTransitiveVPEN determinerSentenceBuilder = new SentenceBuilderTransitiveVPEN(
        determinerToken,
        annotatedVerb.getWrittenRepValue(),
        String.format(
          BINDING_TOKEN_TEMPLATE,
          getBindingVariable(),
          DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
            LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
          )).name(),
          SentenceType.NP
        )
      );
      sentence = determinerSentenceBuilder.getSentence();
      generatedSentences.add(sentence);
    }
    generatedSentences.sort(String::compareToIgnoreCase);
    return generatedSentences;
  }
}
