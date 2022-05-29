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
public class VerbFinderGerman implements TempConstants {

    private Boolean mainVerbFlag = false;
    private Boolean auxilaryVerbFlag = false;
    private Boolean imperativeVerbFlag = false;
    private Boolean trennVerbFlag = false;
    private Boolean reflexiveFlag = false;
    private String word = "XX";
    private LexicalEntryUtil lexicalEntryUtil = null;
    private ParamterFinder paramterFinder = null;
    private FrameType frameType = null;

    public VerbFinderGerman(FrameType frameType, LexicalEntryUtil lexicalEntryUtil, String attribute, String reference) throws QueGGMissingFactoryClassException {
        this.frameType = frameType;
        this.lexicalEntryUtil = lexicalEntryUtil;
        this.paramterFinder = new ParamterFinder(attribute, reference);
        this.setCategory(paramterFinder.getReference());

        /*System.out.println("paramterFinder::" + paramterFinder);
        System.out.println("mainVerbFlag::" + this.mainVerbFlag);
        System.out.println("trennVerbFlag::" + this.trennVerbFlag);
        System.out.println("auxilaryVerbFlag::" + this.auxilaryVerbFlag);
        System.out.println("imperativeVerbFlag::" + this.imperativeVerbFlag);
        //exit(1);*/

        if (this.mainVerbFlag) {
            word = findMainVerb(attribute, reference);
        } else if (this.trennVerbFlag) {
            word = this.getTrennVerb();
        } else if (this.reflexiveFlag) {
            word = findMainVerb(attribute, reference);
        } else if (this.auxilaryVerbFlag || this.imperativeVerbFlag) {
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
        String word = "XX";

        if (paramterFinder.getTensePair().second.contains(perfect)) {
            word = getPerfectMainVerb();
        } else {
            word = this.getMainVerb(present);
        }

        return word;
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

    private String getMainVerb(String tense) throws QueGGMissingFactoryClassException {
        String key=getMainVerbPresent(tense);
        Pair<Boolean, String> pair = GenderUtils.getPerfecterbType(key, this.paramterFinder.getTensePair().second);
        if (pair.first) {
            return pair.second;
        }
        return "XX";
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

    private String getTrennVerb() throws QueGGMissingFactoryClassException {
        String word = "XX";
        if (paramterFinder.getTensePair().second.contains(past) || paramterFinder.getTensePair().second.contains(present)) {
            word = getMainVerb(past);
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

    private void setCategory(String reference) {
        //System.out.println("reference::" + reference);
        //System.out.println("trennVerb hash::" + GenderUtils.trennVerb);

      
            if (reference.contains("component_be")
                    || reference.contains("component_haben")
                    || reference.contains("component_heißen")
                    || reference.contains("component_werden")) {
                this.auxilaryVerbFlag = true;
                return;
            } else if (reference.contains("imperative_transitive")
                    || reference.contains("imperative_transitive_show")
                    ||reference.contains("imperative_verb")) {
                this.imperativeVerbFlag = true;
            } else if (isTrenn()) {
                if (reference.contains(TrennVerb)) {
                    this.trennVerbFlag = true;
                }
            } else {
                if (reference.contains(mainVerb)) {
                    this.mainVerbFlag = true;
                } else if (reference.contains(RefVerb)) {
                    this.reflexiveFlag = true;
                }

            }
        

    }

    public String getWord() {
        return word;
    }

    private String checkTrennOrPerfect(String word) throws QueGGMissingFactoryClassException {
        String tense = this.paramterFinder.getTensePair().second;
        if (isTrennVerb(word)) {
            return word = this.getTrennVerb();

        } else {
            if (this.mainVerbFlag && tense.contains(perfect)) {
                word = getPerfectMainVerb();
            } else if (this.mainVerbFlag && (tense.contains(present)
                    || tense.contains(past))) {
                word = this.getMainVerb(tense);
            } else if (this.trennVerbFlag) {
                return "XX";
            }

        }
        return word;
    }

    private Boolean isTrenn() {
        String verbWrittenForm = getMainVerbPresent(past).trim().strip();
        if (GenderUtils.trennVerb.containsKey(verbWrittenForm)) {
            return true;
        }
        return false;
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

}
