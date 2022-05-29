/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author elahi
 */
public class MatcherExample {

    public static String replaceQuestion(String question, String[] values) throws Exception {
        Integer index = 0;
        String questionT = "";
        List<String> variables = numberOfVariable(question);
        question = question.replace("(", "\n(");
        question = question.replace(")", ")\n");
        String[] lines = question.split(System.getProperty("line.separator"));

        if (!(variables.size() == values.length)) {
            throw new Exception("Number of variable and given labels are not equal");
        }

        for (String token : lines) {
            if (isSame(token, variables)) {
                token = values[index];
                index = index + 1;
            }
            String line = token + " ";
            questionT += line;
        }

        return questionT;
    }

    private static List<String> numberOfVariable(String question) {
        java.util.regex.Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(question);
        List<String> variables = new ArrayList<String>();
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }

    private static boolean isSame(String token, List<String> variables) {
        for (String variable : variables) {
            variable = variable.replace(" ", "").replace("(", "").replace(")", "").toLowerCase().trim().strip().stripLeading().stripLeading();
            token = token.replace(" ", "").replace("(", "").replace(")", "").toLowerCase().trim().strip().stripLeading().stripLeading();
            if (token.contains(variable)) {
                return true;
            }
        }
        return false;

    }
    
      public static void main(String[] args) throws Exception {
        String question = "ist ($x | Person_NP) die Kind von ($x | Person_NP)?";
        //replaceQuestion(String question, String rangeLabel, String domainLabel);
        System.out.println("question:" + question);
        String questionT = replaceQuestion(question, new String[]{"XXX", "YYYY"});
        System.out.println("questionT:" + questionT);

    }


}
