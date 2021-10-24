package grammar.generator;

import grammar.datasets.annotated.AnnotatedVerb;
import static grammar.datasets.sentencetemplates.TemplateConstants.ACTIVE;
import static grammar.datasets.sentencetemplates.TemplateConstants.PASSIVE;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;
import java.util.ArrayList;
import java.util.List;
import grammar.structure.component.GrammarEntry;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import static util.io.TemplateConstants.BACKWARD;
import static util.io.TemplateConstants.FORWARD;

public class TransitiveVPGrammarRuleGenerator extends GrammarRuleGeneratorRoot {

    private static final Logger LOG = LogManager.getLogger(TransitiveVPGrammarRuleGenerator.class);

    public TransitiveVPGrammarRuleGenerator(Language language) {
        super(FrameType.VP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }
    
    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        List<String> sentenceTemplates = getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                getLanguage(), new String[]{getFrameType().getName(), FORWARD, getLanguage().toString()});
        String bindingVar = getBindingVariable();
        try {
            SentenceBuilderTransitiveVPEN sentenceBuilder = new SentenceBuilderTransitiveVPEN(
                    getLanguage(),
                    lexicalEntryUtil,
                    sentenceTemplates);
            generatedSentences = sentenceBuilder.generateFullSentencesForward(bindingVar, lexicalEntryUtil);
            generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return generatedSentences;
    }

    protected List<String> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        List<String> sentenceTemplates = getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                getLanguage(), new String[]{getFrameType().getName(), BACKWARD, getLanguage().toString()});
        String bindingVar = getBindingVariable();
        try {
            SentenceBuilderTransitiveVPEN sentenceBuilder = new SentenceBuilderTransitiveVPEN(
                    getLanguage(),
                    lexicalEntryUtil,
                    sentenceTemplates);
            generatedSentences = sentenceBuilder.generateFullSentencesBackward(bindingVar, new String[2], lexicalEntryUtil);
            generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    /*@Override
    public List<String> generateSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        String sentence = null;

        //SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(),DomainOrRangeType.PERSON);
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
       
       
        
        Map<String, String> verbTokens = new TreeMap<String, String>();

        
        String variableString = getBindingVariable();

        SentenceBuilderTransitiveVPEN sentenceBuilder;
        try {
            sentenceBuilder = new SentenceBuilderTransitiveVPEN(
                    getLanguage(),
                    getFrameType(),
                    getSentenceTemplateRepository(),
                    getSentenceTemplateParser(),
                    variableString,
                    lexicalEntryUtil);
            generatedSentences.addAll(sentenceBuilder.getSentences());
            generatedSentences.sort(String::compareToIgnoreCase);
             //System.out.println("generatedSentences:::"+generatedSentences);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return generatedSentences;
    }*/
    
     
}
