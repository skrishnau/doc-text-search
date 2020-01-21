/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;

import org.apache.lucene.util.Version;

/**
 *
 * @author grey
 */
public class Constants {
    public static String usage = "java org.apache.lucene.demo.IndexFiles"
            + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
            + "This indexes the documents in DOCS_PATH, creating a Lucene index"
            + "in INDEX_PATH that can be searched with Searching";
    
    
    public static boolean createInitialFileIndex = true;
    public static boolean createTenWordsIndex = true;
    public static Version luceneVersion = Version.LUCENE_48;
    public static int windowSize = 10;
    
    public static String content = "content";
    public static String fieldWindow = "windowField";
    public static String fieldSimilarity = "simField";
    
    public static String text = "";
}
