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
@Deprecated
public class Tupple {

    private String entry;
    private String uri;
    private String type;

    public Tupple(String entry, String uri, String type) {
        this.entry = entry;
        this.type = type;
        this.uri = uri;

    }


    public String getEntry() {
        return entry;
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tupple{" + "entry=" + entry + ", uri=" + uri + ", type=" + type + '}';
    }

}
