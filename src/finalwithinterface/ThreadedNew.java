/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;

import Interface.Interface;
import AnalyzersAndFilters.InitialFileAnalyzer;
import Constants.StaticVariables;
import Interface.FinalInterface;
import Interface.Interface;
import java.awt.Color;
import java.awt.Event;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
public class ThreadedNew {

    static float threshold = (float) 6.0;
    static int dherai = 1;
    
    static String indexAveragePath = StaticVariables.indexAveragePath;
    static StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Constants.luceneVersion);
    static IndexReader simiIndexReader;
    static String initialIndexPath;
    static IndexReader initialIndexReader;
    static String simiIndexPath;
    static InitialFileAnalyzer initFileAnalyzer = new InitialFileAnalyzer();
    
    static int totalNoOfWords = 0;
//    static List<String> relevantWords = new ArrayList<>();
//    static List<String> similarWords = new ArrayList<>();
//    static List<String> wordsList = new ArrayList<>();
    Map<String, List<String>> AllRelevantWordsMap;
    static List<Thread> threadList;
//    FileReader fileReader ;
    //this 'query' variable is initialized form 'Interface' class
    public static String query;
    Interface displayer;
private ThreadedNew(){
    
}
public  ThreadedNew(Interface displayer){
    this.displayer = displayer;
}
//    FileWriter writer;
//    File recentSearchFile;
    public static void main(String[] args) 
                                        throws IOException, Exception {
        simiIndexPath = StaticVariables.simiIndexPath;
        simiIndexReader = DirectoryReader.open(FSDirectory.open(new File(simiIndexPath)));
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));

        
        //show interface
        Interface displayer = new Interface();
        displayer.setVisible(true);
        JButton button = displayer.searchB();
        
        //it gets input from console
        System.out.print("Enter query: \t");
        System.out.println();

//        String query = null;


//        if ((query = input.nextLine()) == null) {
//            System.out.println("Query Not found!");
//            System.exit(3);
//        }
//        Interface inter = new Interface();
//        inter.show();
//        
        //it initializes the 'query' variable by values from 'args' of this method
        try {
            if (args != null) {
                query = args[0];
            }
        } catch (Exception e) {
            System.out.println("Direct Class");
            Scanner input = new Scanner(System.in);
            if ((query = input.nextLine()) == null) {
                System.out.println("Query Not found!");
                System.exit(3);
            }
        }

        
        List<String> queryWords = analyzeQuery(query);

//        String[] queryArray = query.replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
//        List<String> queryWords = new ArrayList<String>();
//queryWords contain the list of input words
//        wordsList = queryWords;

        for (String s : queryWords) {
            System.out.println("**********" + s
                    + "********");
        }
//        vectorReader(queryWords);

        ThreadedNew mainThread = new ThreadedNew();
        mainThread.startThread(displayer, queryWords);


        /*
         * System.out.println("-----------------------Relevant
         * Words---------------------"); for (String s : relevantWords) {
         * System.out.print(s + " "); } while(Thread.activeCount()>3){
         *
         * System.out.println("therad count Is estimated as:
         * "+Thread.activeCount()); Thread.sleep(100); }
         * System.out.println("--------------------------Query
         * Start------------------");
         *
         * //printing both words and their respective similar words for (int
         * sim = 0; sim < queryWords.size(); sim++) {
         *
         * System.out.println("For [" + queryWords.get(sim) + "] :");
         * System.out.println("\t\t " + similarWords.get(sim)); }
         *
         */

        if (mainThread.areAllThreadsFinished()) {

            System.err.println("--------------------------------------------------"
                    + "------------MY RELEVANT WORDS--------------------------"
                    + "------------------------------------------"
                    + "-------" + mainThread.AllRelevantWordsMap);
            
            mainThread.convertMapToString("gui",displayer, mainThread.AllRelevantWordsMap);
            
            mainThread.runWriteThread();
            
            String text = mainThread.uniqueWords();
            //displayer is the object od Interface class
            //searching the index with all relevant words
            mainThread.searchIndex(displayer, text);
        }

          
//          System.exit(2);

    }//end main
    
   public static void main(String[] args, Interface displayer) 
                                        throws IOException, Exception {
       System.out.println("main with arguments: string and Interface");
        simiIndexPath = StaticVariables.simiIndexPath;
        simiIndexReader = DirectoryReader.open(FSDirectory.open(new File(simiIndexPath)));
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));

        //to clear the textArea
        JTextArea area = displayer.expandedQueryTA();
        JTextArea area1 = displayer.searchResultTA();
        JTextField field = displayer.queryTF();
