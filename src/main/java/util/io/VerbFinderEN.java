/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

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

/**
 *
 * @author elahi
 */
public class VerbFinderEN implements TempConstants {

    private Boolean mainVerbFlag = false;
    private Boolean auxilaryVerbFlag = false;
    private String word = "XX";
    private LexicalEntryUtil lexicalEntryUtil = null;
    private ParamterFinder paramterFinder = null;
    private FrameType frameType = null;

    public VerbFinderEN(FrameType frameType, LexicalEntryUtil lexicalEntryUtil, String attribute, String reference) throws QueGGMissingFactoryClassException {
        this.frameType = frameType;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.paramterFinder = new ParamterFinder(attribute, reference);
        this.setCategory(paramterFinder.getReference());
        System.out.println("paramterFinder::"+paramterFinder);
         System.out.println("auxilaryVerbFlag::"+this.auxilaryVerbFlag);

        //exit(1);
        if (this.mainVerbFlag) {
            word = findMainVerb(attribute, reference);
        } else if (this.auxilaryVerbFlag) {
            if (paramterFinder.getParameterLength() == 2 && paramterFinder.getTensePair().first != null) {
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second);
            } else if (paramterFinder.getParameterLength() == 3 && paramterFinder.getTensePair().first != null && paramterFinder.getNumberPair().first != null) {
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second,
                        paramterFinder.getNumberPair().first, paramterFinder.getNumberPair().second);
            } else {
                word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, paramterFinder.getReference());
            }
        }

    }

    private String findMainVerb(String attribute, String reference) throws QueGGMissingFactoryClassException {

        if (paramterFinder.getTensePair().second.contains(past) || paramterFinder.getTensePair().second.contains(present)) {
            return word = getMainVerb();
        }

        return "XX";
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

    private void setCategory(String reference) {
        System.out.println("reference::" + reference);
        System.out.println("trennVerb hash::" + GenderUtils.trennVerb);
        if (reference.contains(mainVerb)) {
            this.mainVerbFlag = true;
        } else {
            this.auxilaryVerbFlag = true;
        }

    }

    public String getWord() {
        return word;
    }

}
