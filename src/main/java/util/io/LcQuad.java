/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LcQuad {

    private String datasetName = "LcQuad_1.1";
    private String fileName = null;

    private List<LcQuadElement> lcQuadElements = new ArrayList<LcQuadElement>();

    public LcQuad(String fileName) {
        this.fileName = fileName;
        this.prepareList();

    }
    
    private void prepareList() {
         ObjectMapper mapper = new ObjectMapper();
        File file = new File("LcQuad/lcquad1_test_en_de.json");
        String langCode = "en";
        try {
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            //System.out.println(jsonString);
            this.lcQuadElements = mapper.readValue(jsonString, new TypeReference<List<LcQuadElement>>() {
            });
            for (LcQuadElement LcQuadElement : lcQuadElements) {
                //System.out.println(LcQuadElement.getId());
                if (langCode.contains("en")) {
                    //System.out.println(LcQuadElement.getQuestion_en());
                } else if (langCode.contains("de")) {
                    //System.out.println(LcQuadElement.getQuestion_de());
                }
                //System.out.println(LcQuadElement.getSparql_query());
            }

        } catch (IOException ex1) {
            Logger.getLogger(LcQuad.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

   /* private void prepareList() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);
        try {
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            System.out.println(jsonString);
            lcQuadElements = mapper.readValue(jsonString, new TypeReference<List<LcQuadElement>>() {
            });

        } catch (IOException ex1) {
            Logger.getLogger(LcQuad.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }*/

    public String getDatasetName() {
        return datasetName;
    }

    public List<LcQuadElement> getLcQuadElements() {
        return lcQuadElements;
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("LcQuad/lcquad1_test_en_de.json");
        String langCode = "en";
        try {
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            System.out.println(jsonString);
            List<LcQuadElement> lcQuadElements = mapper.readValue(jsonString, new TypeReference<List<LcQuadElement>>() {
            });
            for (LcQuadElement LcQuadElement : lcQuadElements) {
                System.out.println(LcQuadElement.getId());
                if (langCode.contains("en")) {
                    System.out.println(LcQuadElement.getQuestion_en());
                } else if (langCode.contains("de")) {
                    System.out.println(LcQuadElement.getQuestion_de());
                }
                System.out.println(LcQuadElement.getSparql_query());
            }

        } catch (IOException ex1) {
            Logger.getLogger(LcQuad.class.getName()).log(Level.SEVERE, null, ex1);
        }

    }
}
