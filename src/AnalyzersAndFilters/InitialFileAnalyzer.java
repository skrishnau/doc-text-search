package AnalyzersAndFilters;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import finalwithinterface.Constants;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttributeImpl;
import org.apache.lucene.util.Version;

/**
 *
 * ------------------------- S. No. 1 (a) ------------------
 * 
 * it is an analyzer class which and is responsible for analyzing 
 * initial File entries i.e. file entries at the begining
 */
public class InitialFileAnalyzer extends Analyzer {

    
  //Version matchVersion = Version.LUCENE_48;
 /**
  public static void main(String[] args) throws IOException {
    // text to tokenize
    //final String text = "This is a demo of the new TokenStream API";
   
      String text = "How are you cooking and running? This is my pen. That will perish . What are you planning to do with their car . They are into these things .";
    InitialFileAnalyzer analyzer = new InitialFileAnalyzer();
    //--------------------------//
    TokenStream stream = analyzer.tokenStream("field", new StringReader(text.toLowerCase()));
    
    // get the TermAttribute from the TokenStream
    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
    //TenWordsAttribute twAtt = stream.addAttribute(TenWordsAttribute.class);
    PositionIncrementAttribute pAwt = stream.addAttribute(PositionIncrementAttribute.class);
    OffsetAttribute offAtt = stream.addAttribute(OffsetAttribute.class);
    stream.reset();
    // print all tokens until stream is exhausted
    while (stream.incrementToken()) {
      System.out.println(termAtt.toString()+" :   --->" + pAwt.getPositionIncrement()+
              "\toffset:\t"+offAtt.startOffset()+"-->"+offAtt.endOffset());
    }
    
    stream.end();
    stream.close();
    //--------------------------//
  }
*/
    @Override
    protected TokenStreamComponents createComponents(String string, Reader reader) {
       // final Tokenizer source = new WhitespaceTokenizer(Constants.luceneVersion, reader);
        Tokenizer source = new LowerCaseTokenizer(Constants.luceneVersion, reader);
        //TokenStream result = new WhitespaceTokenizer(Version.LUCENE_48,reader);
        
         TokenStream result = new  LengthFilter(Constants.luceneVersion, source, 2, Integer.MAX_VALUE);
//        result = new TenWordsFilter(result);
        // return new TokenStreamComponents(source, new PorterStemFilter(source));
         //result = new WhitespaceFilter(result);
        result = new StopWordsFilter(result);
        result = new PorterStemFilter(result);
//        result = new  LengthFilter(Constants.luceneVersion, source, 2, Integer.MAX_VALUE);
          return new TokenStreamComponents(source,result);

    }
    
   
}
