package grammar.generator;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.sentence.BindingConstants;
import grammar.generator.sentence.SentenceBuilderTransitiveVPEN;
import grammar.generator.sentence.SubjectType;
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

import static grammar.generator.sentence.BindingConstants.BINDING_TOKEN_TEMPLATE;
import static java.lang.System.exit;
import java.util.Map;
import java.util.TreeMap;
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
        String sentence = null;

        //SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(),DomainOrRangeType.PERSON);
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        String variableString = String.format(
                BINDING_TOKEN_TEMPLATE,
                getBindingVariable(),
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                )).name(),
                SentenceType.NP);

        Map<String, String> verbTokens = new TreeMap<String, String>();

        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());
            String determiner = lexicalEntryUtil.getSubjectBySubjectType(
                    SubjectType.INTERROGATIVE_DETERMINER,
                    getLanguage(),
                    null
            );
            String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
            String number=annotatedVerb.getNumber().toString();
            if(annotatedVerb.getNumber().toString().contains("#")){
               number=number.split("#")[1];
               verbTokens.put(number, determinerToken);
            }
           
        }

        SentenceBuilderTransitiveVPEN sentenceBuilder = new SentenceBuilderTransitiveVPEN(
                getLanguage(),
                getFrameType(),
                getSentenceTemplateRepository(),
                getSentenceTemplateParser(),
                variableString,
                lexicalEntryUtil,
                verbTokens);
        
        generatedSentences.addAll(sentenceBuilder.getSentences());
        
        System.out.println(generatedSentences);

        generatedSentences.sort(String::compareToIgnoreCase);
        exit(1);
        return generatedSentences;
    }
}
