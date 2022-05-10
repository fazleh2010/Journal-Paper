/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
public class Main implements Constants{
    
    public static void main(String []args){

        //Entity.generateEntities(turtleDir,allTriple_en,propertyDir,language_en,numberOfTriples,PROPERTY);
        QueGG.generateProperty(propertyDir,turtleDir+labelFile_en,numberOfTriples, language_en);
    }
    
}
