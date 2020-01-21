/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;

import AnalyzersAndFilters.InitialFileAnalyzer;
import Constants.StaticVariables;
import Interface.Interface;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**---------------------------No need to run this file--------------------
 *------------------------XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-----
 * @author grey
 */
public class Searching {

     static String line = null;
    private static String initialIndexPath;
    private static IndexReader initialIndexReader;

    static Interface displayer;
    
  public Searching(Interface displayer, String line) {
      this.displayer = displayer;
      this.line = line;
  }

  /** Simple command-line based search demo.
     * @param args
     * @throws java.lang.Exception */
  
  public static void main() throws Exception {
      initialIndexPath = StaticVariables.initialFileIndexPath;
//        initialIndexReader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)));
   
//    File averageVector;
//    float threshold = 7;    
//    String index = "F:\\Index";
    String field = Constants.content;
//    String titleField = "title";
    String queries = null;
    int repeat = 0;
    boolean raw = false;
    String queryString = null;
    int hitsPerPage = 10;
    
    
      try (IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(initialIndexPath)))) {
          IndexSearcher searcher = new IndexSearcher(reader);
          //reader.getSumTotalTermFreq(index);
          /*IndexSearcher is to searching what IndexWriter is to indexing
          
          */
           InitialFileAnalyzer analyzer = new InitialFileAnalyzer();
//          Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
          
          BufferedReader in = null;
          if (queries != null) {
              in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), StandardCharsets.UTF_8));
          } else {
              in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
          }
          // :Post-Release-Update-Version.LUCENE_XY:
          QueryParser parser = new QueryParser(Version.LUCENE_48, field, analyzer);
          /*Processes a human-entered (and readable) expression into 
          a concrete Query object
          */
          int quit = 0;
          while (true) {
              if (quit>=2){
                  System.err.println("//----------------------------------------------//");
                  System.out.println("\t\tNo Match Found!!!");
                  System.err.println("//----------------------------------------------//");
                  break;
              }
              if (queries == null && queryString == null) {                        // prompt the user
                  System.out.println("Enter query: ");
              }
              
               //line = queryString != null ? queryString : in.readLine();
              
               //----------------------------------------------------------//
               //--------------------------------------------------------//
               
//               line =generateQuery();
               
               //-----------------------------------------------------------//
            //------------------------------------------------------------------//
              if (line == null || line.length() == -1) {
                  break;
              }
              
              line = line.trim();
              if (line.length() == 0) {
                  break;
              }
              
              Query query = parser.parse(line);
              
              String searchingforText="Searching for: "+query.toString(field);
              
              Searching searching = new Searching();
              searching.renderGui("searchingText",displayer, searchingforText,3);
            
              
              
              System.out.println("Searching for: " + query.toString(field));
              
              if (repeat > 0) {                           // repeat & time as benchmark
                  Date start = new Date();
                  for (int i = 0; i < repeat; i++) {
                      searcher.search(query, null, 100);
                  }
                  Date end = new Date();
                  System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
              }
              
              doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);
              
              if (queryString != null) {
                    break;
                }
                quit++;
            }
        }
    }

    void renderGui(String threadName, Interface displayer, String text, int i) {
        String searchingforText = "Searching for above query....";
        
        GUI gui = new GUI(threadName, displayer, text, i);
        gui.start();
    }

    public class GUI implements Runnable {

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
  /**
  
  private static String generateQuery() throws IOException {
      float threshold = 7;
        File file = new File("AverageVector.txt");
        String s = null;
        int ii = 0;
        
        int tos=0;
        StringBuilder combinedQuery = new StringBuilder();
        
        Scanner input = new Scanner(System.in);
        String line = null;
        
        if ((line = input.nextLine()) != null) {
            System.out.println("-------------------------------------------------------");
           // System.out.println(line);

            Version matchVersion = Version.LUCENE_48;
        Analyzer anal = new StandardAnalyzer(matchVersion);
     //for(String term : tokenizedTerms){
        TokenStream queryStream = anal.tokenStream("field", new StringReader(line.toString()));
        CharTermAttribute token = queryStream.addAttribute(CharTermAttribute.class);

        try {
            queryStream.reset();

            // print all tokens until stream is exhausted
            while (queryStream.incrementToken()) {
                String myTerm = token.toString().trim().toLowerCase();
                    //System.out.println(token.toString()+"  ");
                    combinedQuery.append(myTerm.toString()+"  ");
                BufferedReader outer = new BufferedReader(new FileReader(file));
                
                //-------------------read line by line---------------//
                int toBreak = 0;
                //---------------------------------------------------------------------//
                //------------------outer loop---------------------------//     
                //---------------------------------------------------------//
                while ((s = outer.readLine()) != null) {
//            if(toBreak >= 100)
//                break;
                    StringBuilder outerString = new StringBuilder(s);
                    String outerTerm1 = null, outerTerm2 = null;
//                  System.out.println(s);
                   // String[] tempTerm = outerString.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
                    String[] tempTerm = outerString.toString().split("\\W+");
                    int l = 0;
                    float outerSimilarity = 0;
                    if (l < tempTerm.length) {
                        outerTerm1 = tempTerm[0];
                        l++;
                    }
                    if (l < tempTerm.length) {
                        outerTerm2 = tempTerm[1];
                        l++;
                    }
                    if (l < tempTerm.length) {
                        outerSimilarity = Float.parseFloat(tempTerm[2]);
                        l++;
                    }
//                    for(String t: tempTerm){
//                        System.out.println(t);
//                    }
                    //System.out.println("---------------******************************---------------------------"+token.toString().trim());
                    //System.out.println("%%%%%%%%%%%%%%%%%      "+outerTerm1+"        %%%%%%%%%%%%%%%%%%%%%   "+outerTerm2);
                    
                    boolean flag = true;
                    if(myTerm.equalsIgnoreCase(outerTerm1) && (float)outerSimilarity >= (float)threshold ){
                            combinedQuery.append(outerTerm2 + " ");
                            System.out.println("***********************"+outerTerm2+"   "+outerSimilarity);
                    }else if(myTerm.equalsIgnoreCase(outerTerm2) && (float) outerSimilarity >= (float)threshold){
                            combinedQuery.append( outerTerm1 + " ");
                            System.out.println("*************************"+outerTerm1+"   "+outerSimilarity);
                    }else{
                        flag = false;
                    }
                    //System.out.println();
//                    if(flag == true){
//                        if(!((float)outerSimilarity>=(float)threshold)){
//                            combinedQuery.append(outerTerm2 + " ");
//                        }
//                    }
                
                }//end of while
                System.out.println("-----------------");
                System.out.println(combinedQuery.toString());
                outer.close();
                    //writer.append(termAtt.toString() + " ");
            }
            queryStream.end();
        } finally {
            queryStream.close();
        }
     }
            
      return(combinedQuery.toString());   
    }
*/
  /**

     * @param in
     * @param searcher
     * @param query
     * @param hitsPerPage
     * @param raw
     * @param interactive
     * @throws java.io.IOException */
  public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean raw, boolean interactive) throws IOException {
 
    // Collect enough docs to show 5 pages
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;
    
    int numTotalHits = results.totalHits;
    //----------------------------------------------------------------//
    //GUI rendering
    Searching searching = new Searching();
    searching.renderGui("doPagingSearch",displayer, numTotalHits+"  total matching documents", 4);
    //--------------------------------------------------------------------//
    
    System.out.println(numTotalHits + " total matching documents");
    

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
    while (true) {
      if (end > hits.length) {
        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
        System.out.println("Collect more (y/n) ?");
        String line = in.readLine();
        if (line.length() == 0 || line.charAt(0) == 'n') {
          break;
        }

        hits = searcher.search(query, numTotalHits).scoreDocs;
     }
      
      end = Math.min(hits.length, start + hitsPerPage);
      String searchPaths = "";
      
      for (int i = start; i < end; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          continue;
        }

        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
            searchPaths = searchPaths+""+System.lineSeparator()+""+(i+1)+". "+path;
//            writeToJText(displayer, (i+1) + ". " + path, 2);
          System.out.println((i+1) + ". " + path);
          String title = doc.get("title");
          if (title != null) {
              searchPaths = searchPaths+""+System.lineSeparator()+""+"   Title: " + doc.get("title");
              
//              writeToJText(displayer, "   Title: " + doc.get("title"), 2);
            System.out.println("   Title: " + doc.get("title"));
          }
        } else {
            searchPaths = searchPaths+""+System.lineSeparator()+""+(i+1) + ". " + "No path for this document";
//            writeToJText(displayer, (i+1) + ". " + "No path for this document", 2);
          System.out.println((i+1) + ". " + "No path for this document");
        }
                  
      }
      searching.renderGui("searchPaths", displayer, searchPaths, 2);
      
      if (!interactive || end == 0) {
        break;
     }

      if (numTotalHits >= end) {
        boolean quit = false;
        while (true) {
          System.out.print("Press ");
          if (start - hitsPerPage >= 0) {
              
            System.out.print("(p)revious page, ");  
          }
          if (start + hitsPerPage < numTotalHits) {
            System.out.print("(n)ext page, ");
          }
          System.out.println("(q)uit or enter number to jump to a page.");
          
          String line = in.readLine();
          if (line.length() == 0 || line.charAt(0)=='q') {
            quit = true;
            break;
          }
          if (line.charAt(0) == 'p') {
            start = Math.max(0, start - hitsPerPage);
            break;
          } else if (line.charAt(0) == 'n') {
            if (start + hitsPerPage < numTotalHits) {
              start+=hitsPerPage;
            }
            break;
          } else {
            int page = Integer.parseInt(line);
            if ((page - 1) * hitsPerPage < numTotalHits) {
              start = (page - 1) * hitsPerPage;
              break;
            } else {
              System.out.println("No such page");
            }
          }
        }
        if (quit) break;
        end = Math.min(numTotalHits, start + hitsPerPage);
      }
    }
  }
  
  static void writeToJText(Interface displayer, String string,int jArea){
        
        switch(jArea){
            case 1:
                JTextArea eqta = displayer.expandedQueryTA();
//                eqta.append(System.lineSeparator());
                eqta.append(string);
                break;
            case 2:
                JTextArea srta = displayer.searchResultTA();
                
                srta.append(string);
                srta.append(System.lineSeparator());
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

    private Searching() {
        
    }
}
