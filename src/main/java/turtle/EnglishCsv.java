/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.NounPPFrame;
import static grammar.datasets.sentencetemplates.TempConstants.past;
import static grammar.datasets.sentencetemplates.TempConstants.perfect;
import static grammar.datasets.sentencetemplates.TempConstants.present;
import static grammar.datasets.sentencetemplates.TempConstants.present3rd;
import static java.lang.System.exit;
import java.util.List;
import java.util.Map;
import util.io.GenderUtils;
import util.io.Tupples;

/**
 *
 * @author elahi
 */
public class EnglishCsv implements TempConstants {

    public static class NounPPFrameCsv {

        public static Integer lemonEntryIndex = 0;
        public static Integer partOfSpeechIndex = 1;
        public static Integer writtenFormInfinitive = 2;
        public static Integer writtenFormPluralIndex = 3;
        public static Integer prepositionIndex = 4;
        public static Integer syntacticFrameIndex = 5;
        public static Integer copulativeArgIndex = 6;
        public static Integer prepositionalAdjunctIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;

        public NounPPFrameCsv() {

        }

        public static String getNounPPFrameHeader(String lemonEntry, String preposition, String language) {
            return "@prefix :        <http://localhost:8080/lexicon#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + lemonEntry+"_form_preposition" + " .\n"
                    + "\n";
        }

        public String getIndexing(String lemonEntry, List<Tupples> senseIds) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a       lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                    + "  lemon:canonicalForm  :" + lemonEntry + "_form ;\n"
                    + "  lemon:otherForm  :" + lemonEntry + "_singular ;\n"
                    + "  lemon:otherForm  :" + lemonEntry + "_plural ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_nounpp .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getWrittenTtl(String lemonEntry, String writtenFormInfinitive, String writtenFormSingular,String writtenFormPlural,String language) {
            /*return ":" + lemonEntry + "_form a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_nounpp a        lexinfo:NounPPFrame ;\n"
                    + "  lexinfo:copulativeArg        :arg1 ;\n"
                    + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                    + "\n";*/
            if(writtenFormPlural.contains("-"))
                writtenFormPlural="XX";
            
            return ":" + lemonEntry + "_form a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_singular a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormSingular + "\"@" + language + " ;\n"
                    + "  lexinfo:number   lexinfo:singular .\n"
                    + "\n"
                    + ":" + lemonEntry + "_plural a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPlural + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:plural .\n"
                    + "\n"
                    + ":" + lemonEntry + "_nounpp a        lexinfo:NounPPFrame ;\n"
                    + "  lexinfo:copulativeArg        :arg1 ;\n"
                    + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                    + "\n";
        }

