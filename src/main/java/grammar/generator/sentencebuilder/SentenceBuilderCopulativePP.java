package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static grammar.generator.SentenceTemplateParser.QUESTION_MARK;
import grammar.sparql.SelectVariable;
import static java.lang.System.exit;
import java.util.Collection;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import util.io.GenderUtils;
import grammar.generator.sentencebuilder.German;
import grammar.generator.sentencebuilder.TemplateFinder;

public class SentenceBuilderCopulativePP extends SentenceBuilderImpl implements TempConstants  {

    private static final Logger LOG = LogManager.getLogger(SentenceBuilderCopulativePP.class);
    private TemplateFinder templateFinder = null;
    private static Map<String, String> templates = new HashMap<String, String>();
    private static List<PropertyValue> numberList = new ArrayList<PropertyValue>();

    public SentenceBuilderCopulativePP(
            Language language,
            FrameType frameType,
            SentenceTemplateRepository sentenceTemplateRepository,
            SentenceTemplateParser sentenceTemplateParser
    ) {
        super(language, frameType, sentenceTemplateRepository, sentenceTemplateParser);
    }
    
    @Override
    protected Map<PropertyValue, String> interpretSentenceTokenNP(List<SentenceToken> parsedSentenceTemplate, String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        System.out.println("this.getFrameType().getName()::" + this.getFrameType().getName());
        Integer index = 0;

        List<String> sentences = new ArrayList<String>();

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

        return  new TreeMap<PropertyValue, String>();
    }

    protected List<String> interpretSentenceToken(
            List<SentenceToken> sentenceTokens,
            String bindingVar,
            LexicalEntryUtil lexicalEntryUtil
    ) throws
            QueGGMissingFactoryClassException {
        List<AnnotatedNounOrQuestionWord> annotatedLexicalEntryNouns = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords();
        //String bindingVariable = this.getVariable(lexicalEntryUtil, bindingVar);
        Optional<SentenceToken> questionWordNounToken = getConditionNounToken(sentenceTokens); // noun(condition:copulativeArg)
        Optional<SentenceToken> rootToken = getRootToken(sentenceTokens);
        Optional<SentenceToken> pronounObjectToken = getObjectPronounToken(sentenceTokens);
        Optional<SentenceToken> copulaToken = getCopulaToken(sentenceTokens); // verb(reference:component_be)
        
        /*List<AnnotatedVerb> toBeVerbs = new ArrayList<AnnotatedVerb>();
        if (copulaToken.isPresent()) {
            URI copulaRef = copulaToken.get().getLocalReference();
            LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(copulaRef);
            toBeVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs(entry.getOtherForms());
        }*/
      

       Set<String> sentences = new TreeSet<String>();
        Integer index = 0;
        SelectVariable selectVariable =lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());
        /*String variable = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVariable,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(selectVariable)
                )).name(),
                SentenceType.NP
        );*/
        
        System.out.println("this.getFrameType().getName()::" + this.getFrameType().getName());


