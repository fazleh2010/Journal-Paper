/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.google.gdata.util.common.base.Pair;



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
    private String turtleString = "";
    private String turtleFileName = "";
    private static Integer index = 0;
    private LinkedData linkedData = null;

    public TurtleCreation(String[] row,LinkedData linkedData) throws Exception {
        this.linkedData=linkedData;
        String syntacticFrame = findSyntacticFrame(row);

        if (syntacticFrame.equals(GoogleXslSheet.NounPPFrameStr)) {
            setNounPPFrame(row, syntacticFrame);
        } else if (syntacticFrame.equals(GoogleXslSheet.TransitiveFrameStr)) {
            setTransitiveFrame(row, syntacticFrame);
        } else if (syntacticFrame.equals(GoogleXslSheet.IntransitivePPFrameStr)) {
            setIntransitivePPFrame(row, syntacticFrame);
        } else {
            syntacticFrame = row[GoogleXslSheet.TransitFrameSyntacticFrameIndex];
        }

    }

    private void setNounPPFrame(String[] row, String syntacticFrame) throws Exception {
        this.setLemonEntryId(row[GoogleXslSheet.writtenFormInfinitive]);
        //this.lemonEntry=row[GoogleXslSheet.writtenFormInfinitive];
        this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
        this.writtenFormInfinitive = row[GoogleXslSheet.writtenFormInfinitive];
        this.writtenForm_plural = row[GoogleXslSheet.NounPPFrame.writtenFormPluralIndex];
        this.preposition = row[GoogleXslSheet.NounPPFrame.prepositionIndex];
        this.sense = row[GoogleXslSheet.NounPPFrame.senseIndex];
        //this.reference = row[GoogleXslSheet.NounPPFrame.referenceIndex];
        //this.domain = row[GoogleXslSheet.NounPPFrame.domainIndex];
        //this.range = row[GoogleXslSheet.NounPPFrame.rangeIndex];
        this.reference = this.setReference(row[GoogleXslSheet.NounPPFrame.referenceIndex]);
        this.domain = this.setReference(row[GoogleXslSheet.NounPPFrame.domainIndex]);
        this.range = this.setReference(row[GoogleXslSheet.NounPPFrame.rangeIndex]);

        
        this.nounPPFrameTurtle();
        this.turtleFileName = getFileName(syntacticFrame);
    }

    private void setTransitiveFrame(String[] row, String syntacticFrame) throws Exception {
        this.setLemonEntryId(row[GoogleXslSheet.writtenFormInfinitive]);
        this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
        this.writtenFormInfinitive = row[GoogleXslSheet.writtenFormInfinitive];
        this.writtenForm3rdPerson = row[GoogleXslSheet.TransitFrame.writtenForm3rdPerson];
        this.writtenFormPast = row[GoogleXslSheet.TransitFrame.writtenFormPast];
        this.sense = row[GoogleXslSheet.TransitFrame.senseIndex];
        this.reference = this.setReference(row[GoogleXslSheet.TransitFrame.referenceIndex]);
        this.domain = this.setReference(row[GoogleXslSheet.TransitFrame.referenceIndex]);
        this.range = this.setReference(row[GoogleXslSheet.TransitFrame.rangeIndex]);
        this.transitiveTurtleSense();
        this.turtleFileName = getFileName(syntacticFrame);
    }

    private void setIntransitivePPFrame(String[] row, String syntacticFrame) throws Exception {
        this.setLemonEntryId(row[GoogleXslSheet.writtenFormInfinitive]);
        this.partOfSpeech = row[GoogleXslSheet.partOfSpeechIndex];
        this.writtenFormInfinitive = row[GoogleXslSheet.writtenFormInfinitive];
        this.writtenForm3rdPerson = row[GoogleXslSheet.InTransitFrame.writtenForm3rdPerson];
        this.writtenFormPast = row[GoogleXslSheet.InTransitFrame.writtenFormPast];
        this.preposition = row[GoogleXslSheet.InTransitFrame.preposition];
        this.sense = row[GoogleXslSheet.InTransitFrame.senseIndex];
        this.reference = this.setReference(row[GoogleXslSheet.InTransitFrame.referenceIndex]);
        this.domain = this.setReference(row[GoogleXslSheet.InTransitFrame.domainIndex]);
        this.range = this.setReference(row[GoogleXslSheet.InTransitFrame.rangeIndex]);
        this.inTransitiveTurtleTemporal();
        this.turtleFileName = getFileName(syntacticFrame);
    }

    public void nounPPFrameTurtle() {
       
        this.turtleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :" + this.lemonEntry + " ;\n"
                + "  lemon:entry    :of .\n"
                + "\n"
                + ":" + this.lemonEntry + " a       lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                + "  lemon:canonicalForm  :" + this.lemonEntry + "_form ;\n"
                + "  lemon:synBehavior    :" + this.lemonEntry + "_nounpp ;\n"
                + "  lemon:sense          :" + this.lemonEntry + "_sense_ontomap .\n"
                + "\n"
                + ":" + this.lemonEntry + "_form a lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenFormInfinitive + "\"@en .\n"
                + "\n"
                + ":" + this.lemonEntry + "_nounpp a        lexinfo:NounPPFrame ;\n"
                + "  lexinfo:copulativeArg        :arg1 ;\n"
                + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                + "\n"
                + ":" + this.lemonEntry + "_sense_ontomap a lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping         :" + this.lemonEntry + "_sense_ontomap ;\n"
                + "  lemon:reference           <"+ reference + "> ;\n"
                + "  lemon:subjOfProp          :arg2 ;\n"
                + "  lemon:objOfProp           :arg1 ;\n"
                + "  lemon:condition           :" + this.lemonEntry + "_condition .\n"
                + "\n"
                + ":" + this.lemonEntry + "_condition a lemon:condition ;\n"
                + "  lemon:propertyDomain  <"+domain + "> ;\n"
                + "  lemon:propertyRange   <"+range + "> .\n"
                + "\n"
                + ":arg2 lemon:marker :of .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":of a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .";


        /*this.turtleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
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

    public void transitiveTurtleSense() {
       

        this.turtleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :" + this.lemonEntry + " ;\n"
                + "  lemon:entry    :" + this.lemonEntry + "ed ;\n"
                + "  lemon:entry    :by .\n"
                + "\n"
                + ":" + this.lemonEntry + " a           lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                + "  lemon:canonicalForm  :form_" + lemonEntry + " ;\n"
                + "  lemon:otherForm      :form_" + lemonEntry + "s ;\n"
                + "  lemon:otherForm      :form_" + lemonEntry + "ed ;\n"
                + "  lemon:synBehavior    :" + lemonEntry + "_frame_transitive ;\n"
                + "  lemon:sense          :" + lemonEntry + "_ontomap .\n"
                + "\n"
                + "\n"
                + ":form_" + lemonEntry + " a         lemon:Form ;\n"
                + "  lemon:writtenRep     \"" + this.writtenFormInfinitive + "\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                + "\n"
                + ":form_" + lemonEntry + "s a    lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenForm3rdPerson + "\"@en ;\n"
                + "  lexinfo:person   lexinfo:secondPerson .\n"
                + "\n"
                + ":form_" + lemonEntry + "ed a   lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenFormPast + "\"@en ;\n"
                + "  lexinfo:tense    lexinfo:past .\n"
                + "\n"
                + ":" + lemonEntry + "_frame_transitive a lexinfo:TransitiveFrame ;\n"
                + "  lexinfo:subject          :" + lemonEntry + "_subj ;\n"
                + "  lexinfo:directObject     :" + lemonEntry + "_obj .\n"
                + "\n"
                + ":" + lemonEntry + "_ontomap a   lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :" + lemonEntry + "_ontomap ;\n"
                + "  lemon:reference   <"+this.reference + "> ;\n"
                + "  lemon:subjOfProp  :" + lemonEntry + "_obj ;\n"
                + "  lemon:objOfProp   :" + lemonEntry + "_subj ;\n"
                + "  lemon:condition   :" + lemonEntry + "_condition .\n"
                + "\n"
                + "\n"
                + ":" + lemonEntry + "_condition a    lemon:condition ;\n"
                + "  lemon:propertyDomain <"+this.domain + "> ;\n"
                + "  lemon:propertyRange  <"+this.range + "> .\n"
                + "\n"
                + "\n"
                + ":" + lemonEntry + "ed a            lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                + "  lemon:canonicalForm  :form_" + lemonEntry + "ed_canonical ;\n"
                + "  lemon:otherForm      :form_" + lemonEntry + "ed_by ;\n"
                + "  lemon:synBehavior    :" + lemonEntry + "ed_frame_adjectivepp ;\n"
                + "  lemon:sense          :" + lemonEntry + "ed_ontomap .\n"
                + "\n"
                + ":form_" + lemonEntry + "ed_canonical a lemon:Form ;\n"
                + "  lemon:writtenRep         \"" + this.writtenFormPast + "\"@en .\n"
                + "\n"
                + ":form_" + lemonEntry + "ed_by a    lemon:Form ;\n"
                + "  lemon:writtenRep     \"" + this.writtenFormPast + "\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:participle .\n"
                + "\n"
                + "\n"
                + ":" + lemonEntry + "ed_frame_adjectivepp a  lexinfo:AdjectivePPFrame ;\n"
                + "  lexinfo:copulativeSubject    :" + lemonEntry + "ed_subj ;\n"
                + "  lexinfo:prepositionalAdjunct :" + lemonEntry + "ed_obj .\n"
                + "\n"
                + ":" + lemonEntry + "ed_ontomap a lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :" + lemonEntry + "ed_ontomap ;\n"
                + "  lemon:reference   <http://dbpedia.org/ontology/" + reference + "> ;\n"
                + "  lemon:subjOfProp  :" + lemonEntry + "ed_subj ;\n"
                + "  lemon:objOfProp   :" + lemonEntry + "ed_obj ;\n"
                + "  lemon:condition   :" + lemonEntry + "_condition .\n"
                + "\n"
                + ":" + lemonEntry + "ed_obj lemon:marker :by .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":by a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"by\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "";

    }

    public void inTransitiveTurtleTemporal() {
      
        //System.out.println(this.preposition+"!!!!!!!!!!!!!"+this.preposition);

        this.turtleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :to_" + this.lemonEntry + " ;\n"
                + "  lemon:entry    :in .\n"
                + "\n"
                + ":to_" + this.lemonEntry + " a             lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                + "  lemon:canonicalForm  :form_" + this.lemonEntry + " ;\n"
                + "  lemon:otherForm      :form_" + this.lemonEntry + "_past ;\n"
                + "  lemon:synBehavior    :" + this.lemonEntry + "_frame ;\n"
                + "  lemon:sense          :" + this.lemonEntry + "_ontomap .\n"
                + "\n"
                + ":form_" + this.lemonEntry + " a           lemon:Form ;\n"
                + "  lemon:writtenRep     \"release\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                + "\n"
                + "\n"
                + ":form_" + this.lemonEntry + "_past a lemon:Form ;\n"
                + "  lemon:writtenRep  \"released\"@en ;\n"
                + "  lexinfo:number    lexinfo:singular ;\n"
                + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                + "  lexinfo:tense     lexinfo:past .\n"
                + "\n"
                + "\n"
                + "\n"
                + ":" + this.lemonEntry + "_frame a                  lexinfo:IntransitivePPFrame ;\n"
                + "  lexinfo:subject              :" + this.lemonEntry + "_subj ;\n"
                + "  lexinfo:prepositionalAdjunct :" + this.lemonEntry + "_obj .\n"
                + "\n"
                + ":" + this.lemonEntry + "_ontomap a     lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :" + this.lemonEntry + "_ontomap ;\n"
                + "  lemon:reference   <"+this.reference+"> ;\n"
                + "  lemon:subjOfProp  :" + this.lemonEntry + "_obj ;\n"
                + "  lemon:objOfProp   :" + this.lemonEntry + "_subj ;\n"
                + "  lemon:condition   :" + this.lemonEntry + "_condition .\n"
                + "\n"
                + "\n"
                + ":" + this.lemonEntry + "_condition a      lemon:condition ;\n"
                + "  lemon:propertyDomain <"+this.domain+"> ;\n"
                + "  lemon:propertyRange  <"+this.range+"> .\n"
                + "\n"
                + ":" + this.lemonEntry + "_obj lemon:marker :in .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":in a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + this.preposition + "\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "\n"
                + "";

    }

    /*public void inTransitiveTurtleSense1() {
        this.reference = this.setReference(reference);
        this.domain = this.setReference(domain);
        this.range = this.setReference(range);

        this.turtleString = "@prefix :        <http://localhost:8080/lexicon#> .\n"
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
                + "  lemon:writtenRep     \"" + this.writtenFormInfinitive + "\"@en ;\n"
                + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                + "\n"
                + ":form_grows a      lemon:Form ;\n"
                + "  lemon:writtenRep \"" + this.writtenForm3rdPerson + "\"@en ;\n"
                + "  lexinfo:number   lexinfo:singular ;\n"
                + "  lexinfo:person   lexinfo:thirdPerson ;\n"
                + "  lexinfo:tense    lexinfo:present .\n"
                + "\n"
                + ":form_grow_plural a lemon:Form ;\n"
                + "  lemon:writtenRep  \"" + this.writtenFormInfinitive + "\"@en ;\n"
                + "  lexinfo:number    lexinfo:plural ;\n"
                + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                + "  lexinfo:tense     lexinfo:present .\n"
                + "\n"
                + ":grow_condition a      lemon:condition ;\n"
                + "  lemon:propertyDomain <http://dbpedia.org/ontology/" + this.domain + "> ;\n"
                + "  lemon:propertyRange  <http://dbpedia.org/ontology/" + this.range + "> .\n"
                + "\n"
                + ":grow_frame a                  lexinfo:IntransitivePPFrame ;\n"
                + "  lexinfo:subject              :grow_subj ;\n"
                + "  lexinfo:prepositionalAdjunct :grow_obj .\n"
                + "\n"
                + ":grow_ontomap a     lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping :grow_ontomap ;\n"
                + "  lemon:reference   <http://dbpedia.org/ontology/" + this.reference + "> ;\n"
                + "  lemon:subjOfProp  :grow_obj ;\n"
                + "  lemon:objOfProp   :grow_subj ;\n"
                + "  lemon:condition   :grow_condition .\n"
                + "\n"
                + ":grow_obj lemon:marker :in .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":in a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + "in" + "\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "";

    }*/
    private String setReference(String reference) throws Exception {
        if (reference.contains(":")) {
            String[] info = reference.split(":");
            String prefix = info[0].strip().trim();
            
            //this is temporary solution. This information should be taken from Json file.
            if (this.linkedData.getEndpoint().contains("wikidata")) {
                if (prefix.contains("wdt")) {
                    reference = "http://www.wikidata.org/prop/direct/" + info[1];
                } else if (prefix.contains("wd")) {
                    reference = "http://www.wikidata.org/entity/" + info[1];
                }
            } else if (this.linkedData.getEndpoint().contains("dbpedia")) {
                if (prefix.contains("dbo")) {
                    reference = "http://dbpedia.org/ontology/" + info[1];
                } else if (prefix.contains("owl")) {
                    reference = "http://www.w3.org/2002/07/owl/" + info[1];
                } else if (prefix.contains("xsd")) {
                    reference = "http://www.w3.org/2001/XMLSchema/" + info[1];
                } else {
                    reference = "http://dbpedia.org/ontology/" + info[1];
                }
            }

            /*Pair<Boolean, String> pair = this.findPrefix(prefix);
            if (pair.getFirst()) {
                reference = pair.getSecond();
            } else {
                throw new Exception("the prefix " + prefix + " is not valid for dataset " + this.linkedData.getEndpoint());
            }*/

        }
        return reference;
    }

    private String findSyntacticFrame(String[] row) throws Exception {
        /*System.out.println("row.length::" + row.length);
        System.out.println("row[GoogleXslSheet.NounPPFrameSyntacticFrameIndex]::" + row[GoogleXslSheet.NounPPFrameSyntacticFrameIndex]);
        System.out.println("row[GoogleXslSheet.TransitFrameSyntacticFrameIndex]::" + row[GoogleXslSheet.TransitFrameSyntacticFrameIndex]);
        System.out.println("row[GoogleXslSheet.InTransitFrameSyntacticFrameIndex]::" + row[GoogleXslSheet.InTransitFrameSyntacticFrameIndex]);
        */
        try{
        if (row[GoogleXslSheet.NounPPFrameSyntacticFrameIndex].equals(GoogleXslSheet.NounPPFrameStr)) {
            return GoogleXslSheet.NounPPFrameStr;
        } else if (row[GoogleXslSheet.TransitFrameSyntacticFrameIndex].equals(GoogleXslSheet.TransitiveFrameStr)) {
            return GoogleXslSheet.TransitiveFrameStr;
        } else if (row[GoogleXslSheet.InTransitFrameSyntacticFrameIndex].equals(GoogleXslSheet.IntransitivePPFrameStr)) {
            return GoogleXslSheet.IntransitivePPFrameStr;
        } else {
            throw new Exception("invalid entry."); //To change body of generated methods, choose Tools | Templates.   
        }
        }catch(Exception ex){
            throw new Exception("invalid entry."+ex.getMessage().toString()); //To change body of generated methods, choose Tools | Templates.   
        }
    }

    private void setLemonEntryId(String[] row) {
        this.lemonEntry = row[GoogleXslSheet.lemonEntryIndex];
    }

    private void setLemonEntryId(String writtenForm) {
        this.lemonEntry = this.modify(writtenForm);

    }

    public String getTurtleFileName() {
        return turtleFileName;
    }

    private String getFileName(String syntacticFrame) {
        return syntacticFrame + "-lexicon" + "-" + lemonEntry.replace("/", "") + ".ttl";

    }

    public String getTurtleString() {
        return this.turtleString;
    }

    private String modify(String string) {
        /*string = string.replaceAll("[^a-zA-Z0-9]", " ");
        string = string.toLowerCase().strip().trim().replace(" ", "_");*/
        index = index + 1;
        return "LexicalEntry_" + index.toString();
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

}
