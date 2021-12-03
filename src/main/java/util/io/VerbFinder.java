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
import static java.lang.System.exit;
import java.util.List;
import java.util.Map;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

/**
 *
 * @author elahi
 */
public class VerbFinder implements TempConstants {

    private Boolean mainVerbFlag = false;
    private Boolean auxilaryVerbFlag = false;
    private Boolean trennVerbFlag = false;
    private Boolean reflexiveFlag = false;
    private String word = "XX";
    private LexicalEntryUtil lexicalEntryUtil = null;
    private ParamterFinder paramterFinder = null;

    public VerbFinder(LexicalEntryUtil lexicalEntryUtil, String attribute, String reference) throws QueGGMissingFactoryClassException {
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.paramterFinder = new ParamterFinder(attribute, reference);
        this.setCategory(paramterFinder.getReference());
        
                  
        if (this.mainVerbFlag) {
            word = findMainVerb(attribute, reference);
        }  
        else if (this.trennVerbFlag) {
            word = this.getTrennVerb();       
        }  
        else if (this.reflexiveFlag) {
            word = findMainVerb(attribute, reference);
        } 
        else if(this.auxilaryVerbFlag) {
            if (paramterFinder.getParameterLength() == 2 && paramterFinder.getTensePair().first != null) {
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second);
            } else if (paramterFinder.getParameterLength() == 3 && paramterFinder.getTensePair().first != null && paramterFinder.getNumberPair().first != null) {
                word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil,paramterFinder.getReference(), paramterFinder.getTensePair().first, paramterFinder.getTensePair().second,
                paramterFinder.getNumberPair().first, paramterFinder.getNumberPair().second);
            } else {
                word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil,paramterFinder.getReference());
            }

        }
        
       

    }

    private String findMainVerb(String attribute, String reference) throws QueGGMissingFactoryClassException {

        if (paramterFinder.getTensePair().second.contains(perfect)) {
            return word = getPerfectMainVerb();
        } else if (paramterFinder.getTensePair().second.contains(past) || paramterFinder.getTensePair().second.contains(present)) {
            return word = getMainVerb();
        }

        return "XX";
    }

    private boolean isTrennVerb(String word) {
        if (word.contains(" ")) {
            return true;
        }
        return false;
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
        String word="XX";
        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();

        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            if (annotatedVerb.getTense().toString().contains(paramterFinder.getTensePair().second) && annotatedVerb.getPerson().toString().contains(paramterFinder.getPersonPair().second)) {
                word= annotatedVerb.getWrittenRepValue();
                break;
            }

        }
        
        return word;
    }

    private String getPerfectTrennVerb(String word) {
        word = getMainVerbPresent(present);
        Pair<Boolean, String> pair = GenderUtils.getTrennVerbType(word, perfect, mainVerb);
        if (pair.first) {
            return word = pair.second;
        } else {
            return "XX";

        }
    }

    private String getPerfectMainVerb() {
        String word = getMainVerbPresent(past);
        Pair<Boolean, String> pair = GenderUtils.getPerfecterbType(word, perfect);
        if (pair.first) {
            return word = pair.second;
        } else {
            return "XX";

        }
    }

    private String getTrennVerb() {
        String word="XX";
        if (paramterFinder.getTensePair().second.contains(past) || paramterFinder.getTensePair().second.contains(present)) {
            word = this.getMainVerb();
            
            String[] info = word.split(" ");
            if (paramterFinder.getReference().contains(TrennVerbPart1)) {
                word = info[0];
            } else {
                word = info[1];
            }

        } else if (paramterFinder.getTensePair().second.contains(perfect)) {
            return word = this.getPerfectTrennVerb(word);
        }
       
        return word;

    }

    public Boolean getMainVerbFlag() {
        return mainVerbFlag;
    }

    public Boolean getAuxilaryVerbFlag() {
        return auxilaryVerbFlag;
    }

    public Boolean getTrennVerbFlag() {
        return trennVerbFlag;
    }

    public Boolean getReflexiveFlag() {
        return reflexiveFlag;
    }

    private void setCategory(String reference) {
        if (reference.contains(mainVerb)) {
            this.mainVerbFlag = true;
        } else if (reference.contains(TrennVerb)) {
             String verbWrittenForm= getMainVerbPresent(past).trim().strip();
             if(GenderUtils.trennVerb.containsKey(verbWrittenForm)){
                this.trennVerbFlag = true;
             }
                   
        } else if (reference.contains(RefVerb)) {
            this.reflexiveFlag = true;
        } else {
            this.auxilaryVerbFlag = true;
        }
    }

    public String getWord() {
        return word;
    }

    private String checkTrennOrPerfect(String word) {
        String tense=this.paramterFinder.getTensePair().second;
          if (isTrennVerb(word)) {
            return word = this.getTrennVerb();

        } else {
            if (this.mainVerbFlag && tense.contains(perfect)) {
                word = getPerfectMainVerb();
            } else if (this.mainVerbFlag && (tense.contains(present)
                    || tense.contains(past))) {
                word = this.getMainVerb();
            } else if (this.trennVerbFlag) {
                return "XX";
            }

        }
          return word;
    }
    
    

}
