package com.lucene.basic.manager;

import com.google.inject.Inject;
import com.lucene.basic.interfaces.DocumentBuilder;
import com.lucene.basic.provider.SearchProvider;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.util.ArrayList;
import java.util.List;

public class DocumentManager<E> {
    private DocumentBuilder<E> documentBuilder;

//    @Inject
//    private SearchProvider<IndexWriter> writerProvider;
//
//    @Inject
//    private SearchProvider<IndexSearcher> searcherProvider;

    public DocumentManager(DocumentBuilder<E> documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    /**
     * Method aims to index any given information to Lucene friendly format. The indexing is done based on user
     * requirements (encrypted or not, compressed or not, to a chosen folder)
     *
     * @param obj - it depends on what type of information is indexed - based on it the lucene document is created
     */
    public void index (E obj, SearchProvider<IndexWriter> writerProvider) throws Exception {
        // creates the index writer based on the user's choice for document and password
        IndexWriter iWriter = writerProvider.get();

        // creating the lucene document with the needed information
        List<Document> docs = new ArrayList<Document>();
        docs = documentBuilder.buildDocument(obj);

        //TODO: test if empty list of docs

        // indexing the documents
        for(Document doc:docs)
            iWriter.addDocument(doc);

       iWriter.close();
    }

    public void search (Query query, String fieldName, SearchProvider<IndexSearcher> searcherProvider) throws Exception {
        // creates the index searcher based on the provided reader
        IndexSearcher iSearcher = searcherProvider.get();

        // getting results
        ScoreDoc[] hits = iSearcher.search(query, null, 1000).scoreDocs;

        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = iSearcher.doc(hits[i].doc);

            // prints only the phrases that contain "text"
            System.out.println(hitDoc.get(fieldName));
        }
        iSearcher.close();
    }
}
