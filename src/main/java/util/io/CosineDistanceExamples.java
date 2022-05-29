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
            {"was ist die amtssprache von surinam?", "was ist die amtssprache von suriname?"},
            // One out of three word similar
            {"was ist die amtssprache von surinam?", "was ist die amtssprache von eritrea?"},
            // Three out of 4 words similar
            {"was ist die amtssprache von surinam?", "was ist die amtssprache von kamerun?"},
            // Completely exact similar
            {"was ist die amtssprache von surinam?", "was ist die amtssprache von china?"},
            // Completely exact similar but different sequence
            {"was ist die amtssprache von surinam?", "was ist die amtssprache von philippinen?"},
            // Different case of same string. Its case sensitive.
            {"was ist die amtssprache von surinam?", "was ist die amtssprache von russland? "},
            // Different case of same string. Its case sensitive.
            {"was ist die amtssprache von surinam?", "was ist die amtssprache von spanien?"}};
      

        for (String[] input : inputStrings) {

         
            double cosineDistance = new CosineDistance().apply(input[0], input[1]);
            double cosineDistancePercentage = Math.round(cosineDistance * 100);
            double cosineSimilarityPercentage = Math.round((1 - cosineDistance) * 100);

            System.out.println(input[0] + "' & '" + input[1] + " "
                    + cosineDistancePercentage + "% dis-similar or " + cosineSimilarityPercentage + "% similar.");
        }
        
          /*String gravityCambridge = "Liste alle Musicals mit Musik von Elton John auf.";
          String gravityNasa = "Liste alle Musicals mit Musik von Elton John auf.";


        // Realistic example to match two documents & find hwo much similar they are
        double cosineDistanceOfGravitDefinitions = new CosineDistance().apply(gravityNasa, gravityCambridge);
        System.out.println( Math.round((1 - cosineDistanceOfGravitDefinitions) * 100) + "% similar.");*/
    }
}
