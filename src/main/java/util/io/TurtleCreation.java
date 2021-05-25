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

    private String lemonEntry = "birthPlace_of";
    private String partOfSpeech = "noun";
    private String writtenForm_singular = "birth place";
    private String writtenForm_plural = "-";
    private String writtenFormInfinitive = "";
    private String writtenForm3rdPerson = "";
    private String writtenFormPast = "";
    private String preposition = "of";
    private String sense = "1";
    private String reference = "dbo:birthPlace";
    private String domain = "dbo:Person";
    private String range = "dbo:Place";
    private String tutleString = "";
    private String tutleFileName = "";

    public TurtleCreation(String[] row, String syntacticFrame) {
        if (syntacticFrame.contains("NounPPFrame")) {
           setNounPPFrame(row, syntacticFrame);
        } else if (syntacticFrame.contains("TransitiveFrame")) {
            setTransitiveFrame(row, syntacticFrame);
        }

    }

    private void setNounPPFrame(String[] row, String syntacticFrame) {
            this.lemonEntry = row[GoogleXslSheet.lemonEntryIndex];
            this.partOfSpeech = row[GoogleXslSheet.NounPPFrame.partOfSpeechIndex];
            this.writtenForm_singular = row[GoogleXslSheet.NounPPFrame.writtenFormSingularIndex];
            this.writtenForm_plural = row[GoogleXslSheet.NounPPFrame.writtenFormPluralIndex];
            this.preposition = row[GoogleXslSheet.NounPPFrame.prepositionIndex];
            this.sense = row[GoogleXslSheet.NounPPFrame.senseIndex];
            this.reference = row[GoogleXslSheet.NounPPFrame.referenceIndex];
            this.domain = row[GoogleXslSheet.NounPPFrame.domainIndex];
            this.range = row[GoogleXslSheet.NounPPFrame.rangeIndex];
            this.tutleString=nounPPFrameTurtle();
            this.tutleFileName = getFileName(syntacticFrame) ;
    }

    private void setTransitiveFrame(String[] row, String syntacticFrame) {
            this.lemonEntry = row[GoogleXslSheet.lemonEntryIndex];
            this.partOfSpeech = row[GoogleXslSheet.TransitFrame.partOfSpeechIndex];
            this.writtenFormInfinitive = row[GoogleXslSheet.TransitFrame.writtenFormInfinitive];
            this.writtenForm3rdPerson = row[GoogleXslSheet.TransitFrame.writtenForm3rdPerson];
            this.writtenFormPast = row[GoogleXslSheet.TransitFrame.writtenFormPast];
            this.sense = row[GoogleXslSheet.TransitFrame.senseIndex];
            this.reference = row[GoogleXslSheet.TransitFrame.referenceIndex];
            this.domain = row[GoogleXslSheet.TransitFrame.domainIndex];
            this.range = row[GoogleXslSheet.TransitFrame.rangeIndex];
            this.tutleString=transitiveTurtleSense1();
            this.tutleFileName = getFileName(syntacticFrame) ;
    }

    public String getTutleFileName() {
        return tutleFileName;
    }
    
    private String getFileName(String syntacticFrame) {
      return syntacticFrame+"-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

    public String getWrittenFormInfinitive() {
        return writtenFormInfinitive;
    }

    public String getWrittenForm3rdPerson() {
        return writtenForm3rdPerson;
    }

    public String getWrittenFormPast() {
        return writtenFormPast;
    }

    public String getRange() {
        return range;
    }

    public String getTutleString() {
        return tutleString;
    }

    public TurtleCreation(String[] row) {

    }

    public TurtleCreation() {
    }

    public String getLemonEntry() {
        return lemonEntry;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getWrittenForm_singular() {
        return writtenForm_singular;
    }

    public String getWrittenForm_plural() {
        return writtenForm_plural;
    }

    public String getPreposition() {
        return preposition;
    }

    public String getSense() {
        return sense;
    }

    public String getReference() {
        return reference;
    }

    public String getDomain() {
        return domain;
    }

    

    public String nounPPFrameTurtle() {
        this.reference = this.setReference(reference);
        this.domain = this.setReference(domain);
        this.range = this.setReference(range);

        String template = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :birthPlace_of ;\n"
                + "  lemon:entry    :of .\n"
                + "\n"
                + ":birthPlace_of a       lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                + "  lemon:canonicalForm  :birthPlace_form ;\n"
                + "  lemon:synBehavior    :birthPlace_of_nounpp ;\n"
                + "  lemon:sense          :birthPlace_sense_ontomap .\n"
                + "\n"
                + ":birthPlace_form a lemon:Form ;\n"
                + "  lemon:writtenRep \"" + writtenForm_singular + "\"@en .\n"
                + "\n"
                + ":birthPlace_of_nounpp a        lexinfo:NounPPFrame ;\n"
                + "  lexinfo:copulativeArg        :arg1 ;\n"
                + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                + "\n"
                + ":birthPlace_sense_ontomap a lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping         :birthPlace_sense_ontomap ;\n"
                + "  lemon:reference           <http://dbpedia.org/ontology/" + reference + "> ;\n"
                + "  lemon:subjOfProp          :arg2 ;\n"
                + "  lemon:objOfProp           :arg1 ;\n"
                + "  lemon:condition           :birthPlace_condition .\n"
                + "\n"
                + ":birthPlace_condition a lemon:condition ;\n"
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
        return template;

    }

    public String transitiveTurtleSense1() {
        this.reference = this.setReference(reference);
        this.domain = this.setReference(domain);
        this.range = this.setReference(range);

        String template = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :to_cross ;\n"
                + "  lemon:entry    :crossed ;\n"
                + "  lemon:entry    :by .\n"
                + "\n"
                + ":to_cross a            lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                + "  lemon:canonicalForm  :form_cross ;\n"
                + "  lemon:otherForm      :form_crosss ;\n"
                + "  lemon:otherForm      :form_crossed ;\n"
                + "  lemon:synBehavior    :cross_frame_transitive ;\n"
                + "  lemon:sense          :cross_ontomap .\n"
                + "\n"
                + "\n"
                + ":form_cross a          lemon:Form ;\n"
                + "  lemon:writtenRep     \"" + this.writtenFormInfinitive + "\"@en ;\n"
                + "  lexinfo:tense        lexinfo:present ;\n"
                + "  lexinfo:person       lexinfo:firstPerson ;\n"
                + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                + "\n"
                + ":form_crosss a     lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenForm3rdPerson + "\"@en ;\n"
                + "  lexinfo:tense    lexinfo:present ;\n"
                + "  lexinfo:person   lexinfo:secondPerson .\n"
                + "\n"
                + ":form_crossed a    lemon:Form ;\n"
                + "  lemon:writtenRep \"" +this.writtenFormPast + "\"@en ;\n"
                + "  lexinfo:tense    lexinfo:past .\n"
                + "\n"
                + "\n"
                + ":cross_frame_transitive a lexinfo:TransitiveFrame ;\n"
                + "  lexinfo:subject         :cross_subj ;\n"
                + "  lexinfo:directObject    :cross_obj .\n"
                + "\n"
                + ":cross_ontomap a    lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :cross_ontomap ;\n"
                + "  lemon:reference   <http://dbpedia.org/ontology/" + reference + "> ;\n"
                + "  lemon:subjOfProp  :cross_subj ;\n"
                + "  lemon:objOfProp   :cross_obj ;\n"
                + "  lemon:condition   :cross_condition .\n"
                + "\n"
                + ":cross_condition a     lemon:condition ;\n"
                + "  lemon:propertyDomain <http://dbpedia.org/ontology/" + domain + "> ;\n"
                + "  lemon:propertyRange  <http://dbpedia.org/ontology/" + range + "> .\n"
                + "\n"
                + "\n"
                + "# see https://lemon-model.net/lemon-cookbook/node17.html\n"
                + "\n"
                + ":crossed a             lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                + "  lemon:canonicalForm  :form_crossed_canonical ;\n"
                + "  lemon:otherForm      :form_crossed_by ;\n"
                + "  lemon:synBehavior    :crossed_frame_adjectivepp ;\n"
                + "  lemon:sense          :crossed_ontomap .\n"
                + "\n"
                + ":form_crossed_canonical a lemon:Form ;\n"
                + "  lemon:writtenRep        \"" + this.writtenForm3rdPerson + "\"@en .\n"
                + "\n"
                + ":form_crossed_by a     lemon:Form ;\n"
                + "  lemon:writtenRep     \"" +this.writtenForm3rdPerson + "\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:participle .\n"
                + "\n"
                + "\n"
                + ":crossed_frame_adjectivepp a   lexinfo:AdjectivePPFrame ;\n"
                + "  lexinfo:copulativeSubject    :crossed_subj ;\n"
                + "  lexinfo:prepositionalAdjunct :crossed_obj .\n"
                + "\n"
                + ":crossed_ontomap a  lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :crossed_ontomap ;\n"
                + "  lemon:reference   <http://dbpedia.org/ontology/" + reference + "> ;\n"
                + "  lemon:subjOfProp  :crossed_obj ;\n"
                + "  lemon:objOfProp   :crossed_subj ;\n"
                + "  lemon:condition   :cross_condition .\n"
                + "\n"
                + ":crossed_obj lemon:marker :by .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":by a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "\n"
                + "";
        return template;

    }

    private String setReference(String reference) {
        if (reference.contains(":")) {
            String[] info = reference.split(":");
            reference = info[1];

        }
        return reference.strip().trim();
    }

}
