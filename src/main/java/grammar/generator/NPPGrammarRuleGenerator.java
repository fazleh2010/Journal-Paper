package grammar.generator;

import grammar.generator.helper.BindingConstants;
import grammar.generator.helper.SentenceBuilder;
import grammar.generator.helper.SentenceBuilderCopulativePP;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import lexicon.LexicalEntryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class NPPGrammarRuleGenerator extends GrammarRuleGeneratorRoot {
  private static final Logger LOG = LogManager.getLogger(NPPGrammarRuleGenerator.class);

  public NPPGrammarRuleGenerator(Language language) {
    super(FrameType.NPP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
  }

  @Override
  public List<String> generateSentences(LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
    SentenceBuilder sentenceBuilder = new SentenceBuilderCopulativePP(
      getLanguage(),
      getFrameType(),
      getSentenceTemplateRepository(),
      getSentenceTemplateParser()
    );

    List<String> sentences= sentenceBuilder.generateFullSentences(getBindingVariable(), lexicalEntryUtil);
    return sentences;
  }

  @Override
  public GrammarEntry generateFragmentEntry(
    GrammarEntry grammarEntry,
    LexicalEntryUtil lexicalEntryUtil
  ) throws QueGGMissingFactoryClassException {
    GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
    fragmentEntry.setType(SentenceType.NP);

    SentenceBuilder sentenceBuilder = new SentenceBuilderCopulativePP(
      getLanguage(),
      getFrameType(),
      getSentenceTemplateRepository(),
      getSentenceTemplateParser()
    );
    List<String> generatedSentences = sentenceBuilder.generateNP(getBindingVariable(),
                                                                 new String[]{"prepositionalAdjunct"},
                                                                 lexicalEntryUtil
    );
    generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
    fragmentEntry.setSentences(generatedSentences);

    return fragmentEntry;
  }

    /*private List<String> filterSentences(List<String> sentences) {
        for (String sentence : sentences) {
            String[] result = sentence.split("\\s");
            for (int x = 0; x < result.length; x++) {
                System.out.println(result[x]);
                if (result[x].contains("-")) {

                }
            }
        }

    }*/
}
