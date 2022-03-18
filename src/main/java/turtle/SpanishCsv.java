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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static turtle.EnglishCsv.getSenseId;
import static turtle.GermanCsv.NounPPFrameCsv.rangeArticleIndex;
import util.io.GenderUtils;
import util.io.Tupples;

/**
 *
 * @author elahi
 */
public class SpanishCsv {

    public static Integer lemonEntryIndex = 0;
    public static Integer partOfSpeechIndex = 1;
    public static Integer writtenFormInfinitive = 2;
    public static Integer AdjectiveFrameIndex = 3;

    public static class NounPPFrameCsv {

        public static Integer lemonEntryIndex = 0;
        public static Integer partOfSpeechIndex = 1;
        public static Integer articleIndex = 2;
        public static Integer writtenFormInfinitive = 3;
        public static Integer writtenFormPluralIndex = 4;
        public static Integer prepositionIndex = 5;
        public static Integer syntacticFrameIndex = 6;
        public static Integer copulativeArgIndex = 7;
        public static Integer prepositionalAdjunctIndex = 8;
        public static Integer senseIndex = 9;
        public static Integer referenceIndex = 10;
        public static Integer domainIndex = 11;
        public static Integer rangeIndex = 12;
        public static Integer domainWrittenSingular = 13;
        public static Integer domainWrittenPlural = 14;
        public static Integer rangeWrittenSingular = 15;
        public static Integer rangeWrittenPlural = 16;
        private static String proeposition_id;

        public NounPPFrameCsv() {

        }

