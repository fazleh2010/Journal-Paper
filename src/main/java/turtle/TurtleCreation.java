/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import com.google.gdata.util.common.base.Pair;
import grammar.datasets.sentencetemplates.TempConstants;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import linkeddata.LinkedData;

/**
 *
 * @author elahi
 */
public class TurtleCreation implements TempConstants {

    protected LinkedData linkedData = null;
    protected String language = null;
    protected String inputDir = null;
    protected String turtleString = null;
    protected String tutleFileName = "";
    protected Boolean conversionFlag = false;
    private Integer nounPPIndex=0;
    private Integer transitiveIndex=0;
    private Integer InTransitiveIndex=0;
    private Integer adjectiveFrameIndex=0;
    private Integer gradableAdjectiveFrameIndex=0;

    public TurtleCreation(String inputDir, LinkedData linkedData, Language language) throws Exception {
        this.linkedData = linkedData;
        this.language = language.name().toLowerCase();
        this.inputDir = inputDir;
    }
    
    public void setSyntacticFrameIndexes(Integer nounPPIndex, Integer transitiveIndex, Integer InTransitiveIndex, Integer adjectiveFrameIndex,Integer gradableAdjectiveFrameIndex) throws Exception {
        this.nounPPIndex = nounPPIndex;
        this.transitiveIndex = transitiveIndex;
        this.InTransitiveIndex = InTransitiveIndex;
        this.adjectiveFrameIndex = adjectiveFrameIndex;
        this.gradableAdjectiveFrameIndex=gradableAdjectiveFrameIndex;
    }

    public String findSyntacticFrame(List<String[]> rows) throws Exception {
        String syntactType = null;

        Integer index = 0;
        for (String[] row : rows) {
            if (index == 0) {
                syntactType = this.findSyntacticFrame(row);
                break;
            }
        }

        return syntactType;
    }

    public String findSyntacticFrame(String[] row) throws Exception {
        System.out.println("row.length::"+row.length);
        String nounPPFrame = row[nounPPIndex];
       
    
        try {
            if (nounPPFrame.equals(NounPPFrame)) {
                return NounPPFrame;
            } else if (row[transitiveIndex].equals(TransitiveFrame)) {
                return TransitiveFrame;
            } else if (row[InTransitiveIndex].equals(IntransitivePPFrame)) {
                return IntransitivePPFrame;
            } else if (row[adjectiveFrameIndex].equals(AdjectiveAttributiveFrame)) {
                return AdjectiveAttributiveFrame;
            } else if (row[gradableAdjectiveFrameIndex].equals(AdjectiveSuperlativeFrame)) {
                return AdjectiveSuperlativeFrame;
            } else {
                throw new Exception("No grammar entry is found!!!!");
            }
        } catch (NullPointerException ex) {
            throw new Exception("no lexical entries found in csv file" + ex.getMessage().toString()); //To change body of generated methods, choose Tools | Templates.   
        } catch (Exception ex) {
             System.out.println("nounPPIndex::"+nounPPIndex);
         System.out.println("row::"+row.length);
         System.out.println("nounPPFrame::"+nounPPFrame);
         exit(1);
            throw new Exception("invalid entry." + ex.getMessage().toString()); //To change body of generated methods, choose Tools | Templates.   
        }
    }

    private Pair<Boolean, String> findPrefix(String prefix) {
        prefix = prefix.strip().trim();
        for (String key : this.linkedData.getPrefixes().keySet()) {
            String value = this.linkedData.getPrefixes().get(key);
            value = value.strip().trim();
            key = key.strip().trim();
            if (key.equals(prefix)) {
                return new Pair<Boolean, String>(Boolean.TRUE, value);
            }

        }
        return new Pair<Boolean, String>(Boolean.FALSE, prefix);
    }

    public String setReference(String reference) {
        if (reference.contains(LinkedData.http)) {
            return reference;
        } else if (reference.contains(LinkedData.colon)) {
            String[] info = reference.split(LinkedData.colon);
            String prefix = info[0].strip().trim();
            if (linkedData.getPrefixes().containsKey(prefix)) {
                reference = linkedData.getPrefixes().get(prefix) + info[1];
            }

        }
        return reference;
    }
    
  
    public String getTutleFileName() {
        return tutleFileName;
    }

 
    public LinkedData getLinkedData() {
        return linkedData;
    }

    public String getLanguage() {
        return language;
    }

    public String getInputDir() {
        return inputDir;
    }

    public String getTutleString() {
        return this.turtleString;
    }

    public Boolean getConversionFlag() {
        return conversionFlag;
    }

}
