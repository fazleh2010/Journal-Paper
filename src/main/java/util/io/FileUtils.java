/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import grammar.read.questions.UriLabel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class FileUtils {

    public static void stringToFile(String content, String fileName)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);
        writer.close();

    }

    public static String fileToString(String fileName) {
        InputStream is;String fileAsString=null;
        try {
            is = new FileInputStream(fileName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            fileAsString = sb.toString();
            //System.out.println("Contents : " + fileAsString);
        } catch (Exception ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fileAsString;
    }

    public static List<File> getFiles(String fileDir, String category, String extension) {
        //System.out.println("fileDir:"+fileDir);
        String[] files = new File(fileDir).list();
        List<File> selectedFiles = new ArrayList<File>();
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(extension)) {
                selectedFiles.add(new File(fileDir + fileName));
            }
        }

        return selectedFiles;

    }

   public static List<UriLabel> getUriLabels(File classFile) {
        List<UriLabel> uriLabels = new ArrayList<UriLabel>();
        Set<String> set = new TreeSet<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(classFile));
            //line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    line = line.strip().trim();
                    if (line.contains("=")) {
                        String uri = line.split("=")[0];
                        String label = line.split("=")[1];
                        UriLabel uriLabel = new UriLabel(uri, label);
                        uriLabels.add(uriLabel);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uriLabels;
    }

    /*public static void deleteFiles(String inputDir, String extension) {
        File f = new File(inputDir);
        String[] pathnames = f.list();
        for (String pathname : pathnames) {
            //System.out.println("pathname::"+inputDir + File.separatorChar + pathname);
            String[] files = new File(inputDir + pathname).list();
            for (String file : files) {
                //System.out.println("file::"+file);
               

            }

        }

    }*/
   
    public static LinkedData getLinkedDataConf(File file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, LinkedData.class);
    }

}
