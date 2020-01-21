/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyzersAndFilters.Window;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.util.AttributeImpl;

/**
 * 2.
 * Has single variable tw to store part of speech attribute
 * This is a simple Attribute implementation has only a single variable that stores the part-of-speech of a token.
 * @author grey
 * 
 * Now here is the actual class that implements our new Attribute i.e. TenWordsAttribute.
 * Notice that the class has to extend AttributeImpl: 
 */
public class WindowAttributeImpl extends AttributeImpl implements WindowAttribute {

    private List<String> words = new ArrayList();

    
    @Override
    public void setWords(List<String> words) {
        this.words = words;
    }

    @Override
    public List<String> getWords() {
        return this.words;
    }
    
    @Override
    public void clear() {
        this.words = null;
    }

    @Override
    public void copyTo(AttributeImpl ai) {
        ((WindowAttributeImpl) ai).words = words;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof WindowAttributeImpl) {
            return words == ((WindowAttributeImpl) other).words;
        }
        return false;
    }
}

    /*
     * This is a simple Attribute implementation has only a single variable that stores the part-of-speech of a token. 
     * It extends the new AttributeImpl class and therefore implements its abstract methods clear(), copyTo(), equals(), hashCode().
     * 
     * now we need a TokenFilter that can set this new TenWordsAttribute for each token.
     * "TenWordsFilter"
     */
