/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;

import AnalyzersAndFilters.InitialFileAnalyzer;
import Constants.StaticVariables;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
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
 * ----------------------------Last------------------
 *
 * @author grey
 */
public class Threaded {

    /**
     * @param args the command line arguments
     */
    static float threshold = (float) 6.0;
    static String indexAveragePath = StaticVariables.indexAveragePath;
    static StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
    static IndexReader simiIndexReader;
    static String initialIndexPath;
    static IndexReader initialIndexReader;
    static String simiIndexPath;
    static InitialFileAnalyzer initFileAnalyzer = new InitialFileAnalyzer();
    static List<String> relevantWords = new ArrayList<>();
    static int dherai = 1;

    public static void main(String[] args) throws IOException, Exception {
        // TODO code application logic here
        simiIndexPath = StaticVariables.simiIndexPath;
        simiIndexReader = DirectoryReader.open(FSDirectory.open(new File(simiIndexPath)));
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
//        String query = "  java nokia  ";
        System.out.print("Enter query: \t");
        System.out.println();
        Scanner input = new Scanner(System.in);
        String query = null;

        if ((query = input.nextLine()) == null) {
            System.out.println("Query Not found!");
            System.exit(3);
        }
        List<String> queryWords = analyzeQuery(query);

//        String[] queryArray = query.replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
//        List<String> queryWords = new ArrayList<String>();
//queryWords contain the list of input words
        wordsList = queryWords;

        for (String s : wordsList) {
            System.out.println("**********" + s
                    + "********");
        }
//        vectorReader(queryWords);
        Threaded mainThread = new Threaded();
        mainThread.startThread(queryWords);
        
   /*     
        System.out.println("-----------------------Relevant Words---------------------");
        for (String s : relevantWords) {
            System.out.print(s + " ");
        }
        while(Thread.activeCount()>3){
            
            System.out.println("therad count Is estimated as: "+Thread.activeCount());
            Thread.sleep(100);
        }
        System.out.println("--------------------------Query Start------------------");
        String text = uniqueWords();

        //printing both words and their respective similar words
        for (int sim = 0; sim < queryWords.size(); sim++) {

            System.out.println("For [" + queryWords.get(sim) + "] :");
            System.out.println("\t\t " + similarWords.get(sim));
        }

        Searching search = new Searching(text);
        search.main();
        System.exit(2);
*/
    }//end main

    
    void startThread(List<String> queryWords){
        for(String s: queryWords){
            SameWordThread eThread = new SameWordThread(s);
            eThread.start();
        }
        
    }
    static List<String> queryList = new ArrayList<>();

class SameWordThread implements Runnable{

        Thread thread;
        String threadName;
        
