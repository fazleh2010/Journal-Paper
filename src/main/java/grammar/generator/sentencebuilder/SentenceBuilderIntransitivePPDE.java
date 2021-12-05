package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.generator.SubjectType.interrogativePlace;
import grammar.generator.sentencebuilder.EnglishSentenceBuilder;
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
import grammar.generator.sentencebuilder.GermanSentenceBuilder;
import util.io.StringMatcher;
import util.io.TemplateFeatures;
import util.io.TemplateFinder;

public class SentenceBuilderIntransitivePPDE implements SentenceBuilder, TempConstants {

    private final Language language;
    private final FrameType frameType;
    private SentenceTemplateRepository sentenceTemplateRepository;
    private SentenceTemplateParser sentenceTemplateParser;
    private final LexicalEntryUtil lexicalEntryUtil;
    private TemplateFinder templateFinder = null;
    private static Map<String, String> templates = new HashMap<String, String>();
    private static List<PropertyValue> numberList = new ArrayList<PropertyValue>();

    public SentenceBuilderIntransitivePPDE(
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
        List<String> sentences = new ArrayList<String>();

        if (this.frameType.equals(FrameType.NPP)) {
            sentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, whQuestion);
             System.out.println(sentences);
             //exit(1);
        } else if (this.frameType.equals(FrameType.VP)) {
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), active});
            sentences = generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(sentences);
            //exit(1);

        } else if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
            System.out.println("template:::" + template);
            //DomainOrRangeType domainOrRangeType = this.templateFinder.getForwardDomainOrRange();
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), template, forward});
            sentences = generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(template);
            System.out.println(sentences);
            //exit(1);
        }

        /*List<String> sentenceTemplates = getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                getLanguage(), new String[]{getFrameType().getName(), FORWARD});*/
        return sentences;
    }

    @Override
    public List<String> generateFullSentencesBackward(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> sentences = new ArrayList<String>();
        if (this.frameType.equals(FrameType.NPP)) {
            sentences = nounPhrase(bindingVariable, lexicalEntryUtil);
            System.out.println(sentences);
            //exit(1);
        } else if (this.frameType.equals(FrameType.VP)) {
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), passive});
            sentences = generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(sentences);
            //sentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, passive);
                    //exit(1);

        } else if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
            System.out.println("template:::" + template);

            DomainOrRangeType domainOrRangeType = this.templateFinder.getOppositeDomainOrRange();
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), template, backward});
            sentences = generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(template);
            System.out.println(sentences);
            //exit(1);
        }

        return sentences;
    }

    @Override
    public List<String> generateBooleanQuestionDomainRange(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, booleanQuestionDomainRange);
        }

        return generatedSentences;
    }

    @Override
    public List<String> generateBooleanQuestionsDomain(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
        if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, booleanQuestionDomain);

        }

        return generatedSentences;
    }

    private List<String> generateSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();

        //dirty code..for quick solution..
        if (this.language.equals(Language.DE)) {
            return this.germanSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
        }
        else if (this.language.equals(Language.EN)) {
            return this.englishSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
        }
       
        return new ArrayList<String>(sentences);
    }

    private List<String> germanSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();

        Integer index = 0;

        for (String sentenceTemplate : sentenceTemplates) {
            System.out.println(sentenceTemplate);
            index = index + 1;
            GermanSentenceBuilder sentenceBuilderFromTemplates = new GermanSentenceBuilder(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
            TemplateFeatures templateFeatures = new TemplateFeatures(sentenceTemplate);
            List<String> positionTokens = templateFeatures.getPositionTokens();
            String sentence = "", positionWord = "";
            Boolean validSentence = true;
            for (String positionString : positionTokens) {

                String npCategory = findNounPhraseCategory(positionString);
                if (npCategory.isEmpty()) {
                    String[] parseToken = StringMatcher.parseToken(positionString);
                    positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index, templateFeatures);
                } else if (npCategory.equals(nounPhrase)) {
                    List<String> nps = nounPhrase(bindingVariable, lexicalEntryUtil);
                    positionWord = nps.iterator().next();
                } else if (npCategory.equals(noun)) {
                    positionWord = noun(bindingVariable, lexicalEntryUtil);
                }

                positionWord = positionWord + " ";
                sentence += positionWord;
                index = index + 1;

                if (positionWord.contains("XX")) {
                    validSentence = false;
                }
            }

            if (!validSentence) {
                continue;
            } else {
                sentences.add(sentence.stripTrailing());
            }
        }
        System.out.println(sentences);

        return new ArrayList<String>(sentences);
    }
    
    private List<String> englishSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();

        Integer index = 0;

        for (String sentenceTemplate : sentenceTemplates) {
            System.out.println(sentenceTemplate);
            index = index + 1;
            EnglishSentenceBuilder sentenceBuilderFromTemplates = new EnglishSentenceBuilder(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
            TemplateFeatures templateFeatures = new TemplateFeatures(sentenceTemplate);
            List<String> positionTokens = templateFeatures.getPositionTokens();
            String sentence = "", positionWord = "";
            Boolean validSentence = true;
            for (String positionString : positionTokens) {

                String npCategory = findNounPhraseCategory(positionString);
                if (npCategory.isEmpty()) {
                    String[] parseToken = StringMatcher.parseToken(positionString);
                    positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index, templateFeatures);
                } else if (npCategory.equals(nounPhrase)) {
                    List<String> nps = nounPhrase(bindingVariable, lexicalEntryUtil);
                    positionWord = nps.iterator().next();
                } else if (npCategory.equals(noun)) {
                    positionWord = noun(bindingVariable, lexicalEntryUtil);
                }

                positionWord = positionWord + " ";
                sentence += positionWord;
                index = index + 1;

                /*if (positionWord.contains("XX")) {
                    validSentence = false;
                }*/
            }

            if (!validSentence) {
                continue;
            } else {
                sentences.add(sentence.stripTrailing());
            }
        }
        System.out.println(sentences);

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
    private List<String> nounPPframeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, String template) throws QueGGMissingFactoryClassException {
         List<String> sentences = new ArrayList<String>();
        //dirty code..for quick solution..
        if (this.language.equals(Language.DE)) {
            sentences =this.germanNounPPframeSentence( bindingVariable,  lexicalEntryUtil,  template);
        }
        else if (this.language.equals(Language.EN)) {
            sentences =this.englishNounPPframeSentence(bindingVariable,  lexicalEntryUtil,  template);
        }
     
        return new ArrayList<String>(sentences);

    }
    
    private List<String> germanNounPPframeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, String template) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new HashSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), template});

        System.out.println("sentenceTemplates:::" + sentenceTemplates);
        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            GermanSentenceBuilder sentenceBuilderFromTemplates = new GermanSentenceBuilder(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
            TemplateFeatures templateFeatures = new TemplateFeatures(sentenceTemplate);
            List<String> positionTokens = templateFeatures.getPositionTokens();
            String str = "", positionWord = "";
            Boolean validFlag = true;
            for (String positionString : positionTokens) {
                if (!checkTokenValidity(positionString, index)) {
                    validFlag = false;
                    break;
                }
                String npCategory = findNounPhraseCategory(positionString);
                if (npCategory.isEmpty()) {
                    String[] parseToken = StringMatcher.parseToken(positionString);
                    positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index, templateFeatures);
                } else if (npCategory.equals(nounPhrase)) {
                    List<String> nps = nounPhrase(bindingVariable, lexicalEntryUtil);
                    positionWord = nps.iterator().next();
                } else if (npCategory.equals(noun)) {
                    positionWord = noun(bindingVariable, lexicalEntryUtil);
                }

                validFlag = this.checkValidity(positionWord);
                if (!validFlag) {
                    break;
                }

                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
            }
            if (!validFlag) {
                continue;
            }
            sentences.add(str.stripTrailing());
        }
        return new ArrayList<String>(sentences);

    }
    
     private List<String> englishNounPPframeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, String template) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new HashSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), template});

        System.out.println("sentenceTemplates:::" + sentenceTemplates);
        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            EnglishSentenceBuilder sentenceBuilderFromTemplates = new EnglishSentenceBuilder(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
            TemplateFeatures templateFeatures = new TemplateFeatures(sentenceTemplate);
            List<String> positionTokens = templateFeatures.getPositionTokens();
            String str = "", positionWord = "";
            Boolean validFlag = true;
            for (String positionString : positionTokens) {
                if (!checkTokenValidity(positionString, index)) {
                    validFlag = false;
                    break;
                }
                String npCategory = findNounPhraseCategory(positionString);
                if (npCategory.isEmpty()) {
                    String[] parseToken = StringMatcher.parseToken(positionString);
                    positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index, templateFeatures);
                } else if (npCategory.equals(nounPhrase)) {
                    List<String> nps = nounPhrase(bindingVariable, lexicalEntryUtil);
                    positionWord = nps.iterator().next();
                } else if (npCategory.equals(noun)) {
                    positionWord = noun(bindingVariable, lexicalEntryUtil);
                }

                validFlag = this.checkValidity(positionWord);
                if (!validFlag) {
                    break;
                }

                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
            }
            if (!validFlag) {
                continue;
            }
            sentences.add(str.stripTrailing());
        }
        return new ArrayList<String>(sentences);

    }
     
    private List<String> nounPhrase(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
         List<String> sentences = new ArrayList<String>();
        //dirty code..for quick solution..
        if (this.language.equals(Language.DE)) {
            sentences =this.germanNounPhrase( bindingVariable,  lexicalEntryUtil);
        }
        else if (this.language.equals(Language.EN)) {
            sentences =this.englishNounPhrase(bindingVariable,  lexicalEntryUtil);
        }
     
        return new ArrayList<String>(sentences);
    }


    private List<String> germanNounPhrase(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
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
                language, new String[]{frameType.getName(), nounPhrase});
        GermanSentenceBuilder sentenceBuilderFromTemplates = new GermanSentenceBuilder(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            TemplateFeatures templateFeatures = new TemplateFeatures(sentenceTemplate);
            List<String> positionTokens = templateFeatures.getPositionTokens();
            String str = "", positionWord = "";
            Boolean validFlag = true;
            for (String positionString : positionTokens) {
                String[] parseToken = StringMatcher.parseToken(positionString);
                positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index, templateFeatures);
                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
                validFlag = this.checkValidity(positionWord);
                if (!validFlag) {
                    break;
                }
            }
            if (!validFlag) {
                continue;
            }
            String newsSentence = str.stripTrailing();

            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens, templateFeatures);
            sentences.add(newSentence);
        }

        return new ArrayList<String>(sentences);
    }
    
     private List<String> englishNounPhrase(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();
        Integer index = 0;

        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), nounPhrase});
        EnglishSentenceBuilder sentenceBuilderFromTemplates = new EnglishSentenceBuilder(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            TemplateFeatures templateFeatures = new TemplateFeatures(sentenceTemplate);
            List<String> positionTokens = templateFeatures.getPositionTokens();
            String str = "", positionWord = "";
            Boolean validFlag = true;
            for (String positionString : positionTokens) {
                String[] parseToken = StringMatcher.parseToken(positionString);
                positionWord = sentenceBuilderFromTemplates.getWords(parseToken, index, templateFeatures);
                positionWord = positionWord + " ";
                str += positionWord;
                index = index + 1;
                validFlag = this.checkValidity(positionWord);
                if (!validFlag) {
                    break;
                }
            }
            if (!validFlag) {
                continue;
            }
            String newsSentence = str.stripTrailing();

            String newSentence = sentenceBuilderFromTemplates.prepareSentence(positionTokens, templateFeatures);
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
        List<String> tokens = GermanSentenceBuilder.parseTemplate(stringPosition);
        for (String token : tokens) {
            token = token.replace("?", "");
            token = token.replace(".", "");
            if (token.equals(noun)) {
                return token;
            } else if (token.equals(nounPhrase)) {
                return token;
            }
        }
        return result;
    }

    private Boolean checkValidity(String word) {
        word = word.replace(" ", "").strip().trim().stripLeading().stripTrailing();
        if (word.equals("-")) {
            return false;
        } else if (word.equals("XX")) {
            return false;
        }
        return true;
    }

    /*private Boolean checkValidity(String word) {
        word = word.strip().trim().stripLeading().stripTrailing();
        if (word.equals("XX")) {
            return false;
        }
        return true;
    }*/
    private Boolean checkTokenValidity(String attribute, Integer index) {
        if (index == 1 && attribute.equals(preposition)) {
            String uri = LexicalEntryUtil.getRange(lexicalEntryUtil);
            if (!TemplateFinder.isPlace(uri)) {
                return false;
            }
        } else if (index == 1 && attribute.equals(interrogativePlace)) {
            String uri = LexicalEntryUtil.getRange(lexicalEntryUtil);
            if (!TemplateFinder.isPlace(uri)) {
                return false;
            }
        }
        return true;
    }

    private List<String> activeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, String whQuestion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<String> passiveSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, String whQuestion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
