package grammar.generator;

import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import java.util.ArrayList;
import lexicon.LexicalEntryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.List;
import java.util.stream.Collectors;

public class APPGrammarRuleGenerator extends GrammarRuleGeneratorRoot {

    private static final Logger LOG = LogManager.getLogger(APPGrammarRuleGenerator.class);

    public APPGrammarRuleGenerator(Language language) {
        super(FrameType.APP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }

    @Override
    public List<String> generateSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        SentenceBuilder sentenceBuilder = new SentenceBuilderCopulativePP(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                getSentenceTemplateParser()
        );

        return sentenceBuilder.generateFullSentencesForward(getBindingVariable(), lexicalEntryUtil);
    }

    @Override
    public List<GrammarEntry> generateFragmentEntry(
            GrammarEntry grammarEntry,
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries=new ArrayList<GrammarEntry>();
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.NP);
        fragmentEntry.setLexicalEntryUri(lexicalEntryUtil.getLexicalEntry().getURI());

        SentenceBuilder sentenceBuilder = new SentenceBuilderCopulativePP(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                getSentenceTemplateParser()
        );
        List<String> generatedSentences = sentenceBuilder.generateFullSentencesBackward(
                getBindingVariable(),
                new String[]{"copulativeSubject", "prepositionalAdjunct"},
                lexicalEntryUtil
        );
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
        fragmentEntry.setSentences(generatedSentences);
        grammarEntries.add(fragmentEntry);
        return grammarEntries;
    }

}
