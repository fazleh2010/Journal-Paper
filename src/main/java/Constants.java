
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public interface Constants {

    public static Map<String, String> labels = new TreeMap<String, String>();
    public static Integer numberOfTriples = -1;
    public static String turtleDir = "../resources/en/turtle/";
    public static String outputDir = "../resources/en/entity/";
    public static String propertyDir = "../resources/en/property/";
    public static String SUBJECT = "subject";
    public static String PROPERTY = "property";
    public static String OBJECT = "object";
    public static String allTriple_en = "mappingbased_properties_cleaned_en.ttl";
    public static String entryTriple = "A-entity.ttl";
    public static String language_en = "en";
    public static String labelFile_en = language_en + "_labels.ttl";



}
