/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class TurtleCreation {

    private String lemonEntry = "";
    private String partOfSpeech = "";
    private String writtenForm_plural = "";
    private String writtenFormInfinitive = "";
    private String writtenForm3rdPerson = "";
    private String writtenFormPast = "";
    private String preposition = "";
    private String sense = "";
    private String reference = "";
    private String domain = "";
    private String range = "";
    private String tutleString = "";
    private String tutleFileName = "";
   

    public TurtleCreation(String[] row) {
        String syntacticFrame = findSyntacticFrame(row);

        if (syntacticFrame.contains(GoogleXslSheet.NounPPFrameStr)) {
            setNounPPFrame(row, syntacticFrame);
        } else if (syntacticFrame.contains(GoogleXslSheet.TransitiveFrameStr)) {
            setTransitiveFrame(row, syntacticFrame);
        } else if (syntacticFrame.contains(GoogleXslSheet.IntransitivePPFrameStr)) {
            setIntransitivePPFrame(row, syntacticFrame);
        } else {
            syntacticFrame = row[GoogleXslSheet.TransitFrameSyntacticFrameIndex];
        }

    }

    private void setNounPPFrame(String[] row, String syntacticFrame) {
        this.lemonEntry = row[GoogleXslSheet.lemonEntryIndex];
        this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
        this.writtenFormInfinitive = row[GoogleXslSheet.writtenFormInfinitive];
        this.writtenForm_plural = row[GoogleXslSheet.NounPPFrame.writtenFormPluralIndex];
        this.preposition = row[GoogleXslSheet.NounPPFrame.prepositionIndex];
        this.sense = row[GoogleXslSheet.NounPPFrame.senseIndex];
        this.reference = row[GoogleXslSheet.NounPPFrame.referenceIndex];
        this.domain = row[GoogleXslSheet.NounPPFrame.domainIndex];
        this.range = row[GoogleXslSheet.NounPPFrame.rangeIndex];
        this.nounPPFrameTurtle();
        this.tutleFileName = getFileName(syntacticFrame);
    }

    private void setTransitiveFrame(String[] row, String syntacticFrame) {
        this.lemonEntry = row[GoogleXslSheet.lemonEntryIndex];
        this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
        this.writtenFormInfinitive = row[GoogleXslSheet.writtenFormInfinitive];
        this.writtenForm3rdPerson = row[GoogleXslSheet.TransitFrame.writtenForm3rdPerson];
        this.writtenFormPast = row[GoogleXslSheet.TransitFrame.writtenFormPast];
        this.sense = row[GoogleXslSheet.TransitFrame.senseIndex];
        this.reference = row[GoogleXslSheet.TransitFrame.referenceIndex];
        this.domain = row[GoogleXslSheet.TransitFrame.domainIndex];
        this.range = row[GoogleXslSheet.TransitFrame.rangeIndex];
        this.transitiveTurtleSense1();
        this.tutleFileName = getFileName(syntacticFrame);
    }

    private void setIntransitivePPFrame(String[] row, String syntacticFrame) {
        this.lemonEntry = row[GoogleXslSheet.lemonEntryIndex];
        this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
        this.writtenFormInfinitive = row[GoogleXslSheet.writtenFormInfinitive];
        this.writtenForm3rdPerson = row[GoogleXslSheet.InTransitFrame.writtenForm3rdPerson];
        this.writtenFormPast = row[GoogleXslSheet.InTransitFrame.writtenFormPast];
        this.sense = row[GoogleXslSheet.InTransitFrame.senseIndex];
        this.reference = row[GoogleXslSheet.InTransitFrame.referenceIndex];
        this.domain = row[GoogleXslSheet.InTransitFrame.domainIndex];
        this.range = row[GoogleXslSheet.InTransitFrame.rangeIndex];
        this.inTransitiveTurtleSense1();
        this.tutleFileName = getFileName(syntacticFrame);
    }

    public String getTutleFileName() {
        return tutleFileName;
    }

    private String getFileName(String syntacticFrame) {
        return syntacticFrame + "-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

    public String getTutleString() {
        return this.tutleString;
    }

  
    public void nounPPFrameTurtle() {
        this.reference = this.setReference(reference);
        this.domain = this.setReference(domain);
        this.range = this.setReference(range);
        
        this.tutleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :"+this.lemonEntry+" ;\n"
                + "  lemon:entry    :of .\n"
                + "\n"
                + ":"+this.lemonEntry+" a       lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                + "  lemon:canonicalForm  :"+this.lemonEntry+"_form ;\n"
                + "  lemon:synBehavior    :"+this.lemonEntry+"_nounpp ;\n"
                + "  lemon:sense          :"+this.lemonEntry+"_sense_ontomap .\n"
                + "\n"
                + ":"+this.lemonEntry+"_form a lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenFormInfinitive + "\"@en .\n"
                + "\n"
                + ":"+this.lemonEntry+"_nounpp a        lexinfo:NounPPFrame ;\n"
                + "  lexinfo:copulativeArg        :arg1 ;\n"
                + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                + "\n"
                + ":"+this.lemonEntry+"_sense_ontomap a lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping         :"+this.lemonEntry+"_sense_ontomap ;\n"
                + "  lemon:reference           <http://dbpedia.org/ontology/" + reference + "> ;\n"
                + "  lemon:subjOfProp          :arg2 ;\n"
                + "  lemon:objOfProp           :arg1 ;\n"
                + "  lemon:condition           :"+this.lemonEntry+"_condition .\n"
                + "\n"
                + ":"+this.lemonEntry+"_condition a lemon:condition ;\n"
                + "  lemon:propertyDomain  <http://dbpedia.org/ontology/" + domain + "> ;\n"
                + "  lemon:propertyRange   <http://dbpedia.org/ontology/" + range + "> .\n"
                + "\n"
                + ":arg2 lemon:marker :of .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":of a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .";
        

        /*this.tutleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :"+this.lemonEntry+" ;\n"
                + "  lemon:entry    :of .\n"
                + "\n"
                + ":"+this.lemonEntry+" a       lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                + "  lemon:canonicalForm  :"+birthPlace+"_form ;\n"
                + "  lemon:synBehavior    :"+this.lemonEntry+"_nounpp ;\n"
                + "  lemon:sense          :"+birthPlace+"_sense_ontomap .\n"
                + "\n"
                + ":"+birthPlace+"_form a lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenFormInfinitive + "\"@en .\n"
                + "\n"
                + ":"+this.lemonEntry+"_nounpp a        lexinfo:NounPPFrame ;\n"
                + "  lexinfo:copulativeArg        :arg1 ;\n"
                + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                + "\n"
                + ":"+birthPlace+"_sense_ontomap a lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping         :"+birthPlace+"_sense_ontomap ;\n"
                + "  lemon:reference           <http://dbpedia.org/ontology/" + reference + "> ;\n"
                + "  lemon:subjOfProp          :arg2 ;\n"
                + "  lemon:objOfProp           :arg1 ;\n"
                + "  lemon:condition           :"+birthPlace+"_condition .\n"
                + "\n"
                + ":"+birthPlace+"_condition a lemon:condition ;\n"
                + "  lemon:propertyDomain  <http://dbpedia.org/ontology/" + domain + "> ;\n"
                + "  lemon:propertyRange   <http://dbpedia.org/ontology/" + range + "> .\n"
                + "\n"
                + ":arg2 lemon:marker :of .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":of a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .";*/

    }

    public void transitiveTurtleSense1() {
        this.reference = this.setReference(reference);
        this.domain = this.setReference(domain);
        this.range = this.setReference(range);

        this.tutleString
                = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :to_design ;\n"
                + "  lemon:entry    :designed ;\n"
                + "  lemon:entry    :by .\n"
                + "\n"
                + ":to_design a           lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                + "  lemon:canonicalForm  :form_design ;\n"
                + "  lemon:otherForm      :form_designs ;\n"
                + "  lemon:otherForm      :form_designed ;\n"
                + "  lemon:synBehavior    :design_frame_transitive ;\n"
                + "  lemon:sense          :design_ontomap .\n"
                + "\n"
                + "\n"
                + ":form_design a         lemon:Form ;\n"
                + "  lemon:writtenRep     \"" + this.writtenFormInfinitive + "\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                + "\n"
                + ":form_designs a    lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenForm3rdPerson + "\"@en ;\n"
                + "  lexinfo:person   lexinfo:secondPerson .\n"
                + "\n"
                + ":form_designed a   lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenFormPast + "\"@en ;\n"
                + "  lexinfo:tense    lexinfo:past .\n"
                + "\n"
                + ":design_frame_transitive a lexinfo:TransitiveFrame ;\n"
                + "  lexinfo:subject          :design_subj ;\n"
                + "  lexinfo:directObject     :design_obj .\n"
                + "\n"
                + ":design_ontomap a   lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :design_ontomap ;\n"
                + "  lemon:reference   <http://dbpedia.org/ontology/" + this.reference + "> ;\n"
                + "  lemon:subjOfProp  :design_obj ;\n"
                + "  lemon:objOfProp   :design_subj ;\n"
                + "  lemon:condition   :design_condition .\n"
                + "\n"
                + "\n"
                + ":design_condition a    lemon:condition ;\n"
                + "  lemon:propertyDomain <http://dbpedia.org/ontology/" + this.domain + "> ;\n"
                + "  lemon:propertyRange  <http://dbpedia.org/ontology/" + this.range + "> .\n"
                + "\n"
                + "\n"
                + ":designed a            lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                + "  lemon:canonicalForm  :form_designed_canonical ;\n"
                + "  lemon:otherForm      :form_designed_by ;\n"
                + "  lemon:synBehavior    :designed_frame_adjectivepp ;\n"
                + "  lemon:sense          :designed_ontomap .\n"
                + "\n"
                + ":form_designed_canonical a lemon:Form ;\n"
                + "  lemon:writtenRep         \"" + this.writtenFormPast + "\"@en .\n"
                + "\n"
                + ":form_designed_by a    lemon:Form ;\n"
                + "  lemon:writtenRep     \"" + this.writtenFormPast + "\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:participle .\n"
                + "\n"
                + "\n"
                + ":designed_frame_adjectivepp a  lexinfo:AdjectivePPFrame ;\n"
                + "  lexinfo:copulativeSubject    :designed_subj ;\n"
                + "  lexinfo:prepositionalAdjunct :designed_obj .\n"
                + "\n"
                + ":designed_ontomap a lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :designed_ontomap ;\n"
                + "  lemon:reference   <http://dbpedia.org/ontology/" + reference + "> ;\n"
                + "  lemon:subjOfProp  :designed_subj ;\n"
                + "  lemon:objOfProp   :designed_obj ;\n"
                + "  lemon:condition   :design_condition .\n"
                + "\n"
                + ":designed_obj lemon:marker :by .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":by a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"by\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "";

    }

    public void inTransitiveTurtleSense1() {
        this.reference = this.setReference(reference);
        this.domain = this.setReference(domain);
        this.range = this.setReference(range);

        this.tutleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :to_grow ;\n"
                + "  lemon:entry    :in .\n"
                + "\n"
                + ":to_grow a             lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                + "  lemon:canonicalForm  :form_grow ;\n"
                + "  lemon:otherForm      :form_grows ;\n"
                + "  lemon:otherForm      :form_grow_plural ;\n"
                + "  lemon:synBehavior    :grow_frame ;\n"
                + "  lemon:sense          :grow_ontomap .\n"
                + "\n"
                + ":form_grow a           lemon:Form ;\n"
                + "  lemon:writtenRep     \""+this.writtenFormInfinitive+"\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                + "\n"
                + ":form_grows a      lemon:Form ;\n"
                + "  lemon:writtenRep \""+this.writtenForm3rdPerson+"\"@en ;\n"
                + "  lexinfo:number   lexinfo:singular ;\n"
                + "  lexinfo:person   lexinfo:thirdPerson ;\n"
                + "  lexinfo:tense    lexinfo:present .\n"
                + "\n"
                + ":form_grow_plural a lemon:Form ;\n"
                + "  lemon:writtenRep  \""+this.writtenFormInfinitive+"\"@en ;\n"
                + "  lexinfo:number    lexinfo:plural ;\n"
                + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                + "  lexinfo:tense     lexinfo:present .\n"
                + "\n"
                + ":grow_condition a      lemon:condition ;\n"
                + "  lemon:propertyDomain <http://dbpedia.org/ontology/"+this.domain+"> ;\n"
                + "  lemon:propertyRange  <http://dbpedia.org/ontology/"+this.range+"> .\n"
                + "\n"
                + ":grow_frame a                  lexinfo:IntransitivePPFrame ;\n"
                + "  lexinfo:subject              :grow_subj ;\n"
                + "  lexinfo:prepositionalAdjunct :grow_obj .\n"
                + "\n"
                + ":grow_ontomap a     lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :grow_ontomap ;\n"
                + "  lemon:reference   <http://dbpedia.org/ontology/"+this.reference+"> ;\n"
                + "  lemon:subjOfProp  :grow_obj ;\n"
                + "  lemon:objOfProp   :grow_subj ;\n"
                + "  lemon:condition   :grow_condition .\n"
                + "\n"
                + ":grow_obj lemon:marker :in .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":in a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \""+"in"+"\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "";

    }

    private String setReference(String reference) {
        if (reference.contains(":")) {
            String[] info = reference.split(":");
            reference = info[1];

        }
        return reference.strip().trim();
    }

    private String findSyntacticFrame(String[] row) {
        String syntacticFrame = row[GoogleXslSheet.NounPPFrameSyntacticFrameIndex];

        if (isSyntacticFrame(syntacticFrame)) {
            return syntacticFrame;
        } else {
            syntacticFrame = row[GoogleXslSheet.InTransitFrameSyntacticFrameIndex];
            if (isSyntacticFrame(syntacticFrame)) {
                return syntacticFrame;
            } else {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.   
            }
        }

    }
    
    private Boolean isSyntacticFrame(String syntacticFrame) {
        if (syntacticFrame.contains(GoogleXslSheet.NounPPFrameStr)
                || syntacticFrame.contains(GoogleXslSheet.TransitiveFrameStr)
                || syntacticFrame.contains(GoogleXslSheet.IntransitivePPFrameStr)) {
            return true;
        }
        return false;

    }

}
