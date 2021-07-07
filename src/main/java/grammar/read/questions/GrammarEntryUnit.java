/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.List;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrammarEntryUnit {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("lexicalEntryUri")
    private URI lexicalEntryUri ;
    @JsonProperty("lanuage")
    private String language;
    @JsonProperty("type")
    private String type;
    @JsonProperty("bindingType")
    private String bindingType;
    @JsonProperty("returnType")
    private String returnType;
    @JsonProperty("frameType")
    private String frameType;
    @JsonProperty("sentences")
    private List<String> sentences;
    @JsonProperty("queryType")
    private String queryType;
    @JsonProperty("sparqlQuery")
    private String sparqlQuery;
    @JsonProperty("sentenceToSparqlParameterMapping")
    private SentenceToSparql sentenceToSparqlParameterMapping;
    @JsonProperty("returnVariable")
    private String returnVariable;
    @JsonProperty("sentenceBindings")
    private SentenceBindings sentenceBindings;
    @JsonProperty("combination")
    private Boolean combination;

    public GrammarEntryUnit() {
    }

    public Integer getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getType() {
        return type;
    }

    public String getBindingType() {
        return bindingType;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getFrameType() {
        return frameType;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getSparqlQuery() {
        return sparqlQuery;
    }

    public String getSentenceToSparqlParameterMappingX() {
        return sentenceToSparqlParameterMapping.getX();
    }

    public String getReturnVariable() {
        return returnVariable;
    }

    public List<UriLabel> getBindingList() {
        return sentenceBindings.getBindingList();
    }
    
    public String getBindingVariableName() {
        return sentenceBindings.getBindingVariableName();
    }

    public Boolean getCombination() {
        return combination;
    }

    public URI getLexicalEntryUri() {
        return lexicalEntryUri;
    }

    public SentenceToSparql getSentenceToSparqlParameterMapping() {
        return sentenceToSparqlParameterMapping;
    }

    public SentenceBindings getSentenceBindings() {
        return sentenceBindings;
    }

}
