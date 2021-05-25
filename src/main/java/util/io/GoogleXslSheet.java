/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

/**
 *
 * @author elahi
 */
public class GoogleXslSheet {

    public static Integer lemonEntryIndex = 0;

    public static class NounPPFrame {
    //LemonEntry	partOfSpeech	writtenForm (singular)	writtenForm (plural)	preposition	SyntacticFrame	copulativeArg	prepositionalAdjunct	sense	reference	domain	range	GrammarRule1:question1	SPARQL	GrammarRule1: question2	SPARQL Question 2	GrammarRule 1: questions	SPARQL 	NP (Grammar Rule 2)		grammar rules	numberOfQuestions
    //birthPlace_of	noun	birth place	-	of	NounPPFrame	range	domain	1	dbo:birthPlace	dbo:Person	dbo:Place	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?		2	


        public static Integer partOfSpeechIndex = 1;
        public static Integer writtenFormSingularIndex = 2;
        public static Integer writtenFormPluralIndex = 3;
        public static Integer prepositionIndex = 4;
        public static Integer syntacticFrameIndex = 5;
        public static Integer copulativeArgIndex = 6;
        public static Integer prepositionalAdjunctIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;

    }

    public static class TransitFrame {
        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	SyntacticFrame	subject	directObject	sense	reference	domain	range	GrammarRule 1:question1	GrammarRule 1:question2	GrammarRule 1:sparql	GrammarRule 2:question1	GrammarRule2: sparql
        //compose	        verb	compose	composes	composed	TransitiveFrame	range	domain	1	dbo:musicComposer	dbo:Work	dbo:Person	Which dbo:Person (X) composes,composed Y(dbo:Work)?	Who dbo:Person (X) composes,composed Y(dbo:Work)?	SELECT ?X WHERE { Y dbo:musicComposer ?X.}	Which dbo:Work(X) was composed by Y(dbo:Person)?	SELECT ?X WHERE { ?X dbo:Person Y.}

        public static Integer partOfSpeechIndex = 1;
        public static Integer writtenFormInfinitive = 2;
        public static Integer writtenForm3rdPerson = 3;
        public static Integer writtenFormPast = 4;
        public static Integer syntacticIndex = 5;
        public static Integer subjectIndex = 6;
        public static Integer directObjectIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;

    }

}
