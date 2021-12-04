/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import grammar.datasets.sentencetemplates.TempConstants;
import static grammar.datasets.sentencetemplates.TempConstants.colon;
import static java.lang.System.exit;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

/**
 *
 * @author elahi
 */
public class PronounFinder implements TempConstants {

    private String word = "XX";
    private LexicalEntryUtil lexicalEntryUtil = null;

    public PronounFinder(LexicalEntryUtil lexicalEntryUtil, String attribute, String reference, TemplateFeatures templateFeatures) throws QueGGMissingFactoryClassException {
        this.lexicalEntryUtil = lexicalEntryUtil;
         if(reference!=null)
            this.word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);
                  
             
        /*//if (TemplateFeatures.isPronounCategory(reference) && templateFeatures.isVerbReflexiveFlag()) {
             word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);
        //}
        //else
           word ="XX"; 
         */
             
        /*System.out.println("reference::"+reference);
        System.out.println("TemplateFeatures.isPronounCategory(reference)::"+TemplateFeatures.isPronounCategory(reference));
          System.out.println("templateFeatures.isVerbReflexiveFlag()::"+templateFeatures.isVerbReflexiveFlag());
           System.out.println("word::"+word);
            System.out.println("templateFeatures::"+templateFeatures);*/
        
        
       

        /*try {
            if (reference.contains(colon)) {
                String[] col = reference.split(colon);
                
       
                if (col.length == 3) {
                    
                    if (TemplateFeatures.isPronounCategory(col[0]) && templateFeatures.isVerbReflexiveFlag()) {
                        word = LexicalEntryUtil.getEntryOneAtrributeCheck(this.lexicalEntryUtil, col[0], TempConstants.caseType, col[1], TempConstants.gender, col[2]);
                    } else {
                        word = "XX";
                    }
                }
            } else {
                word = LexicalEntryUtil.getSingle(this.lexicalEntryUtil, reference);
            }
        } catch (NullPointerException e) {
            System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
            System.err.printf("The reference is not in base file!!", reference);
           
        }*/

    }

    public String getWord() {
        return word;
    }
}
