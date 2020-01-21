/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;



import AnalyzersAndFilters.*;
import AnalyzersAndFilters.Window.TenWordsCalculator;
import AnalyzersAndFilters.Window.WindowAnalyzer;
import AnalyzersAndFilters.Window.WindowAttribute;
import AnalyzersAndFilters.Window.WindowFilter;
import Constants.StaticVariables;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** -------------------------------3----------------------------
 *                      reads:         ten words index from :  _3TenWordsIndex1  AND     _3TenWordsIndex2
 *                      writes:        similarity(initial similarity without average) in _4SimilarityIndex1
 * @author grey
 */
public class TenWordIndexReader {
    private static IndexReader initialIndexReader;
    private static String initialIndexPath;
    static InitialFileAnalyzer InitFileAnalyzer = new InitialFileAnalyzer();
    static StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
//    public static String text = "";
    static String nextIndexPath = StaticVariables.tenWordsIndexPathNext;
    static String indexPath = StaticVariables.tenWordsIndexPath;
    
    static String simiIndexPath = StaticVariables.simiIndexPath;
    
    public static void main(String[] args) throws IOException{
        System.out.println("Class No. 3. TenWordsIndexReader ");
        indexReader();
        System.exit(1);
    }
   
    static private  void indexReader() throws IOException {
        WindowAnalyzer winAnalyzer = new WindowAnalyzer();
        
        Directory simiDir = FSDirectory.open(new File(simiIndexPath));
        IndexWriterConfig simiIwc = new IndexWriterConfig(Version.LUCENE_48,standardAnalyzer);
        simiIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter simiWriter = new IndexWriter(simiDir,simiIwc);
        
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
      /*   //-------------------------------------------witer configuration---------------------------------------------------///
        Directory simiDir1 = FSDirectory.open(new File(simiIndexPath1));
        Directory simiDir2 = FSDirectory.open(new File(simiIndexPath2));
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
        IndexWriterConfig simiIwc1 = new IndexWriterConfig(Version.LUCENE_48,standardAnalyzer);
        IndexWriterConfig simiIwc2 = new IndexWriterConfig(Version.LUCENE_48,standardAnalyzer);
        simiIwc1.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        simiIwc2.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter simiWriter1 = new IndexWriter(simiDir1,simiIwc1);
        IndexWriter simiWriter2 = new IndexWriter(simiDir2,simiIwc2);
        //------------------------------------------------------------------//
        */
//        StandardAnalyzer winAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
        int maxDoc = reader.maxDoc();
        WindowFilter.x = 0;
int docNo =0;
        for (int i = 0; i < maxDoc; i++) {
            Document doc = reader.document(i);
            List<IndexableField> listIF = doc.getFields();
            if (listIF == null) {
//                println("Indexable list is empty");
            } else {
//                println("Indexable list is not empty");
            }
            
            globalTextDefiner(initialIndexReader,i,false);
            for (int w = 0; w < listIF.size(); w++) {
                IndexableField inField = listIF.get(w);
                //Reader ts = inField.readerValue();
//                System.out.println("Doc No. " + i+ " FieldName: "+ inField.name());
                TokenStream strm = inField.tokenStream(winAnalyzer);
                CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                //PositionIncrementAttribute posAtt = strm.addAttribute(PositionIncrementAttribute.class);
                //OffsetAttribute offAtt = strm.addAttribute(OffsetAttribute.class);
                WindowAttribute winAtt = strm.addAttribute(WindowAttribute.class);
                if (strm == null) {
//                    System.out.println("Strm is null");
                } else {
                    strm.reset();
                    int incrementer = 0 ;
                    
                    while ((strm.incrementToken())) {
                        //we cant use globalTextDefiner() here coz it makes WindowFilter.x = 0
                        // this makes the analyzer to retrieve only the first five words
                        globalTextDefiner(initialIndexReader,i,true);
                        //needed
//                        System.out.println(
//                                term.toString() + "  >>>--->>    "+ docNo);
                        List<String> outerList = winAtt.getWords();
                        //needed
//                        for (String s : outerList) {
//                            System.out.print("       " + s);
//                        }
                        System.out.println();
                        
                        //globalChecker(initialIndexReader,i,maxDoc);
                        //-----------------------Check with next----------------------------------//
                        int windowFilterValue = WindowFilter.x;
//                        
                        Document docWriter = indexReader(maxDoc, i, w,incrementer,
                                                outerList,term.toString());
                        simiWriter.addDocument(docWriter);
                        
                        WindowFilter.x= windowFilterValue;
                        globalTextDefiner(initialIndexReader,i,true);
                        //----------------------------------------------------------//
                        
                        incrementer++;
                        docNo++;
                    }
                }
                strm.end();
                strm.close();
            
        }
    }
      simiWriter.commit();  
    simiWriter.close();
//    simiWriter2.close();
    }
       
  
    private static Document indexReader(int maxDoc, int ii, int ww, int incrementer, 
            List<String> outerListTerm, String outerTerm
             ) throws IOException {
        Document docWriter = new Document();
        WindowAnalyzer winAnalyzer = new WindowAnalyzer();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(nextIndexPath)));
        //needed
