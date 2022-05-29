/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator.sentencebuilder;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.annotated.AnnotatedVerb;
import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.mainVerb;
import static grammar.datasets.sentencetemplates.TempConstants.past;
import static grammar.datasets.sentencetemplates.TempConstants.perfect;
import static grammar.datasets.sentencetemplates.TempConstants.present;
import grammar.structure.component.FrameType;
import static java.lang.System.exit;
import java.util.List;
import java.util.Map;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;
import util.io.GenderUtils;
import util.io.ParamterFinder;

/**
 *
 * @author elahi
 */
public class VerbFinderEnglish implements TempConstants {

    private Boolean mainVerbFlag = false;
    private Boolean auxilaryVerbFlag = false;
    private Boolean imperativeVerbFlag = false;
    private String word = "XX";
    private LexicalEntryUtil lexicalEntryUtil = null;
    private ParamterFinder paramterFinder = null;
    private FrameType frameType = null;

    public VerbFinderEnglish(FrameType frameType, LexicalEntryUtil lexicalEntryUtil, String attribute, String reference) throws QueGGMissingFactoryClassException {
        this.frameType = frameType;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.paramterFinder = new ParamterFinder(attribute, reference);        
        this.setCategory(paramterFinder.getReference());
      
      


        if (this.mainVerbFlag) {
            word = findMainVerb(attribute, reference);
        }  else if (this.auxilaryVerbFlag || this.imperativeVerbFlag) {
            if (paramterFinder.getParameterLength() == 2 && paramterFinder.getTensePair().first != null) {
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second);
            } else if (paramterFinder.getParameterLength() == 3 && paramterFinder.getTensePair().first != null && paramterFinder.getNumberPair().first != null) {
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second,
                        paramterFinder.getNumberPair().first, paramterFinder.getNumberPair().second);
            } else {
                word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, paramterFinder.getReference());
            }
        }
          
        /*System.out.println("paramterFinder::" + paramterFinder);
        System.out.println("mainVerbFlag::" + this.mainVerbFlag);
        System.out.println("auxilaryVerbFlag::" + this.auxilaryVerbFlag);
        System.out.println("imperativeVerbFlag::" + this.imperativeVerbFlag);
        System.out.println("word::" + this.word);
        //exit(1);*/
        
    }

    private String findMainVerb(String attribute, String reference) throws QueGGMissingFactoryClassException {
        String word = "XX";

        if (paramterFinder.getTensePair().second.contains(perfect)) {
            word = this.getPerfectMainVerb();
        } else {
            word = this.getMainVerb(present);
        }

        return word;
    }

    
     private String getPerfectMainVerb() {
        String word = getMainVerbPresent(past);
        System.out.println(word);
        Pair<Boolean, String> pair = GenderUtils.getPerfecterbType(word, perfect);
        
        if (pair.first) {
            return word = pair.second;
        } else {
            return "XX";

        }
    }
     
      private String getMainVerb(String tense) throws QueGGMissingFactoryClassException {
        String key=getMainVerbPresent(tense);
        Pair<Boolean, String> pair = GenderUtils.getPerfecterbType(key, this.paramterFinder.getTensePair().second);
        if (pair.first) {
            return pair.second;
        }
        return "XX";
    }
      
    private String getInfinitiveVerb(String tense) {
        String word = getMainVerbPresent(past);
        Pair<Boolean, String> pair = GenderUtils.getPerfecterbType(word, tense);
        if (pair.first) {
            return word = pair.second;
        } else {
            return "XX";

        }
    }

    private String getMainVerbPresent(String tense) {
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();

        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            if (annotatedVerb.getTense().toString().contains(tense)) {
                return annotatedVerb.getWrittenRepValue();
            }

        }
        return null;
    }

    private String getMainVerb() {
        String word = "XX";
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();

        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            if (annotatedVerb.getTense().toString().contains(paramterFinder.getTensePair().second) && annotatedVerb.getPerson().toString().contains(paramterFinder.getPersonPair().second)) {
                word = annotatedVerb.getWrittenRepValue();
                break;
            }

        }

        return word;
    }

    public Boolean getMainVerbFlag() {
        return mainVerbFlag;
    }

    public Boolean getAuxilaryVerbFlag() {
        return auxilaryVerbFlag;
    }

    /*private void setCategory(String reference) {
        if (reference.contains(mainVerb)) {
            this.mainVerbFlag = true;
        } else if (reference.contains(imperative)) {
            this.imperativeVerbFlag = true;
        } else {
            this.auxilaryVerbFlag = true;
        }

    }*/
    
    private void setCategory(String reference) {
        if (reference.contains(component_be)||reference.contains(component_do)||reference.contains(component_se)
                ||reference.contains(component_ha)||reference.contains(component_estado)
                ||reference.contains(component_esta)||reference.contains(component_aux_object_past)||reference.contains(componentVerb)) {
            this.auxilaryVerbFlag = true;
            return;
        } else if (reference.contains(imperative)||reference.contains(verb)) {
            this.imperativeVerbFlag = true;
        } else if (reference.contains(mainVerb)) {
            this.mainVerbFlag = true;
        }

    }

    public String getWord() {
        return word;
    }

}
