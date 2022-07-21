/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import util.io.FileProcessUtils;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UriLabel {

    @JsonProperty("label")
    private String label;
    @JsonProperty("uri")
    private String uri;
    
    private String answerUri;
    private String answerLabel;
    
    private String abstractText;
    private String wikiLink;
    private String thumbnail;
    private String returnSubjOrObj;


    
    public UriLabel() {
        
    }
    
    public UriLabel(String uri, String label) {
        this.uri = uri;
        this.label = label;
    }
    
    public UriLabel(String uri, String label, String answerUri, String answerLabel,String returnSubjOrObj) {
        this.uri = uri;
        this.label = label;
        this.answerUri = answerUri;
        this.answerLabel = answerLabel;
        this.returnSubjOrObj=returnSubjOrObj;
    }
    
    public UriLabel(String uri, String label, String answerUri, String answerLabel,String wikiLink,String thumbnail,String abstractText) {
        this.uri = uri;
        this.label = label;
        this.answerUri = answerUri;
        this.answerLabel = answerLabel;
        this.abstractText = abstractText;
        this.wikiLink = wikiLink;
        this.thumbnail = thumbnail;
    }

    public String getLabel() {
        return label;
    }

    public String getUri() {
        return uri;
    }

    public String getAnswerUri() {
        return answerUri;
    }

    public String getAnswerLabel() {
        return answerLabel;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    

    @Override
    public String toString() {
        return "UriLabel{" + "label=" + label + ", uri=" + uri + '}';
    }

}
