/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.read.questions;

/**
 *
 * @author elahi
 */
public class ResultQA {

    String match;

    String uri;

    String type;

    public ResultQA(String match, String uri, String type) {
        this.match = match;
        this.uri = uri;
        this.type = type;

    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return  match + ", uri=" + uri + ", type=" + type;
    }

}
