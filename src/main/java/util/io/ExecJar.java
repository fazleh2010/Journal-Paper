/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.io;

/**
 *
 * @author elahi
 */
public class ExecJar {

public static void callInterface(String jarLocation, String jarFile) throws Exception
    {
        //Process ps=Runtime.getRuntime().exec(new String[]{"java","-jar","/home/elahi/grammar/hackthon/fork/QueGG-web/target/quegg-web-0.0.1-SNAPSHOT.jar"});
        Process ps=Runtime.getRuntime().exec(new String[]{"java","-jar",jarLocation+jarFile});
        ps.waitFor();
        java.io.InputStream is=ps.getInputStream();
        byte b[]=new byte[is.available()];
        is.read(b,0,b.length);
        //System.out.println(new String(b));
    }
   

}
