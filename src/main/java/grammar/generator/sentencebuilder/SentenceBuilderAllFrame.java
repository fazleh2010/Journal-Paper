package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.datasets.annotated.AnnotatedNounOrQuestionWord;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.generator.SubjectType.interrogativePlace;
import grammar.generator.sentencebuilder.English;
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
import grammar.generator.sentencebuilder.German;
import grammar.generator.sentencebuilder.Italian;
import grammar.generator.sentencebuilder.MultilingualBuilder;
import grammar.generator.sentencebuilder.Spanish;
import util.io.StringMatcher;
import util.io.TemplateFeatures;
import grammar.generator.sentencebuilder.TemplateFinder;

public class SentenceBuilderAllFrame implements SentenceBuilder, TempConstants {

    private final Language language;
    private final FrameType frameType;
    private SentenceTemplateRepository sentenceTemplateRepository;
    private SentenceTemplateParser sentenceTemplateParser;
    private final LexicalEntryUtil lexicalEntryUtil;
    private TemplateFinder templateFinder = null;

    public SentenceBuilderAllFrame(
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
    
     public SentenceBuilderAllFrame(
            Language language,
            FrameType frameType,
            SentenceTemplateRepository sentenceTemplateRepository,
            SentenceTemplateParser sentenceTemplateParser,
            LexicalEntryUtil lexicalEntryUtil,
            String  template
    ) {
        this.language = language;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.templateFinder = new TemplateFinder(lexicalEntryUtil, frameType);
        this.templateFinder.setSelectedTemplate(template);
        this.frameType = frameType;
        this.sentenceTemplateParser = sentenceTemplateParser;
        this.sentenceTemplateRepository = sentenceTemplateRepository;
    }

    @Override
    public List<String> generateFullSentencesForward(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> sentences = new ArrayList<String>();

        if (this.frameType.equals(FrameType.NPP)) {
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), whQuestion});
            sentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, sentenceTemplates);
             //exit(1);
            
        } else if (this.frameType.equals(FrameType.VP)) {
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            if(selectVariable.equals(SelectVariable.subjOfProp)&&this.templateFinder.getSelectedTemplate().contains(PERSON_CAUSE)){
               this.templateFinder.setSelectedTemplate(PERSON_CAUSE_SUBJECT);
            }
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), this.templateFinder.getSelectedTemplate(),activeTransitive});
            sentences = SentenceBuilderAllFrame.this.generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            /*System.out.println(sentences);
            System.out.println("selectVariable:::"+selectVariable);
            System.out.println("oppositeSelectVariable::"+oppositeSelectVariable);
            System.out.println("this.templateFinder.getSelectedTemplate()::"+this.templateFinder.getSelectedTemplate());
            System.out.println(activeTransitive);
              exit(1);*/

        } else if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
            //System.out.println("template:::" + template);
            //DomainOrRangeType domainOrRangeType = this.templateFinder.getForwardDomainOrRange();
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), template, forward});
            sentences = SentenceBuilderAllFrame.this.generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(template);
            System.out.println(sentences);
            
        }
        else if (this.frameType.equals(FrameType.AG)) {
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            String template=this.getTemplateFinder().getSelectedTemplate();
            if(template.contains(superlativeCountry)||template.contains(superlativeLocation)){
                template=superlativeCountry;
            }
                
            
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(),template,forward});
            
           
            
            sentences = SentenceBuilderAllFrame.this.generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            /*System.out.println("template::"+template);
            System.out.println("sentenceTemplates::"+sentenceTemplates);
            System.out.println("sentences::"+sentences);
            exit(1);*/
        }
        
        //System.out.println("this.templateFinder.getSelectedTemplate()::"+this.templateFinder.getSelectedTemplate());
        System.out.println("sentences::"+sentences);

      
        /*List<String> sentenceTemplates = getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                getLanguage(), new String[]{getFrameType().getName(), FORWARD});*/
        //exit(1);
        return this.filter(sentences);
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
            if(selectVariable.equals(SelectVariable.subjOfProp)&&this.templateFinder.getSelectedTemplate().contains(PERSON_CAUSE)){
               this.templateFinder.setSelectedTemplate(PERSON_CAUSE_SUBJECT);
            }
            
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), this.templateFinder.getSelectedTemplate(),passiveTransitive});
            sentences = SentenceBuilderAllFrame.this.generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
             System.out.println(sentences);
            /*System.out.println("selectVariable:::"+selectVariable);
            System.out.println("oppositeSelectVariable::"+oppositeSelectVariable);
            System.out.println("this.templateFinder.getSelectedTemplate()::"+this.templateFinder.getSelectedTemplate());
            System.out.println(passiveTransitive);
              exit(1);*/

        } else if (this.frameType.equals(FrameType.IPP)) {
            String template = this.templateFinder.getSelectedTemplate();
           
            //System.out.println("template:::" + template);

            DomainOrRangeType domainOrRangeType = this.templateFinder.getOppositeDomainOrRange();
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            
            
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), template, backward});
            sentences = SentenceBuilderAllFrame.this.generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println("template::"+template);
            System.out.println("sentences::"+sentences);
            //exit(1);
            
           
        }
        else if (this.frameType.equals(FrameType.AG)) {
            /*this.templateFinder.setSelectedTemplate(adjectiveBaseForm);
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());

            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), adjectiveBaseForm,backward});
           
            sentences = generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(sentenceTemplates);
            System.out.println(sentences);
            System.out.println(frameType.getName());
             System.out.println(this.getTemplateFinder().getSelectedTemplate());
            //exit(1);*/
        }
        //System.out.println(sentences);
        //exit(1);
        
        return this.filter(sentences);
    }
    
    
    
    @Override
    public List<String> generateBackwardAmount(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> sentences = new ArrayList<String>();
        this.templateFinder.setSelectedTemplate(HOW_MANY_THING);
        if (this.frameType.equals(FrameType.NPP)) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        } else if (this.frameType.equals(FrameType.VP)) {
            SelectVariable selectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), this.templateFinder.getSelectedTemplate(), passiveTransitive});
            System.out.println(sentenceTemplates);
            sentences = SentenceBuilderAllFrame.this.generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(sentences);
            //exit(1);

        } else if (this.frameType.equals(FrameType.IPP)) {
            SelectVariable oppositeSelectVariable = this.lexicalEntryUtil.getSelectVariable();
            SelectVariable selectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                    language, new String[]{frameType.getName(), this.templateFinder.getSelectedTemplate(), backward});
            System.out.println(sentenceTemplates);
             //exit(1);

            sentences = SentenceBuilderAllFrame.this.generateSentences(bindingVariable, lexicalEntryUtil, selectVariable, oppositeSelectVariable, sentenceTemplates);
            System.out.println(sentences);
           
        }

        return this.filter(sentences);
    }
    
      @Override
    public List<String> generateForwardAmount(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public List<String> generateBooleanQuestionDomainRange(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
         List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), booleanQuestionDomainRange});
          
        if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, sentenceTemplates);
        }

        return this.filter(generatedSentences);
    }

    @Override
    public List<String> generateBooleanQuestionsDomain(String bindingVariable, String[] string, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<String>();
          List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), booleanQuestionDomainRange});
     
        if (this.frameType.equals(FrameType.NPP)) {
            generatedSentences = nounPPframeSentence(bindingVariable, lexicalEntryUtil, sentenceTemplates);

        }

        return this.filter(generatedSentences);
    }

    private List<String> generateSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        List<String> sentences = new ArrayList<String>();
        MultilingualBuilder multilingualBuilder = null;
        if (this.language.equals(Language.EN)) {
            multilingualBuilder = new English(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        } else if (this.language.equals(Language.DE)) {
            multilingualBuilder = new German(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        } else if (this.language.equals(Language.ES)) {
            multilingualBuilder = new Spanish(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        }
        else if (this.language.equals(Language.IT)) {
            multilingualBuilder = new Italian(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        }
        sentences = this.generateSentences(bindingVariable, sentenceTemplates, multilingualBuilder);
        return this.filter(sentences);
    }

    /*private List<String> germanSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, SelectVariable selectVariable, SelectVariable oppositeSelectVariable, List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();

        Integer index = 0;

        for (String sentenceTemplate : sentenceTemplates) {
            System.out.println(sentenceTemplate);
            index = index + 1;
            German sentenceBuilderFromTemplates = new German(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
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
        //System.out.println(sentences);

        return new ArrayList<String>(sentences);
    }*/
    
    private List<String> generateSentences(String bindingVariable, List<String> sentenceTemplates,MultilingualBuilder multilingualBuilder) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new TreeSet<String>();

        Integer index = 0;
        

        for (String sentenceTemplate : sentenceTemplates) {
            System.out.println(sentenceTemplate);
            index = index + 1;
            TemplateFeatures templateFeatures = new TemplateFeatures(sentenceTemplate);
            List<String> positionTokens = templateFeatures.getPositionTokens();
            String sentence = "", positionWord = "";
            Boolean validSentence = true;
            for (String positionString : positionTokens) {

                String npCategory = findNounPhraseCategory(positionString);
                if (npCategory.isEmpty()) {
                    String[] parseToken = StringMatcher.parseToken(positionString);
                    positionWord = multilingualBuilder.getWords(parseToken, index, templateFeatures);
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
             sentence=modify(sentence);
            if (!validSentence) {
                continue;
            } else {
                sentences.add(sentence.stripTrailing());
            }
            System.out.println(sentence);
        }
        //System.out.println(sentences);

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
    private List<String> nounPPframeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        List<String> sentences = new ArrayList<String>();
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());
        MultilingualBuilder bultilingualBuilder = new English(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

        if (this.language.equals(Language.EN)) {
            bultilingualBuilder = new English(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        } else if (this.language.equals(Language.DE)) {
            bultilingualBuilder = new German(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        } else if (this.language.equals(Language.ES)) {
            bultilingualBuilder = new Spanish(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        }else if (this.language.equals(Language.IT)) {
            bultilingualBuilder = new Italian(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
        }
        sentences = this.getNounPPframeSentence(bindingVariable, bultilingualBuilder, sentenceTemplates);

        return new ArrayList<String>(sentences);

    }

    private List<String> germanNounPPframeSentence(String bindingVariable, LexicalEntryUtil lexicalEntryUtil, String template) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new HashSet<String>();
        Integer index = 0;
        SelectVariable selectVariable = lexicalEntryUtil.getSelectVariable();
        SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable());

        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), template});

        //System.out.println("sentenceTemplates:::" + sentenceTemplates);
        for (String sentenceTemplate : sentenceTemplates) {
            index = index + 1;
            German sentenceBuilderFromTemplates = new German(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);
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
    
     private List<String> getNounPPframeSentence(String bindingVariable, MultilingualBuilder bultilingualBuilder,List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        Set<String> sentences = new HashSet<String>();
        Integer index = 0;
       
        //System.out.println("sentenceTemplates:::" + sentenceTemplates);
        for (String sentenceTemplate : sentenceTemplates) {   
             System.out.println("sentenceTemplate::"+sentenceTemplate);
            index = index + 1;
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
                    positionWord = bultilingualBuilder.getWords(parseToken, index, templateFeatures);
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
            sentences.add(str);
            System.out.println("generated sentence::"+str);
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
         else if (this.language.equals(Language.ES)) {
            sentences =this.englishNounPhrase(bindingVariable,  lexicalEntryUtil);
        }
        else if (this.language.equals(Language.IT)) {
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
        German sentenceBuilderFromTemplates = new German(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

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
        English sentenceBuilderFromTemplates = new English(this.frameType, this.language, this.lexicalEntryUtil, selectVariable, oppositeSelectVariable, bindingVariable);

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
        List<String> tokens = German.parseTemplate(stringPosition);
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
        if (word.contains("-")) {
            return false;
        } else if (word.contains("XX")) {
            return false;
        }
        return true;
    }
    
    private String modify(String word) {
       return word.replace("-", " ");
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

    public TemplateFinder getTemplateFinder() {
        return templateFinder;
    }
    
    public String  getTemplate() {
        return templateFinder.getSelectedTemplate();
    }

    private List<String> filter(List<String> sentences) {
        List<String> validSentences = new ArrayList<String>();
        for (String sentence : sentences) {
            if (!sentence.contains("XX")) {
                validSentences.add(sentence);
            }

        }
        return validSentences;

    }

   

}