        public SameWordThread(String name){
            this.threadName = name;
            System.out.println("Creating Thread: "+threadName);
        }
        @Override
        public void run() {
            System.out.println("Running thread: "+threadName);
            try {
                List<String> list = new ArrayList<>();
                list.add(threadName);
                vectorReader(list);
            } catch (IOException ex) {
                Logger.getLogger(Threaded.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   
       public void start(){
           System.out.println("Starting thread "+threadName);
           if(thread == null){
               thread = new Thread(this, threadName);
               thread.start();
           }
       } 
    }
class SimilarityThread implements Runnable{

        Thread thread;
        String threadName;
        
        int docNo;
        String query;
        public SimilarityThread(int docNo, String query){
            this.threadName = query;
            this.docNo = docNo;
            this.query = query;
            System.out.println("Creating Thread: "+threadName);
        }
        @Override
        public void run() {
            System.out.println("Running thread: "+threadName);
            try {
                singleSimilarityReader(docNo,query);
            } catch (IOException ex) {
                Logger.getLogger(Threaded.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   
       public void start(){
           System.out.println("Starting thread "+threadName);
           if(thread == null){
               thread = new Thread(this, threadName);
               thread.start();
           }
       } 
    }
    // queryWords contain the input words
     private void singleVectorReader(String queryWords) throws IOException{
//        for (String s : queryWords) {
//            System.out.println("Its my query: " + s);
//        }

        //----------------------------------------------//
        int docNo = 0;// it is the position of the word
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxDoc = initialIndexReader.maxDoc();
//this list stores the positions of the multiple occurance of query (input) words
     // i.e. if query is apple and apple occurs at 4 more places then the place number of 4 apple words are stored in positionsOfRealtedWords list
        List<Integer> positionsOfRelatedWords = new ArrayList<>();
      
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
          

            for (int w = 0; w < listIF.size(); w++) {
                String myTerm = listIF.get(w);
                //needed
//                System.out.println(
//                        myTerm.toString() + "  >>>--->>    \tdocNo:" + docNo + "\tw:" + w);
//if the input word matches the indexed word
                if (queryWords.equalsIgnoreCase(myTerm)) {//queryWords contains input words
                    System.out.println("------------------------------------------------------------" + myTerm);
                    queryList.add(myTerm);//queryList contains all relevant similar words
                    positionsOfRelatedWords.add(docNo);//it contains the position (address) of each repeated input words in the indexing
                    //i.e. if input querry is apple and apple occurs 4 times in index then it stors all 4 postions of the word apple
                    
                    //apply thread here
                    SimilarityThread st = new SimilarityThread(docNo,myTerm);
                    st.start();
//                    singleSimilarityReader(docNo,myTerm);
                }
                docNo++;
//                System.out.println(myTerm.toString());
            }

            listIF.clear();
        }
//        for (String s : queryWords) {
            similarWords.add(" ");
//        }
            
//        similarityReader(simiIndexReader, positionsOfRelatedWords, queryList);
        for (int jj : positionsOfRelatedWords) {
            System.out.println("-------------------------------------     " + jj);
        }
        System.out.println("For next word");
    }

     //2----------
     private static void SimilarityWithOthers(String myTerm, int docNo) {
        
    }  
     //1-----------
     static private void SameWordsOnDifferentLocation(String queryWord) throws IOException {
            System.out.println("Its my query: " + queryWord);
        int docNo = 0;
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxDoc = initialIndexReader.maxDoc();

        List<Integer> positionsOfRelatedWords = new ArrayList<>();
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
            
            for (int w = 0; w < listIF.size(); w++) {
                String myTerm = listIF.get(w);

                if (queryWord.equalsIgnoreCase(myTerm)) {//queryWords contains input words
                    System.out.println("------------------------------------------------------------"+myTerm);
                    queryList.add(myTerm);//queryList contains all relevant similar words
                    positionsOfRelatedWords.add(docNo);
                    SimilarityWithOthers( myTerm,docNo);
                }
                docNo++;
            }
            listIF.clear();
        }
        
    }
     
     
     
     //it creates a list of input query words occured multiple times in the index and their corresponding postions
     
     static private void vectorReader(List<String> queryWords) throws IOException {
//        CheckMyCache checkCache = null ;

//         new Thread(checkCache).start();
        //------------------------------------------------//
        for (String s : queryWords) {
            System.out.println("Its my query: " + s);
        }

//         IndexWriter averageWriter;
//         Directory simiDir;
//         simiDir = FSDirectory.open(new File(indexAveragePath));
//        IndexWriterConfig simiIwc = new IndexWriterConfig(Version.LUCENE_48, standardAnalyzer);
//        simiIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
//        averageWriter = new IndexWriter(simiDir, simiIwc);
        //----------------------------------------------//
        int docNo = 0;// it is the position of the word
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxDoc = initialIndexReader.maxDoc();
//this list stores the positions of the multiple occurance of query (input) words
     // i.e. if query is apple and apple occurs at 4 more places then the place number of 4 apple words are stored in positionsOfRealtedWords list
        List<Integer> positionsOfRelatedWords = new ArrayList<>();
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
            /*
             * //do the indexing of docNo = 0 two times String myTerm1 =
             * listIF.get(0); Document tempDoc =
             * indexReader(initialIndexReader,maxDoc,i,0,docNo,
             * myTerm1.toString()); averageWriter.addDocument(tempDoc);
             * averageWriter.commit(); //end
             */

            for (int w = 0; w < listIF.size(); w++) {
                String myTerm = listIF.get(w);
                //needed
//                System.out.println(
//                        myTerm.toString() + "  >>>--->>    \tdocNo:" + docNo + "\tw:" + w);
//                Document docWriter = indexReader(initialIndexReader, maxDoc, i, w, docNo,
//                        myTerm.toString());
//                averageWriter.addDocument(docWriter);
//                if(docNo%15 == 0)
//                    averageWriter.commit();
//if the input word matches the indexed word
                if (queryWords.contains(myTerm)) {//queryWords contains input words
                    System.out.println("------------------------------------------------------------" + myTerm);
                    queryList.add(myTerm);//queryList contains all relevant similar words
                    positionsOfRelatedWords.add(docNo);//it contains the position (address) of each repeated input words in the indexing
                    //i.e. if input querry is apple and apple occurs 4 times in index then it stors all 4 postions of the word apple
                }
                docNo++;
//                System.out.println(myTerm.toString());
            }

            listIF.clear();
        }
//        averageWriter.commit();
//        averageWriter.close();

        for (String s : queryWords) {
            similarWords.add(" ");
        }
        similarityReader(simiIndexReader, positionsOfRelatedWords, queryList);
        for (int jj : positionsOfRelatedWords) {
            System.out.println("-------------------------------------     " + jj);
        }
        System.out.println("For next word");
    }
    static List<String> similarWords = new ArrayList<>();
    static List<String> wordsList = new ArrayList<>();

    
    //gets the similarity of all and adds those words to the list if similarity is greater than threshold
    static private void singleSimilarityReader( int docNo, String queriesList) throws IOException {
//        System.out.println("For :" + queriesList.get(docNo) + " ther are following words:");
        int i = docNo;
//            System.out.println("-----------Relevant Nos.-----------------"+i);
//            int w = fieldNo;
        int thorai = dherai;
        for (int priorDoc = 0, nextDocsFieldNo = i; priorDoc < i; priorDoc++) {
//                System.out.println("Inside each documents i.e. single word. Doc no.= "+priorDoc+" fieldNo: "+nextDocsFieldNo);
            if (thorai < 1) {
                break;
            }
            //dont look 5 words that come adjacently before the word
            if (priorDoc < (i - 5)) {
                Document doc = simiIndexReader.document(priorDoc);
                List<IndexableField> listIF = doc.getFields();
                IndexableField inField = listIF.get(nextDocsFieldNo);
                TokenStream strm = inField.tokenStream(standardAnalyzer);
                CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                strm.reset();
                String myTerm = "0.0";
                while ((strm.incrementToken())) {
                    myTerm = term.toString();
                    float similarity = Float.parseFloat(myTerm);
                    if (similarity > (float) threshold) {
//                        retrieveWord(priorDoc);
                        thorai--;
                        retrivingFunction(priorDoc, queriesList, docNo);
                        System.out.println("IIIIIIIIIIhigh similatity " + similarity);
                    }
                }
                strm.end();
                strm.close();
                nextDocsFieldNo--;
            }
//            return Float.parseFloat(myTerm.toString());

        }
        Document doc = simiIndexReader.document(i);
        List<IndexableField> listIF = doc.getFields();
        for (int w = 0; w < listIF.size(); w++) {
            if (thorai < 1) {
                break;
            }
            //dont look 5 words that come adjacently after the word
            if (w > 5) {
                IndexableField inField = listIF.get(w);
                TokenStream strm = inField.tokenStream(standardAnalyzer);
                CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                strm.reset();
                String myTerm = "0.0";
                while (strm.incrementToken()) {
                    myTerm = term.toString();
                    float similarity = Float.parseFloat(myTerm);

                    if (similarity > (float) threshold) {
//                        retrieveWord( i + w );
                        thorai--;
                        retrivingFunction(i + w, queriesList, docNo);
                        System.out.println("myyyyyyyy high similatity " + similarity);
                    }
                }
                strm.end();
                strm.close();
            }
        }
        System.out.println("For Next word");
    }

    static private void similarityReader(IndexReader reader, List<Integer> docNos, List<String> queriesList) throws IOException {

//        StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);

//        int maxDoc = reader.maxDoc();
        for (int docNo = 0; docNo < docNos.size(); docNo++) {
            System.out.println("For :" + queriesList.get(docNo) + " ther are following words:");
            int i = docNos.get(docNo);
//            System.out.println("-----------Relevant Nos.-----------------"+i);
//            int w = fieldNo;
            int thorai = dherai;
            for (int priorDoc = 0, nextDocsFieldNo = i; priorDoc < i; priorDoc++) {
//                System.out.println("Inside each documents i.e. single word. Doc no.= "+priorDoc+" fieldNo: "+nextDocsFieldNo);
                if (thorai < 1) {
                    break;
                }
                //dont look 5 words that come adjacently before the word
                if (priorDoc < (i - 5)) {
                    Document doc = reader.document(priorDoc);
                    List<IndexableField> listIF = doc.getFields();
                    IndexableField inField = listIF.get(nextDocsFieldNo);
                    TokenStream strm = inField.tokenStream(standardAnalyzer);
                    CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                    strm.reset();
                    String myTerm = "0.0";
                    while ((strm.incrementToken())) {
                        myTerm = term.toString();
                        float similarity = Float.parseFloat(myTerm);
                        if (similarity > (float) threshold) {
//                        retrieveWord(priorDoc);
                            thorai--;
                            retrivingFunction(priorDoc, queriesList.get(docNo), docNo);
                            System.out.println("IIIIIIIIIIhigh similatity " + similarity);
                        }
                    }
                    strm.end();
                    strm.close();
                    nextDocsFieldNo--;
                }
//            return Float.parseFloat(myTerm.toString());

            }
            Document doc = reader.document(i);
            List<IndexableField> listIF = doc.getFields();
            for (int w = 0; w < listIF.size(); w++) {
                if (thorai < 1) {
                    break;
                }
                //dont look 5 words that come adjacently after the word
                if (w > 5) {
                    IndexableField inField = listIF.get(w);
                    TokenStream strm = inField.tokenStream(standardAnalyzer);
                    CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                    strm.reset();
                    String myTerm = "0.0";
                    while (strm.incrementToken()) {
                        myTerm = term.toString();
                        float similarity = Float.parseFloat(myTerm);

                        if (similarity >= (float) threshold) {
//                        retrieveWord( i + w );
                            thorai--;
                            retrivingFunction(i + w, queriesList.get(docNo), docNo);
                            System.out.println("myyyyyyyy high similatity " + similarity);
                        }
                    }
                    strm.end();
                    strm.close();
                }
            }
            System.out.println("For Next word");
//            thorai--;
        }
        for (String s : relevantWords) {
            System.out.println("My Words are: " + s);
        }

    }

//static List<String> uniqueWords = new ArrayList<String> ();
    private static String uniqueWords() {
        List<String> myWords = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        try {
//        if(relevantWords.get(0) ==null)
//            System.exit(2);

            myWords.add(relevantWords.get(0));

            for (String s : relevantWords) {
                if (!myWords.contains(s)) {
                    myWords.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("No match found!!! \nError: " + e.toString());
        }
        for (String s : myWords) {
            sb.append(s + " ");
        }

        System.out.println("my unique words");
        System.out.println(sb.toString());
        return sb.toString();

    }

    //retrives the words that at the position given by wordNo
    static private void retrivingFunction(int wordNo, String forWord, int myNo) throws IOException {
        int docNo = 0;
//        initialIndexPath = StaticVariables.initialFileIndexPath;
//        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxDoc = initialIndexReader.maxDoc();

//        List<Integer> positionsOfRelatedWords = new ArrayList<>();

        for (int i = 0; i < maxDoc; i++) {
//            List<String> listIF = new ArrayList<String>();
            Document doc = initialIndexReader.document(i);
            IndexableField content = doc.getField("content");
            TokenStream strm = content.tokenStream(initFileAnalyzer);
            CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
            strm.reset();
            while ((strm.incrementToken())) {
                if (docNo == wordNo) {
                    relevantWords.add(term.toString());

                    for (int w = 0; w < wordsList.size(); w++) {
                        if (forWord.equalsIgnoreCase(wordsList.get(w))) {
                            myNo = w;
                            break;
                        }
                    }

                    String similarString = similarWords.get(myNo);

                    //similar string check
                    String[] queryArray = similarString.replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
                    boolean flag = false;
                    for (String s : queryArray) {
                        if (s.equalsIgnoreCase(term.toString()) || wordsList.contains(term.toString())) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        similarString = similarString + " " + term.toString();
                        similarWords.set(myNo, similarString);
                    }
                    //end of similar String check
                    System.out.println("----------------------------------------" + term.toString() + "  --- " + docNo);
                    strm.end();
                    strm.close();
                    return;
                }
                docNo++;
            }
            strm.end();
            strm.close();
        }

    }
//uses analyzer to parse the entered query
    ///example apple is converted to appl to match the indexed word appl

    private static List<String> analyzeQuery(String query) throws IOException {

        TokenStream stream = initFileAnalyzer.tokenStream("field", new StringReader(query));
        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
        List<String> queryList = new ArrayList<>();
        try {
            stream.reset();
            // print all tokens until stream is exhausted

            while (stream.incrementToken()) {
                if (termAtt != null) {
                    System.out.println("*_*" + termAtt.toString());
                    queryList.add(termAtt.toString());
                } else {
                    System.out.println("token empty");
                }
            }
            stream.end();
        } finally {
            stream.close();
        }
        return queryList;
    }
}