//        System.out.println(ii+" "+ww+"--------IndexCheck-------"+maxDoc+" "+ii+" "+ww+" \t"+incrementer+" "+outerTerm);
        //test only
        WindowFilter.x --;
//        WindowFilter.x = 0;
        for (int i = ii; i < maxDoc; i++) {
            Document doc = reader.document(i);
            
            List<IndexableField> listIF = doc.getFields();
           
            int w;
            if(i== ii){
                globalTextDefiner(initialIndexReader,i,true);
                w=ww;
            }
            else{
                globalTextDefiner(initialIndexReader,i,false);
                w=0;
            }
            
            
            for (; w < listIF.size(); w++) {
                
                IndexableField inField = listIF.get(w);
                //Reader ts = inField.readerValue();
//                System.out.println("Doc No. " + i+ " Field Name: "+ inField.name());
                TokenStream strm = inField.tokenStream(winAnalyzer);
                CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                //PositionIncrementAttribute posAtt = strm.addAttribute(PositionIncrementAttribute.class);
                //OffsetAttribute offAtt = strm.addAttribute(OffsetAttribute.class);
                WindowAttribute winAtt = strm.addAttribute(WindowAttribute.class);
                if (strm == null) {
                    System.out.println("Strm is null");
                } else {
                    strm.reset();
//                    for(int inc = 0; inc<incrementer; inc++){
//                        strm.incrementToken();
//                    }
                    
                    while ((strm.incrementToken())) {
//                        System.out.print("     **>>>----term 2  "
//                                + term.toString() + "  --->>    ");
                         List<String> innerListTerms  = winAtt.getWords();
//                         System.out.println();
//                        for (String s : innerListTerms) {
//                            System.out.print("       " + s);
//                        }
                        int count = 0;
//                        int m;
                        
                       System.out.println();
                            for(String ss: innerListTerms){
                               if(outerListTerm.contains(ss) && ss!=" ")
//                                   if(outerListTerm.contains(ss))
                                        count++;
                                    
                                
                            }
                            /*---needed
                            System.out.println();
                            System.out.println("-----------InnerList----------------");
                         for(String ss: innerListTerms)
                             System.out.print("["+ss+"]");
                         System.out.println();
                         System.out.println("------------OuterList---------------");
                         for(String sx : outerListTerm)
                             System.out.print(" "+sx);
                         System.out.println();
                         System.out.println("---------------------");
                         * */
                         //needed
//                            System.out.println( "\t\tT2: "+ outerTerm.toString() +"\tT1: "+term.toString()+ " \tSimilarity: "+ count);
                            //----------------------------------------------------------------------------------------------//
                            Field field = new TextField(Constants.fieldSimilarity, String.valueOf(count).toString(),Field.Store.YES);
                            docWriter.add(field);
                            
                            //simIndexer.indexMySimilarity(outerTerm.toString(),term.toString(),count,simiWriter1,simiWriter2,doc1,doc2);
//                            indexMySimilarity(outerTerm.toString(),term.toString(),count,doc1,doc2);
                            //--------------------------------------------------------------------------//
                    
                    }
                }
                strm.end();
                strm.close();
            }
        }
        return docWriter;
    }
   
     static void globalTextDefiner(IndexReader reader, int i,boolean isX) throws IOException{
        Document doc = reader.document(i);
                    IndexableField content = doc.getField("content");
                    TokenStream strm = content.tokenStream(InitFileAnalyzer);
                    CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                    strm.reset();
//                    println("Field Name:   " + content.name() + "\t Doc Name: " + i);
                    StringBuilder stBuild = new StringBuilder();
                    while ((strm.incrementToken())) {
                        //tokensAfterSWs.add(term.toString()+" ");
                        stBuild.append(term.toString() + " ");
                    }
                    TenWordsCalculator.text = stBuild.toString();
                    /*----needed
                    System.out.println(TenWordsCalculator.text);
                    */
//                    System.out.println(text);
//                    CustomIndexer ci = new CustomIndexer();
//                    ci.indexDocs(writer, text);
                    if(!isX)
                        WindowFilter.x=0;
                    strm.end();
                    strm.close();
//                    println("$$$$$$$$$$$$$$$$$$ END OF TOKEN   $$$$$$$$$$$$$$$$$$$$$");
    }
}
