/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import org.apache.tika.language.LanguageIdentifier;

/**
 *
 * @author elahi
 */
public class LanguageDetector {
    private static String english="en";

    public static Boolean isEnglish(String text) {
        LanguageIdentifier identifier = new LanguageIdentifier(text);
        String language = identifier.getLanguage();
        if(language.contains(english))
            return true;
        return false;
    }
    
    public static void main (String []args){
       // System.out.println(isEnglish("Angelique Boyer (Spanish pronunciation: [aɲɟʝeˈlik βoˈʝeɾ]), born Angélique Monique-Paulette Boyer Rousseau (French pronunciation: ​[ɑ̃ʒelik mɔnik polɛt bwa.jeʁ ʁuso]), on July 4, 1988, is a French-born Mexican actress. She started her career playing secondary roles in the telenovelas Rebelde, Muchachitas como tú, and Corazón Salvaje. In 2010, she was given her first protagonist role in Teresa. (en)"));
    }

}