//        area.setBackground(Color.ORANGE);
        area.setText("");
        area1.setText("");
//        field.setText(" ");
        //end of clearance

        //it gets input from console
        System.out.print("Enter query: \t");
        System.out.println();
        //it initializes the 'query' variable by values from 'args' of this method
        try {
            if (args != null) {
                query = args[0];
            }
        } catch (Exception e) {
            System.out.println("Direct Class");
            Scanner input = new Scanner(System.in);
            if ((query = input.nextLine()) == null) {
                System.out.println("Query Not found!");
                System.exit(3);
            }
        } 
        List<String> queryWords = analyzeQuery(query);
        for (String s : queryWords) {
            System.out.println("**********" + s
                    + "********");
        }
        ThreadedNew mainThread = new ThreadedNew();
        mainThread.startThread(displayer, queryWords);

        if (mainThread.areAllThreadsFinished()) {

            System.err.println("--------------------------------------------------"
                    + "------------MY RELEVANT WORDS--------------------------"
                    + "------------------------------------------"
                    + "-------" + mainThread.AllRelevantWordsMap);
            
            mainThread.convertMapToString("gui",displayer, mainThread.AllRelevantWordsMap);
            
            mainThread.runWriteThread();
            
            String text = mainThread.uniqueWords();
            //displayer is the object od Interface class
            //searching the index with all relevant words
            mainThread.searchIndex(displayer, text);
            
          
        }

    }//end main
    
    private String uniqueWords(){
        Set<String> keys = AllRelevantWordsMap.keySet();
        StringBuilder sb = new StringBuilder();
        for(String key : keys){
               List<String> values = AllRelevantWordsMap.get(key);
               for(String value : values){
                   sb.append(value+" ");
               }     
        }
        return sb.toString();
    }

    void startThread(Interface displayer, List<String> queryWords) {
        //initialize Map
        if (displayer != null) {
            GUI gui = new GUI("clearlabel4", displayer, "Total Documents:", 4);
            gui.start();
            GUI gui1 = new GUI("clearlabel3", displayer, " ", 3);
            gui1.start();
            GUI g = new GUI("totalWords", displayer, "Total Words" + simiIndexReader.numDocs() + "" + System.lineSeparator()+""+System.lineSeparator(), 1);
            g.start();
        }
        int id = 0;
        AllRelevantWordsMap = new LinkedHashMap<>();
        threadList = new ArrayList<>();
        //---------------------------------------//

        //--------------------------------------//
        for (String s : queryWords) {
            //fileThread checks if the query words are already stored in file namedd RecentSearch.txt
            FileThread fThread = new FileThread(s, null);
            fThread.start();

            SameWordThread eThread = new SameWordThread(s, id);
            eThread.start();
            id++;
            fThread.start();
        }
        
        ThreadDeletingThread deletingThread = new ThreadDeletingThread("DeleteFrom_StartThread");
        deletingThread.start();
    }
