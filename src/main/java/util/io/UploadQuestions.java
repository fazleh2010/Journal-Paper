/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class UploadQuestions {

    private Boolean processSuccessFlag = false;
    private static String dataSet = "dbpedia.json";
    private static String scriptName = "test.csv";
    private static String qaSystemUrl = "https://webtentacle1.techfak.uni-bielefeld.de/quegg/importQuestions";
    //curl -X "POST" -F "file=@questionsSuper1.csv" -F "config=@dbpedia.json" "https://webtentacle1.techfak.uni-bielefeld.de/quegg/importQuestions"

    public static void main(String[] args) throws Exception {
        UploadQuestions uploadQuestions = new UploadQuestions(dataSet, scriptName, qaSystemUrl);
        if (uploadQuestions.processSuccessFlag) {
            //System.out.println("process run successfully!!!");
        }

    }

    public UploadQuestions(String dataSet, String scriptName, String qaSystemUrl) throws Exception {
        //works
            //System.out.println("Reading DBpedia abstract and knowledge graph and corpus based lexicalization!!\n");
            //System.out.println("Step 1. find frequent entities and process abstracts. Wait..." + "\n");

            String command = "curl -X POST -F file=@" + scriptName + " -F config=@" + dataSet + " \"" + qaSystemUrl + "\"";
            command = "curl -X POST -F file=@test.csv -F config=@dbpedia.json https://webtentacle1.techfak.uni-bielefeld.de/quegg/importQuestions";

            this.runCommandLine(command);
            //Runtime runTime = Runtime.getRuntime();
            //System.out.println("location + scriptName::" + location + scriptName);
            //String[] commands = {"perl", location + scriptName};
            //System.out.println("command::"+command);
            //Process process = runTime.exec(commands);
            //String command = "curl -X POST file=@test.csv https://postman-echo.com/post --data foo1=bar1&foo2=bar2";
           // command = "curl -X POST -F file=@test.csv -F config=@dbpedia.json https://webtentacle1.techfak.uni-bielefeld.de/quegg/importQuestions";
            //Process process = Runtime.getRuntime().exec(command);

            /*if (runCommandLine(location, "frequentClass.pl", class_url)) {
                System.out.println("done with step 1." + "\n");
                if (runCommandLine(location, "tripleProcess.pl", class_url)) {
                    //System.out.println("done with step 1." + "\n");
                }
            } else
                throw new PerlException("Step 1. failed to process");*/
        

    }

    public Boolean runCommandLine(String command) throws IOException, InterruptedException {

        Runtime runTime = Runtime.getRuntime();
        //System.out.println("location + scriptName::" + location + scriptName);
        //String[] commands = {"perl", location + scriptName};
        //System.out.println("command::"+command);
        Process process = runTime.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        // Read the output from the command
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            //System.out.println(s);
        }
        // Read any errors from the attempted command
        //System.out.println("Error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.err.println(s);
        }

        if (process.waitFor() == 0) {
            System.err.println("Process terminated ");
            processSuccessFlag = true;
            return true;
        } else {
            return false;
        }

    }

    public Boolean getProcessSuccessFlag() {
        return processSuccessFlag;
    }

}
