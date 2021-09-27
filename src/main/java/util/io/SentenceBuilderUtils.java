/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;


import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import static grammar.generator.sentence.BindingConstants.BINDING_TOKEN_TEMPLATE;
import grammar.generator.sentence.SubjectType;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lexicon.LexicalEntryUtil;
import org.apache.commons.lang3.StringUtils;
import util.exceptions.QueGGMissingFactoryClassException;

/**
 *
 * @author elahi
 */
public class SentenceBuilderUtils implements TemplateConstants{

    private String attribute = null;
    private String reference = null;
    private String generatedString = null;
    private LexicalEntryUtil lexicalEntryUtil = null;
    private Language language = null;
    private String bindingVariable = null;
    private Map<String, String> determinerTokens = new TreeMap<String, String>();
    private Map<String, String> auxilaries = new TreeMap<String, String>();

    public SentenceBuilderUtils(Language language, LexicalEntryUtil lexicalEntryUtil, String bindingVariable, Map<String, String> determinerTokens, Map<String, String> auxilaries) {
        this.generatedString = this.makeString();
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.language = language;
        this.bindingVariable = bindingVariable;
        this.determinerTokens = determinerTokens;
        this.auxilaries = auxilaries;
    }

    public SentenceBuilderUtils(String string) {
        this.reference = StringUtils.substringBetween(string, "(", ")");
        this.attribute = string.split("(")[0];
    }

    public List<String> parseTemplate(String sentenceTemplate) {
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

    public String prepareSentence(List<String> positionTokens) throws QueGGMissingFactoryClassException {
        String str = "";
        for (String positionString : positionTokens) {
            String positionWord = getWord(parseToken(positionString)) + " ";
            str += positionWord;
        }
        return str.stripTrailing()+QUESTION_MARK;
    }

    public String[] parseToken(String string) {
        if (string.contains("(") && string.contains(")")) {
            String reference = StringUtils.substringBetween(string, "(", ")");
            String attribute = string.replaceAll("\\((.*?)\\)", "");
            return new String[]{attribute, reference};
        }
        return new String[]{string};
    }

    public String getWord(String[] tokens) throws QueGGMissingFactoryClassException {
        String atrribute = null, reference = null;
        String word = " X ";

        //System.out.println(tokens.length);
        //System.out.println("tokens[0]::" + tokens[0]);

        if (tokens.length == 1) {
            if (tokens[0].contains(DETERMINER)) {
                word = getGender(tokens[0], "Person");

            } else if (tokens[0].contains(PREPOSITION)) {
                word = this.lexicalEntryUtil.getPreposition();

            }
        } else {
          
            if (tokens[0].contains("component_aux_object_past")) {
                String number = tokens[1].trim();
                if (this.auxilaries.containsKey(number)) {
                    word = this.auxilaries.get(number);
                } else {
                    word = "no_auxilariy_verbs_found";
                }
            }
            else if (tokens[0].contains(VERB)) {
               
                List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
                String tense = tokens[1];
                for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
                    if (annotatedVerb.getTense().toString().contains(tense)) {
                        word = annotatedVerb.getWrittenRepValue();
                        break;
                    }
                }

            } else {
                word = getReplaceToken(tokens[1]);
            }
        }

        return word;
    }

    public String getReplaceToken(String reference) throws QueGGMissingFactoryClassException {
       
        String word = "X";
        if (reference.contains("X")) {
            word = this.bindingVariable;
            return word;
        }
        if (reference.contains("INTERROGATIVE_PRONOUN")) {
            if (isPerson(reference)) {
                word = lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.PERSON_INTERROGATIVE_PRONOUN,
                        language,
                        null
                );
            } else {
                word = lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.THING_INTERROGATIVE_PRONOUN,
                        language,
                        null
                );
            }

        }
        if (reference.contains(SubjectType.INTERROGATIVE_DETERMINER_SINGULAR.name())) {
            if (determinerTokens.containsKey("singular")) {
                word = determinerTokens.get("singular");
            }
        }
        if (reference.contains(SubjectType.INTERROGATIVE_DETERMINER_PLURAL.name())) {
            if (determinerTokens.containsKey("plural")) {
                word = determinerTokens.get("plural");
            }
        }
        if (reference.contains(SubjectType.INTERROGATIVE_PLACE.name())) {
                 word = lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.INTERROGATIVE_PLACE,
                        language,
                        null
                );
        }
        
        if (reference.contains(SubjectType.INTERROGATIVE_TEMPORAL.name())) {
                 word = lexicalEntryUtil.getSubjectBySubjectType(
                        SubjectType.INTERROGATIVE_TEMPORAL,
                        language,
                        null
                );
        }
        
        
      
       
        return word;
    }

    public String getGender(String token, String noun) {
        GenderUtils genderUtils = new GenderUtils(noun, language);
        return genderUtils.getArticle();

    }

    private boolean isPerson(String token) {
        return true;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getReference() {
        return reference;
    }

    public String getGeneratedString() {
        return generatedString;
    }

    private String makeString() {
        return null;
    }

}
