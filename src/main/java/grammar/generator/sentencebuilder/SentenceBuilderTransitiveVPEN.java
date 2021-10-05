package grammar.generator;

import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import static grammar.generator.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.datasets.sentencetemplates.TemplateConstants;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import org.apache.commons.lang3.StringUtils;
import util.exceptions.QueGGMissingFactoryClassException;
import util.io.GenderUtils;

public class SentenceBuilderTransitiveVPEN extends SentenceBuilderImpl implements TemplateConstants {

    private String bindingVariable;
    private LexicalEntryUtil lexicalEntryUtil;
    private Map<String, String> determinerTokens = new TreeMap<String, String>();
    private List<String> sentences = new ArrayList<String>();

   

    public SentenceBuilderTransitiveVPEN() {
        super(null, null, null, null);
    }

    public SentenceBuilderTransitiveVPEN(
            Language language,
            FrameType frameType,
            SentenceTemplateRepository sentenceTemplateRepository,
            SentenceTemplateParser sentenceTemplateParser,
            String bindingVariable,
            LexicalEntryUtil lexicalEntryUtil,
            Map<String, String> determinerTokens
    ) throws QueGGMissingFactoryClassException {
        super(language, frameType, sentenceTemplateRepository, sentenceTemplateParser);
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.bindingVariable = bindingVariable;
        this.determinerTokens = determinerTokens;
        List<String> sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), ACTIVE, language.toString()});
        this.getSentencesFromTemplates(sentenceTemplates);
        sentenceTemplates = new ArrayList<String>();
        sentenceTemplates = sentenceTemplateRepository.findOneByEntryTypeAndLanguageAndArguments(SentenceType.SENTENCE,
                language, new String[]{frameType.getName(), PASSIVE, language.toString()});
        this.getSentencesFromTemplates(sentenceTemplates);

    }

    /*// Who writes $x?
    private String sentenceSubjOfPropActive() {
        return String.format("%s %s %s?", subject, verb, directObject);
    }

    public String getActive() {
        return String.format("%s %s %s?", directObject, verb, subject);
    }

    public String getActiveOther() {
        return String.format("%s %s %s?", subject, directObject, verb);
    }*/
    @Override
    protected List<String> interpretSentenceToken(List<SentenceToken> sentenceTokens, String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Map<PropertyValue, String> interpretSentenceTokenNP(List<SentenceToken> parsedSentenceTemplate, String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<String> parseTemplate(String sentenceTemplate) {
        List<String> list = new ArrayList<String>();
        if (sentenceTemplate.contains(" ")) {
            String[] value = sentenceTemplate.split(" ");
            for (String string : value) {
                list.add(string);
            }
        } else {
            list.add(sentenceTemplate);
        }
        return list;
    }

    private String[] parseToken(String string) {
        if (string.contains("(") && string.contains(")")) {
            String reference = StringUtils.substringBetween(string, "(", ")");
            String attribute = string.replaceAll("\\((.*?)\\)", "");
            return new String[]{attribute, reference};
        }
        return new String[]{string};
    }

    private String getWord(String[] tokens,String range) throws QueGGMissingFactoryClassException {
        String atrribute = null, reference = null;
        String word = " X ";

        if (tokens[0].contains(VERB)) {
            List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
            String tense = tokens[1];
            for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
                if (annotatedVerb.getTense().toString().contains(tense)) {
                    word = annotatedVerb.getWrittenRepValue();
                    break;
                }
            }

        } else if (tokens[0].contains(DETERMINER)) {
            GenderUtils genderUtils = new GenderUtils(range);
            word = genderUtils.getArticle();
            System.out.println(DETERMINER+"   "+word);

        } else {
            word = this.getReplaceToken(tokens[1]);
        }
        /*LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        List<PropertyValue> numberList = new ArrayList<PropertyValue>();
        numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
        numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
        Map<String, String> auxilaries = this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);
        System.out.println("auxilaries:::"+auxilaries);*/
        return word;
    }

    private String prepareSentence(List<String> positionTokens,String range) throws QueGGMissingFactoryClassException {
        String str = "";
        for (String positionString : positionTokens) {
            String positionWord = this.getWord(parseToken(positionString),range) + " ";
            str += positionWord;
        }
        return str.stripTrailing();
    }

    private String getReplaceToken(String token) throws QueGGMissingFactoryClassException {
        String word = "X";
        if (token.contains("X")) {
            word = bindingVariable;
        }
        if (token.contains("INTERROGATIVE_PRONOUN")) {
            if (isPerson(token)) {
                word = lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.PERSON_INTERROGATIVE_PRONOUN,
                        getLanguage(),
                        null
                );
            } else {
                word = lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.THING_INTERROGATIVE_PRONOUN,
                        getLanguage(),
                        null
                );
            }

        }
        if (token.contains("INTERROGATIVE_DETERMINER")) {
            if (this.determinerTokens.containsKey("singular")) {
                word = this.determinerTokens.get("singular");
            }
        }

        //SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable());
        return word;
    }

    private Map<String, String> getAuxilariesVerb(List<PropertyValue> numberList, String auxilaryVerbString, LexInfo lexInfo) {
        LexicalEntry auxilaryVerb = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(auxilaryVerbString);

        Map<String, String> auxilaries = new HashMap<String, String>();
        for (PropertyValue number : numberList) {
            String[] info = number.toString().split("#");
            String auxVerb = auxilaryVerb.getForms().stream()
                    .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("tense"))
                    .contains(lexInfo.getPropertyValue("past")))
                    .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("number"))
                    .contains(lexInfo.getPropertyValue(info[1])))
                    .findFirst()
                    .orElseThrow()
                    .getWrittenRep().value;
            auxilaries.put(info[1], auxVerb);

        }
        return auxilaries;
    }

    public static void main(String[] arg) throws QueGGMissingFactoryClassException {
        System.out.println("Hello World");
        String sentenceTemplate = "subject(interrogativeDeterminer) verb(past) directObject(binding)?";
        SentenceBuilderTransitiveVPEN test = new SentenceBuilderTransitiveVPEN();
        /* List<String> positionTokens = test.parseTemplate(sentenceTemplate);
        System.out.println(positionTokens);
        String sentence=test.prepareSentence(positionTokens);
        System.out.println(sentence);*/

        System.out.println(SubjectType.PERSON_INTERROGATIVE_PRONOUN.toString());

    }

  
    private boolean isPerson(String token) {
        return true;
    }

    public List<String> getSentences() {
        return sentences;
    }

    private void getSentencesFromTemplates(List<String> sentenceTemplates) throws QueGGMissingFactoryClassException {
        Integer index = 0;           
        //String range=this.getRange();
        String domain=this.getDomain();
        //System.out.println("domain::::"+domain);
         System.out.println("domain::::"+domain);

        for (String sentenceTemplate : sentenceTemplates) {
            try {
                System.out.println(sentenceTemplate + " " + index);
                index = index + 1;
                List<String> positionTokens = this.parseTemplate(sentenceTemplate);
                String sentence = this.prepareSentence(positionTokens,domain);
                System.out.println(sentence);
                sentences.add(sentence);
                //getSentences(tokens);
                //sentences.add(str);
            } catch (QueGGMissingFactoryClassException ex) {
                System.err.println("problem in parsing subject type!!!!");
                Logger.getLogger(SentenceBuilderTransitiveVPEN.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private String getRange() {
        SelectVariable selectVarForward = lexicalEntryUtil.getSelectVariable();
        return lexicalEntryUtil.getConditionUriBySelectVariable(selectVarForward).toString();
    }
     private String getDomain() {
         SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
        return lexicalEntryUtil.getConditionUriBySelectVariable(oppositeSelectVariable).toString();
    }
    
}
