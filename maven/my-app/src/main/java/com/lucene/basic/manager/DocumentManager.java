package com.lucene.basic.manager;

import com.lucene.basic.interfaces.DocumentBuilder;
import com.lucene.basic.provider.SearchProvider;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DocumentManager<E> {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(DocumentManager.class);

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
     * @should index the documents returned by the document builder
     * @should return 0 when there are no documents to index
     *
     */
    //TODO: @should return negative if obj parameter is null
    public int index (E obj, SearchProvider<IndexWriter> writerProvider) throws Exception {
        if(obj == null)
            return -1;

        // creates the index writer based on the user's choice for document and password
        IndexWriter iWriter = writerProvider.get();

        // creating the lucene document with the needed information
        List<Document> docs = new ArrayList<Document>();
        docs = documentBuilder.buildDocument(obj);

        if(docs.size() == 0) {
            return 0;
        }

        // indexing the documents
        for(Document doc:docs)
            iWriter.addDocument(doc);

        iWriter.close();

        return docs.size();
    }

    public ScoreDoc[] search (Query query, String fieldName, SearchProvider<IndexSearcher> searcherProvider) throws Exception {
        // creates the index searcher based on the provided reader
        IndexSearcher iSearcher = searcherProvider.get();

        // getting results
        ScoreDoc[] hits = iSearcher.search(query, null, 1000).scoreDocs;

        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = iSearcher.doc(hits[i].doc);

            // prints only the phrases that contain "text"
            logger.debug("Found" + hitDoc.get(fieldName));
        }
        iSearcher.close();

        return hits;
    }
}
