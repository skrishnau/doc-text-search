/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyzersAndFilters.Window;

import java.util.List;
import org.apache.lucene.util.Attribute;

/**
 *  1.
 * interface of the new Attribute
 * First we need to define the interface of the new Attribute:
 */
public interface WindowAttribute extends Attribute{
    
//    public class TenWords{
//        List<String> words;
//        List<Float> tfidf;
//        public void setTenWords(List<String> words){
//            this.words = words;
//        }
//        public List<String> getTenWords(){
//            return this.words;
//        }
//    }
    public void setWords(List<String> words);
    public List<String> getWords();
}
/*
 * Now we also need to write the implementing class.
 * The name of that class is important here: By default, 
 * Lucene checks if there is a class with the name of the Attribute with the postfix 'Impl'.
 * In this example, we would consequently call the implementing class TwoWordsAttributeImpl. 
 */
