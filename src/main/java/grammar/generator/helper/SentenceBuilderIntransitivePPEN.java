package grammar.generator.helper;

import com.github.andrewoma.dexx.collection.Pair;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.sparql.SelectVariable;
import grammar.structure.component.DomainOrRangeType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.List;

import static grammar.generator.helper.BindingConstants.BINDING_TOKEN_TEMPLATE;

import java.util.HashMap;
import java.util.Map;

import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumberNew;

public class SentenceBuilderIntransitivePPEN implements SentenceBuilder {

    private final Language language;
    private final AnnotatedVerb annotatedVerb;
    private final LexicalEntryUtil lexicalEntryUtil;
    //Since many things are hard coded or solved by if else so this is a temporary solution. 
    //the logic of Sentencebuilder works almost same way so it should be merged NNP
    private Map<String,String> sentenceTemplates = new HashMap<String,String>();
    private static final String TEMPORAL_PAST_TEMPLATE = "TEMPORAL_PAST_TEMPLATE";
    private static final String TEMPORAL_DO_TEMPLATE = "TEMPORAL_PAST_TEMPLATE";
    private static  String SELECTED_TEMPLATE = null;
    private static  List<PropertyValue> numberList = new ArrayList<PropertyValue>();;
           

    public SentenceBuilderIntransitivePPEN(
            AnnotatedVerb annotatedVerb,
            LexicalEntryUtil lexicalEntryUtil
    ) {
        this.language = Language.EN;
        this.annotatedVerb = annotatedVerb;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.sentenceTemplates.put(TEMPORAL_PAST_TEMPLATE, "TemporalDeterminer verb(reference:component_be) determiner(reference:component_the) noun(condition:subject) VP(temporalAdjunct)");
        this.sentenceTemplates.put(TEMPORAL_DO_TEMPLATE, "TemporalDeterminer verb(reference:component_do) noun(condition:subject) VP(temporalAdjunct)");
        this.SELECTED_TEMPLATE=TEMPORAL_PAST_TEMPLATE;
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
        this.numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
    }

    @Override
    public List<String> generateFullSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        String sentence, preposition;
        DomainOrRangeType domainOrRangeType;
        String binding = null, verb = null;
        
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();

        preposition = this.lexicalEntryUtil.getPreposition();

        //This has to be descided from the range..temporary code for solving problem.
        Boolean flag = true;

        //The code was not written to use sentence template to work for intransitive verb. This has to be integreated. This is temporary solution.
        if (this.SELECTED_TEMPLATE.contains(TEMPORAL_PAST_TEMPLATE)) {
            domainOrRangeType = DomainOrRangeType.YEAR;
        } else if(this.SELECTED_TEMPLATE.contains(TEMPORAL_DO_TEMPLATE)){
             domainOrRangeType = DomainOrRangeType.PERSON;
        }
        else {
            domainOrRangeType = DomainOrRangeType.THING;
        }

     

        Map<String,String> auxilaries = new HashMap<String,String>();
             

