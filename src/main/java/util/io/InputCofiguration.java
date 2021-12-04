/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import grammar.generator.GrammarRuleGeneratorRoot;
import grammar.sparql.SPARQLRequest;
import grammar.structure.component.Language;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import turtle.EnglishTurtle;
import turtle.GermanTurtle;
import turtle.TutleConverter;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputCofiguration {

    //java -jar target/QuestionGrammarGenerator.jar DE lexicon/de output/de 10 all dataset/dbpedia.json 80.0  
    @JsonProperty("languageCode")
    private String languageCode = null;
    @JsonProperty("inputDir")
    private String inputDir = null;
    @JsonProperty("outputDir")
    private String outputDir = null;
    @JsonProperty("qaldDir")
    private String qaldDir = null;
    @JsonProperty("numberOfEntities")
    private Integer numberOfEntities;
    @JsonProperty("similarityThresold")
    private Double similarityThresold;
    @JsonProperty("csvToTurtle")
    private Boolean csvToTurtle = false;
    @JsonProperty("turtleToProtoType")
    private Boolean turtleToProtoType = false;
    @JsonProperty("protoTypeToQuestion")
    private Boolean protoTypeToQuestion = false;
    @JsonProperty("evalution")
    private Boolean evalution;
    private LinkedData linkedData = null;

    public InputCofiguration() {

    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Language getLanguage() {
        if (languageCode.contains("de")) {
            return Language.DE;
        } else if (languageCode.contains("en")) {
            return Language.EN;
        }
        return Language.EN;
    }

    public String getInputDir() {
        return inputDir + File.separator + this.languageCode;
    }

    public String getOutputDir() {
        return outputDir + File.separator + this.languageCode;
    }

    public Integer getNumberOfEntities() {
        return numberOfEntities;
    }

    public Boolean isCsvToTurtle() {
        return csvToTurtle;
    }

    public Boolean getTurtleToProtoType() {
        return turtleToProtoType;
    }

    public Boolean isProtoTypeToQuestion() {
        return protoTypeToQuestion;
    }

    public Boolean isEvalution() {
        return evalution;
    }

    public Double getSimilarityThresold() {
        return similarityThresold;
    }

    public void setLinkedData(String fileName) throws Exception {
        this.linkedData = FileUtils.getLinkedDataConf(new File(fileName));
    }

    public LinkedData getLinkedData() {
        return linkedData;
    }

    public String getQaldDir() {
        return qaldDir;
    }
    
    @Override
    public String toString() {
        return "InputCofiguration{" + "language=" + languageCode + ", inputDir=" + getInputDir() + ", outputDir=" + getOutputDir() + ", numberOfEntities=" + numberOfEntities + ", similarityThresold=" + similarityThresold + ", csvToTurtle=" + csvToTurtle + ", turtleToProtoType=" + turtleToProtoType + ", protoTypeToQuestion=" + protoTypeToQuestion + ", evalution=" + evalution + '}';
    }

}
