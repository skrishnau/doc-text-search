/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;

import AnalyzersAndFilters.InitialFileAnalyzer;
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

/**------------------------------------------Its worthless to run-------------------------------
 * ----------------XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-----
 * 
 *              reads similarity from:                 /Documents/LuceneIndexes/_4SimilarityIndex1"
 *              writes average Similarity to:           /Documents/LuceneIndexes/_5AverageSimilarity3"
 *
 * @author grey
 */
public class AvgFinal2 {
    
    static InitialFileAnalyzer initFileAnalyzer = new InitialFileAnalyzer();
    static StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
//    public static String text = "";
//    static String nextIndexPath = "C:/Users/grey/Documents/LuceneIndexes/_3TenWordsIndex2";
//    static String indexPath = "C:/Users/grey/Documents/LuceneIndexes/_3TenWordsIndex1";
    static IndexReader simiIndexReader;
    static String initialIndexPath;
    static IndexReader initialIndexReader;
    //------------------------------------------------------//
    static String indexAveragePath = StaticVariables.indexAveragePath;
    //-----------------------------------------------------//
//static PostAnalyzer winAnalyzer = new PostAnalyzer();
    static String simiIndexPath ;

    
    static int count = 1;
    static float totalSimilarity = (float) 0.0;
    static float averageSimilarity = (float) 0.0;
    
    static public void main(String[] args) throws IOException {

        simiIndexPath = StaticVariables.simiIndexPath;
        simiIndexReader = DirectoryReader.open(FSDirectory.open(new File(simiIndexPath)));
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        //indexReader();
        vectorReader();
    }

    static private void vectorReader() throws IOException {
//        CheckMyCache checkCache = null ;
        
//         new Thread(checkCache).start();
        //------------------------------------------------//
         IndexWriter averageWriter;
         Directory simiDir;
         simiDir = FSDirectory.open(new File(indexAveragePath));
        IndexWriterConfig simiIwc = new IndexWriterConfig(Version.LUCENE_48, standardAnalyzer);
        simiIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        averageWriter = new IndexWriter(simiDir, simiIwc);
        //----------------------------------------------//
        int docNo = 0;
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxDoc = initialIndexReader.maxDoc();
        
        //test//
        
        //test end//

        for (int i = 0; i < maxDoc; i++) {
            List<String> listIF = new ArrayList<String>();
            Document doc = initialIndexReader.document(i);
            IndexableField content = doc.getField("content");
            TokenStream strm = content.tokenStream(initFileAnalyzer);
            CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
            strm.reset();
            while ((strm.incrementToken())) {
                listIF.add(term.toString());
            }
            strm.end();
            strm.close();
          /* //do the indexing of  docNo = 0 two times
             String myTerm1 = listIF.get(0);
             Document tempDoc = indexReader(initialIndexReader,maxDoc,i,0,docNo,
             myTerm1.toString());
             averageWriter.addDocument(tempDoc);
             averageWriter.commit();
            //end*/
             
            for (int w = 0; w < listIF.size(); w++) {
                String myTerm = listIF.get(w);
//                System.out.println(
//                        myTerm.toString() + "  >>>--->>    \tdocNo:" + docNo + "\tw:" + w);
                Document docWriter = indexReader(initialIndexReader, maxDoc, i, w, docNo,
                        myTerm.toString());
                averageWriter.addDocument(docWriter);
//                if(docNo%15 == 0)
//                    averageWriter.commit();
                docNo++;
            }
            listIF.clear();
        }
        averageWriter.commit();
        averageWriter.close();
    }

    private static Document indexReader(IndexReader reader, int maxDoc, int ii, int ww, int docNo,
            String outerTerm) throws IOException {

        Document docWriter = new Document();
        int fieldNo = 0;

        for (int i = ii; i < maxDoc; i++) {
            Document doc = reader.document(i);
            List<String> listIF = new ArrayList<String>();
            boolean checkFirst5 ;
            int w;
            if (i == ii) {
                w = ww;
                checkFirst5 = true;
            } else {
                w = 0;
                checkFirst5 = false;
            }

            IndexableField content = doc.getField("content");
            TokenStream strm = content.tokenStream(initFileAnalyzer);
            CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
            strm.reset();
            while ((strm.incrementToken())) {
                listIF.add(term.toString());
            }
            strm.end();
            strm.close();
//            int inc = listIF.size();
            for (; w < listIF.size(); w++) {
                
                String myTerm = listIF.get(w);
//                System.out.print(
//                        outerTerm + "\t\t" + myTerm.toString() + "    \tdocNo:" + docNo + "\tw:" + w);
                float similarity = (float)similarityReader(docNo, fieldNo, 0, simiIndexReader);
                
                //$$$ needed below
                
//                 if(inc>listIF.size()-5)
                float averageSim = vectorReader2(reader, maxDoc, i, ii, w, ww, docNo, fieldNo,
                        outerTerm, myTerm, similarity);
                
                System.out.print(outerTerm + "\t\t" + myTerm.toString() + " \t\t" + 
                        "\tSimilarity: " + String.valueOf(similarity)+
                        "\tAverage Similatiy: " +String.valueOf(averageSim)+
                        "\t\t\tfieldNo:" + fieldNo+"\tDoc No. :"+docNo);
                System.out.println();
               
                    Field field = new TextField(Constants.fieldSimilarity, String.valueOf(averageSim).toString(), Field.Store.YES);
                    docWriter.add(field);
                
                fieldNo++;
            }

            listIF.clear();

        }
        return docWriter;
    }

