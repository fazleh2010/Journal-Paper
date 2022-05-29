/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class CosineSimilarity {
    /**
037     * Calculates the cosine similarity for two given vectors.
038     *
039     * @param leftVector left vector
040     * @param rightVector right vector
041     * @return cosine similarity between the two vectors
042     */
    public Double cosineSimilarity(final Map<CharSequence, Integer> leftVector,
                                   final Map<CharSequence, Integer> rightVector) {
        if (leftVector == null || rightVector == null) {
            throw new IllegalArgumentException("Vectors must not be null");
        }

        final Set<CharSequence> intersection = getIntersection(leftVector, rightVector);

        final double dotProduct = dot(leftVector, rightVector, intersection);
        double d1 = 0.0d;
        for (final Integer value : leftVector.values()) {
            d1 += Math.pow(value, 2);
        }
        double d2 = 0.0d;
        for (final Integer value : rightVector.values()) {
            d2 += Math.pow(value, 2);
        }
        double cosineSimilarity;
        if (d1 <= 0.0 || d2 <= 0.0) {
            cosineSimilarity = 0.0;
        } else {
            cosineSimilarity = dotProduct / (Math.sqrt(d1) * Math.sqrt(d2));
        }
        return cosineSimilarity;
  }
    
    /**
070     * Returns a set with strings common to the two given maps.
071     *
072     * @param leftVector left vector map
073     * @param rightVector right vector map
074     * @return common strings
075     */
   private Set<CharSequence> getIntersection(final Map<CharSequence, Integer> leftVector,
            final Map<CharSequence, Integer> rightVector) {
        final Set<CharSequence> intersection = new HashSet<>(leftVector.keySet());
        intersection.retainAll(rightVector.keySet());
        return intersection;
    }
    /**
084     * Computes the dot product of two vectors. It ignores remaining elements. It means
085     * that if a vector is longer than other, then a smaller part of it will be used to compute
086     * the dot product.
087     *
088     * @param leftVector left vector
089     * @param rightVector right vector
090     * @param intersection common elements
091     * @return The dot product
092     */
    private double dot(final Map<CharSequence, Integer> leftVector, final Map<CharSequence, Integer> rightVector,
            final Set<CharSequence> intersection) {
        long dotProduct = 0;
        for (final CharSequence key : intersection) {
            dotProduct += leftVector.get(key) * (long) rightVector.get(key);
        }
        return dotProduct;
   }
    
    
}
