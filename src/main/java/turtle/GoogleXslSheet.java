/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.past;
import static grammar.datasets.sentencetemplates.TempConstants.perfect;
import static grammar.datasets.sentencetemplates.TempConstants.present;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.io.GenderUtils;
import util.io.Tupples;

/**
 *
 * @author elahi
 */
public class GoogleXslSheet {

    public static Integer lemonEntryIndex = 0;
    public static Integer partOfSpeechIndex = 1;
    public static Integer writtenFormInfinitive = 2;
    public static Integer AdjectiveFrameIndex = 3;

    public static class NounPPFrame {
        //LemonEntry	partOfSpeech	writtenForm (singular)	writtenForm (plural)	preposition	SyntacticFrame	copulativeArg	prepositionalAdjunct	sense	reference	domain	range	GrammarRule1:question1	SPARQL	GrammarRule1: question2	SPARQL Question 2	GrammarRule 1: questions	SPARQL 	NP (Grammar Rule 2)		grammar rules	numberOfQuestions
        //birthPlace_of	noun	birth place	-	of	NounPPFrame	range	domain	1	dbo:birthPlace	dbo:Person	dbo:Place	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?		2	

        public static Integer lexicalIdIndex = 0;
        public static Integer partOfSpeechIndex = 1;
        public static Integer genderIndex = 2;
        public static Integer writtenFormSingularIndex = 3;
        public static Integer writtenFormPluraIndex = 4;
        public static Integer writtenFormAccusativeIndex = 5;
        public static Integer writtenFormDativeIndex = 6;
        public static Integer writtenFormGenetiveIndex = 7;
        public static Integer prepositionIndex = 8;
        public static Integer SyntacticFrame = 9;
        public static Integer copulativeArgIndex = 10;
        public static Integer prepositionalAdjunctIndex = 11;
        public static Integer senseIndex = 12;
        public static Integer referenceIndex = 13;
        public static Integer domainIndex = 14;
        public static Integer rangeIndex = 15;
        public static Integer domainArticleIndex = 16;
        public static Integer domainWrittenSingular = 17;
        public static Integer domainWrittenPlural = 18;
        public static Integer rangeArticleIndex = 19;
        public static Integer rangeWrittenSingular = 20;
        public static Integer rangeWrittenPlural = 21;

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
                    + "  lemon:entry    :" + preposition + " .\n"
                    + "\n";
        }