        public static String getPreposition(String lemonEntry,String preposition, String language) {
            return getPrepostionL(lemonEntry,preposition, language);
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getWrittenFormPluralIndex(String[] row) {
            return row[writtenFormPluralIndex];
        }

        public String getPrepositionIndex(String[] row) {
            return row[prepositionIndex];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }
        
         public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getCopulativeArgIndex(String[] row) {
            return row[copulativeArgIndex];
        }

        public String getPrepositionalAdjunctIndex(String[] row) {
            return row[prepositionalAdjunctIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public static String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";
            if (syntacticFrame.equals(NounPPFrame)) {
                for (Tupples tupple : tupples) {
                    String line = ":" + tupple.getSenseId() + " a lemon:OntoMap, lemon:LexicalSense ;\n"
                            + "  lemon:ontoMapping         :" + tupple.getSenseId() + " ;\n"
                            + "  lemon:reference           <" + tupple.getReference() + "> ;\n"
                            + "  lemon:subjOfProp          :arg2 ;\n"
                            + "  lemon:objOfProp           :arg1 ;\n"
                            + "  lemon:condition           :" + tupple.getSenseId() + "_condition .\n"
                            + "\n"
                            + ":" + tupple.getSenseId() + "_condition a lemon:condition ;\n"
                            + "  lemon:propertyDomain  <" + tupple.getDomain() + "> ;\n"
                            + "  lemon:propertyRange   <" + tupple.getRange() + "> .\n"
                            + "\n";
                    str += line;
                }
            }

            return str;
        }

    }

    public static class TransitFrameCsv {

        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	SyntacticFrame	subject	directObject	sense	reference	domain	range	GrammarRule 1:question1	GrammarRule 1:question2	GrammarRule 1:sparql	GrammarRule 2:question1	GrammarRule2: sparql
        //compose	        verb	compose	composes	composed	TransitiveFrame	range	domain	1	dbo:musicComposer	dbo:Work	dbo:Person	Which dbo:Person (X) composes,composed Y(dbo:Work)?	Who dbo:Person (X) composes,composed Y(dbo:Work)?	SELECT ?X WHERE { Y dbo:musicComposer ?X.}	Which dbo:Work(X) was composed by Y(dbo:Person)?	SELECT ?X WHERE { ?X dbo:Person Y.}
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormInfinitive = 2;
        private Integer writtenForm3rdPerson = 3;
        private Integer writtenFormPast = 4;
        private Integer syntacticFrameIndex = 5;
        private Integer subjectIndex = 6;
        private Integer directObjectIndex = 7;
        private Integer senseIndex = 8;
        private Integer referenceIndex = 9;
        private Integer domainIndex = 10;
        private Integer rangeIndex = 11;
        private Integer passivePrepositionIndex=12;
        private Integer domainWrittenSingularFormIndex=passivePrepositionIndex+1;
        private Integer domainWrittenPluralFormIndex=domainWrittenSingularFormIndex+1;
        private Integer rangeWrittenSingularFormIndex=domainWrittenPluralFormIndex+1;
        private Integer rangeWrittenPluralFormIndex=rangeWrittenSingularFormIndex+1;


        public String getHeader(String lemonEntry, String preposition, String language) {
            return "@prefix :        <http://localhost:8080/lexicon#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + "to_" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + lemonEntry + "ed" + " ;\n"
                    + "  lemon:entry    :" + preposition + " .\n"
                    + "\n";
        }

        public String getSenseIndexing(List<Tupples> tupples, String lemonEntry) {
            String senseIdStr = getSenseId(tupples);
            senseIdStr = ":" + "to_" + lemonEntry + " a           lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                    + "  lemon:canonicalForm  :form_" + lemonEntry + " ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "s ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "ed ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_frame_transitive .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getWritten(String lemonEntry, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String language) {
            System.out.println("----------------" + writtenFormPast);

            return ":form_" + lemonEntry + " a         lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "s a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:person   lexinfo:secondPerson .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "ed a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:past .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame_transitive a lexinfo:TransitiveFrame ;\n"
                    + "  lexinfo:subject          :" + lemonEntry + "_subj ;\n"
                    + "  lexinfo:directObject     :" + lemonEntry + "_obj .\n"
                    + "\n";
        }

        public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";
            str = "";
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a   lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping :" + tupple.getSenseId() + " ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + lemonEntry + "_obj ;\n"
                        + "  lemon:objOfProp   :" + lemonEntry + "_subj ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .\n"
                        + "\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_condition a    lemon:condition ;\n"
                        + "  lemon:propertyDomain <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange  <" + tupple.getRange() + "> .\n"
                        + "\n";
                str += line;
            }
            String intransitiveStr = "";
            lemonEntry = lemonEntry + "ed";
            for (Tupples tupple : tupples) {
                String line = ":" + lemonEntry + " a            lemon:LexicalEntry ;\n"
                        + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                        + "  lemon:canonicalForm  :form_" + lemonEntry + "_canonical ;\n"
                        + "  lemon:otherForm      :form_" + lemonEntry + "_by ;\n"
                        + "  lemon:synBehavior    :" + lemonEntry + "_frame_adjectivepp ;\n"
                        + "  lemon:sense          :" + lemonEntry + "_ontomap .\n"
                        + "\n"
                        + ":form_" + lemonEntry + "_canonical a lemon:Form ;\n"
                        + "  lemon:writtenRep         \"" + pastTense + "\"@" + language + " .\n"
                        + "\n"
                        + ":form_" + lemonEntry + "_by a    lemon:Form ;\n"
                        + "  lemon:writtenRep     \"" + pastTense + "\"@" + language + " ;\n"
                        + "  lexinfo:verbFormMood lexinfo:participle .\n"
                        + "\n"
                        + "\n"
                        + ":" + lemonEntry + "_frame_adjectivepp a  lexinfo:AdjectivePPFrame ;\n"
                        + "  lexinfo:copulativeSubject    :" + lemonEntry + "_subj ;\n"
                        + "  lexinfo:prepositionalAdjunct :" + lemonEntry + "_obj .\n"
                        + "\n"
                        + ":" + lemonEntry + "_ontomap" + " a lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping :" + lemonEntry + "_ontomap" + " ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + lemonEntry + "_subj ;\n"
                        + "  lemon:objOfProp   :" + lemonEntry + "_obj ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .";
                intransitiveStr += line;
            }
            String prep = "\n"
                    + ":" + lemonEntry + "_obj lemon:marker :" + preposition + " .\n"
                    + "\n";
            str = str + intransitiveStr + prep;

            return str;
        }
        
        public  void setArticle(Tupples tupple, String[] row) {
           GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
           GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }

        public String getPrepostion(String lemonEntry,String preposition, String language) {
            return getPrepostionL(lemonEntry,preposition, language);
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getWrittenForm3rdPerson(String[] row) {
            return row[writtenForm3rdPerson];
        }

        public String getWrittenFormPast(String[] row) {
            return row[writtenFormPast];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }
        
         public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getSubjectIndex(String[] row) {
            return row[subjectIndex];
        }

        public String getDirectObjectIndex(String[] row) {
            return row[directObjectIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public String getPassivePrepositionIndex(String[] row) {
            return row[passivePrepositionIndex];
        }

        public Integer  getDomainWrittenSingular() {
            return domainWrittenSingularFormIndex;
        }

        public Integer getDomainWrittenPlural() {
            return domainWrittenPluralFormIndex;
        }

        public Integer getRangeWrittenSingular() {
            return rangeWrittenSingularFormIndex;
        }

        public Integer getRangeWrittenPlural() {
            return rangeWrittenPluralFormIndex;
        }

    }

    public static class InTransitFrame {

        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	preposition	SyntacticFrame	subject	prepositionalAdjunct	sense	reference	domain	range	GrammarRule 1 :question 1	GrammarRule 1 :question 2	GrammarRule 1 :sparql	Grammar rule 2: question1	Grammar rule 2: question2	sparql2
        //flow_through	verb	flow	flows	flowed	through	IntransitivePPFrame	domain	range	1	dbo:country	dbo:River	dbo:Country	What dbo:River(X) flows through Y(dbo:Country)?	FALSE	SELECT ?X WHERE { ?X dbo:country Y.}	Which dbo:Country(X) does Y(dbo:River) flow through?	FALSE	SELECT ?X WHERE { Y dbo:country ?X.}
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormInfinitive = 2;
        private Integer writtenForm3rdPerson = 3;
        private Integer writtenFormPast = 4;
        private Integer preposition = 5;
        private Integer syntacticFrameIndex = 6;
        private Integer subject = 7;
        private Integer prepositionalAdjunct = 8;
        private Integer senseIndex = 9;
        private Integer referenceIndex = 10;
        private Integer domainIndex = 11;
        private Integer rangeIndex = 12;
        private Integer domainWrittenSingularFormIndex=rangeIndex+1;
        private Integer domainWrittenPluralFormIndex=domainWrittenSingularFormIndex+1;
        private Integer rangeWrittenSingularFormIndex=domainWrittenPluralFormIndex+1;
        private Integer rangeWrittenPluralFormIndex=rangeWrittenSingularFormIndex+1;

        public String getHeader(String lemonEntry, String proposition, String language) {
            return "@prefix :        <http://localhost:8080/lexicon#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + proposition + " .\n"
                    + "\n";
        }

        public String getSenseIndexing(List<Tupples> senseIds, String lemonEntry) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                    + "  lemon:canonicalForm  :form_" + lemonEntry + " ;\n"
                    + "  lemon:otherForm      :form_" + lemonEntry + "_past ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_frame .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getWritten(String lemonEntry, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String language) {
            return ":form_" + lemonEntry + " a           lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + "\n"
                    + ":form_" + lemonEntry + " a      lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense    lexinfo:present .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "_past a lemon:Form ;\n"
                    + "  lemon:writtenRep  \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:singular ;\n"
                    + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense     lexinfo:past .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame a  lexinfo:IntransitivePPFrame ;\n"
                    + "  lexinfo:subject              :" + lemonEntry + "_subj ;\n"
                    + "  lexinfo:prepositionalAdjunct :" + lemonEntry + "_obj .\n"
                    + "\n";
        }

        public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";

            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a     lemon:OntoMap, lemon:LexicalSense ;\n"
                        + "  lemon:ontoMapping :" + lemonEntry + "_ontomap ;\n"
                        + "  lemon:reference   <" + tupple.getReference() + "> ;\n"
                        + "  lemon:subjOfProp  :" + lemonEntry + "_obj ;\n"
                        + "  lemon:objOfProp   :" + lemonEntry + "_subj ;\n"
                        + "  lemon:condition   :" + tupple.getSenseId() + "_condition .\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_condition a      lemon:condition ;\n"
                        + "  lemon:propertyDomain <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange  <" + tupple.getRange() + "> .\n"
                        + "\n";
                str += line;
            }
            String prep = "\n"
                    + ":" + lemonEntry + "_obj lemon:marker :" + preposition + " .\n"
                    + "\n";
            str = str + prep;

            return str;
        }
        
        public  void setArticle(Tupples tupple, String[] row) {
           GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
           GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }
        
        public  void setVerbInfo(String partOfSpeech, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast) {
             Map<String, String> verbTypes = Map.of(
                        infinitive, writtenFormInfinitive,
                        present3rd, writtenForm3rdPerson,
                        past, writtenFormPast
                );
             
             String [] verbs=new String[]{writtenFormInfinitive,writtenForm3rdPerson,writtenFormPast};
             
              
            GenderUtils.setVerbTypes(partOfSpeech, verbs,verbTypes);  
            
           
            
            /*Map<String, String> verb = Map.of(
                        present, writtenFromIn,
                        past, writtenFormPast,
                        perfect, writtenFormPerfect
                );
              
              
              
            if (writtenFormPast.contains(" ")) {
                GenderUtils.setTrennVerbType(writtenFromIn, verb);
                GenderUtils.setTrennVerbType(writtenForm3rd, verb);
                GenderUtils.setTrennVerbType(writtenFormPast, verb);
                GenderUtils.setTrennVerbType(writtenFormPerfect, verb);
            }
            
            GenderUtils.setPerfectVerbType(writtenFromIn, verb);
            GenderUtils.setPerfectVerbType(writtenForm3rd, verb);
            GenderUtils.setPerfectVerbType(writtenFormPast, verb);
            GenderUtils.setPerfectVerbType(writtenFormPerfect, verb);*/
            
        }

        public String getPrepostion(String lemonEntry,String preposition, String language) {
            return getPrepostionL(lemonEntry,preposition, language);
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getWrittenForm3rdPerson(String[] row) {
            return row[writtenForm3rdPerson];
        }

        public String getWrittenFormPast(String[] row) {
            return row[writtenFormPast];
        }

        public String getPreposition(String[] row) {
            return row[preposition];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }
        
         public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getSubject(String[] row) {
            return row[subject];
        }

        public String getPrepositionalAdjunct(String[] row) {
            return row[prepositionalAdjunct];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }
        
        public Integer  getDomainWrittenSingular() {
            return domainWrittenSingularFormIndex;
        }

        public Integer getDomainWrittenPlural() {
            return domainWrittenPluralFormIndex;
        }

        public Integer getRangeWrittenSingular() {
            return rangeWrittenSingularFormIndex;
        }

        public Integer getRangeWrittenPlural() {
            return rangeWrittenPluralFormIndex;
        }

    }

    public static class AttributiveAdjectiveFrame {

        //LemonEntry	partOfSpeech	writtenForm	SyntacticFrame	copulativeSubject	
        //attributiveArg	sense	reference	owl:onProperty	owl:hasValue	
        //domain	range	question (attributive use)							
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormInfinitive = 2;
        private Integer syntacticFrameIndex = 3;
        private Integer copulativeSubjectIndex = 4;
        private Integer attributiveArgIndex = 5;
        private Integer senseIndex = 6;
        private Integer referenceIndex = 7;
        private Integer owl_onPropertyIndex = 8;
        private Integer owl_hasValueIndex = 9;
        private Integer domainIndex = 10;
        private Integer rangeIndex = 11;
        private Integer classIndex = 12;
        private Integer originalIndex = 13;
        private Integer size = originalIndex + 1;

        public String getAtrributiveFrameHeader(String lemonEntry, List<Tupples> senseIds, String language) {
            String senseIdStr = getSenseIdRes(senseIds);

            return "@prefix :        <http://localhost:8080/#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    //+ "  lemon:entry    :" + lemonEntry + "_res ;\n"
                    + senseIdStr
                    + "  lemon:entry    :" + lemonEntry + " .\n"
                    + "\n";
        }

        public String getAtrributiveFrameIndexing(List<Tupples> senseIds, String lemonEntry) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                    + "  lemon:canonicalForm  :" + lemonEntry + "_lemma ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_attrFrame, :" + lemonEntry + "_predFrame .\n"
                    + "\n";
            return senseIdStr;
        }

        public String getAtrrtibutiveWrittenForm(String lemonEntry, String writtenFormInfinitive, String language) {
            return ":" + lemonEntry + "_lemma lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
                    + "  lexinfo:copulativeSubject :" + lemonEntry + "_PredSynArg .\n"
                    + "\n"
                    + ":" + lemonEntry + "_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
                    + "  lexinfo:attributiveArg :" + lemonEntry + "_AttrSynArg .\n"
                    + "\n";

        }

        public String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
            String str = "";
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a  lemon:LexicalSense ;\n"
                        + "  lemon:reference :" + tupple.getSenseId() + "_res ;\n"
                        + "  lemon:isA       :" + lemonEntry + "_AttrSynArg, :" + lemonEntry + "_PredSynArg .\n"
                        + "\n"
                        + ":" + tupple.getSenseId() + "_res a   owl:Restriction ;\n"
                        + "  owl:onProperty <" + tupple.getDomain() + "> ;\n"
                        + "  owl:hasValue   <" + tupple.getRange() + "> .\n";
                str += line;
            }

