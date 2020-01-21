/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grey
 */
public class TenWords {
    static List<String> tarray ;
    //public static String[] tenWords;
    
    
    public void setTenWords(List<String> words){
        this.tarray = words;
    }
    
    
    public List<String> getTenWords(){
        return this.tarray;
    }
    
    
}