        public static String getIndexing(String lemonEntry, List<Tupples> senseIds) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a       lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:noun ;\n"
                    + "  lemon:canonicalForm  :" + lemonEntry + "_form ;\n"
                    + "  lemon:otherForm      :" + lemonEntry+"_plural_form" + " ;\n"
                    + "  lemon:otherForm      :" + lemonEntry+"_accusative_form" + " ;\n"
                    + "  lemon:otherForm      :" + lemonEntry+"_dative_form" + " ;\n"
                    + "  lemon:otherForm      :" + lemonEntry+"_genitive_form" + " ;\n"
                    + "  lemon:otherForm      :" + lemonEntry+"_plural_form" + " ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_nounpp .\n"
                    + "\n";
            return senseIdStr;
        }

        public static String getWrittenFormAll(String lemonEntry,String gender, String singular, String plural, String accusativeForm, String dativeForm, String writtenGenitiveForm, String language) {
            String writtenForm
                    = ":" + lemonEntry + "_form" + " a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + singular + "\"@" + language + " ;\n"
                    + "  lexinfo:gender   lexinfo:"+gender+" ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:case     lexinfo:nominativeCase"+" .\n"
                    + "\n"
                    + ":" + lemonEntry+"_plural_form" + " a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + plural + "\"@" + language + " ;\n"
                    + "  lexinfo:gender   lexinfo:"+gender+" ;\n"
                    + "  lexinfo:number   lexinfo:plural ;\n"
                    + "  lexinfo:case     lexinfo:nominativeCase"+" .\n"
                    + "\n"
                    + ":" + lemonEntry+"_accusative_form" + " a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + accusativeForm + "\"@" + language + " ;\n"
                    + "  lexinfo:gender   lexinfo:"+gender+" ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:case     lexinfo:accusativeCase"+" .\n"
                    + "\n"
                    + ":" + lemonEntry+"_dative_form" + " a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + dativeForm + "\"@" + language + " ;\n"
                    + "  lexinfo:gender   lexinfo:"+gender+" ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:case     lexinfo:dativeCase"+" .\n"
                    + "\n"
                    + ":" + lemonEntry+"_genitive_form" + " a lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenGenitiveForm + "\"@" + language + " ;\n"
                    + "  lexinfo:gender   lexinfo:"+gender+" ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:case     lexinfo:genitiveCase"+" .\n"
                    + "\n";
           
            String copulativeStr= ":" + lemonEntry + "_nounpp"+" a        lexinfo:NounPPFrame ;\n"
                    + "  lexinfo:copulativeArg        :arg1 ;\n"
                    + "  lexinfo:prepositionalAdjunct :arg2 .\n"
                    + "\n";

            return writtenForm+copulativeStr;

        }

       

        public static String getPreposition(String prepositionAtt,String preposition, String language) {
            return ":arg2 lemon:marker :" + prepositionAtt + " .\n"
                    + "\n"
                    + "## Prepositions ##\n"
                    + "\n"
                    + ":" + prepositionAtt + " a                  lemon:SynRoleMarker ;\n"
                    + "  lemon:canonicalForm  [ lemon:writtenRep \"" + preposition + "\"@" + "en" + " ] ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:preposition .";
        }
     
        public static void setArticle(Tupples tupple, String gender, String[] row) {
            GenderUtils.setArticles(tupple.getReference(), gender);
            GenderUtils.setArticles(tupple.getDomain(), row[getDomainArticleIndex()]);
            GenderUtils.setArticles(tupple.getRange(), row[getRangeArticleIndex()]);
            GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
            GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }

        public static Integer getLexicalIdIndex() {
            return lexicalIdIndex;
        }

        public static Integer getPartOfSpeechIndex() {
            return partOfSpeechIndex;
        }

        public static Integer getGenderIndex() {
            return genderIndex;
        }

        public static Integer getWrittenFormSingularIndex() {
            return writtenFormSingularIndex;
        }

        public static Integer getWrittenFormPluraIndex() {
            return writtenFormPluraIndex;
        }

        public static Integer getWrittenFormAccusativeIndex() {
            return writtenFormAccusativeIndex;
        }

        public static Integer getWrittenFormDativeIndex() {
            return writtenFormDativeIndex;
        }

        public static Integer getWrittenFormGenetiveIndex() {
            return writtenFormGenetiveIndex;
        }

        public static Integer getPrepositionIndex() {
            return prepositionIndex;
        }

        public static Integer getSyntacticFrame() {
            return SyntacticFrame;
        }

        public static Integer getCopulativeArgIndex() {
            return copulativeArgIndex;
        }

        public static Integer getPrepositionalAdjunctIndex() {
            return prepositionalAdjunctIndex;
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

        public static Integer getDomainWrittenSingular() {
            return domainWrittenSingular;
        }

        public static Integer getDomainWrittenPlural() {
            return domainWrittenPlural;
        }

        public static Integer getRangeArticleIndex() {
            return rangeArticleIndex;
        }

        public static Integer getRangeWrittenSingular() {
            return rangeWrittenSingular;
        }

        public static Integer getRangeWrittenPlural() {
            return rangeWrittenPlural;
        }

        

    }

    public static class TransitFrame {
        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	SyntacticFrame	subject	directObject	sense	reference	domain	range	GrammarRule 1:question1	GrammarRule 1:question2	GrammarRule 1:sparql	GrammarRule 2:question1	GrammarRule2: sparql
        //compose	        verb	compose	composes	composed	TransitiveFrame	range	domain	1	dbo:musicComposer	dbo:Work	dbo:Person	Which dbo:Person (X) composes,composed Y(dbo:Work)?	Who dbo:Person (X) composes,composed Y(dbo:Work)?	SELECT ?X WHERE { Y dbo:musicComposer ?X.}	Which dbo:Work(X) was composed by Y(dbo:Person)?	SELECT ?X WHERE { ?X dbo:Person Y.}

        public static Integer writtenForm3rdPerson = 3;
        public static Integer writtenFormPast = 4;
        public static Integer SyntacticFrame = 5;
        public static Integer subjectIndex = 6;
        public static Integer directObjectIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;
        public static Integer domainArticleIndex = 12;
        public static Integer domainWrittenSingular =13;
        public static Integer domainWrittenPlural =14;
        public static Integer rangeArticleIndex = 15;
        public static Integer rangeWrittenSingular = 16;
        public static Integer rangeWrittenPlural = 17;
        public static Integer passivePrepositionIndex = 18;


        public static String getHeader(String lemonEntry, String prepositionAttr,String preposition, String language) {
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
                    + "  lemon:entry    :" + prepositionAttr + " .\n"
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

        public static String getWritten(String lemonEntry, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String language) {
            return ":form_" + lemonEntry + " a         lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "s a    lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:past ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson .\n"
                    + "\n"
                    + ":form_" + lemonEntry + "ed a   lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:tense    lexinfo:perfect ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame_transitive a lexinfo:TransitiveFrame ;\n"
                    + "  lexinfo:subject          :" + lemonEntry + "_subj ;\n"
                    + "  lexinfo:directObject     :" + lemonEntry + "_obj .\n"
                    + "\n";
        }
       
        public static void setArticle(Tupples tupple, String gender, String[] row) {
            GenderUtils.setArticles(tupple.getReference(), gender);
            GenderUtils.setArticles(tupple.getDomain(), row[getDomainArticleIndex()]);
            GenderUtils.setArticles(tupple.getRange(), row[getRangeArticleIndex()]);
            GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
            GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }

        public static Integer getWrittenForm3rdPerson() {
            return writtenForm3rdPerson;
        }

        public static Integer getWrittenFormPast() {
            return writtenFormPast;
        }

        public static Integer getSyntacticFrame() {
            return SyntacticFrame;
        }

        public static Integer getSubjectIndex() {
            return subjectIndex;
        }

        public static Integer getDirectObjectIndex() {
            return directObjectIndex;
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

        public static Integer getPassivePrepositionIndex() {
            return passivePrepositionIndex;
        }

        public static Integer getDomainArticleIndex() {
            return domainArticleIndex;
        }

        public static Integer getDomainWrittenSingular() {
            return domainWrittenSingular;
        }

        public static Integer getDomainWrittenPlural() {
            return domainWrittenPlural;
        }

        public static Integer getRangeArticleIndex() {
            return rangeArticleIndex;
        }

        public static Integer getRangeWrittenSingular() {
            return rangeWrittenSingular;
        }

        public static Integer getRangeWrittenPlural() {
            return rangeWrittenPlural;
        }

        
       

    }

    public static class InTransitFrame {

        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	preposition	SyntacticFrame	subject	prepositionalAdjunct	sense	reference	domain	range	GrammarRule 1 :question 1	GrammarRule 1 :question 2	GrammarRule 1 :sparql	Grammar rule 2: question1	Grammar rule 2: question2	sparql2
        //flow_through	verb	flow	flows	flowed	through	IntransitivePPFrame	domain	range	1	dbo:country	dbo:River	dbo:Country	What dbo:River(X) flows through Y(dbo:Country)?	FALSE	SELECT ?X WHERE { ?X dbo:country Y.}	Which dbo:Country(X) does Y(dbo:River) flow through?	FALSE	SELECT ?X WHERE { Y dbo:country ?X.}
        public static Integer writtenForm3rdPerson = 3;
        public static Integer writtenFormPast = 4;
        public static Integer writtenFormPerfect = 5;
        public static Integer preposition = 6;
        public static Integer SyntacticFrame = 7;
        public static Integer subject = 8;
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
                    + "  lemon:entry    :" + proposition + " .\n"
                    + "\n";
        }

        public static String getSenseIndexing(List<Tupples> senseIds, String lemonEntry) {
            String senseIdStr = getSenseId(senseIds);
            senseIdStr = ":" + lemonEntry + " a             lemon:LexicalEntry ;\n"
                    + "  lexinfo:partOfSpeech lexinfo:verb ;\n"
                    + "  lemon:canonicalForm  :" + lemonEntry + "_present"+" ;\n"
                    + "  lemon:otherForm      :" + lemonEntry + "_past"+" ;\n"
                    + "  lemon:otherForm      :" + lemonEntry + "_perfect"+" ;\n"
                    + senseIdStr
                    + "  lemon:synBehavior    :" + lemonEntry + "_frame .\n"
                    + "\n";
            return senseIdStr;
        }

        public static String getWritten(String lemonEntry, String writtenFormInfinitive, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect, String language) {
            return ":" + lemonEntry + "_present"+ " a           lemon:Form ;\n"
                    + "  lemon:writtenRep     \"" + writtenFormInfinitive + "\"@" + language + " ;\n"
                    + "  lexinfo:verbFormMood lexinfo:infinitive .\n"
                    + "\n"
                    + "\n"
                    + ":" + lemonEntry + "_present"+ " a      lemon:Form ;\n"
                    + "  lemon:writtenRep \"" + writtenForm3rdPerson + "\"@" + language + " ;\n"
                    + "  lexinfo:number   lexinfo:singular ;\n"
                    + "  lexinfo:person   lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense    lexinfo:present .\n"
                    + "\n"
                    + ":" + lemonEntry + "_past"+" a lemon:Form ;\n"
                    + "  lemon:writtenRep  \"" + writtenFormPast + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:singular ;\n"
                    + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense     lexinfo:past .\n"
                    + "\n"
                    + ":" + lemonEntry + "_perfect"+" a lemon:Form ;\n"
                    + "  lemon:writtenRep  \"" + writtenFormPerfect + "\"@" + language + " ;\n"
                    + "  lexinfo:number    lexinfo:singular ;\n"
                    + "  lexinfo:person    lexinfo:thirdPerson ;\n"
                    + "  lexinfo:tense     lexinfo:perfect .\n"
                    + "\n"
                    + ":" + lemonEntry + "_frame a  lexinfo:IntransitivePPFrame ;\n"
                    + "  lexinfo:subject              :" + lemonEntry + "_subj ;\n"
                    + "  lexinfo:prepositionalAdjunct :" + lemonEntry + "_obj .\n"
                    + "\n";
        }
        
        public static void setArticle(Tupples tupple, String gender, String[] row) {
            GenderUtils.setArticles(tupple.getReference(), gender);
            GenderUtils.setArticles(tupple.getDomain(), row[getDomainArticleIndex()]);
            GenderUtils.setArticles(tupple.getRange(), row[getRangeArticleIndex()]);
            GenderUtils.setWrittenForms(tupple.getDomain(), row[getDomainWrittenSingular()], row[getDomainWrittenPlural()]);
            GenderUtils.setWrittenForms(tupple.getRange(), row[getRangeWrittenSingular()], row[getRangeWrittenPlural()]);
        }
        public static void setVerbInfo(String partOfSpeech, String writtenFromIn, String writtenForm3rd, String writtenFormPast, String writtenFormPerfect) {
              Map<String, String> verb = Map.of(
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
            GenderUtils.setPerfectVerbType(writtenFormPerfect, verb);
            
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
            return subject;
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

       
       
        

    }

    public static class AttributiveAdjectiveFrame {
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
        }

        return str;
    }

    public static String getPrepostion(String preopistionAttr,String preposition, String language) {
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

   

    public static Integer getAdjectiveFrameIndex() {
        return AdjectiveFrameIndex;
    }
    
      /*
                      "  lemon:otherForm    :" + lemonEntry+"_accusative_form" + " ;\n"
                    + "  lemon:otherForm    :" + lemonEntry+"_dative_form" + " ;\n"
                    + "  lemon:otherForm    :" + lemonEntry+"_genitive_form" + " ;\n"
                    + "  lemon:otherForm    :" + lemonEntry+"_plural_form" + " ;\n"
            */

   
}