        public String getNounPPFrameHeader(String lemonEntry, String preposition, String language) {
            proeposition_id = lemonEntry + "_form_preposition";
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
                    + "  lemon:entry    :" + proeposition_id + " .\n"
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

        public String getWrittenTtl(String lemonEntry, String writtenFormInfinitive, String writtenFormSingular, String writtenFormPlural, String language, String copulativeArg) {
            /*return ":" + lemonEntry + "_form a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_nounpp a        lexinfo:NounPPFrame ;\n"
                    + "  lexinfo:copulativeArg        :arg1 ;\n"
                    + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                    + "\n";*/
            if (writtenFormPlural.contains("-")) {
                writtenFormPlural = "XX";
            }

            String arg1 = null, arg2 = null;

            if (copulativeArg.contains("domain")) {
                arg1 = "arg2";
                arg2 = "arg1";
            } else {
                arg1 = "arg1";
                arg2 = "arg2";
            }

            String writtenForm = ":" + lemonEntry + "_form a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_singular a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormSingular + "\"@" + language + " ;\n"
                    + "  lexinfo:number   lexinfo:singular .\n"
                    + "\n"
                    + ":" + lemonEntry + "_plural a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPlural + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:plural .\n"
                    + "\n";

            String copulativeStr = ":" + lemonEntry + "_nounpp" + " a        lexinfo:NounPPFrame ;\n"
                    + "  lexinfo:copulativeArg        :" + arg1 + " ;\n"
                    + "  lexinfo:prepositionalAdjunct :" + arg2 + " .\n"
                    + "\n";

            return writtenForm + copulativeStr;
        }

        public void setArticle(Tupples tupple, String[] row) {
            GenderUtils.setWrittenForms(tupple.getDomain(), getDomainWrittenSingular(row), getDomainWrittenPlural(row));
            GenderUtils.setWrittenForms(tupple.getRange(), getRangeWrittenSingular(row), getRangeWrittenPlural(row));
        }

        public static String getPreposition(String lemonEntry, String preposition, String language) {
            if (preposition.contains("X")) {
                preposition = "";
            }
            return ":arg2 lemon:marker :" + proeposition_id + " .\n"
                    + "## Prepositions ##\n"
                    + ":" + proeposition_id + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                    + "\n"
                    + "";
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

        public String getDomainWrittenSingular(String[] row) {
            return row[domainWrittenSingular];
        }

        public String getDomainWrittenPlural(String[] row) {
            return row[domainWrittenPlural];
        }

        public String getRangeArticleIndex(String[] row) {
            return row[rangeArticleIndex];
        }

        public String getRangeWrittenSingular(String[] row) {
            return row[rangeWrittenSingular];
        }

        public String getRangeWrittenPlural(String[] row) {
            return row[rangeWrittenPlural];
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

        public static Integer writtenForm3rdPerson = 3;
        public static Integer writtenFormPast = 4;
        public static Integer writtenFormPerfect = 5;
        public static Integer SyntacticFrame = 6;
        public static Integer subjectIndex = 7;
        public static Integer directObjectIndex = 8;
        public static Integer senseIndex = 9;
        public static Integer referenceIndex = 10;
        public static Integer domainIndex = 11;
        public static Integer rangeIndex = 12;
        public static Integer domainArticleIndex = 13;
        public static Integer domainWrittenSingular = 14;
        public static Integer domainWrittenPlural = 15;
        public static Integer rangeArticleIndex = 16;
        public static Integer rangeWrittenSingular = 17;
        public static Integer rangeWrittenPlural = 18;
        public static Integer passivePrepositionIndex = 19;

        public static String getHeader(String lemonEntry, String prepositionAttr, String preposition, String language) {
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
                    + "  lemon:entry    :" + "form_" + lemonEntry + "_preposition" + " .\n"
                    + "\n";
        }

        public static String getSenseIndexing(List<Tupples> tupples, String lemonEntry) {
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

        public static String getWritten(String lemonEntry, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect, String language, String subject) {
            String subjectLemon = null, objectLemon = null;

            if (subject.contains("domain")) {
                objectLemon = lemonEntry + "_subj";
                subjectLemon = lemonEntry + "_obj";
            } else {
                subjectLemon = lemonEntry + "_subj";
                objectLemon = lemonEntry + "_obj";
            }

            return ":" + "form_" + lemonEntry + " a         lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "s a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:past ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson .\n"
                    + "\n"
                    + "\n"
                    + ":form_" + lemonEntry + "s a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:past ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "ed a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPerfect + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:perfect ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame_transitive a lexinfo:TransitiveFrame ;\n"
                    + "  lexinfo:subject          :" + subjectLemon + " ;\n"
                    + "  lexinfo:directObject     :" + objectLemon + " .\n"
                    + "\n";
        }

        public void setArticle(Tupples tupple, String gender, String[] row) {
            GenderUtils.setArticles(tupple.getReference(), gender);
            GenderUtils.setArticles(tupple.getDomain(), row[getDomainArticleIndex()]);
            GenderUtils.setArticles(tupple.getRange(), row[getRangeArticleIndex()]);
            GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
            GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }

        public void setVerbInfo(String partOfSpeech, String writtenFromIn, String writtenForm3rd, String writtenFormPast, String writtenFormPerfect) {
            Map<String, String> verbTypes = Map.of(
                    present, writtenFromIn,
                    present3rd, writtenForm3rd,
                    past, writtenFormPast,
                    perfect, writtenFormPerfect
            );

            String[] verbs = new String[]{writtenFromIn, writtenForm3rd, writtenFormPast, writtenFormPerfect};

            GenderUtils.setVerbTypes(partOfSpeech, verbs, verbTypes);

        }

        public String getPreposition(String lemonEntry, String preposition, String language) {
            return ":arg2 lemon:marker :" + "form_" + lemonEntry + "_form_preposition" + " .\n"
                    + "\n"
                    + "## Prepositions ##\n"
                    + "\n"
                    + ":" + "form_" + lemonEntry + "_preposition" + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + "en" + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .";
        }

        public Integer getWrittenForm3rdPerson(String[] row) {
            return writtenForm3rdPerson;
        }

        public Integer getWrittenFormPast(String[] row) {
            return writtenFormPast;
        }

        public Integer getSyntacticFrame(String[] row) {
            return SyntacticFrame;
        }

        public String getSubjectIndex(String[] row) {
            return row[subjectIndex];
        }

        public String getDirectObjectIndex(String[] row) {
            return row[directObjectIndex];
        }

        public Integer getSenseIndex() {
            return senseIndex;
        }

        public Integer getReferenceIndex() {
            return referenceIndex;
        }

        public Integer getDomainIndex() {
            return domainIndex;
        }

        public Integer getRangeIndex() {
            return rangeIndex;
        }

        public Integer getPassivePrepositionIndex() {
            return passivePrepositionIndex;
        }

        public Integer getDomainArticleIndex() {
            return domainArticleIndex;
        }

        public Integer getDomainWrittenSingular() {
            return domainWrittenSingular;
        }

        public Integer getDomainWrittenPlural() {
            return domainWrittenPlural;
        }

        public Integer getRangeArticleIndex() {
            return rangeArticleIndex;
        }

        public Integer getRangeWrittenSingular() {
            return rangeWrittenSingular;
        }

        public Integer getRangeWrittenPlural() {
            return rangeWrittenPlural;
        }

        public Integer getSyntacticFrameIndex() {
            return SyntacticFrame;
        }

    }

    public static class InTransitFrameCsv {

        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	preposition	SyntacticFrame	subject	prepositionalAdjunct	sense	reference	domain	range	GrammarRule 1 :question 1	GrammarRule 1 :question 2	GrammarRule 1 :sparql	Grammar rule 2: question1	Grammar rule 2: question2	sparql2
        //flow_through	verb	flow	flows	flowed	through	IntransitivePPFrame	domain	range	1	dbo:country	dbo:River	dbo:Country	What dbo:River(X) flows through Y(dbo:Country)?	FALSE	SELECT ?X WHERE { ?X dbo:country Y.}	Which dbo:Country(X) does Y(dbo:River) flow through?	FALSE	SELECT ?X WHERE { Y dbo:country ?X.}
        public static Integer writtenForm3rdPerson = 3;
        public static Integer writtenFormPast = 4;
        public static Integer writtenFormPerfect = 5;
        public static Integer preposition = 6;
        public static Integer SyntacticFrame = 7;
        public static Integer subjectIndex = 8;
        public static Integer prepositionalAdjunct = 9;
        public static Integer senseIndex = 10;
        public static Integer referenceIndex = 11;
        public static Integer domainIndex = 12;
        public static Integer rangeIndex = 13;
        public static Integer domainArticleIndex = 14;
        public static Integer domainWrittenSingular = 15;
        public static Integer domainWrittenPlural = 16;
        public static Integer rangeArticleIndex = 17;
        public static Integer rangeWrittenSingular = 18;
        public static Integer rangeWrittenPlural = 19;

        public static String getHeader(String lemonEntry, String proposition, String language) {
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
                    + "  lemon:entry    :" + "form_" + lemonEntry + "_present" + "_preposition" + " .\n"
                    + "\n";
        }

        public static String getSenseIndexing(List<Tupples> senseIds, String lemonEntry) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                    + "  lemon:canonicalForm  :" + "form_" + lemonEntry + "_" + present + " ;\n"
                    + "  lemon:otherForm      :" + lemonEntry + "_" + present3rd + " ;\n"
                    + "  lemon:otherForm      :" + lemonEntry + "_" + past + " ;\n"
                    + "  lemon:otherForm      :" + lemonEntry + "_" + perfect + " ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_frame .\n"
                    + "\n";
            return senseIdStr;
        }

        public static String getWritten(String lemonEntry, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect, String language, String subject) {
            String subjectLemon = null, objectLemon = null;
            //subjectLemon = lemonEntry + "_subj";
            //objectLemon = lemonEntry + "_obj";
            //System.out.println("subject:"+subject);

            if (subject.contains("domain")) {
                subjectLemon = lemonEntry + "_subj";
                objectLemon = lemonEntry + "_obj";
            } else {
                objectLemon = lemonEntry + "_subj";
                subjectLemon = lemonEntry + "_obj";
            }

            /*if (subject.contains("range")) {
                subjectLemon = lemonEntry + "_subj";
                objectLemon = lemonEntry + "_obj";
            } else {
                objectLemon = lemonEntry + "_subj";
                subjectLemon = lemonEntry + "_obj";
            }*/
            return ":" + "form_" + lemonEntry + "_" + present + " a           lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + "\n"
                    + ":" + lemonEntry + "_" + present3rd + " a      lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense    lexinfo:present .\n"
                    + "\n"
                    + ":" + lemonEntry + "_" + past + " a lemon:Form ;\n"
                    + "  lemon:writtenRep  \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:singular ;\n"
                    + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense     lexinfo:past .\n"
                    + "\n"
                    + ":" + lemonEntry + "_" + perfect + " a lemon:Form ;\n"
                    + "  lemon:writtenRep  \"" + writtenFormPerfect + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:singular ;\n"
                    + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense     lexinfo:perfect .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame a  lexinfo:IntransitivePPFrame ;\n"
                    + "  lexinfo:subject              :" + subjectLemon + " ;\n"
                    + "  lexinfo:prepositionalAdjunct :" + objectLemon + " .\n"
                    + "\n";
        }

        public static void setNoun(Tupples tupple, String gender, String[] row) {
            GenderUtils.setArticles(tupple.getReference(), gender);
            GenderUtils.setArticles(tupple.getDomain(), row[getDomainArticleIndex()]);
            GenderUtils.setArticles(tupple.getRange(), row[getRangeArticleIndex()]);
            GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
            GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }

        public static void setVerbInfo(String partOfSpeech, String writtenFromIn, String writtenForm3rd, String writtenFormPast, String writtenFormPerfect) {
            Map<String, String> verbTypes = Map.of(
                    present, writtenFromIn,
                    present3rd, writtenForm3rd,
                    past, writtenFormPast,
                    perfect, writtenFormPerfect
            );

            String[] verbs = new String[]{writtenFromIn, writtenForm3rd, writtenFormPast, writtenFormPerfect};

            GenderUtils.setVerbTypes(partOfSpeech, verbs, verbTypes);

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

        static String getSubjectIndex(String[] row) {
            return row[subjectIndex];
        }

        public String getPreposition(String lemonEntry, String preposition, String language) {
            if (preposition.contains("X")) {
                preposition = "";
            }
            return ":arg2 lemon:marker :" + "form_" + lemonEntry + "_present_preposition" + " .\n"
                    + "\n"
                    + "## Prepositions ##\n"
                    + "\n"
                    + ":" + "form_" + lemonEntry + "_present_preposition" + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + "en" + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .";
        }

        public static Integer getWrittenForm3rdPerson() {
            return writtenForm3rdPerson;
        }

        public static Integer getWrittenFormPast() {
            return writtenFormPast;
        }

        public static Integer getPreposition() {
            return preposition;
        }

        public static Integer getSyntacticFrame() {
            return SyntacticFrame;
        }

        public static Integer getSubject() {
            return subjectIndex;
        }

        public static Integer getPrepositionalAdjunct() {
            return prepositionalAdjunct;
        }

        public static Integer getSenseIndex() {
            return senseIndex;
        }

        public static Integer getReferenceIndex() {
            return referenceIndex;
        }

        public static Integer getDomainIndex() {
            return domainIndex;
        }

        public static Integer getRangeIndex() {
            return rangeIndex;
        }

        public static Integer getDomainArticleIndex() {
            return domainArticleIndex;
        }

        public static Integer getRangeArticleIndex() {
            return rangeArticleIndex;
        }

        public static Integer getDomainWrittenSingular() {
            return domainWrittenSingular;
        }

        public static Integer getDomainWrittenPlural() {
            return domainWrittenPlural;
        }

        public static Integer getRangeWrittenSingular() {
            return rangeWrittenSingular;
        }

        public static Integer getRangeWrittenPlural() {
            return rangeWrittenPlural;
        }

        public Integer getSyntacticFrameIndex() {
            return SyntacticFrame;
        }

    }

    public static class AttributiveAdjectiveFrameCsv {
        //LemonEntry	partOfSpeech	writtenForm	SyntacticFrame	copulativeSubject	
        //attributiveArg	sense	reference	owl:onProperty	owl:hasValue	
        //domain	range	question (attributive use)							

        public static Integer SyntacticFrameIndex = 3;
        public static Integer copulativeSubjectIndex = 4;
        public static Integer attributiveArgIndex = 5;
        public static Integer senseIndex = 6;
        public static Integer referenceIndex = 7;
        public static Integer owl_onPropertyIndex = 8;
        public static Integer owl_hasValueIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;
        public static Integer classIndex = 12;
        public static Integer originalIndex = 13;
        public static Integer size = originalIndex + 1;

        public static String getAtrributiveFrameHeader(String lemonEntry, List<Tupples> senseIds, String language) {
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

        public static String getAtrributiveFrameIndexing(List<Tupples> senseIds, String lemonEntry) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                    + "  lemon:canonicalForm  :" + lemonEntry + "_lemma ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_attrFrame, :" + lemonEntry + "_predFrame .\n"
                    + "\n";
            return senseIdStr;
        }

        public static String getAtrrtibutiveWrittenForm(String lemonEntry, String writtenFormInfinitive, String language) {
            return ":" + lemonEntry + "_lemma lemon:writtenRep \"" + writtenFormInfinitive + "\"@" + language + " .\n"
                    + "\n"
                    + ":" + lemonEntry + "_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
                    + "  lexinfo:copulativeSubject :" + lemonEntry + "_PredSynArg .\n"
                    + "\n"
                    + ":" + lemonEntry + "_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
                    + "  lexinfo:attributiveArg :" + lemonEntry + "_AttrSynArg .\n"
                    + "\n";

        }

        public static Integer getSyntacticFrameIndex() {
            return SyntacticFrameIndex;
        }

        public static Integer getCopulativeSubjectIndex() {
            return copulativeSubjectIndex;
        }

        public static Integer getAttributiveArgIndex() {
            return attributiveArgIndex;
        }

        public static Integer getSenseIndex() {
            return senseIndex;
        }

        public static Integer getReferenceIndex() {
            return referenceIndex;
        }

        public static Integer getOwl_onPropertyIndex() {
            return owl_onPropertyIndex;
        }

        public static Integer getOwl_hasValueIndex() {
            return owl_hasValueIndex;
        }

        public static Integer getDomainIndex() {
            return domainIndex;
        }

        public static Integer getRangeIndex() {
            return rangeIndex;
        }

        public static Integer getClassIndex() {
            return classIndex;
        }

        public static Integer getOriginalIndex() {
            return originalIndex;
        }

        public static Integer getSize() {
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

    public static String getSenseDetail(List<Tupples> tupples, String syntacticFrame, String lemonEntry, String pastTense, String preposition, String language) {
        String str = "";
        if (syntacticFrame.equals(TempConstants.TransitiveFrame)) {
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
        } else if (syntacticFrame.equals(TempConstants.IntransitivePPFrame)) {
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

        } else if (syntacticFrame.equals(TempConstants.NounPPFrame)) {
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
        } else if (syntacticFrame.equals(TempConstants.AdjectiveAttributiveFrame)) {
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
        } else if (syntacticFrame.equals(TempConstants.AdjectivePPFrame)) {
            for (Tupples tupple : tupples) {
                String line = ":" + tupple.getSenseId() + " a  lemon:LexicalSense ;\n"
                        + "  lemon:reference :" + tupple.getSenseId() + "_res ;\n"
                        + "  lemon:isA       :" + lemonEntry + "_PredSynArg ;\n"
                        + "  lemon:condition :" + lemonEntry + "_condition .\n"
                        + "\n"
                        + ":" + lemonEntry + "_res a   oils:CovariantScalar ;\n"
                        + "  oils:boundTo  <" + tupple.getOils_boundTo() + "> ;\n"
                        + "  oils:degree   <" + tupple.getOils_degree() + "> .\n"
                        + "\n"
                        + ":" + lemonEntry + "_condition a lemon:condition ;\n"
                        + "  lemon:propertyDomain   <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange    <" + tupple.getRange() + "> .";
                str += line;
            }
        }

        return str;
    }

    public static String getPrepostion(String preopistionAttr, String preposition, String language) {
        return "## Prepositions ##\n"
                + ":" + preopistionAttr + " a                  lemon:SynRoleMarker ;\n"
                + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                + "\n"
                + "";
    }

    public static Integer getLemonEntryIndex() {
        return lemonEntryIndex;
    }

    public static Integer getPartOfSpeechIndex() {
        return partOfSpeechIndex;
    }

    public static Integer getWrittenFormInfinitive() {
        return writtenFormInfinitive;
    }

    public Integer getSyntacticFrameIndex() {
        return AdjectiveFrameIndex;
    }

    public static class GradbleAdjectiveFrameCsv {

        //LemonEntry	partOfSpeech	writtenForm	comparative	superlative	SyntacticFrame	
        //predFrame	sense	reference	oils:boundTo	oils:degree	domain	range
        private Integer lemonEntryIndex = 0;
        private Integer partOfSpeechIndex = 1;
        private Integer writtenFormIndex = 2;
        private Integer comparativIndex = 3;
        private Integer superlativeSingularIndex = 4;
        private Integer superlativePluralIndex = 5;
        private Integer syntacticFrameIndex = 6;
        private Integer predFrameIndex = 7;
        private Integer senseIndex = 8;
        private Integer referenceIndex = 9;
        private Integer oils_boundToIndex = 10;
        private Integer oils_degreeIndex = 11;
        private Integer domainIndex = 12;
        private Integer rangeIndex = 13;
        private Integer prepostionIndex = 14;
        public  Integer domainArticleIndex = 15;
        private Integer domainWrittenSingularFormIndex = 16;
        private Integer domainWrittenPluralFormIndex = 17;
        public  Integer rangeArticleIndex = 18;
        private Integer rangeWrittenSingularFormIndex = 19;
        private Integer rangeWrittenPluralFormIndex = 20;
              

        public String getHeader(String lemonEntry, String language) {
            return "@prefix :        <http://localhost:8080/#> .\n"
                    + "\n"
                    + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                    + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                    + "@prefix oils:     <http://lemon-model.net/oils#> .\n"
                    + "\n"
                    + "@base            <http://localhost:8080#> .\n"
                    + "\n"
                    + ":lexicon_en a    lemon:Lexicon ;\n"
                    + "  lemon:language \"" + language + "\" ;\n"
                    + "  lemon:entry    :" + lemonEntry + " ;\n"
                    + "  lemon:entry    :" + "form_" + lemonEntry + "_preposition" + " ;\n"
                    + "  lemon:entry    :" + lemonEntry + "_res .\n"
                    + "\n";
        }

        public String getIndexing(String lemonEntry, List<Tupples> senseIds) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr
                    = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                    + "  lemon:canonicalForm  :" + "form_" + lemonEntry + " ;\n"
                    + "  lemon:otherForm      :" + "form_" + lemonEntry + "_comperative" + " ;\n"
                    + "  lemon:otherForm      :" + "form_" + lemonEntry + "_superlative" + " ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_predFrame" + " .\n"
                    + "\n";

            return senseIdStr;
        }

        public String getWrittenTtl(String lemonEntry, String baseForm, String comparative, String superlative, String language) {
            String written
                    = ":" + "form_" + lemonEntry + " lemon:writtenRep \"" + baseForm + "\"@" + language + " .\n"
                    + "\n";
            String writtenFormComparative
                    = ":" + "form_" + lemonEntry + "_comperative" + " lemon:writtenRep \"" + comparative + "\"@" + language + " .\n"
                    + "\n";

            String writtenFormSuperlative
                    = ":" + "form_" + lemonEntry + "_superlative" + " lemon:writtenRep \"" + superlative + "\"@" + language + " .\n"
                    + "\n";

            String predFrame
                    = ":" + lemonEntry + "_predFrame" + " a        lexinfo:" + TempConstants.AdjectiveSuperlativeFrame + " ;\n"
                    + "  lexinfo:copulativeSubject :" + lemonEntry + "_PredSynArg .\n"
                    + "\n";

            return written + writtenFormComparative + writtenFormSuperlative + predFrame;
        }

        public String getSenseDetail(String lemonEntry, List<Tupples> tupples, String language) {
            String str = "";

            for (Tupples tupple : tupples) {
                String line
                        = ":" + tupple.getSenseId() + " a  lemon:LexicalSense ;\n"
                        + "  lemon:reference :" + lemonEntry + "_res" + " ;\n"
                        + "  lemon:isA       :" + lemonEntry + "_PredSynArg" + " ;\n"
                        + "  lemon:condition :" + tupple.getSenseId() + "_condition" + " .\n"
                        + "\n";
                String res
                        = ":" + lemonEntry + "_res" + " a   oils:CovariantScalar ;\n"
                        + "  oils:boundTo  <" + tupple.getOils_boundTo() + "> ;\n"
                        + "  oils:degree   <" + tupple.getOils_degree() + "> .\n"
                        + "\n";
                String condition
                        = ":" + tupple.getSenseId() + "_condition" + " a lemon:condition ;\n"
                        + "  lemon:propertyDomain   <" + tupple.getDomain() + "> ;\n"
                        + "  lemon:propertyRange    <" + tupple.getRange() + "> .";

                str += line + res + condition;
            }

            return str;
        }

        public String getPrepostion(String lemonEntry, String preposition, String language) {
            return "## Prepositions ##\n"
                    + ":" + "form_" + lemonEntry + "_preposition" + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + language + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .\n"
                    + "\n"
                    + "";
        }

        public void setArticle(Tupples tupple, String[] row) {
            //GenderUtils.setWrittenForms(tupple.getDomain(), getDomainWrittenSingularFormIndex(row), getRangeWrittenSingularFormIndex(row));
            //GenderUtils.setWrittenForms(tupple.getRange(), getRangeWrittenSingularFormIndex(row), getRangeWrittenPluralFormIndex(row));
            System.out.println("domainArticleIndex::"+domainArticleIndex);
            System.out.println("rangeArticleIndex::"+rangeArticleIndex);
            
            GenderUtils.setArticles(tupple.getDomain(), row[this.domainArticleIndex]);
            GenderUtils.setArticles(tupple.getRange(), row[this.rangeArticleIndex]);
            GenderUtils.setWrittenForms(tupple.getDomain(), row[this.domainWrittenSingularFormIndex], row[this.domainWrittenPluralFormIndex]);
            GenderUtils.setWrittenForms(tupple.getRange(), row[this.rangeWrittenSingularFormIndex], row[this.rangeWrittenPluralFormIndex]);

        }

        public String getLemonEntryIndex(String[] row) {
            return row[lemonEntryIndex];
        }

        public String getPartOfSpeechIndex(String[] row) {
            return row[partOfSpeechIndex];
        }

        public String getWrittenFormIndex(String[] row) {
            return row[writtenFormIndex];
        }

        public String getPrepostion(String[] row) {
            return row[prepostionIndex];
        }

        public String getComparativIndex(String[] row) {
            return row[comparativIndex];
        }

        public String getSuperlativeIndex(String[] row) {
            return row[superlativeSingularIndex];
        }

        public Integer getSyntacticFrameIndex() {
            return syntacticFrameIndex;
        }

        public String getPredFrameIndex(String[] row) {
            return row[predFrameIndex];
        }

        public String getSenseIndex(String[] row) {
            return row[senseIndex];
        }

        public String getReferenceIndex(String[] row) {
            return row[referenceIndex];
        }

        public String getOils_boundToIndex(String[] row) {
            return row[oils_boundToIndex];
        }

        public String getOils_degreeIndex(String[] row) {
            return row[oils_degreeIndex];
        }

        public String getDomainIndex(String[] row) {
            return row[domainIndex];
        }

        public String getRangeIndex(String[] row) {
            return row[rangeIndex];
        }

        public String getDomainWrittenSingularFormIndex(String[] row) {
            return row[domainWrittenSingularFormIndex];
        }

        public String getDomainWrittenPluralFormIndex(String[] row) {
            return row[domainWrittenPluralFormIndex];
        }

        public String getRangeWrittenSingularFormIndex(String[] row) {
            return row[rangeWrittenSingularFormIndex];
        }

        public String getRangeWrittenPluralFormIndex(String[] row) {
            return row[rangeWrittenPluralFormIndex];
        }

     

    }

}
