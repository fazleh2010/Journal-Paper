package grammar.generator;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomain;
import static grammar.datasets.sentencetemplates.TempConstants.booleanQuestionDomainRange;
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
import org.apache.jena.query.QueryType;

public class TransitiveVPGrammarRuleGenerator extends GrammarRuleGeneratorRoot implements TempConstants {

    private static final Logger LOG = LogManager.getLogger(TransitiveVPGrammarRuleGenerator.class);
    private String template="";

    public TransitiveVPGrammarRuleGenerator(Language language) {
        super(FrameType.VP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }
    
    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
    
        String bindingVar = getBindingVariable();
        try {
            SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    this.getSentenceTemplateParser(),
                    lexicalEntryUtil);
            generatedSentences = sentenceBuilder.generateFullSentencesForward(bindingVar, lexicalEntryUtil);
            this.template=sentenceBuilder.getTemplateFinder().getSelectedTemplate();
            //generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return generatedSentences;
    }

    protected List<String> generateOppositeSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String bindingVar = getBindingVariable();
        String sentenceTemplate=null;
        try {
             SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    this.getSentenceTemplateParser(),
                    lexicalEntryUtil);
            generatedSentences = sentenceBuilder.generateFullSentencesBackward(bindingVar, new String[2], lexicalEntryUtil);
            this.template=sentenceBuilder.getTemplateFinder().getSelectedTemplate();
            //generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedSentences;
    }
    
    protected List<String> generateAPP(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        String bindingVar = getBindingVariable();
        try {
             SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    this.getSentenceTemplateParser(),
                    lexicalEntryUtil);
            generatedSentences = sentenceBuilder.generateFullSentencesBackward(bindingVar, new String[2], lexicalEntryUtil);
            //generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedSentences;
    }

    /**
     * Generate an entry with sentence structure: Which _noun_ does $x _verb_
     * _preposition_?
     */
    //original one
    /*public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries=new ArrayList<GrammarEntry>();
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
        grammarEntries.add(fragmentEntry);
        
        

        return grammarEntries;
    }*/
    
     /**
     * Generate an entry with sentence structure: Which _noun_ does $x _verb_
     * _preposition_?
     */
    //original one
    public List<GrammarEntry> generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<GrammarEntry> grammarEntries=new ArrayList<GrammarEntry>();
        grammarEntry.setSentenceTemplate(this.template);
        
        GrammarEntry oppositeGrammarEntry = getOppositeGrammarEntry(grammarEntry, lexicalEntryUtil);
        grammarEntries.add(oppositeGrammarEntry);
        
        GrammarEntry amountGrammarEntry = getAmountGrammarEntry(grammarEntry, lexicalEntryUtil);
        grammarEntries.add(amountGrammarEntry);
        
        

        return grammarEntries;
    }

    private GrammarEntry getOppositeGrammarEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.SENTENCE);
        // Assign opposite values
        fragmentEntry.setReturnType(grammarEntry.getBindingType());
        fragmentEntry.setBindingType(grammarEntry.getReturnType());
        fragmentEntry.setReturnVariable(grammarEntry.getBindingVariable());
        fragmentEntry.setSentenceTemplate(grammarEntry.getSentenceTemplate());

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
    
     private GrammarEntry getAmountGrammarEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        
        String bindingVar = getBindingVariable();
        try {
             SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    this.getSentenceTemplateParser(),
                    lexicalEntryUtil);
           List<String> generatedSentences = sentenceBuilder.generateBackwardAmount(bindingVar, new String[2], lexicalEntryUtil);
            fragmentEntry.setType(SentenceType.SENTENCE);
            fragmentEntry.setSentenceTemplate(sentenceBuilder.getTemplateFinder().getSelectedTemplate());
            fragmentEntry.setSentences(generatedSentences);
            fragmentEntry.setBindingType(grammarEntry.getReturnType());
            fragmentEntry.setReturnType(grammarEntry.getBindingType());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return fragmentEntry;
    }

    private List<String> generateAmountSentences(LexicalEntryUtil lexicalEntryUtil) {
        List<String> generatedSentences = new ArrayList<String>();
        String bindingVar = getBindingVariable();
        try {
             SentenceBuilderAllFrame sentenceBuilder = new SentenceBuilderAllFrame(
                    getLanguage(),
                    this.getFrameType(),
                    this.getSentenceTemplateRepository(),
                    this.getSentenceTemplateParser(),
                    lexicalEntryUtil);
            generatedSentences = sentenceBuilder.generateBackwardAmount(bindingVar, new String[2], lexicalEntryUtil);
            //generatedSentences.sort(String::compareToIgnoreCase);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TransitiveVPGrammarRuleGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedSentences;
    }
        
    
    
    
    
   

}
