/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyzersAndFilters.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.*;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;


/**
 * 3.
 *now we need a TokenFilter that can set this new TenWordsAttribute for each token.
 * "TenWordsFilter"
 * 
 * In this example we show a very naive filter that tags every word with a leading upper-case letter as a 'Noun' and all other words as 'Unknown'. 
 * @author grey
 */
public class WindowFilter extends TokenFilter{
public static int x=0;

    CharTermAttribute termAtt;
    WindowAttribute winAtt;
    OffsetAttribute offsetAtt;
    PositionIncrementAttribute posIncAtt;
    
    String[] tenW =null;
    public WindowFilter(TokenStream tokenStream){
        super(tokenStream);
        winAtt = addAttribute(WindowAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
    }
    @Override
    public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {return false;}
        //determine ten words
      winAtt.setWords(determineTenWords());
      //System.out.println("  inside WindowFilter ---increment token \n");
      return true;
    } 
    // determine the part of speech for the given term
    protected List<String> determineTenWords() throws IOException {
      // naive implementation that tags every uppercased word as noun
        List<String> checkList = new ArrayList();
//        checkList.add("Krish1");checkList.add("ramesh2");checkList.add("dharma3");checkList.add("aakah4");checkList.add("sapana5");
//        checkList.add("Hello 6");checkList.add("world7");checkList.add("tigers8");checkList.add("Softeare9");checkList.add("cup10");
//     TenWords tw = new TenWords();
////        tw.setTenWords(checkList);
//     //System.out.print(" From WindowFilter CLASS ::::>>>  ");
//     for(String te : tw.getTenWords()){
//        // System.out.print(" {"+te+"} ");
//     }
     
     TenWordsCalculator twc =new TenWordsCalculator();
     List<String> getT = twc.getTens();
//     System.out.println("_______________"
//             +x++ + "____*_*_____________");
     x++;
     return getT;
        //return tw.getTenWords();
    // checkList.add(insertThese[x]);
    // x++;
    // return checkList;
    }

}
/*
 * Just like the LengthFilter, 
 * this new filter accesses the attributes it needs in the constructor and stores references in instance variables.
 * Notice how you only need to pass in the interface of the new Attribute and instantiating the correct 
 * class is automatically been taken care of.
 * Now we need to add the filter to the chain: in class "MyAnalyzer "/ 
 */
