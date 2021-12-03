/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.datasets.sentencetemplates.TempConstants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
public class TemplateFeatures implements TempConstants {

    private List<String> positionTokens = new ArrayList<String>();
    private Boolean verbMainFlag = false;
    private Boolean verbAuxilaryFlag = false;
    private Boolean verbTrennFlag = false;
    private Boolean verbReflexiveFlag = false;
    private Boolean pronounReflexiveFlag = false;
   

    public TemplateFeatures(String sentenceTemplate) {
        this.parseTemplate(sentenceTemplate);
        this.checkTemplate();
    }

    private void parseTemplate(String sentenceTemplate) {
        if (sentenceTemplate.contains(" ")) {
            String[] value = sentenceTemplate.split(" ");
            for (String string : value) {
                positionTokens.add(string);
            }
        } else {
            positionTokens.add(sentenceTemplate);
        }
    }

    private void checkTemplate() {
        for (String tokenString : positionTokens) {
            this.setVerbCategory(tokenString);
            this.setPronounCategory(tokenString);
        }
    }

    private void setVerbCategory(String tokenString) {
        if (tokenString.contains(mainVerb)) {
            this.verbMainFlag = true;
        } else if (tokenString.contains(TrennVerb)) {
            this.verbTrennFlag = true;
        } else if (tokenString.contains(RefVerb)) {
            this.verbReflexiveFlag = true;
        } else {
            this.verbAuxilaryFlag = true;
        }
    }

    private void setPronounCategory(String tokenString) {
        if (tokenString.contains(pronounReflexive)) {
            this.pronounReflexiveFlag = true;
        } 
    }
    
    public static Boolean isPronounCategory(String tokenString) {
        if (tokenString.contains(pronounReflexive)) {
           return true;
        } 
        return false;
    }
    
   

    public Boolean getVerbMainFlag() {
        return verbMainFlag;
    }

    public Boolean getVerbAuxilaryFlag() {
        return verbAuxilaryFlag;
    }

    public Boolean getVerbTrennFlag() {
        return verbTrennFlag;
    }

    public Boolean isVerbReflexiveFlag() {
        return verbReflexiveFlag;
    }

    public Boolean getPronounReflexiveFlag() {
        return pronounReflexiveFlag;
    }
    

    public List<String> getPositionTokens() {
        return positionTokens;
    }

    @Override
    public String toString() {
        return "TemplateFeatures{" + "positionTokens=" + positionTokens + ", verbMainFlag=" + verbMainFlag + ", verbAuxilaryFlag=" + verbAuxilaryFlag + ", verbTrennFlag=" + verbTrennFlag + ", verbReflexiveFlag=" + verbReflexiveFlag + ", pronounReflexiveFlag=" + pronounReflexiveFlag + '}';
    }

}
