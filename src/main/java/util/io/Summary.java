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
public class Summary {
    private String frameType=null;
    private Integer numberOfGrammarRules=null;
    private Integer numberOfQuestions=null;
    
    public Summary(String frameType,Integer numberOfGrammarRules,Integer numberOfQuestions){
       this.frameType=frameType;
       this.numberOfGrammarRules=numberOfGrammarRules;
       this.numberOfQuestions=numberOfQuestions;
        
    }

   
    public String getFrameType() {
        return frameType;
    }

    public Integer getNumberOfGrammarRules() {
        return numberOfGrammarRules;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

   
    
}
