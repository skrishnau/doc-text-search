/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalwithinterface;

import AnalyzersAndFilters.InitialFileAnalyzer;
import Constants.StaticVariables;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * ---------------- S. No. 1 --------------------
 *
 * creates initial File index in folder :
 *                      C:\Users\grey\Documents\NetBeansProjects\CustomIndex\Allindexes 
 *this indexing uses standard analyzer first *this indexing uses custom stopWordsFilter 
 *this indexing uses stemmer but stemmer is not yet decided
 *
 * Then we retrieve the tokens to create 10 words window in FinalIndexer.java
 */
public class Indexer {

    public static void main(String[] args) throws IOException {

        //-------------------check if the directory exists  and is not hidden---------------------//
        final File docDir = new File(StaticVariables.docsPath);
        if (!docDir.canRead() && !docDir.isDirectory()
                && !docDir.isHidden()
                && !docDir.exists()) {
            System.out.println("Document directory '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }
        //------------------------- end of check if the directory..... ------------------------------------------------------------------//
        Date start = new Date();

        //------------------create indexes----------------//
        try {
            Directory dir = FSDirectory.open(new File(StaticVariables.initialFileIndexPath));
            //this analyzer has to be custom analyzer which uses stopwords and stemmer
            //Analyzer analyzer = new StandardAnalyzer(Constants.luceneVersion);
            InitialFileAnalyzer analyzer= new InitialFileAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_48, analyzer);

            if (Constants.createInitialFileIndex) {
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            } else {
                // Add new documents to an existing index:
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }
            try (
                    IndexWriter writer = new IndexWriter(dir, iwc)) {

                indexDocs(writer, docDir);
                System.out.println();
            }

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass()
                    + "\n with message: " + e.getMessage());
        }
        
    }

    private static void indexDocs(IndexWriter writer, File file) throws IOException {
        BufferedReader in;
         StringBuilder sb;
        // do not try to index files that cannot be read
        if (file.canRead()) {
            if (file.isDirectory()) {
                String[] files = file.list();

                // an IO error could occur
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        System.out.print(files[i].toString() + "--*_*--");
                        indexDocs(writer, new File(file, files[i]));
                    }
                }
            } else {
                System.out.println("\n\n***");
                FileInputStream fis;
                try {
                    fis = new FileInputStream(file);
                    in = new BufferedReader(new FileReader(file));
                    
                    
                    
                    //-------------------------------//
                    //stopWordsRemoval(file);//------//
                   //--------------------------------//
                     sb = new StringBuilder();
                     String s = null;

                     while ((s = in.readLine()) != null) {
                      sb.append(s);                      
                  }
                     
                     
                } catch (FileNotFoundException fnfe) {

                    return;
                }


                try {
                    // make a new, empty document
                    Document doc = new Document();
                    FieldType fieldType = new FieldType();
                    fieldType.setStoreTermVectors(true);
                    fieldType.setIndexed(true);
                    fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS);
                    fieldType.setStored(true);
                    fieldType.setStoreTermVectorPositions(true);
                    fieldType.setStoreTermVectorOffsets(true);

                    
                    Field text = new TextField("content", sb.toString(),Field.Store.YES);
                    doc.add(text);
                    in.close();
                    
                    Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
                    doc.add(pathField);
                    
                    /**
                    // If that's not the case searching for special characters will fail.
                    Field contentsField = new Field("contents", 
                            new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))
                            ,Field.TermVector.WITH_POSITIONS_OFFSETS
                            );
                    contentsField.fieldType();
                    doc.add(contentsField);
                    */
                    
                    //     Tf_Idf tfidf = new Tf_Idf();
                    //String field=null,term=null;
                    //File file = null;
                    //FileInputStream  fis = new FileInputStream(file);
                    //tfidf.scoreCalculator("contentsField",new Term("path", file.getPath()).toString());



//                    doc.add(new Field("con", content, Store.YES, Index.ANALYZED,
//                    TermVector.WITH_POSITIONS_OFFSETS));

                    if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                        // New index, so we just add the document (no old document can be there):
                        System.out.println();
                        System.out.println("adding " + file);
                        writer.addDocument(doc);
                    } else {
                        // Existing index (an old copy of this document may have been indexed) so 
                        // we use updateDocument instead to replace the old one matching the exact 
                        // path, if present:
                        System.out.println("updating " + file);
                        writer.updateDocument(new Term("path", file.getPath()), doc);
                    }

                } finally {
                    //   Tf_Idf tfidf = new Tf_Idf();
                    //String field=null,term=null;
                    // File file = null;
                    // FileInputStream  fis = new FileInputStream(file);
                    // tfidf.scoreCalculator("contentsField",new Term("path", file.getPath()).toString());
                    fis.close();
                }
            }
        }

        
        
    
    }
}
