/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator.sentencebuilder;

import util.exceptions.QueGGMissingFactoryClassException;
import util.io.TemplateFeatures;

/**
 *
 * @author elahi
 */
public interface MultilingualBuilder {

    public String getWords(String[] tokens, Integer index, TemplateFeatures templateFeatures) throws QueGGMissingFactoryClassException;

}
