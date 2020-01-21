/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexCheckers;

import Constants.StaticVariables;
import finalwithinterface.Constants;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author grey
 */
public class AverageChecker {

    IndexReader reader;
    static String indexAveragePath = StaticVariables.indexAveragePath;
    static StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);

    public static void main(String args[]) throws IOException {
        System.out.println("Similarity Check:::\n");
        AverageChecker avgCheck = new AverageChecker();
        avgCheck.vectorReader();
    }

    private float similarityReader(int docNo, int fieldNo, int tokenNo, IndexReader reader) throws IOException {

        StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);

        int maxDoc = reader.maxDoc();
        int i = docNo;
        int w = fieldNo;

        Document doc = reader.document(i);
        List<IndexableField> listIF = doc.getFields();
        if (listIF == null) {
//                println("Indexable list is empty");
        } else {
//                println("Indexable list is not empty");
        }
        IndexableField inField = listIF.get(w);
        TokenStream strm = inField.tokenStream(standardAnalyzer);
        CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
        String myTerm = "0.0";
        if (strm == null) {
//                    System.out.println("Strm is null");
        } else {
            strm.reset();
            while ((strm.incrementToken())) {
                //$$$ needed below
             /*
                 * System.out.print("\t\t---" + term.toString() + " --- "); //                        
                System.out.println();
                 */
                myTerm = term.toString();
            }
        }
        strm.end();
        strm.close();

        return Float.parseFloat(myTerm.toString());
    }

     private void vectorReader() throws IOException {
        int docNo = 0;
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexAveragePath)));
        int maxDoc = reader.maxDoc();
        Document tempDoc = reader.document(0);
        List<IndexableField> lIF = tempDoc.getFields();
        IndexableField IF = lIF.get(0);
        TokenStream ss = IF.tokenStream(standardAnalyzer);
        CharTermAttribute term1 = ss.addAttribute(CharTermAttribute.class);
        ss.reset();
        ss.incrementToken();
        System.out.println(term1.toString()); 
        ss.end();
        ss.close();
        
        for (int i = 0; i < maxDoc; i++) {
            Document doc = reader.document(i);
            List<IndexableField> listIF = doc.getFields();
            int inc = 0;
            for (int w = 0; w < listIF.size(); w++) {
                IndexableField inField = listIF.get(w);
                TokenStream strm = inField.tokenStream(standardAnalyzer);
                CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                strm.reset();
                
                while ((strm.incrementToken())) {
                    System.out.print("doc NO. : "+i+"\tField No. :"+inc+"\t"
                            + "\t"+  term.toString() + "  \t\tName of Field: "+inField.name());
                    System.out.println();
                    inc++;
                }
                
                strm.end();
                strm.close();

            }
        }
    }
}