        List<String> sentenceTemplates = this.getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                this.getLanguage(), new String[]{this.getFrameType().getName(), whQuestion,  this.getLanguage().toString()});
      
        //System.out.println("sentenceTemplates::" + type);

       

        return new ArrayList<String>(sentences);

   
        

        /*for (AnnotatedNounOrQuestionWord annotatedNoun : annotatedLexicalEntryNouns) {
            System.out.println("annotatedNoun::"+annotatedNoun.getWrittenRepValue());
            String[] sentenceArray = new String[sentenceTokens.size()];
            // Get NP for this annotatedNoun
            for (AnnotatedVerb toBeVerb : toBeVerbs) {
                if (annotatedNoun.getNumber().equals(toBeVerb.getNumber())
                        || (rootToken.isPresent() && isValidAdjectiveForm(rootToken.get()))) {
                    if (isNPPPresent(sentenceTokens) && rootToken.isPresent()) {
                        String np;
                        try {
                            np = generateNPOrAP(sentenceTokens, bindingVariable, lexicalEntryUtil).get(annotatedNoun.getNumber());
                            sentenceArray[rootToken.get().getPosition()] = np;
                        } catch (Exception ex) {
                            System.err.println("failed to generate Noun Phrase" + ex.getMessage());
                            java.util.logging.Logger.getLogger(SentenceBuilderCopulativePP.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        copulaToken.ifPresent(sentenceToken -> sentenceArray[sentenceToken.getPosition()] = toBeVerb.getWrittenRepValue());
                        // Get interrogative determiner or pronoun for this sentence
                        if (pronounObjectToken.isPresent()) {
                            URI detRef = pronounObjectToken.get().getLocalReference();
                            LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(detRef);
                            String object_pronoun = entry.getCanonicalForm().getWrittenRep().value;
                            sentenceArray[pronounObjectToken.get().getPosition()] = object_pronoun;
                        }

                        if (questionWord!=null) {
                            sentenceArray[findQuestionWord.getPosition()] = questionWord;
                        }
                        String sentence = buildSentence(Arrays.asList(sentenceArray));
                        if (!pronounObjectToken.isPresent()) {
                            sentence = sentence.concat(QUESTION_MARK);
                        }
                        if (!generatedSentences.contains(sentence)) {
                            generatedSentences.add(sentence);
                        }
                    } else {
                        LOG.error("Please add the tag 'root' to every possible sentence template for this class!");
                    }
                }
            }
        }*/
       
    }

    private boolean isValidAdjectiveForm(SentenceToken rootToken) {
        return (rootToken.getPropertyMap().containsValue(getLexInfo().getPropertyValue("participle"))
                && rootToken.getPartOfSpeechValue().equals(getLexInfo().getPropertyValue("verb")))
                || rootToken.getPartOfSpeechValue().equals(getLexInfo().getPropertyValue("adjective"));
    }

    private boolean isNPPPresent(List<SentenceToken> sentenceTokens) {
        Optional<SentenceToken> rootToken = getRootToken(sentenceTokens);
        Optional<SentenceToken> npPreposition = getNPPreposition(sentenceTokens);
        Optional<SentenceToken> npObject = getNPObject(sentenceTokens);
        return rootToken.isPresent() && npPreposition.isPresent() && npObject.isPresent();
    }

   

    private List<String> generateNPOrAP(
            List<SentenceToken> sentenceTokens,
            String bindingVariable,
            LexicalEntryUtil lexicalEntryUtil
    ) throws Exception {

        System.out.println("this.getFrameType().getName()::" + this.getFrameType().getName());
        Integer index = 0;

        List<String> sentences = new ArrayList<String>();

        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());
        

        List<String> sentenceTemplates = this.getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                this.getLanguage(), new String[]{this.getFrameType().getName(), whQuestion, this.getLanguage().toString()});

       

        /*for (AnnotatedNounOrQuestionWord annotatedNoun : annotatedLexicalEntryNouns) {
            System.out.println("annotatedNoun::"+annotatedNoun.getWrittenRepValue());
            
            if (annotatedNoun.getGrammaticalCase() != null && !annotatedNoun.getGrammaticalCase().equals(lexicalEntryUtil.getLexInfo().getPropertyValue(grammaticalCase))) {
                continue;
            }
            String[] sentenceArray = new String[sentenceTokens.size()];
            // Get NP for this annotatedNoun
            if (npDeterminer.isPresent()) {
                URI detRef = npDeterminer.get().getLocalReference();
                LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(detRef);
               
                List<AnnotatedNounOrQuestionWord> determiners = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords(entry.getOtherForms());
                                
                for (AnnotatedNounOrQuestionWord det : determiners) {
                    if (annotatedNoun.getNumber().equals(det.getNumber())) {
                        if (det.getNumber().equals(lexicalEntryUtil.getLexInfo().getPropertyValue("singular"))
                                && annotatedNoun.getGender().equals(det.getGender())) {
                            determiner = det.getWrittenRepValue();
                        }
                        else if (det.getNumber().equals(lexicalEntryUtil.getLexInfo().getPropertyValue("plural"))) {
                            determiner = det.getWrittenRepValue();
                        }
                    }
                }
                sentenceArray[npDeterminer.get().getPosition()] = determiner;
            }
            if (conditionLabel.isPresent()
                    && conditionLabel.get().getPropertyMap().containsKey(getLexInfo().getProperty("number"))) {
                // Get noun for determiner token
                String determinerToken;

                if (getLanguage().equals(Language.DE)) {
                    URI nounRef;
                    if (nounToken.contains(" ")) {
                        nounRef = URI.create(LexiconSearch.LEXICON_BASE_URI + nounToken.replace(' ', '_').toLowerCase() + "_strong");
                    } else {
                        nounRef = URI.create(LexiconSearch.LEXICON_BASE_URI + nounToken.toLowerCase());
                    }
                    LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(nounRef);
                    String questionNoun = entry.getCanonicalForm().getWrittenRep().value;
                    List<AnnotatedNounOrQuestionWord> questionWordNoun = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords(entry.getOtherForms());

                    for (AnnotatedNounOrQuestionWord noun : questionWordNoun) {
                        if (noun.getNumber().equals(lexicalEntryUtil.getLexInfo().getPropertyValue("plural"))) {
                            questionNoun = noun.getWrittenRepValue();
                        }
                    }

                    determinerToken = getDeterminerTokenByNumber(
                            lexicalEntryUtil.getLexInfo().getPropertyValue("plural"),
                            questionNoun,
                            "",
                            getLanguage()
                    );
                } else {
                    determinerToken = getDeterminerTokenByNumber(
                            lexicalEntryUtil.getLexInfo().getPropertyValue("plural"),
                            nounToken,
                            "",
                            getLanguage()
                    );
                }
                determinerToken="";
                sentenceArray[conditionLabel.get().getPosition()] = determinerToken;
            }
            if (rootToken.isPresent() && npPreposition.isPresent() && npObject.isPresent()) {
                sentenceArray[rootToken.get().getPosition()] = annotatedNoun.getWrittenRepValue();
                sentenceArray[npPreposition.get().getPosition()] = preposition;
                sentenceArray[npObject.get().getPosition()] = object;
                String sentence = buildSentence(Arrays.asList(sentenceArray));
                if (!generatedSentences.containsValue(sentence)) {
                    generatedSentences.put(annotatedNoun.getNumber(), sentence);
                }
            }
        }*/
        return sentences;
    }


    private Optional<SentenceToken> getConditionNounToken(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(sentenceToken -> !isNull(sentenceToken.getSynArgForCondition()))
                .filter(sentenceToken -> sentenceToken.getSynArgForCondition()
                .getURI()
                .getFragment()
                .startsWith("copulative"))
                .findFirst();
    }

    private Optional<SentenceToken> getNPDeterminer(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(sentenceToken -> !isNull(sentenceToken.getPartOfSpeechValue()))
                .filter(sentenceToken -> sentenceToken.getPartOfSpeechValue()
                .equals(getLexInfo().getPropertyValue("determiner")))
                .findFirst();
    }

    private Optional<SentenceToken> getRootToken(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(SentenceToken::isRootToken)
                .findFirst();
    }

    private Optional<SentenceToken> getNPPreposition(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(sentenceToken -> !isNull(sentenceToken.getPartOfSpeechValue()))
                .filter(sentenceToken -> sentenceToken.getPartOfSpeechValue()
                .equals(getLexInfo().getPropertyValue("preposition")))
                .findFirst();
    }

    private Optional<SentenceToken> getNPObject(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(sentenceToken -> !isNull(sentenceToken.getSynArgValue()))
                .findFirst();
    }

    private Optional<SentenceToken> getCopulaToken(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(sentenceToken -> !isNull(sentenceToken.getPartOfSpeechValue()))
                .filter(sentenceToken -> sentenceToken.getPartOfSpeechValue()
                .equals(getLexInfo().getPropertyValue("verb")))
                .filter(sentenceToken -> !isNull(sentenceToken.getLocalReference()))
                .findFirst();
    }

    private Optional<SentenceToken> getObjectPronounToken(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(sentenceToken -> !isNull(sentenceToken.getPartOfSpeechValue()))
                .filter(sentenceToken -> sentenceToken.getPartOfSpeechValue()
                .equals(getLexInfo().getPropertyValue("pronoun")))
                .findFirst();
    }

    @Override
    public List<String> generateBooleanQuestionDomainRange(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateBooleanQuestionsDomain(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateBackwardAmount(String bindingVar, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> generateForwardAmount(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTemplate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
    
}
