/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import grammar.read.questions.GrammarEntries;
import grammar.read.questions.GrammarEntryUnit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class Test {


    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!!");
        String grammar_FULL_DATASET = "grammar_FULL_DATASET";
        String offLinePropertyDir = "../resources/de/property/";
        String inputDir = "../resources/";
        List<File> protoSimpleQFiles = new ArrayList<File>();
        String language = "es";
        protoSimpleQFiles.addAll(FileUtils.getFiles(inputDir +language+"/", grammar_FULL_DATASET , ".json"));
        
          if (protoSimpleQFiles.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }
        matchProperties(offLinePropertyDir, protoSimpleQFiles);
    }

    public static Set<String> matchProperties(String offLinePropertyDir, List<File> protoSimpleQFiles) {
        Set<String> offlineProperties = new TreeSet<String>();
        Set<String> existingProperties = new TreeSet<String>();
        String[] file = new File(offLinePropertyDir).list();
        for (String fileString : file) {
            if (fileString.contains("dbo_") || fileString.contains("dbp_")) {
                offlineProperties.add(FilenameUtils.removeExtension(new File(fileString).getName()));
            }

        }

        for (File jsonFile : protoSimpleQFiles) {
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries;
            try {
                grammarEntries = mapper.readValue(jsonFile, GrammarEntries.class);
                for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
                    String property = getProperty(grammarEntryUnit.getSparqlQuery());
                    if (offlineProperties.contains(property)) {
                        existingProperties.add(property);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        /*Set<String> intersection = new TreeSet<String>(offlineProperties);
        intersection.retainAll(existingProperties);
        System.out.println("intersection::" + intersection);*/
        return existingProperties;
    }

    private static String getProperty(String sparqlQueryOrg) {
        String property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
        property = property.replace("http://dbpedia.org/ontology/", "dbo_");
        property = property.replace("http://dbpedia.org/property/", "dbp_");
        return property;
    }

}
