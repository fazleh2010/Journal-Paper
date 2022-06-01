/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

/**
 *
 * @author elahi
 */
public interface ReadWriteConstants {
    public static String FRAMETYPE_NPP = "NPP";
    public static final String ID = "id";
    public static final String QUESTION = "question";
    public static final String SPARQL = "sparql";
    public static final String ANSWER_URI = "answer_uri";
    public static final String ANSWER_LABEL = "answer_label";
    public static final String FRAME = "frame";
    public static final String LEXICAL_ENTRY = "lexicalEntry";
    public static final String SENTENCETYPE = "sentenceType";
    public static final String FRAMETYPE = "frameType";
    public static final String NUMBER_OF_GRAMMAR_RULES = "numberOfGrammarRules";
    public static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    public static final String Status = "numberOfQuestions";
    public static final String Reason = "numberOfQuestions";
    public static final String ENTITY = "entity";
    public static String qaldFileBinding = "qaldEntities.csv";
    public static String propertyReport = "A-propertyReport.txt";
    public String[] questionHeader = new String[]{ID, QUESTION, SPARQL, ANSWER_URI, ANSWER_LABEL, FRAME};
    public String[] summaryHeader = new String[]{LEXICAL_ENTRY, NUMBER_OF_GRAMMAR_RULES, NUMBER_OF_QUESTIONS, FRAMETYPE, Status, Reason};
    public static String GENERATED = "GENERATED";
    public static String ALL = "ALL";
    
}
