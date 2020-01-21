/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;

import AnalyzersAndFilters.*;
import AnalyzersAndFilters.Window.TenWordsCalculator;
import AnalyzersAndFilters.Window.WindowAnalyzer;
import Constants.StaticVariables;
import Helpers.CustomIndexer;

import java.util.Date;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**     ------------------------2-------------------------
 *              reads initial files index from:    Constants.initialFileIndexPath 
 *              writes ten words to :           /Documents/LuceneIndexes/_3TenWordsIndex1";
 *                                              /Documents/LuceneIndexes/_3TenWordsIndex2";
 *
 * @author grey
 */
public class FinalIndexer {

    private static String initialIndexPath;
    private static IndexReader initialIndexReader;
//    public static String text = "";
    static InitialFileAnalyzer InitFileAnalyzer = new InitialFileAnalyzer();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

//--------------------------------------------reader configuration---------------------------------------//
        initialIndexPath = StaticVariables.initialFileIndexPath;

        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxInitialDocs = reader.maxDoc();

        String indexPath = StaticVariables.tenWordsIndexPath;
//---------------------------------------------------------------------------------------------------------//   
        boolean create = true;
        

        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + indexPath + "'...");

            Directory nextDir = FSDirectory.open(new File(indexPath));
//            Directory dir = FSDirectory.open(new File(indexPath));


            WindowAnalyzer winAnalyzer = new WindowAnalyzer();
            //StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_48, winAnalyzer);

            if (create) {

                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            } else {
                // Add new documents to an existing index:
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                //simiIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }


            try (
                    IndexWriter writer = new IndexWriter(nextDir, iwc)) {

                //String myText = 
//------------------------------------------------------------------------------------------------//  
//                StandardAnalyzer analyze = new StandardAnalyzer(Constants.luceneVersion);

                for (int i = 0; i < maxInitialDocs; i++) {
                    Document doc = reader.document(i);
                    IndexableField content = doc.getField("content");
                    TokenStream strm = content.tokenStream(InitFileAnalyzer);
                    //TokenStream strm = content.tokenStream(analyze);
                    CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                    strm.reset();
                    System.out.println("--------------------------------Field Name:   " + content.name() + "\t Doc Name: " + i);
                    StringBuilder stBuild = new StringBuilder();
                    while ((strm.incrementToken())) {
                        //tokensAfterSWs.add(term.toString()+" ");
                        stBuild.append(term.toString() + " ");
                    }
                    TenWordsCalculator.text = stBuild.toString();
                    CustomIndexer ci = new CustomIndexer();
                    ci.indexDocs(writer, TenWordsCalculator.text);
                    strm.end();
                    strm.close();
                    System.out.println("$$$$$$$$$$$$$$$$$$ END OF TOKEN   $$$$$$$$$$$$$$$$$$$$$");
                }
                reader.close();
                writer.close();
//-------------------------------------------------------------------------------------------------------//                
                System.out.println();
            }


            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass()
                    + "\n with message: " + e.getMessage());
        }

        //text="";
//        for (Field.Store c : Field.Store.values())
//    System.out.println(c);
        System.out.println("----------------------____________----------------------");
        System.out.println("----------------------||||||||||||----------------------");
        System.out.println("----------------------||||||||||||----------------------");
        System.out.println("----------------------||||||||||||----------------------");
        System.out.println("----------------------||||||||||||----------------------");
        System.out.println("----------------------____________----------------------");
        
        main();
//        indexReader();

    }
    
    
    public static void main() throws IOException {

//--------------------------------------------reader configuration---------------------------------------//
        initialIndexPath = StaticVariables.initialFileIndexPath;
        String nextIndexPath = StaticVariables.tenWordsIndexPathNext;
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        initialIndexReader= DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxInitialDocs = reader.maxDoc();

//---------------------------------------------------------------------------------------------------------//        
      
        boolean create = true;



        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + nextIndexPath + "'...");

            Directory nextDir = FSDirectory.open(new File(nextIndexPath));
            //Directory dir = FSDirectory.open(new File(indexPath));
            WindowAnalyzer winAnalyzer = new WindowAnalyzer();
            //IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_48, winAnalyzer);
            IndexWriterConfig nextIwc = new IndexWriterConfig(Version.LUCENE_48,winAnalyzer);
            if (create) {

              //  iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                nextIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            } else {
                // Add new documents to an existing index:
               // iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                nextIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }


            try (
                    IndexWriter writer = new IndexWriter(nextDir, nextIwc)) {
                StandardAnalyzer analyze = new StandardAnalyzer(Constants.luceneVersion);
                
                for (int i = 0; i < maxInitialDocs; i++) {
                    Document doc = reader.document(i);
                    IndexableField content = doc.getField("content");
                    TokenStream strm = content.tokenStream(InitFileAnalyzer);
                    CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                    strm.reset();
                    System.out.println("--------------------------------Field Name:   " + content.name() + "\t Doc Name: " + i);
                    StringBuilder stBuild = new StringBuilder();
                    while ((strm.incrementToken())) {
                        stBuild.append(term.toString() + " ");
                    }
                    TenWordsCalculator.text = stBuild.toString();
                    CustomIndexer ci = new CustomIndexer();
                    ci.indexDocs(writer,TenWordsCalculator.text);
                    strm.end();
                    strm.close();
                    System.out.println("$$$$$$$$$$$$$$$$$$ END OF TOKEN   $$$$$$$$$$$$$$$$$$$$$");
                }
                reader.close();
                writer.close();
//-------------------------------------------------------------------------------------------------------//                
                System.out.println();
            }


            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass()
                    + "\n with message: " + e.getMessage());
        }
        //text="";
//        for (Field.Store c : Field.Store.values())
//    System.out.println(c);
System.out.println("----------------------____________----------------------");
System.out.println("----------------------||||||||||||----------------------");
System.out.println("----------------------||||||||||||----------------------");
System.out.println("----------------------||||||||||||----------------------");
System.out.println("----------------------||||||||||||----------------------");
System.out.println("----------------------____________----------------------");
       

    }
    
    
}
