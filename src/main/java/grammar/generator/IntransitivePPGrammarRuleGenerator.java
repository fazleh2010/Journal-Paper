package grammar.generator;

import grammar.datasets.annotated.AnnotatedVerb;
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

public class IntransitivePPGrammarRuleGenerator extends GrammarRuleGeneratorRoot {
    
    private String template = null;


    public IntransitivePPGrammarRuleGenerator(Language language) {
        super(FrameType.IPP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }

    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();

        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                getSentenceTemplateParser(),
                lexicalEntryUtil
        );
        generatedSentences.addAll(sentenceBuilder.generateFullSentencesForward(getBindingVariable(), lexicalEntryUtil));
         this.template=sentenceBuilder.getTemplate();

        //generatedSentences.sort(String::compareToIgnoreCase);
        return generatedSentences;
    }

    protected List<String> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        SentenceBuilder sentenceBuilder = new SentenceBuilderAllFrame(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                getSentenceTemplateParser(),
                lexicalEntryUtil
        );
        generatedSentences.addAll(sentenceBuilder.generateFullSentencesBackward(getBindingVariable(), new String[]{}, lexicalEntryUtil));
        generatedSentences = generatedSentences.stream().distinct().collect(Collectors.toList());
        //generatedSentences.sort(String::compareToIgnoreCase);
        return generatedSentences;
    }

    /**
     * Generate an entry with sentence structure: Which _noun_ does $x _verb_
     * _preposition_?
     */
    public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries=new ArrayList<GrammarEntry>();
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setSentenceTemplate(this.template);
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
        grammarEntries.add(fragmentEntry);

        return grammarEntries;
    }

    
}
