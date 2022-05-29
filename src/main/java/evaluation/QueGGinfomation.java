/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
public class QueGGinfomation {

    private String id = null;
    private String question = null;
    private String queGGSparqlQuery = null;
    private String qaldSparqlQuery = null;
    private String answerUri = null;
    private String answerLabel = null;
    private String syntacticFrame = null;
    private Double similarityScore = null;

    public QueGGinfomation(String[] row, Double value,String qaldSparqlQuery) {
        this.id = row[0];
        this.question = row[1];
        this.queGGSparqlQuery = row[2];
        this.answerUri = row[3];
        this.answerLabel = row[4];
        this.syntacticFrame = row[5];
        this.similarityScore = value;
        this.qaldSparqlQuery=qaldSparqlQuery;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getSparqlQuery() {
        return queGGSparqlQuery;
    }

    public String getAnswerUri() {
        return answerUri;
    }

    public String getAnswerLabel() {
        return answerLabel;
    }

    public String getSyntacticFrame() {
        return syntacticFrame;
    }

    public Double getValue() {
        return similarityScore;
    }

    public String getQueGGSparqlQuery() {
        return queGGSparqlQuery;
    }

    public String getQaldSparqlQuery() {
        return qaldSparqlQuery;
    }

    @Override
    public String toString() {
        return "QueGGinfomation{" + "id=" + id + ", question=" + question + ", queGGSparqlQuery=" + queGGSparqlQuery + ", qaldSparqlQuery=" + qaldSparqlQuery + ", answerUri=" + answerUri + ", answerLabel=" + answerLabel + ", syntacticFrame=" + syntacticFrame + ", similarityScore=" + similarityScore + '}';
    }

}
