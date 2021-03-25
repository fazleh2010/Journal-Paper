package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import grammar.generator.helper.BindingConstants;
import grammar.generator.helper.SentenceBuilderTransitiveVPEN;
import grammar.generator.helper.SubjectType;
import grammar.generator.helper.sentencetemplates.AnnotatedNounOrQuestionWord;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.net.URI;
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

    SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(),DomainOrRangeType.PERSON);
    String qWord = lexicalEntryUtil.getSubjectBySubjectTypeAndNumber(subjectType, getLanguage(), new LexInfo().getPropertyValue("singular"), null);

    List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
    for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
      // skip infinitive forms
      if (new LexInfo().getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
        continue;
      }
      String particle = lexicalEntryUtil.getVerbParticle();
      String sentence;
      // Make simple sentence (who develops $x?)
      if (annotatedVerb.getNumber().equals(new LexInfo().getPropertyValue("singular"))) {
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
                ),
                particle
        );
        sentence = sentenceBuilder.getSentence();
        generatedSentences.add(sentence);
      }
      // Make sentence using the specified domain or range property (Which museum exhibits $x?)
      String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());
      // Only generate "Which <condition-label>" if condition label is a DBPedia entity
      if (lexicalEntryUtil.hasInvalidDeterminerToken(lexicalEntryUtil.getSelectVariable())) {
        continue;
      }
      String determiner = lexicalEntryUtil.getSubjectBySubjectTypeAndNumber(
        SubjectType.INTERROGATIVE_DETERMINER,
        getLanguage(),
        annotatedVerb.getNumber(),
        null
      );

      // Get noun for determiner token
      String determinerToken;

      if (getLanguage().equals(Language.DE)) {
        URI nounRef;
        if (conditionLabel.contains(" ")) {
          nounRef = URI.create(LexiconSearch.LEXICON_BASE_URI + conditionLabel.replace(' ', '_').toLowerCase() + "_weak");
        } else {
          nounRef = URI.create(LexiconSearch.LEXICON_BASE_URI + conditionLabel.toLowerCase());
        }
        LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(nounRef);
        String questionNoun = entry.getCanonicalForm().getWrittenRep().value;
        List<AnnotatedNounOrQuestionWord> questionWordNoun = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords(entry.getOtherForms());

        for (AnnotatedNounOrQuestionWord noun : questionWordNoun) {
          if (noun.getNumber().equals(annotatedVerb.getNumber())) {
            questionNoun = noun.getWrittenRepValue();
          }
        }

        determinerToken = getDeterminerTokenByNumber(
                annotatedVerb.getNumber(),
                questionNoun,
                determiner,
                getLanguage()
        );
      } else {
        determinerToken = getDeterminerTokenByNumber(
                annotatedVerb.getNumber(),
                conditionLabel,
                determiner,
                getLanguage()
        );
      }


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
        ),
        particle
      );
      sentence = determinerSentenceBuilder.getSentence();
      generatedSentences.add(sentence);
    }
    generatedSentences.sort(String::compareToIgnoreCase);
    return generatedSentences;
  }
}
