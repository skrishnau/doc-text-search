/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyzersAndFilters.Window;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.util.Version;

/**
 *
 * @author grey
 */
public class WindowAnalyzer extends Analyzer {
    private static int windowSize=10;

    
  Version matchVersion = Version.LUCENE_48;
  static String indexPath = "\\Windowing";
 

    @Override
    protected TokenStreamComponents createComponents(String string, Reader reader) {
        final Tokenizer source = new WhitespaceTokenizer(Version.LUCENE_48, reader);
        //TokenStream result = new WhitespaceTokenizer(Version.LUCENE_48,reader);
        //TokenStream result = new LengthFilter(Version.LUCENE_48, source, 3, Integer.MAX_VALUE);
        //return new TokenStreamComponents(source, result);
       
        //independent from above
         TokenStream result = new  LengthFilter(matchVersion, source, 1, Integer.MAX_VALUE);
         //result = new StopWordsFilter(result);
        result = new WindowFilter(result);
        
          return new TokenStreamComponents(source,result);

    }
  
   private static void println(String string) {
        System.out.println(string);
    }
   
}
