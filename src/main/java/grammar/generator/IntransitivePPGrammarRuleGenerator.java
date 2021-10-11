package grammar.generator;

import grammar.generator.helper.BindingConstants;
import grammar.generator.helper.SentenceBuilder;
import grammar.generator.helper.SentenceBuilderIntransitivePPEN;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntransitivePPGrammarRuleGenerator  extends GrammarRuleGeneratorRoot {

    public IntransitivePPGrammarRuleGenerator(Language language) {
        super(FrameType.IPP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }

    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();

        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            SentenceBuilder sentenceBuilder = new SentenceBuilderIntransitivePPEN(
                    this.language,
                    annotatedVerb,
                    lexicalEntryUtil
            );
            generatedSentences.addAll(sentenceBuilder.generateFullSentences(getBindingVariable(), lexicalEntryUtil));
        }
        generatedSentences.sort(String::compareToIgnoreCase);
        return generatedSentences;
    }

    /**
     * Generate an entry with sentence structure: Which _noun_ does $x _verb_
     * _preposition_?
     */
    public GrammarEntry generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getBindingVariable());
        Map<String, String> sentenceToSparqlParameterMapping = new HashMap<String, String>();
        sentenceToSparqlParameterMapping.put(grammarEntry.getSentenceBindings().getBindingVariableName(),
                grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceToSparqlParameterMapping(sentenceToSparqlParameterMapping);

        // sentences
        List<String> generatedSentences = generateOppositeSentences(lexicalEntryUtil);
        fragmentEntry.setSentences(generatedSentences);

        return fragmentEntry;
    }

    protected List<String> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            SentenceBuilder sentenceBuilder = new SentenceBuilderIntransitivePPEN(
                    this.language,
                    annotatedVerb,
                    lexicalEntryUtil
            );
            generatedSentences.addAll(sentenceBuilder.generateNP(getBindingVariable(), new String[]{}, lexicalEntryUtil));
            generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
            generatedSentences.sort(String::compareToIgnoreCase);
        }
        return generatedSentences;
    }
}
