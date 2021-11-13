/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

/**
 *
 * @author elahi
 */
public class Tupples {

    private String senseId = null;
    private String reference = null;
    private String domain = null;
    private String range = null;

    public Tupples(String lemonEntry, Integer index,  String reference, String domain, String range) {
        this.senseId = lemonEntry + "_sense_" + index.toString();
        this.reference = reference.replace(" ", "");
        this.domain = domain.replace(" ", "");
        this.range = range.replace(" ", "");

    }

    public String getSenseId() {
        return senseId;
    }

    public String getReference() {
        return reference;
    }

    public String getDomain() {
        return domain;
    }

    public String getRange() {
        return range;
    }

}
