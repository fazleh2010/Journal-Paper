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
public class Statistics {
    private String frameType=null;
    private Integer numberOfGrammarRules=null;
    private Integer numberOfQuestions=null;
    private Integer bindingList=null;
    private String Success_Fail="Success";
    private String reason="-";


    
    public Statistics(String frameType,Integer numberOfGrammarRules,Integer numberOfQuestions,Integer bindingList){
       this.frameType=frameType;
       this.numberOfGrammarRules=numberOfGrammarRules;
       this.numberOfQuestions=numberOfQuestions;
       this.bindingList= bindingList;
       if(bindingList==0){
          this.Success_Fail="Failed";
          this.reason="binding list is empty";
       }
       
    }

    public Integer getBindingList() {
        return bindingList;
    }

    public String getSuccess_Fail() {
        return Success_Fail;
    }

    public String getReason() {
        return reason;
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
