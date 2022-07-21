/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import evaluation.Matcher;
import grammar.read.questions.GrammarEntries;
import grammar.read.questions.GrammarEntryUnit;
import grammar.read.questions.ReadWriteConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class ReferenceManagement implements ReadWriteConstants {

    private Set<String> matchedProperties = new TreeSet<String>();
    private Set<String> notMatchedProperties = new TreeSet<String>();
    private Set<String> offlineGeneratedProperties = new TreeSet<String>();
     private Set<String> offlineGeneratedEntities = new TreeSet<String>();
    private Collection<String> notAddressedProperties = new TreeSet<String>();
    private String propertyFile = null;
    


    public ReferenceManagement(String propertyDir, String entityDir, List<File> protoSimpleQFiles, String type) {
        this.propertyFile = propertyDir + File.separator + "property.txt";
        if (type.contains(GENERATED)) {
            String[] file = new File(propertyDir).list();
            for (String fileString : file) {
                if ((fileString.contains("dbo_") || fileString.contains("dbp_"))) {
                    this.offlineGeneratedProperties.add(FilenameUtils.removeExtension(new File(fileString).getName()));
                }

            }
            String[] fileEnities = new File(entityDir).list();
            for (String fileString : fileEnities) {
                if (fileString.contains("dbo_")&&fileString.contains(".txt")) {
                    this.offlineGeneratedEntities.add(FilenameUtils.removeExtension(new File(fileString).getName()));
                }

            }
        } else if (type.contains(ALL)) {
            this.offlineGeneratedProperties = FileProcessUtils.getSetFromFile(propertyFile);

        }

        matchProperties(this.offlineGeneratedProperties, protoSimpleQFiles, type);

        getRetainProperties(this.offlineGeneratedProperties, this.matchedProperties);

    }

    public Set<String> matchProperties(Set<String> offlineGeneratedProperties, List<File> protoSimpleQFiles, String type) {

        for (File jsonFile : protoSimpleQFiles) {
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries;
            try {
                grammarEntries = mapper.readValue(jsonFile, GrammarEntries.class);
                for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
                    String property = getProperty(grammarEntryUnit.getSparqlQuery());
                    if (offlineGeneratedProperties.contains(property)) {
                        matchedProperties.add(property);
                    } else {
                        notMatchedProperties.add(property);
                    }

                }
                //this.matchedProperties=FileUtils.findCommonElements(queggProperties, offlineGeneratedProperties);
            } catch (IOException ex) {
                Logger.getLogger(ReferenceManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return matchedProperties;
    }

    /*public Set<String> matchProperties(Set<String> offlineGeneratedProperties, List<File> protoSimpleQFiles, String type) {

        for (File jsonFile : protoSimpleQFiles) {
            ObjectMapper mapper = new ObjectMapper();
            GrammarEntries grammarEntries;
            try {
                grammarEntries = mapper.readValue(jsonFile, GrammarEntries.class);
                Set<String> queggProperties = new TreeSet<String>();
                for (GrammarEntryUnit grammarEntryUnit : grammarEntries.getGrammarEntries()) {
                    String property = getProperty(grammarEntryUnit.getSparqlQuery());
                    if (property.contains("dbo_abbreviation")) {
                        if (offlineGeneratedProperties.contains(property)) {
                            matchedProperties.add(property);
                        } else {
                            notMatchedProperties.add(property);
                        }
                    }
                   

                }
                //this.matchedProperties=FileUtils.findCommonElements(queggProperties, offlineGeneratedProperties);
            } catch (IOException ex) {
                Logger.getLogger(ReferenceManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return matchedProperties;
    }*/
    private Set<String> getPropertiesFromFile(String fileName) {
        Set<String> results = new TreeSet<String>();
        String line = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                line = Matcher.cleanPrefix(line);
                results.add(line);
            }
            reader.close();
        } catch (Exception ex) {

        }
        return results;
    }

    private static String getProperty(String sparqlQueryOrg) {
        String property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
        property = property.replace("http://dbpedia.org/ontology/", "dbo_");
        property = property.replace("http://dbpedia.org/property/", "dbp_");
        return property;
    }

    /*private boolean isMatched(String property) {

        for (String offlineProp : this.offlineGeneratedProperties) {
            property = property.toLowerCase().strip().stripLeading().stripTrailing().trim();
            offlineProp = offlineProp.toLowerCase().strip().stripLeading().stripTrailing().trim();

            if (offlineProp.contains(property)) {
                return true;
            }

        }

        return false;
    }*/

    public static void setToFile(Set<String> properties, String fileName) throws IOException, Exception {
        if(properties.isEmpty()){
            System.out.println("no element to write in file and no file is created::"+fileName);
            return;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (String property : properties) {
            property = property.replace("dbo_", "http://dbpedia.org/ontology/");
            property = property.replace("dbp_", "http://dbpedia.org/property/");
            writer.write(property + "\n");
        }
        writer.close();
    }

    public void getRetainProperties(Set<String> a, Set<String> b) {
        this.notAddressedProperties = CollectionUtils.subtract(a, b);
    }

    public Set<String> getMatchedProperties() {
        return matchedProperties;
    }

    public Set<String> getNotMatchedProperties() {
        return notMatchedProperties;
    }

    public Set<String> getOfflineProperties() {
        return offlineGeneratedProperties;
    }

    public Collection<String> getNotAddressedProperties() {
        return notAddressedProperties;
    }

    public Set<String> getOfflineGeneratedProperties() {
        return offlineGeneratedProperties;
    }

    public Set<String> getOfflineGeneratedEntities() {
        return offlineGeneratedEntities;
    }

    public String getPropertyFile() {
        return propertyFile;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!!");
        String language = "it";
        String grammar_FULL_DATASET = "grammar_FULL_DATASET";
        String offLinePropertyDir = "../resources/"+language+"/property/";
        String offLineEntityDir = "../resources/"+language+"/entity/";

        String inputDir = "../resources/";
        List<File> protoSimpleQFiles = new ArrayList<File>();
        
        protoSimpleQFiles.addAll(FileProcessUtils.getFiles(inputDir + language + "/", grammar_FULL_DATASET, ".json"));

        if (protoSimpleQFiles.isEmpty()) {
            throw new Exception("No proto file found to process!!");
        }

        ReferenceManagement referenceManagement = new ReferenceManagement(offLinePropertyDir, offLineEntityDir,protoSimpleQFiles, ReadWriteConstants.GENERATED);

        System.out.println("Matched::" + referenceManagement.matchedProperties.size() + " " + referenceManagement.matchedProperties);
        System.out.println("NotMatched::" + referenceManagement.notMatchedProperties.size() + " " + referenceManagement.notMatchedProperties);
        System.out.println("NotAddressed::" + referenceManagement.notAddressedProperties.size() + " " + referenceManagement.notAddressedProperties);
        System.out.println("offlineGeneratedProperties::" + referenceManagement.offlineGeneratedProperties.size() + " " + referenceManagement.offlineGeneratedProperties);
        System.out.println("propertyMatch::" + referenceManagement.propertyFile.replace("property.txt", "propertyMatch.txt"));
        setToFile(referenceManagement.matchedProperties, referenceManagement.propertyFile.replace("property.txt", "propertyMatch.txt"));
        setToFile(referenceManagement.notMatchedProperties, referenceManagement.propertyFile.replace("property.txt", "propertyNotMatch.txt"));

    }

}
