/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;


import AnalyzersAndFilters.Window.WindowFilter;
import finalwithinterface.Constants;
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
public class CustomIndexer {

    static int windowSize = 10;

    static public void indexDocs(IndexWriter writer, String text)
            throws IOException {
        // BufferedReader in;
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append(text);
        try {
            // make a new, empty document
            String[] tokenterm = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
            Document doc = new Document();
            FieldType fieldType = new FieldType();
            fieldType.setStoreTermVectors(true);
            fieldType.setIndexed(true);
            fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS);
            fieldType.setStored(true);
            fieldType.setStoreTermVectorPositions(true);
            fieldType.setStoreTermVectorOffsets(true);

            //--------------------------//
            if (tokenterm != null) {
                for (int i = 0; i < tokenterm.length; i++) {
                    //for current token
                    String term = tokenterm[i];

                    List<String> tarray = new ArrayList<String>();
                    int m = 0;
                    for (int j = windowSize / 2; j > 0; j--) {
                        if (i - j < 0) {
                            tarray.add(" ");
                        } else {
                            tarray.add(tokenterm[i - j]);

                            //write(tokenterm[i-j]);
                            m++;
                        }
                    }

//                m++;

                    for (int j = 1; j <= windowSize / 2; j++) {
                        if (i + j > tokenterm.length - 1) {
                            tarray.add(" ");
                        } else {
                            tarray.add(tokenterm[i + j]);
                            //write(tokenterm[i+j]);
                            m++;
                        }
                    }

                    //tarray.add(insertThese[i]);
                    TenWords tw = new TenWords();
                    tw.setTenWords(tarray);
//                tw.setTenWords(tarray);


                    Field field = new TextField(Constants.fieldWindow, term.toString(), Field.Store.YES);

                    System.out.println("---- doc.add()  FOR:  " + term);
                    doc.add(field);
                    for (String t : tw.getTenWords()) {
                        System.out.print("  [ " + t + " ]  ");
                    }
                    System.out.println("-------------->>>" + term + " counter :  " + m);

                    //write(System.lineSeparator());
                }
            }
            System.out.println("Finished!");
            TenWords tenW = null;
            WindowFilter.x = 0;
            System.out.println("Executing: Writer.addDocument()   ");

            writer.addDocument(doc);
            System.out.println("end of : Writer.addDocument()   ");



        } finally {
            // writer.close();
        }
    }
}
