/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import org.apache.commons.text.similarity.CosineDistance;

/**
 *
 * @author elahi
 */
public class CosineDistanceExamples {

    private static String gravityCambridge = "the force that makes objects fall toward the earth, or toward some other large object such as a planet or a star";
    private static String gravityNasa = " the force by which a planet or other body draws objects toward its center";

    public static void main(String[] args) {

        String[][] inputStrings = new String[][]{
            // No similarity
            {"Its All Binary", "Java is great"},
            // One out of three word similar
            {"Hi All Its All Binary", "Hi Binary"},
            // Three out of 4 words similar
            {"Its All Binary", "Really Its All Binary"},
            // Completely exact similar
            {"Its All Binary", "Its All Binary"},
            // Completely exact similar but different sequence
            {"Its All Binary", "All Binary Its"},
            // Different case of same string. Its case sensitive.
            {"Its All Binary", "iTS aLL bINARY"}};

        for (String[] input : inputStrings) {

            // How dis-similar words are between both strings.
            double cosineDistance = new CosineDistance().apply(input[0], input[1]);
            double cosineDistancePercentage = Math.round(cosineDistance * 100);
            double cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);

            System.out.println("CosineDistance of '" + input[0] + "' & '" + input[1] + "' | Words in strings are "
                    + cosineDistancePercentage + "% dis-similar or " + cosineSimilarityPercentage + "% similar.");
        }

        // Realistic example to match two documents & find hwo much similar they are
        double cosineDistanceOfGravitDefinitions = new CosineDistance().apply(gravityNasa, gravityCambridge);
        System.out.println("Gravity definitions from Nasa Website & Cambdrige Dictionary are "
                + Math.round((1 - cosineDistanceOfGravitDefinitions) * 100) + "% similar.");
    }
}