public void runWriteThread(){
    FileWriterAndSearcher fileWriterAndSearcher = new FileWriterAndSearcher("fileWriter");
    fileWriterAndSearcher.start();
}
    //------------------------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------------//
    void writeToFile (File file){
        FileWriter fileWriter; 
        try {
            fileWriter = new FileWriter(file, true);
//            fileWriter.append(System.lineSeparator());
            Set<String> mySet = AllRelevantWordsMap.keySet();
            for (String set : mySet) {
                if (search(file, set) == null) {
                    
                    List<String> list = AllRelevantWordsMap.get(set);
                    for (String s : list) {
                        fileWriter.append(s + " ");
                    }
                    fileWriter.append(System.lineSeparator());
                }else{
//                    System.err.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
//                            + "Word Found in file "+set+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                }
            }

            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
//----------------------------------------------------------------------------------------------//  
    //-----------------------------------------------------------------------------------//

    private void searchIndex(Interface displayer, String text) {
        SearchThread searchThread = new SearchThread("search",displayer,text);
        searchThread.start();
    }
    
        class FileWriterAndSearcher 
                    implements Runnable {

        Thread thread;
        String threadName;
        File file;
        public FileWriterAndSearcher(String threadName) {
            System.out.println("Creating  FileWriterAndSearcherThread " + this.threadName);
            this.threadName = threadName;
            file = new File(StaticVariables.recentlySearchedWordsPath);
        }

        @Override
        public void run() {
            System.out.println("Running  FileWriterAndSearcherThread " + threadName);
            writeToFile(file);
        }

        public void start() {
            System.out.println("Starting  FileWriterAndSearcherThread " + threadName);
            if (thread == null) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }
    }
    
   //----------------------------------------------------------------------------//
        //----------------------------------------------------------------------------------//
        
    
    class FileThread
                    implements Runnable {

        Thread thread;
        String threadName;
        File recentSearchFile;
        String key;
        List<String> values;
        //key is the single serch query word and values is the list of words that have higher similarity with the key

        public FileThread(String key, List<String> values) {
            System.out.println("Creating  FileWriterThread " + threadName);
            recentSearchFile = new File(StaticVariables.recentlySearchedWordsPath);
            this.threadName = key;
            this.key = key;
            if (values == null) {
                this.values = null;
            } else {
                this.values = values;
            }
        }

        @Override
        public void run() {
            System.out.println("Running  FileWriterThreadThread " + threadName);
            if (values == null) {
                List<String> directSearchWords = search(recentSearchFile, key);
                if (directSearchWords != null) {
                    synchronized (AllRelevantWordsMap) {
                        AllRelevantWordsMap.put(key, directSearchWords);
                        for (Thread thread : threadList) {
                            if (thread.getName().equals(key + "SameWordThread")) {
                                try {
                                    
                                    System.err.println("-----------------------------Thread " + thread.getName()
                                            + "  this thread will be interrupted here .****  "
                                             + " ******************************");
                                    thread.stop();
                                    
                                } catch (Exception e) {
                                    System.err.println("-----------------------------Thread " + thread.getName()
                                            + " thread interrupt Exception .****  "
                                            + e.getMessage() + " ******************************");
                                }

                            }
                        }
                    }
                }else{
//                    synchronized(AllRelevantWordsMap){
//                        AllRelevantWordsMap.put(key, values);
//                    }
                }


            } else {
                this.values = values;
                synchronized (recentSearchFile) {
                    writeToFile(recentSearchFile);
                }
            }
        }

        public void start() {
            System.out.println("Starting  FileWriterThreadThread " + threadName);
            if (thread == null) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }
    }
    class SearchThread 
                    implements Runnable {

        Thread thread;
        String threadName;
        String text;
        Interface displayer;
        public SearchThread(String threadName,Interface displayer, String text) {
            System.out.println("Creating  SearchThread " + threadName);
            this.threadName = threadName;
            this.displayer = displayer;
            this.text = text;
        }

        @Override
        public void run() {
            System.out.println("Running  SearchThread " + threadName);
            Searching search = new Searching(displayer, text); 
            try {
                search.main();
            } catch (Exception ex) {
                Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public void start() {
            System.out.println("Starting  SearchThread " + threadName);
            if (thread == null) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }
    }

    class InterfaceThread 
                    implements Runnable {

        Thread thread;
        String threadName;

        public InterfaceThread(String threadName) {
            System.out.println("Creating  InterfaceThread " + threadName);
            this.threadName = threadName;
        }

        @Override
        public void run() {
            System.out.println("Running  InterfaceThread " + threadName);
            Interface interfaceClass = new Interface();

            interfaceClass.setExpandedText("lroshna");

        }

        public void start() {
            System.out.println("Starting  InterfaceThread " + threadName);
            if (thread == null) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }
    }

    class ThreadDeletingThread implements Runnable {

        Thread thread;
        String threadName;

        public ThreadDeletingThread(String name) {
            this.threadName = name;
            System.out.println("Creating ThreadDeletingThread " + threadName);
        }

        @Override
        public void run() {
            System.out.println("Running ThreadDeletingThread " + threadName);
            areAllThreadsFinished();
        }

        public void start() {
            System.out.println("Starting ThreadDeletingThread " + threadName);
            if (thread == null) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }
    }

    class SameWordThread implements Runnable {

        Thread thread;
        String threadName;
        int id;
//        String name;

        public SameWordThread(String name, int id) {
            this.threadName = name;
//            this.name = name;
            this.id = id;
            System.out.println("Creating SameWordThread: " + threadName + " id = " + id);
        }

        @Override
        public void run() {
            //////////////////////////////////
            
            synchronized (AllRelevantWordsMap) {
                List<String> myList = AllRelevantWordsMap.get(threadName);
                List<String> tempList = (myList == null) ? new ArrayList() : myList;
                if (!tempList.contains(threadName)) {
                    tempList.add(threadName);
                }
                AllRelevantWordsMap.put(threadName, tempList);
                //fileThread checks if the query words are already stored in file namedd RecentSearch.txt
                FileThread fThread = new FileThread(threadName, null);
                fThread.start();
//                InterfaceThread intThread = new InterfaceThread("wordRetrieveFunction");
//                intThread.start();
            }
            System.out.println("Running SameWordThread: " + threadName + " id = " + id);
            try {
//                List<String> list = new ArrayList<>();
//                list.add(threadName);
                SameWordsOnDifferentLocation(threadName);
            } catch (IOException ex) {
                Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void start() {
            System.out.println("Starting SameWordThread " + threadName + " id = " + id);
            if (thread == null) {
                thread = new Thread(this, threadName+"SameWordThread"); //thread = new Thread(this, threadName + "SameWordThread");
                threadList.add(thread);
                thread.start();

            }
        }
    }

    class SimilarityThread implements Runnable {

        Thread thread;
        String threadName;
        //id is the id of thread.. used for debuging the threads only
        int id;
        int docNo;
        String query;

        public SimilarityThread(String query, int docNo, int id) {
            this.threadName = query;
            this.docNo = docNo;
            this.query = query;
            this.id = id;

            System.out.println("Creating SimilarityThread: " + threadName + " id = " + id);
        }

        @Override
        public void run() {
            System.out.println("Running SimilarityThread: " + threadName + " id = " + id);
            try {
                SimilarityWithOthers(docNo, query, id);
            } catch (IOException ex) {
                Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void start() {
            System.out.println("Starting SimilarityThread " + threadName + " id = " + id);
            if (thread == null) {
                thread = new Thread(this, threadName);
                threadList.add(thread);
                thread.start();
            }
        }
    }

    class WordRetrieveThread implements Runnable {

        Thread thread;
        String threadName;
        //id is the id of the thread.. used for debugging only
        int id;
        int docNo;
//        String query;

        public WordRetrieveThread(String query, int docNo, int id) {
            this.threadName = query;
            this.docNo = docNo;

            this.id = id;
            System.out.println("Creating WordRetrieveThread: " + threadName + " id = " + id);
        }

        @Override
        public void run() {
            System.out.println("Running WordRetrieveThread: " + threadName + " id = " + id);
            try {
                wordRetrieveFunction(threadName, docNo);
            } catch (IOException ex) {
                Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void start() {
            System.out.println("Starting WordRetrieveThread " + threadName + " id = " + id);
            if (thread == null) {
                thread = new Thread(this, threadName);
                threadList.add(thread);
                thread.start();
            }
        }
    }

    private void SameWordsOnDifferentLocation(String queryWord) throws IOException {
        System.out.println("Its my query: " + queryWord);
        int docNo = 0;
        initialIndexPath = StaticVariables.initialFileIndexPath;
        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
        int maxDoc = initialIndexReader.maxDoc();
//id is used to denote the id of the thread only . not used for anything else
        int id = 0;

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
                    System.out.println("------------------------------------------------------------" + myTerm);
//                    queryList.add(myTerm);//queryList contains all relevant similar words
//                    positionsOfRelatedWords.add(docNo);
                    SimilarityThread thread = new SimilarityThread(myTerm, docNo, id);
                    thread.start();
                    id++;
//                    SimilarityWithOthers( myTerm,docNo);
                }
                docNo++;
            }
            listIF.clear();
        }
        //Its a  thread to check if all threads have ended
        ThreadDeletingThread deletingThread = new ThreadDeletingThread("DeleteFrom_SameWordsOnDifferentLocation");
        deletingThread.start();
    }

    private void SimilarityWithOthers(int docNo, String myWord, int id) throws IOException {
//        System.out.println("For :" + queriesList.get(docNo) + " ther are following words:");
        int i = docNo;
//        int id =0;
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
//                System.err.println("female reached");
                Document doc = simiIndexReader.document(priorDoc);
                List<IndexableField> listIF = doc.getFields();
                //Exception in thread "world" java.lang.IndexOutOfBoundsException: Index: 13867, Size: 13721
                
                if(nextDocsFieldNo>=simiIndexReader.numDocs()){ break; }
                
                IndexableField inField = listIF.get(nextDocsFieldNo);
                TokenStream strm = inField.tokenStream(standardAnalyzer);
                CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
                strm.reset();
                String myTerm = "0.0";
                while ((strm.incrementToken())) {
                    myTerm = term.toString();
                    float similarity = Float.parseFloat(myTerm);
                    if (similarity >= (float) threshold) {
//                        retrieveWord(priorDoc);
                        thorai--;
//                        retrievingFunction( myWord, priorDoc);

                        WordRetrieveThread thread = new WordRetrieveThread(myWord, priorDoc, id);
                        thread.start();
                        id++;
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

                    if (similarity >= (float) threshold) {
//                        retrieveWord( i + w );
                        thorai--;
//                        retrievingFunction( myWord, i + w);
                        
                        WordRetrieveThread thread = new WordRetrieveThread(myWord, i + w, id);
                        thread.start();
                        id++;
                        System.out.println("myyyyyyyy high similatity " + similarity);
                    }
                }
                strm.end();
                strm.close();
            }
        }
        System.out.println("For Next word");
//        ThreadDeletingThread deletingThread = new ThreadDeletingThread("DeleteFrom_SimilarityWithOthers");
//        deletingThread.start();
    }

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


    //retrives the words that at the position given by wordNo

    private void wordRetrieveFunction(String forWord, int wordNo) throws IOException {
        int docNo = 0;
        int maxDoc = initialIndexReader.maxDoc();

        for (int i = 0; i < maxDoc; i++) {

            Document doc = initialIndexReader.document(i);
            IndexableField content = doc.getField("content");
            TokenStream strm = content.tokenStream(initFileAnalyzer);
            CharTermAttribute term = strm.addAttribute(CharTermAttribute.class);
            strm.reset();
            while ((strm.incrementToken())) {
                if (docNo == wordNo) {
                    //------------------------//

                    // 'forWord' is the main word for which similarity > threshold with  current term
                    synchronized (AllRelevantWordsMap) {
                        List<String> currentList = AllRelevantWordsMap.get(forWord);
                        List<String> tempList = (currentList == null) ? new ArrayList() : currentList;
                        if (!tempList.contains(term.toString())) {
                            tempList.add(term.toString());
                        }
                        AllRelevantWordsMap.put(forWord, tempList);
//                        FileThread fThread = new FileThread(forWord, tempList);
//                        fThread.start();
//                        InterfaceThread intThread = new InterfaceThread("wordRetrieveFunction");
//                        intThread.start();
//                        Interface inter = new Interface();
//                        inter.setExpandedText("krishna");
                    }
                    //-------------------------//
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
//        ThreadDeletingThread deletingThread = new ThreadDeletingThread("DeleteFrom_wordRetrievingFunction");
//        deletingThread.start();
    }
//--------------------------------------------------------------------------------------------//
 //-----------------------------------Search---------------------------------------------------------//
 //--------------------------------------------------------------------------------------------//
    
    public List<String> search(File file, String name) {
        try {
            FileReader fileReader = new FileReader(file);
            boolean flag = false;
            BufferedReader in = new BufferedReader(fileReader);
            String s = "";

            String key = "";
            List<String> myList = new ArrayList<>();
            try {
                while ((s = in.readLine()) != null) {
                    //                    sb.append(toappended);
                    String[] tokenterm = s.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
                    if (tokenterm[0].equals(name)) {
                        key = tokenterm[0];
                        for (int i = 0; i < tokenterm.length; i++) {
                            myList.add(tokenterm[i]);
                        }
                        flag = true;
                        break;
                    }
//                        Map<String,List<String>> justFoundOrAddedResult = new HashMap<String,List<String>>();


                }
                fileReader.close();
                if (flag == false) {
                    return null;
                } else {
                    return myList;
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
//--------------------------------------------------------------------------------------------//
 //--------------------------------------------------------------------------------------------//
 
  /*  
public void writeToFile(File file, String name, List<String> map) {        
//System.err.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
//                    + sb.toString()+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");     
                    FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file,true);
            String s = "";
            for (String stringFromList : map) {
                s = s + " " + stringFromList;
            }
            s = s+ ""+ System.lineSeparator();
            fileWriter.append(s);
                    fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
*/
/*    
    public Map<String, List<String>> writeToFile(File file, String name, List<String> map) {
//        Set<String> names = map.keySet();
//        File file = new File("CustomVector.txt");

        try {
            FileReader fileReader = new FileReader(file);
            boolean flag = false;
            BufferedReader in = new BufferedReader(fileReader);
            int noOfLines = 0;
            try {
                while (in.readLine() != null) {
                    noOfLines++;
                }
            } catch (IOException ex) {
                Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
            }
            StringBuilder sb = new StringBuilder();
            String s = "";
            for (String stringFromList : map) {
                s = s + " " + stringFromList;
            }
            String toappend = "";
            String toappended = "";
            sb.append(name + " " + s);
            
            String key = "";
            List<String> myList = new ArrayList<>();
//            Map<String,List<String>> myMap = new HashMap<>();
            try {

                while ((s = in.readLine()) != null) {
                    if (noOfLines >= 100) {
                        sb.append(System.lineSeparator()+""+toappended);
                    } else {
                        sb.append(System.lineSeparator()+""+toappend);
                    }
                    String[] tokenterm = s.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
                    if (tokenterm[0].equals(name)) {
                        key = tokenterm[0];
                        for (int i = 1; i < tokenterm.length; i++) {
                            myList.add(tokenterm[i]);
                        }
                        flag = true;
                        break;
                    }

                    toappended = toappend;
                    toappend = s;
System.err.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
                    + sb.toString()+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                }
                Map<String, List<String>> justFoundOrAddedResult = new HashMap<String, List<String>>();
                if (flag == false) {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(sb.toString());
                    fileWriter.close();
                    justFoundOrAddedResult.put(name, map);
                    return justFoundOrAddedResult;
                } else {

                    justFoundOrAddedResult.put(key, myList);
                    return justFoundOrAddedResult;
                }
            } catch (IOException ex) {
                Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ThreadedNew.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
*/
    boolean areAllThreadsFinished() {
        synchronized (threadList) {
            while (threadList.size() > 0) {

                for (int i = 0; i < threadList.size(); i++) {
                    Thread thread = threadList.get(i);
                    try {
                        // starting from the first wait for each one to finish.
                        thread.join();

                        if (!thread.isAlive()) {
                            threadList.remove(i);
                            System.out.println(thread.getName() + " ----- is dead and deleted from our List");
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Thread Interrupted before completion.\nError:" + e.toString() + "\nThread:" + thread.getName());
                    }
                    i++;
                }
                System.out.println("Threads still remained to die are:---  " + threadList.size());
                if (threadList.size() > 0) {
                    continue;
                }
            }
            if (threadList.size() <= 0) {
                return true;
            } else {
                return false;
            }
        }
    }

     
    void convertMapToString(String threadName, Interface displayer, Map<String, List<String>> myMap) {
        Set<String> keys = myMap.keySet();
        String overall="";
        for(String key : keys){
            overall = overall +""+key+" :"+System.lineSeparator();
            List<String> values = myMap.get(key);
            
            int i =0;
            for (String s : values) {
                if (i > 12) {
                    overall = overall + ""+System.lineSeparator();
                    i=0;
                } 
                overall = overall+ ""+s+"  ";
                    i ++;
            }
            overall = overall + ""+System.lineSeparator();
            overall = overall + ""+System.lineSeparator();
        }
        GUI gui = new GUI(threadName,displayer,overall,1);
        gui.start();
        
        
    }
     
     
    //convert Map into String
   

    //jArea indicates to which we are going to write our text/output
    //1: expanded query
    //2: search Result

    void writeToJText(Interface displayer, String string,int jArea){
//        FinalInterface displayer = new FinalInterface();
        
        switch(jArea){
            case 1:
                JTextArea eqta = displayer.expandedQueryTA();
//                eqta.append(System.lineSeparator());
                eqta.append(string);
                break;
            case 2:
                JTextArea srta = displayer.searchResultTA();
                srta.append(System.lineSeparator());
                srta.append(string);
                break;
            case 3:
                JLabel sl = displayer.searchingL();
                sl.setText(string);
                break;
            case 4:
                JLabel totalDocs = displayer.totalDocsL();
                totalDocs.setText(string);
                break;
            default:
                
                
        }
    }
    public class GUI implements Runnable{

        Thread thread;
        String threadName;
        File file;
        Interface displayer;
        int whichArea;
        String string;
        
        public GUI(String threadName, Interface displayer, String string, int whichArea) {
            System.out.println("Creating  GUIThread " + this.threadName);
            this.displayer = displayer;
            this.threadName = threadName;
            this.whichArea = whichArea;
            this.string = string;
        }

        @Override
        public void run() {
            System.out.println("Running  GUIThread " + threadName);
            writeToJText(displayer, string, whichArea);
            
        }

        public void start() {
            System.out.println("Starting  GUIThread " + threadName);
            if (thread == null) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }
        
    }
}
