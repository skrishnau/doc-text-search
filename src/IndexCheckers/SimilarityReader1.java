/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexCheckers;


import AnalyzersAndFilters.InitialFileAnalyzer;
import AnalyzersAndFilters.Window.*;
import Constants.StaticVariables;
import finalwithinterface.Constants;
import java.io.File;
import java.io.IOException;
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

/**
 *
 * @author grey
 */
public class SimilarityReader1 {
    
    //---------------------------------------------------//
    private static IndexReader initialIndexReader;
    private static String initialIndexPath;
    static InitialFileAnalyzer InitFileAnalyzer = new InitialFileAnalyzer();
    static StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
    public static String text = "";
    static String nextIndexPath = StaticVariables.tenWordsIndexPathNext;
    static String indexPath = StaticVariables.tenWordsIndexPath;
    static IndexReader simiIndexReader ;
    //------------------------------------------------------//

    static String simiIndexPath = StaticVariables.simiIndexPath;

    static public void main(String[] args) throws IOException{
        simiIndexReader = DirectoryReader.open(FSDirectory.open(new File(simiIndexPath)));
        //indexReader();
        vectorReader();
    }
    
    static private void indexReader() throws IOException {

        StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(simiIndexPath)));
        int maxDoc = reader.maxDoc();
//int docno =0;

        for (int i = 0; i < maxDoc; i++) {
            Document doc = reader.document(i);
            List<IndexableField> listIF = doc.getFields();
            if (listIF == null) {
//                println("Indexable list is empty");
            } else {
//                println("Indexable list is not empty");
            }

            for (int w = 0; w < listIF.size(); w++) {
                IndexableField inField = listIF.get(w);
                TokenStream strm = inField.tokenStream(standardAnalyzer);
                CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                if (strm == null) {
//                    System.out.println("Strm is null");
                } else {
                    strm.reset();

                    while ((strm.incrementToken())) {
                        System.out.print(
                                term.toString() + "  ---   ");
//                        
                        System.out.println();

                    }
                }
                strm.end();
                strm.close();
            }
        }
    }
    
    static private  void vectorReader() throws IOException {
        WindowAnalyzer winAnalyzer = new WindowAnalyzer();
        int docNo = 0;
        
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
        int maxDoc = reader.maxDoc();
        WindowFilter.x = 0;

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
                        System.out.print(
                                term.toString() + "  >>>--->>    "+docNo);
                        List<String> outerList = winAtt.getWords();
                        for (String s : outerList) {
                            System.out.print("       " + s);
                        }
                        System.out.println();
                        
                        //globalChecker(initialIndexReader,i,maxDoc);
                        //-----------------------Check with next----------------------------------//
                        int windowFilterValue = WindowFilter.x;
//                      
                        
                        Document docWriter = indexReader(maxDoc, i, w,incrementer,docNo,
                                                outerList,term.toString());
//                        simiWriter.addDocument(docWriter);
                        docNo ++;
                        WindowFilter.x= windowFilterValue;
                        globalTextDefiner(initialIndexReader,i,true);
                        //----------------------------------------------------------//
                        
                        incrementer++;
                    }
                }
                strm.end();
                strm.close();
            
        }
    }
    }
       
  
    private static Document indexReader(int maxDoc, int ii, int ww, int incrementer, int docNo,
            List<String> outerListTerm, String outerTerm
             ) throws IOException {
        int fieldNo = 0;
        Document docWriter = new Document();
        WindowAnalyzer winAnalyzer = new WindowAnalyzer();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(nextIndexPath)));
        System.out.println("--------IndexCheck-------"+maxDoc+" "+ii+" "+ww+" \t"+incrementer+" "+outerTerm);
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
                                        count++;
                                    
                                
                            }
                            System.out.println();
                            System.out.println("-----------InnerList----------------\t\t"+docNo);
                         for(String ss: innerListTerms)
                             System.out.print("["+ss+"]");
                         System.out.println();
//                         System.out.println("------------OuterList---------------");
                         for(String sx : outerListTerm)
                             System.out.print(" "+sx);
//                         System.out.println();
//                         System.out.println("---------------------");
                            System.out.println( "T2: "+ outerTerm.toString() +"\tT1: "+term.toString()+ " \tSimilarity: "+ count);
                            //----------------------------------------------------------------------------------------------//
                            Field field = new TextField(Constants.fieldSimilarity, String.valueOf(count).toString(),Field.Store.YES);
                            docWriter.add(field);
                            
                            similarityReader(docNo,fieldNo,0,simiIndexReader);
                            fieldNo++;
                    
                    }
                }
                strm.end();
                strm.close();
            }
        }
        return docWriter;
    }
    
    static private void similarityReader(int docNo, int fieldNo, int tokenNo, IndexReader reader) throws IOException {

        StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
       
        int maxDoc = reader.maxDoc();
        int i= docNo;
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
                if (strm == null) {
//                    System.out.println("Strm is null");
                } else {
                    strm.reset();

                    while ((strm.incrementToken())) {
                        System.out.print(
                                term.toString() + "  ---   ");
//                        
                        System.out.println();

                    }
                }
                strm.end();
                strm.close();
            
        
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
                    System.out.println(text);
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


