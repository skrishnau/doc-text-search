/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import finalwithinterface.*;
import java.io.IOException;

/**
 *
 * @author grey
 */
public class Executer {

    public static void main(String args[]) throws IOException, Exception {

        Indexer indexer = new Indexer();
        indexer.main(args);
        System.out.println();
        System.out.println("Indexer finished ");
        
        FinalIndexer finalTenWords = new FinalIndexer();
        finalTenWords.main(args);
        System.out.println();
        System.out.println("FinalIndexer Finished");
        
        TenWordIndexReader similarityIndexer = new TenWordIndexReader();
        similarityIndexer.main(args);
        System.out.println();
        System.out.println("TenWordsIndexReader finished");
//        AvgFinal2 averageIndexer = new AvgFinal2();
//        averageIndexer.main(args);
        Query3 query = new Query3();
        query.main(args);
        System.out.println();
        System.out.println("Query1 finished");
    }
}
