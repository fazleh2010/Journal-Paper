/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    
    public UriLabel() {
        
    }
    
    public UriLabel(String uri, String label) {
        this.uri = uri;
        this.label = label;
    }
    
    public UriLabel(String uri, String label, String answerUri, String answerLabel) {
        this.uri = uri;
        this.label = label;
        this.answerUri = answerUri;
        this.answerLabel = answerLabel;
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
    

    @Override
    public String toString() {
        return "UriLabel{" + "label=" + label + ", uri=" + uri + '}';
    }

}
