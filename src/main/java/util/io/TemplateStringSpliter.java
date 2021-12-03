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
public class TemplateStringSpliter {

    private String template = null;
    private String attribute = null;
    private String reference = null;
    private Boolean referenceFlag = true;

    public TemplateStringSpliter(String template) {
        String[] tokens = StringMatcher.parseToken(template);
        if (tokens.length == 2) {
            this.attribute = tokens[0];
            this.reference = tokens[1];
        } else if (tokens.length == 1) {
            this.attribute = tokens[0];
            referenceFlag = false;
        }
    }

    public String getTemplate() {
        return template;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getReference() {
        return reference;
    }

    public Boolean getReferenceFlag() {
        return referenceFlag;
    }
    
    

}