            return str;
        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormInfinitive(String[] row) {
            return row[writtenFormInfinitive];
        }

        public String getSyntacticFrameIndex(String[] row) {
            return row[syntacticFrameIndex];
        }
        
         public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getCopulativeSubjectIndex(String[] row) {
            return row[copulativeSubjectIndex];
        }

        public String getAttributiveArgIndex(String[] row) {
            return row[attributiveArgIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getOwl_onPropertyIndex(String[] row) {
            return row[owl_onPropertyIndex];
        }

        public String getOwl_hasValueIndex(String[] row) {
            return row[owl_hasValueIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public String getClassIndex(String[] row) {
            return row[classIndex];
        }

        public String getOriginalIndex(String[] row) {
            return row[originalIndex];
        }

        public Integer getSize() {
            return size;
        }

    }

    public static String getSenseId(List<Tupples> senseIds) {
        String str = "";
        for (Tupples tupple : senseIds) {
            String line = "  lemon:sense          :" + tupple.getSenseId() + " ;\n";
            str += line;
        }
        return str;
    }

    public static String getSenseIdRes(List<Tupples> senseIds) {
        String str = "";
        for (Tupples tupple : senseIds) {
            String line = "   lemon:entry          :" + tupple.getSenseId() + "_res ;\n";
            str += line;
        }
        return str;
    }

    public static String getPrepostionL(String lemonEntry,String preposition, String language) {
        return "## Prepositions ##\n"
                + ":" + lemonEntry+"_form_preposition" + " a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "\n"
                + "";
    }

}