    static private float vectorReader2(IndexReader reader, int maxDoc, int iiNext, int ii, int wwNext, int ww, int docNo, int fieldNo,
            String outerTerm1, String outerTerm2, float outerSimilarity)
            throws IOException {



        int thisDocNo = docNo;
        int thisFieldNo = fieldNo;
        int iiToSend = iiNext;
        int wwToSend = wwNext;
        count = 1;
        totalSimilarity = (float) outerSimilarity;

        for (int i = ii; i < maxDoc; i++) {

            Document doc = reader.document(i);
            List<String> listIF = new ArrayList<>();
            int w;

            if (i == ii) {
                w = ww;
            } else {
                w = 0;
            }
            
            IndexableField content = doc.getField("content");
            TokenStream strm = content.tokenStream(initFileAnalyzer);
            CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
            strm.reset();
            while ((strm.incrementToken())) {
                listIF.add(term.toString());
            }
            strm.end();
            strm.close();
//            int inc=listIF.size();
            for (; w < listIF.size(); w++) {
                String myTerm = listIF.get(w);
                
//                System.out.println(
//                        myTerm.toString() + "  >>>--->>    \tdocNo:" + docNo + "\tw:" + w);

                //need
                //if ww changes we have to pass ww instead of wwNext
                if (i == ii && w == ww) {
                    iiToSend = iiNext;
                    wwToSend = wwNext;
                    thisFieldNo = fieldNo;
                } else {
                    iiToSend = i;
                    wwToSend = w;
                    thisFieldNo = 0;
                }
                if(myTerm.equalsIgnoreCase(outerTerm2)
                    || myTerm.equalsIgnoreCase(outerTerm2) )
                     vectorReader22(reader, maxDoc, iiToSend, wwToSend, thisDocNo, thisFieldNo,
                        outerTerm1, outerTerm2, totalSimilarity, myTerm);
                thisDocNo++;
            }
            listIF.clear();

        }
        averageSimilarity = totalSimilarity / count;
        //$$$ needed below (both)
//        System.out.print("\tAverage Similariity:" + averageSimilarity);
//        System.out.println();
        return averageSimilarity;

        //if count>3 add the words and their corresponding similarity and count  in some list or map to check if the word has already arrived
    }

    private static void vectorReader22(IndexReader reader, int maxDoc, int ii, int ww, int docNo, int fieldNo,
            String outerTerm1, String outerTerm2, float outerSimilarity, String innerTerm1)
            throws IOException {
//        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
        int fieldNo1 = fieldNo;

        float similarity = (float) 0.0;

        for (int i = ii; i < maxDoc; i++) {
            Document doc = reader.document(i);
            List<String> listIF = new ArrayList<>();
            int w;
            if (i == ii) {
                w = ww;
            } else {
                w = 0;
            }
            IndexableField content = doc.getField("content");
            TokenStream strm = content.tokenStream(initFileAnalyzer);
            CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
            strm.reset();
            while ((strm.incrementToken())) {
                listIF.add(term.toString());
            }
            strm.end();
            strm.close();
            for (; w < listIF.size(); w++) {
                String innerTerm2 = listIF.get(w);
                //$$$ needed just below
//                System.out.print("\t\t" + docNo + "," + fieldNo1 + "\t" + innerTerm1 + " , " + innerTerm2.toString() + "--> ");
                similarity = similarityReader(docNo, fieldNo1, 0, simiIndexReader);
//                        System.out.println();
                if ((outerTerm1.equalsIgnoreCase(innerTerm1) && outerTerm2.equalsIgnoreCase(innerTerm2))
                        || (outerTerm2.equalsIgnoreCase(innerTerm1) && outerTerm1.equalsIgnoreCase(innerTerm2))) {
                    count++;
                    totalSimilarity += similarity;
                    //$$$ needed just below
//                    System.out.println("-\t\t-----------similar;" + outerTerm1 + "\t" + outerTerm2 + "\t-------------------");
                }

                fieldNo1++;
            }
            listIF.clear();
        }

    }

    static private float similarityReader(int docNo, int fieldNo, int tokenNo, IndexReader reader) throws IOException {

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
             /*   System.out.print("\t\t---"
                        + term.toString() + "  ---   ");
//                        
                System.out.println();*/
                myTerm = term.toString();
            }
        }
        strm.end();
        strm.close();

        return Float.parseFloat(myTerm.toString());
    }
    /*
   class TwoWords{
       String first;
       String second;

        public TwoWords(String first, String second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public void setSecond(String second) {
            this.second = second;
        }
       
   }
   class SimiAndCount{
       float simi;
       int count;

        public SimiAndCount(float simi, int count) {
            this.simi = simi;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public float getSimi() {
            return simi;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setSimi(float simi) {
            this.simi = simi;
        }
       
   }
   * 
   */
}
