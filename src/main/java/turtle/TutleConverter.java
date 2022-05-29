/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtle;

import java.util.List;

/**
 *
 * @author elahi
 */
public interface TutleConverter {

    public Boolean getConversionFlag();

    public void setNounPPFrame(String key, List<String[]> rows, String syntacticFrame);

    public void setTransitiveFrame(String key, List<String[]> rows, String syntacticFrame);

    public void setIntransitivePPFrame(String key, List<String[]> rows, String syntacticFrame);

    public void setAdjectiveFrame(String key, List<String[]> rows, String syntacticFrame);
    
    
    public void setAdjectiveGradableFrame(String key, List<String[]> rows, String syntacticFrame);
    
    

    public String getTutleString();

}
