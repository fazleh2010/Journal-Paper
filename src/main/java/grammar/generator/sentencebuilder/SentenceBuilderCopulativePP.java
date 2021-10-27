package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.annotated.AnnotatedVerb;
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

import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import static grammar.generator.SentenceTemplateParser.QUESTION_MARK;
import grammar.sparql.SelectVariable;
import static java.lang.System.exit;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import util.io.GenderUtils;

public class SentenceBuilderCopulativePP extends SentenceBuilderImpl {

    private static final Logger LOG = LogManager.getLogger(SentenceBuilderCopulativePP.class);

    public SentenceBuilderCopulativePP(
            Language language,
            FrameType frameType,
            SentenceTemplateRepository sentenceTemplateRepository,
            SentenceTemplateParser sentenceTemplateParser
    ) {
        super(language, frameType, sentenceTemplateRepository, sentenceTemplateParser);
    }

    protected List<String> interpretSentenceToken(
            List<SentenceToken> sentenceTokens,
            String bindingVar,
            LexicalEntryUtil lexicalEntryUtil
    ) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        // We already know how to find the matching AnnotatedWords or String tokens than need to be added to the sentence
        // Load all forms of this lexical entry
        List<AnnotatedNounOrQuestionWord> annotatedLexicalEntryNouns = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords();
        AnnotatedNounOrQuestionWord questionWord
                = // Who / what
                getAnnotatedQuestionWordBySubjectTypeAndNumber(lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(), DomainOrRangeType.PERSON), getLanguage(), lexicalEntryUtil, getLexInfo().getPropertyValue("singular"), null);
        String nounToken = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());
        String object = String.format(
                BINDING_TOKEN_TEMPLATE,
                bindingVar,
                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                )).name(),
                SentenceType.NP.toString()
        );
        
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!nounToken::"+nounToken);


        // We already know, which sentence tokens to expect, so we search for them to find out where to put our expected AnnotatedWords and Strings
        Optional<SentenceToken> questionWordToken = getQuestionWordToken(sentenceTokens); // interrogativeDeterminer/-Pronoun
        Optional<SentenceToken> questionWordNounToken = getConditionNounToken(sentenceTokens); // noun(condition:copulativeArg)
        Optional<SentenceToken> rootToken = getRootToken(sentenceTokens);
        Optional<SentenceToken> pronounObjectToken = getObjectPronounToken(sentenceTokens);


        // Load a list of to be forms to make every possible sentence combination
        Optional<SentenceToken> copulaToken = getCopulaToken(sentenceTokens); // verb(reference:component_be)
        List<AnnotatedVerb> toBeVerbs = new ArrayList<>();
        if (copulaToken.isPresent()) {
            URI copulaRef = copulaToken.get().getLocalReference();
            LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(copulaRef);
            toBeVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs(entry.getOtherForms());
        }

        for (AnnotatedNounOrQuestionWord annotatedNoun : annotatedLexicalEntryNouns) {
            String[] sentenceArray = new String[sentenceTokens.size()];
            // Get NP for this annotatedNoun
            for (AnnotatedVerb toBeVerb : toBeVerbs) {
                if (annotatedNoun.getNumber().equals(toBeVerb.getNumber())
                        || (rootToken.isPresent() && isValidAdjectiveForm(rootToken.get()))) {
                    if (isNPPPresent(sentenceTokens) && rootToken.isPresent()) {
                        String np;
                        try {
                            np = generateNPOrAP(sentenceTokens, object, lexicalEntryUtil).get(annotatedNoun.getNumber());
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

                        if (questionWordToken.isPresent()) {
                            // Get determiner token
                            if (questionWordNounToken.isPresent() && !lexicalEntryUtil.hasInvalidDeterminerToken(
                                    lexicalEntryUtil
                                            .getSelectVariable())) { // check if there is any noun with a condition
                                // must be determiner token
                                questionWord = getAnnotatedQuestionWordBySubjectTypeAndNumber(
                                        SubjectType.INTERROGATIVE_DETERMINER_SINGULAR,
                                        getLanguage(),
                                        lexicalEntryUtil,
                                        toBeVerb.getNumber(),
                                        null
                                );

                                // Get noun for determiner token
                                String determinerToken;
                                //||getLanguage().equals(Language.IT)
                                if (getLanguage().equals(Language.DE)) {
                                    URI nounRef;
                                    if (nounToken.contains(" ")) {
                                        nounRef = URI.create(LexiconSearch.LEXICON_BASE_URI + nounToken.replace(' ', '_').toLowerCase() + "_weak");
                                    } else {
                                        nounRef = URI.create(LexiconSearch.LEXICON_BASE_URI + nounToken.toLowerCase());
                                    }
                                    LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(nounRef);
                                    String questionNoun = entry.getCanonicalForm().getWrittenRep().value;
                                    List<AnnotatedNounOrQuestionWord> questionWordNoun = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords(entry.getOtherForms());

                                    for (AnnotatedNounOrQuestionWord noun : questionWordNoun) {
                                        if (noun.getNumber().equals(toBeVerb.getNumber())) {
                                            questionNoun = noun.getWrittenRepValue();
                                        }
                                    }

                                    determinerToken = getDeterminerTokenByNumber(
                                            toBeVerb.getNumber(),
                                            questionNoun,
                                            questionWord.getWrittenRepValue(),
                                            getLanguage()
                                    );
                                } else {
                                    determinerToken = getDeterminerTokenByNumber(
                                            toBeVerb.getNumber(),
                                            nounToken,
                                            questionWord.getWrittenRepValue(),
                                            getLanguage()
                                    );
                                }
                                sentenceArray[questionWordToken.get().getPosition()] = determinerToken;
                            } else if (isValidAdjectiveForm(rootToken.get())
                                    && toBeVerb.getNumber().equals(getLexInfo().getPropertyValue("plural"))) {
                                // skip plural copula for interrogative pronoun if adjective / participle
                                continue;
                            } else {
                                sentenceArray[questionWordToken.get().getPosition()] = questionWord.getWrittenRepValue();
                            }
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
        }
        return generatedSentences;
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

    @Override
    protected Map<PropertyValue, String> interpretSentenceTokenNP(
            List<SentenceToken> sentenceTokens,
            String bindingVar,
            LexicalEntryUtil lexicalEntryUtil
    ) {
        try {
            return generateNPOrAP(sentenceTokens, bindingVar, lexicalEntryUtil);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SentenceBuilderCopulativePP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*private Map<PropertyValue, String> generateNPOrAP(
            List<SentenceToken> sentenceTokens,
            String bindingVar,
            LexicalEntryUtil lexicalEntryUtil
    ) {
        Map<PropertyValue, String> generatedSentences = new HashMap<>();
        // We already know how to find the matching AnnotatedWords or String tokens than need to be added to the sentence
        // Load all forms of this lexical entry
        List<AnnotatedNounOrQuestionWord> annotatedLexicalEntryNouns = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords();
        String determiner = "";
        String preposition = lexicalEntryUtil.getPreposition();
        String object = String.format("%s", bindingVar);
        String nounToken = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());

        // We already know, which sentence tokens to expect, so we search for them to find out where to put our expected AnnotatedWords and Strings
        Optional<SentenceToken> npDeterminer = getNPDeterminer(sentenceTokens);
        if (npDeterminer.isPresent()) {
            URI detRef = npDeterminer.get().getLocalReference();
            LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(detRef);
            determiner = entry.getCanonicalForm().getWrittenRep().value;
        }
        Optional<SentenceToken> rootToken = getRootToken(sentenceTokens);
        Optional<SentenceToken> npPreposition = getNPPreposition(sentenceTokens);
        Optional<SentenceToken> npObject = getNPObject(sentenceTokens);
        Optional<SentenceToken> conditionLabel = getConditionNounToken(sentenceTokens);

        for (AnnotatedNounOrQuestionWord annotatedNoun : annotatedLexicalEntryNouns) {
            String[] sentenceArray = new String[sentenceTokens.size()];
            // Get NP for this annotatedNoun
            if (npDeterminer.isPresent()) {
                sentenceArray[npDeterminer.get().getPosition()] = determiner;
            }
            if (conditionLabel.isPresent()
                    && conditionLabel.get().getPropertyMap().containsKey(getLexInfo().getProperty("number"))) {
                sentenceArray[conditionLabel.get().getPosition()] = getDeterminerTokenByNumber(
                        conditionLabel.get().getPropertyMap().get(getLexInfo().getProperty("number")), nounToken,
                        ""
                );
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
        }
        return generatedSentences;
    }*/
    
     private Map<PropertyValue, String> generateNPOrAP(
            List<SentenceToken> sentenceTokens,
            String bindingVar,
            LexicalEntryUtil lexicalEntryUtil
    ) throws Exception {
        Map<PropertyValue, String> generatedSentences = new HashMap<>();
        // We already know how to find the matching AnnotatedWords or String tokens than need to be added to the sentence
        // Load all forms of this lexical entry
        List<AnnotatedNounOrQuestionWord> annotatedLexicalEntryNouns = lexicalEntryUtil.parseLexicalEntryToAnnotatedAnnotatedNounOrQuestionWords();
        String determiner = "";
        String preposition = lexicalEntryUtil.getPreposition();
        String object = String.format("%s", bindingVar);
        String nounToken = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());
        System.out.println("SentenceToken::"+sentenceTokens.toString());
       

        // We already know, which sentence tokens to expect, so we search for them to find out where to put our expected AnnotatedWords and Strings
        Optional<SentenceToken> npDeterminer = getNPDeterminer(sentenceTokens);
        Optional<SentenceToken> rootToken = getRootToken(sentenceTokens);
        Optional<SentenceToken> npPreposition = getNPPreposition(sentenceTokens);
        Optional<SentenceToken> npObject = getNPObject(sentenceTokens);
        Optional<SentenceToken> conditionLabel = getConditionNounToken(sentenceTokens);
       
        String grammaticalCase = "nominativeCase";
        if (rootToken.isPresent()) {
            grammaticalCase = rootToken.get().getGrammaticalCase();
        }
        

        for (AnnotatedNounOrQuestionWord annotatedNoun : annotatedLexicalEntryNouns) {
            if (annotatedNoun.getGrammaticalCase() != null && !annotatedNoun.getGrammaticalCase().equals(lexicalEntryUtil.getLexInfo().getPropertyValue(grammaticalCase))) {
                continue;
            }
            String[] sentenceArray = new String[sentenceTokens.size()];
            // Get NP for this annotatedNoun
            if (npDeterminer.isPresent()) {
                URI detRef = npDeterminer.get().getLocalReference();
                LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(detRef);
                try{
                    //System.out.println("lexicalEntryUtil.getLexicon()::" + lexicalEntryUtil.getLexicon());
                    //System.out.println("entry::" + entry);
                    //System.out.println("detRef::" + detRef);
                    if (getLanguage().equals(Language.IT)) {
                        /*GenderUtils GenderUtils = new GenderUtils(Language.IT, lexicalEntryUtil.getReferenceUri());
                        determiner = GenderUtils.getArticle();*/
                        determiner ="";
                        //temporary solution is adding article in written form
                    } else {
                        determiner = entry.getCanonicalForm().getWrittenRep().value;
                    }
                   
                }
                catch(Exception ex){
                    throw new Exception("some value of lemon not found::"+ex.getMessage());
                }
               
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
        }
                

        return generatedSentences;
    }

    private Optional<SentenceToken> getQuestionWordToken(List<SentenceToken> sentenceTokens) {
        return sentenceTokens.stream()
                .filter(sentenceToken -> !isNull(sentenceToken.getPartOfSpeechValue()))
                .filter(sentenceToken
                        -> sentenceToken.getPartOfSpeechValue()
                        .equals(getLexInfo().getPropertyValue(
                                "interrogativePronoun"))
                || sentenceToken.getPartOfSpeechValue()
                        .equals(getLexInfo().getPropertyValue("interrogativeDeterminer"))
                )
                .findFirst();
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
}
