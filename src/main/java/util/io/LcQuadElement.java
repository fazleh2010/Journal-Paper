/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author elahi
 */
public class LcQuadElement {

    @JsonProperty("_id")
    private Integer _id = null;
    @JsonProperty("question_en")
    private String question_en = null;
    @JsonProperty("question_de")
    private String question_de = null;
    @JsonProperty("sparql_query")
    private String sparql_query = null;
    @JsonProperty("sparql_template_id")
    private Integer sparql_template_id = null;

    private String english = "en";
    private String german = "de";

    public Integer getId() {
        return _id;
    }

    public String getQuestion_en() {
        return question_en;
    }

    public String getQuestion_de() {
        return question_de;
    }

    public String getSparql_query() {
        return sparql_query;
    }

    public Integer getSparql_template_id() {
        return sparql_template_id;
    }

    public String getEnglish() {
        return english;
    }

    public String getGerman() {
        return german;
    }

}
