/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.read.questions.UriLabel;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class AddQuote {

    private static Set<String> excludes = new HashSet<String>();

    public static String[] doubleQuote(String[] row) {
        String[] newRow = new String[row.length];
        Integer index = 0;
        for (String string : row) {
            string = doubleQuote(string);
            newRow[index] = string;
            index = index + 1;
        }
        return newRow;
    }

    public static String doubleQuote(String string) {
        return "\"" + string + "\"";
    }

    public static String modifySparql(String sparql) {
        sparql = sparql.stripLeading().trim();
        sparql = sparql.replace("\n", "");
        sparql = sparql.replace(" ", "+");
        sparql = sparql.replace("+", " ");
        return sparql;
    }

    public static String getProperty(String entityDir, String sparqlQueryOrg) {
        String property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
        property = property.replace("http://dbpedia.org/ontology/", "dbo_");
        property = property.replace("http://dbpedia.org/property/", "dbp_");
        return entityDir + property + ".txt";
    }

    public static String getEntity(String entityDir, String bindingType) {
        return entityDir + bindingType + ".txt";
    }

    public static String getProperty(String sparqlQueryOrg) {
        String property = StringUtils.substringBetween(sparqlQueryOrg, "<", ">");
        property = property.replace("http://dbpedia.org/ontology/", "dbo_");
        property = property.replace("http://dbpedia.org/property/", "dbp_");
        return property;
    }

    public static boolean isKbValid(UriLabel uriLabel) {
        if (uriLabel.getLabel() != null)
            ; else {
            return false;
        }

        String kb = uriLabel.getUri().replace("http://dbpedia.org/resource/", "");
        if (excludes.contains(kb)) {
            return false;
        }

        return true;
    }

}
