/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author elahi
 */
public class UrlUtils {
    
    public static String lastSegment(String uri){
        Path path = Paths.get(uri);
        String lastSegment = path.getFileName().toString();
        return lastSegment;
    }
    
}
