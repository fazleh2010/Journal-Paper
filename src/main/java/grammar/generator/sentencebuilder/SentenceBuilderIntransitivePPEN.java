package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import util.io.TemplateConstants;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TemplateVariable;
import static grammar.datasets.sentencetemplates.TemplateVariable.variableIndicator;
import static grammar.generator.SubjectType.interrogativePlace;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import util.exceptions.QueGGMissingFactoryClassException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import util.io.FindQuestionWord;
import util.io.SentenceBuilderUtils;
import util.io.TemplateFinder;

public class SentenceBuilderIntransitivePPEN implements SentenceBuilder, TemplateConstants {

    private final Language language;
    private final FrameType frameType;
    private SentenceTemplateRepository sentenceTemplateRepository;
    private SentenceTemplateParser sentenceTemplateParser;
    private final LexicalEntryUtil lexicalEntryUtil;
    private TemplateFinder templateFinder = null;
    private static Map<String, String> templates = new HashMap<String, String>();
    private static List<PropertyValue> numberList = new ArrayList<PropertyValue>();

    public SentenceBuilderIntransitivePPEN(
            Language language,
            FrameType frameType,
            SentenceTemplateRepository sentenceTemplateRepository,
            SentenceTemplateParser sentenceTemplateParser,
            LexicalEntryUtil lexicalEntryUtil
    ) {
        this.language = language;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.templateFinder = new TemplateFinder(lexicalEntryUtil, frameType);
        this.frameType = frameType;
        this.sentenceTemplateParser = sentenceTemplateParser;
        this.sentenceTemplateRepository = sentenceTemplateRepository;
    }

    @Override
    public List<String> generateFullSentencesForward(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();

        if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
            DomainOrRangeType domainOrRangeType = this.templateFinder.getForwardDomainOrRange();
            generatedSentences = forwardSentences(bindingVariable, lexicalEntryUtil, domainOrRangeType, template);
        } else if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil,TemplateVariable.whQuestion);
        }
        return generatedSentences;
    }

    @Override
    public List<String> generateFullSentencesBackward(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
            DomainOrRangeType domainOrRangeType = this.templateFinder.getOppositeDomainOrRange();
            generatedSentences = backwordSentences(bindingVariable, argument, lexicalEntryUtil, domainOrRangeType, template);
        } else if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPhrase(bindingVariable, lexicalEntryUtil);
        }

        return generatedSentences;
    }
    
    @Override
    public List<String> generateBooleanQuestionDomainRange(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, TemplateVariable.booleanQuestionDomainRange);
        }

        return generatedSentences;
    }
    
    @Override
    public List<String> generateBooleanQuestionsDomain(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, TemplateVariable.booleanQuestionDomain);
           
        }

        return generatedSentences;
    }

    private List<String> forwardSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType, String type) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        /*String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );*/

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), type, FORWARD, language.toString()});
        //System.out.println("sentenceTemplates::" + type);

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            //System.out.println("positionTokens::" + positionTokens);
            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            //System.out.println("sentence::" + sentence);
            sentences.add(newSentence);
        }

        return new ArrayList<String>(sentences);
    }

    public List<String> backwordSentences(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil, DomainOrRangeType domainOrRangeType, String type) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        /*String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );*/

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), type, FORWARD, language.toString()});
        //System.out.println("sentenceTemplates::" + type);

        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            //System.out.println("sentences:::" + sentences);
            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            sentences.add(newSentence);
            //System.out.println("sentences:::" + sentences);
        }

        return new ArrayList<String>(sentences);
    }
 
    /*private List<String> booleanSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, String template) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), template});

        System.out.println("sentenceTemplates:::" + sentenceTemplates);

        sentences.add("Testetetetetettetete");
        System.out.println("sentences:::" + sentences);

        return new ArrayList<String>(sentenceTemplates);

    }*/


    private List<String> nounPPframeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil,String template) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new HashSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), template});

         System.out.println("sentenceTemplates:::" + sentenceTemplates);
        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String str = "", positionWord = "";
             Boolean validFlag=true;
            for (String positionString : positionTokens) {
                if(!checkTokenValidity(positionString,index)){
                    validFlag=false;
                    break;
                }
                String npCategory = findNounPhraseCategory(positionString);
                if (npCategory.isEmpty()) {
                    String[] parseToken = sentenceBuilderFromTemplates.parseToken(positionString);
                    positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index);
                } else if (npCategory.equals(TemplateVariable.nounPhrase)) {
                    List<String> nps = nounPhrase(bindingVariable, lexicalEntryUtil);
                    positionWord = nps.iterator().next();
                } else if (npCategory.equals(TemplateVariable.noun)) {
                    positionWord = noun(bindingVariable, lexicalEntryUtil);
                }
                
                validFlag=this.checkValidity(positionWord);
                if(!validFlag){
                    break;
                }

                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
            }
            if(!validFlag){
                    continue;
                }
            sentences.add(str.stripTrailing());
            System.out.println("sentences:::" + sentences);
        }
        return new ArrayList<String>(sentences);

    }
    
   
    private List<String> nounPhrase(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;

        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        /*String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );*/
        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), TemplateVariable.nounPhrase});
        SentenceBuilderUtils sentenceBuilderFromTemplates = new SentenceBuilderUtils(language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            List<String> positionTokens = sentenceBuilderFromTemplates.parseTemplate(sentenceTemplate);
            String str = "", positionWord = "";
            Boolean validFlag = true;
            for (String positionString : positionTokens) {
                String[] parseToken = sentenceBuilderFromTemplates.parseToken(positionString);
                positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index);
                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
                System.out.println("positionString:::" + positionString + " positionWord:" + positionWord);
                validFlag = this.checkValidity(positionWord);
                if (!validFlag) {
                    break;
                }
            }
            if(!validFlag){
                    continue;
                }
            String newsSentence = str.stripTrailing();

            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens);
            sentences.add(newSentence);
        }

        return new ArrayList<String>(sentences);
    }

    private String noun(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;

        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );
        return variable;

    }

    private String findNounPhraseCategory(String stringPosition) {
        String result = "";
        List<String> tokens = SentenceBuilderUtils.parseTemplate(stringPosition);
        for (String token : tokens) {
            token = token.replace("?", "");
            token = token.replace(".", "");
            if (token.equals(TemplateVariable.noun)) {
                return token;
            } else if (token.equals(TemplateVariable.nounPhrase)) {
                return token;
            }
        }
        return result;
    }

   private Boolean checkValidity(String word) {
        word=word.replace(" ", "").strip().trim().stripLeading().stripTrailing();
        if(word.equals("-"))
            return false;
        return true;
    }
   
    private Boolean checkTokenValidity(String attribute, Integer index) {
        if (index == 1 && attribute.equals(preposition)) {
            String uri = LexicalEntryUtil.getRange(lexicalEntryUtil);
            if (!TemplateFinder.isPlace(uri)) {
                return false;
            }
        }
        else if (index == 1 && attribute.equals(interrogativePlace)) {
            String uri = LexicalEntryUtil.getRange(lexicalEntryUtil);
            if (!TemplateFinder.isPlace(uri)) {
                return false;
            }
        }
        return true;
    }
   
   


   
}
