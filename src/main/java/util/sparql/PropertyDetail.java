/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sparql;

import grammar.structure.component.Binding;

/**
 *
 * @author elahi
 */
public class PropertyDetail {

    private String domain = null;
    private String range = null;
    private Binding binding=null;   
    
    public PropertyDetail(String domain,String range,Binding binding ){
         this.domain = domain;
         this.range = range;
         this.binding=binding;   
        
    }

    public String getDomain() {
        return domain;
    }

    public String getRange() {
        return range;
    }

    public Binding getBinding() {
        return binding;
    }
    
}
