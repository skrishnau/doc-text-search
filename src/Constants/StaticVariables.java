/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

/**
 *
 * @author grey
 */
public class StaticVariables {
    //initial documents path
//    "C:/Users/grey/Documents/FinalWithInterface/_3TenWordsIndex2";
    public static String docsPath = "I:/Lucene/RajIndex/ToBeIndexed";
    
    // first documents indexing path _1InitialIndex
    public static String initialFileIndexPath = "I:/Lucene/RajIndex/_1InitialIndex";
//     public static String initialFileIndexPath = "C:/Users/grey/Documents/NetBeansProjects/CustomIndex/Allindexes/InitialFileIndex";
   
    //indexing of words only that we get after the use of PorterStemmer and StopWordsFilter
    public static String initialFileIndexWOStopWordsPath = 
            "I:/Lucene/RajIndex/_2StopWordsIndex";
   
    // Ten words Indexing Path
    public static String tenWordsIndexPath = "I:/Lucene/RajIndex/_3TenWordsIndex1";
    
    //next Ten words Indexing Path
    public static String tenWordsIndexPathNext = "I:/Lucene/RajIndex/_3TenWordsIndex2";
    
    // Similarity  indexing path: (raw similarity i.e. initial similarity)--NOT average.
    public static String simiIndexPath = "I:/Lucene/RajIndex/_4SimilarityIndex1";
//    public static String simiIndexPath ="C:/Users/grey/Documents/LuceneIndexes/_4SimilarityIndex1";
    //Average Similarity Path
    public static String indexAveragePath = "I:/Lucene/RajIndex/_5AverageSimilarity1";
    
     public static String recentlySearchedWordsPath = "I:/Lucene/RajIndex/_6RecentSearchesStore/RecentSearch.txt";
//    public static String TenWordsIndexPath = "C:/Users/grey/Documents/NetBeansProjects/CustomIndex/Allindexes/TenWordsIndex";
    
}
