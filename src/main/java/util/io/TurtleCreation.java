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
//LemonEntry	partOfSpeech	writtenForm (singular)	writtenForm (plural)	preposition	SyntacticFrame	copulativeArg	prepositionalAdjunct	sense	reference	domain	range	GrammarRule1:question1	SPARQL	GrammarRule1: question2	SPARQL Question 2	GrammarRule 1: questions	SPARQL 	NP (Grammar Rule 2)		grammar rules	numberOfQuestions
//birthPlace_of	noun	birth place	-	of	NounPPFrame	range	domain	1	dbo:birthPlace	dbo:Person	dbo:Place	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?		2	

    private String LemonEntry = "birthPlace_of";
    private String partOfSpeech = "noun";
    private String writtenForm_singular = "birth place";
    private String writtenForm_plural = "-";
    private String preposition = "of";
    private String SyntacticFrame = "NounPPFrame";
    private String copulativeArg = "range";
    private String prepositionalAdjunct = "domain";
    private String sense = "1";
    private String reference = "dbo:birthPlace";
    private String domain = "dbo:Person";
    private String range = "dbo:Place";
    private String []verbForms=new String[3];

    public TurtleCreation(String[] row) {

        this.LemonEntry = row[0];
        this.partOfSpeech = row[1];
        this.writtenForm_singular = row[2];
        this.writtenForm_plural = row[3];

        this.preposition = row[4];
        this.SyntacticFrame = row[5];
        this.copulativeArg = row[6];
        this.prepositionalAdjunct = row[7];

        this.sense = row[8];
        this.reference = row[9];
        this.domain = row[10];
        this.range = row[11];

    }

    public TurtleCreation() {
    }


    public String getLemonEntry() {
        return LemonEntry;
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

    public String getSyntacticFrame() {
        return SyntacticFrame;
    }

    public String getCopulativeArg() {
        return copulativeArg;
    }

    public String getPrepositionalAdjunct() {
        return prepositionalAdjunct;
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
    
    public static void main(String args[]) throws IOException {

      
        String mainResources = "src/main/resources/lexicon/";
        String csvDir = "csv/";
        String inputDir = mainResources + csvDir;
        String outputDir = null;
        String testResources = "src/test/resources/lexicon/";
        
        Set<String> frames=new HashSet<String>();
        frames.add("NounPPFrame");
      
        for (String frame : frames) {
            List<File> files = FileUtils.getFiles(inputDir, frame, ".csv");
            if (frame.contains("NounPPFrame")) {
                outputDir = testResources + "en/" + "nouns/";
            }
            for (File file : files) {
                CsvFile csvFile = new CsvFile();
                List<String[]> rows = csvFile.getRows(file);
                Integer index = 0;
                for (String[] row : rows) {
                    if (index == 0) {
                        ;
                    } else {
                        TurtleCreation nounPPFrameXsl = new TurtleCreation(row);
                        String lemonEntry=nounPPFrameXsl.getLemonEntry();
                        lemonEntry=lemonEntry.replace("/", "");
                        String fileName = "z-csv-lexicon" + "-" + lemonEntry + ".ttl";
                        String tutleString = nounPPFrameXsl.nounPPFrameTurtle();
                        FileUtils.stringToFile(tutleString, outputDir + fileName);
                    }
                    index=index+1;
                    
                }

            }
        }
        

      

        /*NounPPFrameXsl nounPPFrameXsl = new NounPPFrameXsl(LemonEntry, partOfSpeech, writtenForm_singular, writtenForm_plural,
                preposition, SyntacticFrame, copulativeArg, prepositionalAdjunct,
                sense, reference, domain);*/
        //System.out.println(tutleString);
    }

    public String nounPPFrameTurtle() {
        this.reference=this.setReference(reference);
        this.domain=this.setReference(domain);
        this.range=this.setReference(range);
       
        
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
                + "  lemon:writtenRep \""+writtenForm_singular+"\"@en .\n"
                + "\n"
                + ":birthPlace_of_nounpp a        lexinfo:NounPPFrame ;\n"
                + "  lexinfo:copulativeArg        :arg1 ;\n"
                + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                + "\n"
                + ":birthPlace_sense_ontomap a lemon:OntoMap, lemon:LexicalSense ;\n"
                + "  lemon:ontoMapping         :birthPlace_sense_ontomap ;\n"
                + "  lemon:reference           <http://dbpedia.org/ontology/"+reference+"> ;\n"
                + "  lemon:subjOfProp          :arg2 ;\n"
                + "  lemon:objOfProp           :arg1 ;\n"
                + "  lemon:condition           :birthPlace_condition .\n"
                + "\n"
                + ":birthPlace_condition a lemon:condition ;\n"
                + "  lemon:propertyDomain  <http://dbpedia.org/ontology/"+domain+"> ;\n"
                + "  lemon:propertyRange   <http://dbpedia.org/ontology/"+range+"> .\n"
                + "\n"
                + ":arg2 lemon:marker :of .\n"
                + "\n"
                + "## Prepositions ##\n"
                + "\n"
                + ":of a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \""+preposition+"\"@en ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .";
        return template;

    }

    public String transitiveTurtleSense1() {
        this.reference=this.setReference(reference);
        this.domain=this.setReference(domain);
        this.range=this.setReference(range);
       
        
        String template = "@prefix :        <http://localhost:8080/lexicon#> .\n" +
"\n" +
"@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n" +
"@prefix lemon:   <http://lemon-model.net/lemon#> .\n" +
"\n" +
"@base            <http://localhost:8080#> .\n" +
"\n" +
":lexicon_en a    lemon:Lexicon ;\n" +
"  lemon:language \"en\" ;\n" +
"  lemon:entry    :to_cross ;\n" +
"  lemon:entry    :crossed ;\n" +
"  lemon:entry    :by .\n" +
"\n" +
":to_cross a            lemon:LexicalEntry ;\n" +
"  lexinfo:partOfSpeech lexinfo:verb ;\n" +
"  lemon:canonicalForm  :form_cross ;\n" +
"  lemon:otherForm      :form_crosss ;\n" +
"  lemon:otherForm      :form_crossed ;\n" +
"  lemon:synBehavior    :cross_frame_transitive ;\n" +
"  lemon:sense          :cross_ontomap .\n" +
"\n" +
"\n" +
":form_cross a          lemon:Form ;\n" +
"  lemon:writtenRep     \""+verbForms[0]+"\"@en ;\n" +
"  lexinfo:tense        lexinfo:present ;\n" +
"  lexinfo:person       lexinfo:firstPerson ;\n" +
"  lexinfo:verbFormMood lexinfo:infinitive .\n" +
"\n" +
":form_crosss a     lemon:Form ;\n" +
"  lemon:writtenRep \""+verbForms[1]+"\"@en ;\n" +
"  lexinfo:tense    lexinfo:present ;\n" +
"  lexinfo:person   lexinfo:secondPerson .\n" +
"\n" +
":form_crossed a    lemon:Form ;\n" +
"  lemon:writtenRep \""+verbForms[2]+"\"@en ;\n" +
"  lexinfo:tense    lexinfo:past .\n" +
"\n" +
"\n" +
":cross_frame_transitive a lexinfo:TransitiveFrame ;\n" +
"  lexinfo:subject         :cross_subj ;\n" +
"  lexinfo:directObject    :cross_obj .\n" +
"\n" +
":cross_ontomap a    lemon:OntoMap, lemon:LexicalSense ;\n" +
"  lemon:ontoMapping :cross_ontomap ;\n" +
"  lemon:reference   <http://dbpedia.org/ontology/"+reference+"> ;\n" +
"  lemon:subjOfProp  :cross_subj ;\n" +
"  lemon:objOfProp   :cross_obj ;\n" +
"  lemon:condition   :cross_condition .\n" +
"\n" +
":cross_condition a     lemon:condition ;\n" +
"  lemon:propertyDomain <http://dbpedia.org/ontology/"+domain+"> ;\n" +
"  lemon:propertyRange  <http://dbpedia.org/ontology/"+range+"> .\n" +
"\n" +
"\n" +
"# see https://lemon-model.net/lemon-cookbook/node17.html\n" +
"\n" +
":crossed a             lemon:LexicalEntry ;\n" +
"  lexinfo:partOfSpeech lexinfo:adjective ;\n" +
"  lemon:canonicalForm  :form_crossed_canonical ;\n" +
"  lemon:otherForm      :form_crossed_by ;\n" +
"  lemon:synBehavior    :crossed_frame_adjectivepp ;\n" +
"  lemon:sense          :crossed_ontomap .\n" +
"\n" +
":form_crossed_canonical a lemon:Form ;\n" +
"  lemon:writtenRep        \""+verbForms[2]+"\"@en .\n" +
"\n" +
":form_crossed_by a     lemon:Form ;\n" +
"  lemon:writtenRep     \""+verbForms[2]+"\"@en ;\n" +
"  lexinfo:verbFormMood lexinfo:participle .\n" +
"\n" +
"\n" +
":crossed_frame_adjectivepp a   lexinfo:AdjectivePPFrame ;\n" +
"  lexinfo:copulativeSubject    :crossed_subj ;\n" +
"  lexinfo:prepositionalAdjunct :crossed_obj .\n" +
"\n" +
":crossed_ontomap a  lemon:OntoMap, lemon:LexicalSense ;\n" +
"  lemon:ontoMapping :crossed_ontomap ;\n" +
"  lemon:reference   <http://dbpedia.org/ontology/"+reference+"> ;\n" +
"  lemon:subjOfProp  :crossed_obj ;\n" +
"  lemon:objOfProp   :crossed_subj ;\n" +
"  lemon:condition   :cross_condition .\n" +
"\n" +
":crossed_obj lemon:marker :by .\n" +
"\n" +
"## Prepositions ##\n" +
"\n" +
":by a                  lemon:SynRoleMarker ;\n" +
"  lemon:canonicalForm  [ lemon:writtenRep \""+preposition+"\"@en ] ;\n" +
"  lexinfo:partOfSpeech lexinfo:preposition .\n" +
"\n" +
"";
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
