/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedData {

    @JsonProperty("endpoint")
    private String endpoint = null;
    @JsonProperty("prefix")
    private LinkedHashMap<String, String> prefix = new LinkedHashMap<String, String>();
    @JsonProperty("bindingLimit")
    private long bindingLimit;

    @JsonIgnore
    public static String dbpedia = "dbpedia";
    @JsonIgnore
    public static String wikidata = "wikidata";
    @JsonIgnore
    public static String http = "http";
    @JsonIgnore
    public static String colon = ":";

    public LinkedData(String endpoint, LinkedHashMap<String, String> prefix) {
        this.endpoint = endpoint;
        this.prefix = prefix;
    }

    public LinkedData() {

    }

    public String getEndpoint() {
        return endpoint;
    }

    public LinkedHashMap<String, String> getPrefixes() {
        return prefix;
    }

    /*LinkedHashMap<String, String> prefix = FileFolderUtils.fileToHashOrg(dataSetConfFile);
        LinkedData linkedData=new LinkedData("https://dbpedia.org/sparql",prefix);
        FileFolderUtils.write(linkedData, dataSetConfFile);*/
    public long getBindingLimit() {
        return bindingLimit;
    }
}