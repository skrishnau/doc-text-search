/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyzersAndFilters;


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
 * ------------------------- S. No. 1 (b) ------------------
 *now we need a TokenFilter that can remove stopWords tokens for each token.
 * "StopWordsFilter"
 * 
 *@author grey
 */
public class StopWordsFilter extends TokenFilter{

    CharTermAttribute termAtt;
    //TenWordsAttribute tw;
    OffsetAttribute offsetAtt;
    public static final String[] stopWords = {
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it","you","its",
    "no", "not", "of", "on", "or", "such",
    "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with", "what"
  };
    List<String> stopWord = new ArrayList<String>();
    
    public StopWordsFilter(TokenStream tokenStream){
        super(tokenStream);
        //tw = addAttribute(TenWordsAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        for(String s: stopWords){
            stopWord.add(s);
        }
    }
    @Override
    public boolean incrementToken() throws IOException {
        //if (!input.incrementToken()) {return false;}
      //tw.setTenWords(determinePOS(termAtt.buffer(), 0, termAtt.length()));
        PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
     // System.out.println("  inside increm  _*_ ent token \n");
      //-------------------
      int extraIncrement = 0;
            while (true) {
                //System.out.println("inside incrementToken");
              boolean hasNext = input.incrementToken();
              if (hasNext) {
                if (stopWord.contains(termAtt.toString())) {
                  extraIncrement++; // filter this word
//                  System.out.println("ExtraIncrement: "+extraIncrement+"\t"+termAtt.toString());
                  continue;
                } 
                if (extraIncrement>0) {
                  posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement()+extraIncrement);
                }
              }
//              System.out.println(termAtt);
              return hasNext;
            }
      //-------------
     // return true;
    } 
   

}

