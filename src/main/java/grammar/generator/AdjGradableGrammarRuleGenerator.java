package grammar.generator;

import grammar.structure.component.FrameType;
import grammar.structure.component.GrammarEntry;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

public class AdjGradableGrammarRuleGenerator extends GrammarRuleGeneratorRoot {

    private String template = null;

    public AdjGradableGrammarRuleGenerator(Language language) {
        super(FrameType.AG, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }

    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();

        String bindingVar = getBindingVariable();
        try {
            SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    this.getSentenceTemplateParser(),
                    lexicalEntryUtil);
            generatedSentences = sentenceBuilder.generateFullSentencesForward(bindingVar, lexicalEntryUtil);
            this.template = sentenceBuilder.getTemplateFinder().getSelectedTemplate();
            //generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            System.out.println(this.frameType+" is not working");
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
            exit(1);
        }
        
        //System.out.println("getLanguage:::"+getLanguage());
        //exit(1);

        return generatedSentences;
    }

    protected List<String> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String bindingVar = getBindingVariable();
        String sentenceTemplate = null;
        try {
            SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    this.getSentenceTemplateParser(),
                    lexicalEntryUtil);
            generatedSentences = sentenceBuilder.generateFullSentencesBackward(bindingVar, new String[2], lexicalEntryUtil);
            this.template = sentenceBuilder.getTemplateFinder().getSelectedTemplate();
            //generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedSentences;
    }

    public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries = new ArrayList<GrammarEntry>();
        grammarEntry.setSentenceTemplate(this.template);

        GrammarEntry oppositeGrammarEntry = getSuperlativeGrammarEntry(grammarEntry, lexicalEntryUtil);
        grammarEntries.add(oppositeGrammarEntry);

        return grammarEntries;
    }

    private GrammarEntry getSuperlativeGrammarEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getBindingVariable());
        fragmentEntry.setSentenceTemplate(grammarEntry.getSentenceTemplate());
        fragmentEntry.setFrameType(FrameType.IPP);

        Map<String, String> sentenceToSparqlParameterMapping = new HashMap<String, String>();
        sentenceToSparqlParameterMapping.put(grammarEntry.getSentenceBindings().getBindingVariableName(),
                grammarEntry.getReturnVariable());
        fragmentEntry.setSentenceToSparqlParameterMapping(sentenceToSparqlParameterMapping);
        // sentences
        List<String> generatedSentences = generateOppositeSentences(lexicalEntryUtil);
        fragmentEntry.setSentenceTemplate(this.template);
        fragmentEntry.setSentences(generatedSentences);
        return fragmentEntry;
    }

   
}
