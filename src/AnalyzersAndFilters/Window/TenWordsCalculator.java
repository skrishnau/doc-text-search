/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyzersAndFilters.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author grey
 */
public class TenWordsCalculator {

    final static int windowSize = 10;
    public static String text;
    public List<String> getTens() throws IOException {
//        final String text = SimilarityReader1.text;
        
        BufferedReader in;
        StringBuilder sb;
        int tokenNumber = WindowFilter.x;
        sb = new StringBuilder();
        sb.append(text);
        String[] tokenterm = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
List<String> tarray = new ArrayList<String>();
        int i = tokenNumber;
        if(tokenterm!=null){
        String term = tokenterm[i];
        
        //int m = 0;
        for (int j = windowSize / 2; j > 0; j--) {
            if (i - j < 0) {
                tarray.add(" ");
            } else {
                tarray.add(tokenterm[i - j]);

                //write(tokenterm[i-j]);
               // m++;
            }
        }

       // m++;

        for (int j = 1; j <= windowSize / 2; j++) {
            if (i + j > tokenterm.length - 1) {
                tarray.add(" ");
            } else {
                tarray.add(tokenterm[i + j]);
                
               // m++;
            }
        }}
        return tarray;
        
    }
}