        if (!lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
            // Make simple sentence (Which river flows through $x?)
            if (lexInfo.getPropertyValue("singular").equals(annotatedVerb.getNumber())) {
                SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(this.lexicalEntryUtil.getSelectVariable(), domainOrRangeType);
                String qWord = this.lexicalEntryUtil.getSubjectBySubjectTypeAndNumber(subjectType, language, lexInfo.getPropertyValue("singular"), null);

                String bindingString = DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
                )).name();
                binding = String.format(
                        BINDING_TOKEN_TEMPLATE,
                        bindingVariable,
                        bindingString,
                        SentenceType.NP
                );
                auxilaries =  this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);

                if (flag) {
                    for (String key : auxilaries.keySet()) {
                        String auxilariesVerb=auxilaries.get(key);
                        sentence = String.format(
                                "%s %s %s %s %s?",
                                qWord,
                                auxilariesVerb,
                                "the",
                                binding,
                                annotatedVerb.getWrittenRepValue()
                        );
                        generatedSentences.add(sentence);
                    }

                } else {
                    sentence = String.format(
                            "%s %s %s %s?",
                            qWord,
                            annotatedVerb.getWrittenRepValue(),
                            preposition,
                            binding
                    );
                    generatedSentences.add(sentence);
                }

            }
            // Make sentence using the specified domain or range property (Which museum exhibits $x?)
            // Only generate "Which <condition-label>" if condition label is a DBPedia entity
            if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
                String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(this.lexicalEntryUtil.getSelectVariable());
                String determiner = this.lexicalEntryUtil.getSubjectBySubjectTypeAndNumber(
                        SubjectType.INTERROGATIVE_DETERMINER,
                        language,
                        annotatedVerb.getNumber(),
                        null
                );
                String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner, language);
                String bindingString = DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
                        LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
                )).name();
                binding = String.format(
                        BINDING_TOKEN_TEMPLATE,
                        bindingVariable,
                        bindingString,
                        SentenceType.NP
                );

                if (flag) {
                    for (String key : auxilaries.keySet()) {
                        String auxilariesVerb=auxilaries.get(key);
                        
                        sentence = String.format(
                                "%s %s %s %s %s?",
                                determinerToken,
                                auxilariesVerb,
                                "the",
                                binding,
                                annotatedVerb.getWrittenRepValue()
                        );
                        generatedSentences.add(sentence);
                    }

                } else {
                    sentence = String.format(
                            "%s %s %s %s?",
                            determinerToken,
                            annotatedVerb.getWrittenRepValue(),
                            preposition,
                            binding
                    );
                    generatedSentences.add(sentence);
                }

            }
        }
        return generatedSentences;
    }

    /*@Override
  public List<String> generateFullSentences(String bindingVariable, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
    List<String> generatedSentences = new ArrayList<>();
    String sentence,preposition;
    LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();

    preposition = this.lexicalEntryUtil.getPreposition();

    if (!lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
      // Make simple sentence (Which river flows through $x?)
      if (lexInfo.getPropertyValue("singular").equals(annotatedVerb.getNumber())) {
        SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(this.lexicalEntryUtil.getSelectVariable());
        String qWord = this.lexicalEntryUtil.getSubjectBySubjectType(subjectType, language, null);

        sentence = String.format(
          "%s %s %s %s?",
          qWord,
          annotatedVerb.getWrittenRepValue(),
          preposition,
          String.format(
            BINDING_TOKEN_TEMPLATE,
            bindingVariable,
            DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
              LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
            )).name(),
            SentenceType.NP
          )
        );
        generatedSentences.add(sentence);

      }
      // Make sentence using the specified domain or range property (Which museum exhibits $x?)
      // Only generate "Which <condition-label>" if condition label is a DBPedia entity
      if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
        String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(this.lexicalEntryUtil.getSelectVariable());
        String determiner = this.lexicalEntryUtil.getSubjectBySubjectType(
          SubjectType.INTERROGATIVE_DETERMINER,
          language,
          null
        );
        String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
        sentence = String.format(
          "%s %s %s %s?",
          determinerToken,
          annotatedVerb.getWrittenRepValue(),
          preposition,
          String.format(
            BINDING_TOKEN_TEMPLATE,
            bindingVariable,
            DomainOrRangeType.getMatchingType(this.lexicalEntryUtil.getConditionUriBySelectVariable(
              LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable())
            )).name(),
            SentenceType.NP
          )
        );
        generatedSentences.add(sentence);
      }
    }
    return generatedSentences;
  }*/
    @Override
    public List<String> generateNP(String bindingVariable, String[] argument, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        String sentence;
        LexInfo lexInfo = this.lexicalEntryUtil.getLexInfo();
        String preposition = this.lexicalEntryUtil.getPreposition();
        DomainOrRangeType domainOrRangeType;

        if (this.SELECTED_TEMPLATE.contains(TEMPORAL_PAST_TEMPLATE)) {
            domainOrRangeType = DomainOrRangeType.THING;
        } else {
            domainOrRangeType = DomainOrRangeType.PERSON;
        }

        if (!lexInfo.getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
            // E.g. Which cities does $x flow through?
            List<PropertyValue> numberList = new ArrayList<>();
            numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"));
            numberList.add(this.lexicalEntryUtil.getLexInfo().getPropertyValue("plural"));
            //LexicalEntry auxilaryVerb = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource("component_aux_object_past");
            Map<String, String> auxilaries = this.getAuxilariesVerb(numberList, "component_aux_object_past", lexInfo);

            // opposite select variable
            SelectVariable oppositeSelectVariable = LexicalEntryUtil.getOppositeSelectVariable(this.lexicalEntryUtil.getSelectVariable());
            // get subjectType of this sentence's object
            SubjectType subjectType = this.lexicalEntryUtil.getSubjectType(oppositeSelectVariable, domainOrRangeType);
            String qWord = this.lexicalEntryUtil.getSubjectBySubjectTypeAndNumber(subjectType, language, this.lexicalEntryUtil.getLexInfo().getPropertyValue("singular"), null); // Who / What
            

            for (String key : auxilaries.keySet()) {
                String auxilariesVerb=auxilaries.get(key);
                sentence = String.format(
                        "%s %s %s %s %s?",
                        qWord,
                        auxilariesVerb,
                        annotatedVerb.getWrittenRepValue(),
                        preposition,
                        bindingVariable
                );
                generatedSentences.add(sentence);
            }

            if (!this.lexicalEntryUtil.hasInvalidDeterminerToken(this.lexicalEntryUtil.getSelectVariable())) {
                for (PropertyValue number : numberList) {
                    String conditionLabel = this.lexicalEntryUtil.getReturnVariableConditionLabel(oppositeSelectVariable);
                    String determiner = this.lexicalEntryUtil.getSubjectBySubjectTypeAndNumber(
                            SubjectType.INTERROGATIVE_DETERMINER,
                            language,
                            number,
                            null
                    );
                    Pair<String, String> determinerTokenPair = getDeterminerTokenByNumberNew(number, conditionLabel, determiner, language);
                    String determinerToken = determinerTokenPair.component1();
                    String determinerTokenNumber = determinerTokenPair.component2();
                    for (String  key : auxilaries.keySet()) {
                         String auxilariesVerb=auxilaries.get(key);
                        if (key.contains(determinerTokenNumber)) {
                            sentence = String.format(
                                    "%s %s %s %s %s?",
                                    determinerToken,
                                    auxilariesVerb,
                                    annotatedVerb.getWrittenRepValue(),
                                    preposition,
                                    bindingVariable
                            );
                            generatedSentences.add(sentence);
                        }
                    }
                }
            }
        }
        return generatedSentences;
    }

    private Map<String,String> getAuxilariesVerb(List<PropertyValue> numberList, String auxilaryVerbString, LexInfo lexInfo) {
        LexicalEntry auxilaryVerb = new LexiconSearch(this.lexicalEntryUtil.getLexicon()).getReferencedResource(auxilaryVerbString);

        Map<String,String> auxilaries = new HashMap<String,String>();
        for (PropertyValue number : numberList) {
            String[] info = number.toString().split("#");
            String auxVerb=auxilaryVerb.getForms().stream()
                    .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("tense"))
                    .contains(lexInfo.getPropertyValue("past")))
                    .filter(lexicalForm -> lexicalForm.getProperty(lexInfo.getProperty("number"))
                    .contains(lexInfo.getPropertyValue(info[1])))
                    .findFirst()
                    .orElseThrow()
                    .getWrittenRep().value;
           auxilaries.put(info[1],auxVerb);
            
        }
        return auxilaries;
    }

   
}
