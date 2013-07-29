package com.lucene.basic.manager;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.lucene.basic.builders.HotelDocumentBuilder;
import com.lucene.basic.data.Hotel;
import com.lucene.basic.data.HotelDatabase;
import com.lucene.basic.module.SearchModule;
import com.lucene.basic.module.TransformationModule;
import com.lucene.basic.provider.SearchProvider;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.hamcrest.core.Is;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class DocumentManagerTest {
    @Inject
    private SearchProvider<IndexWriter> writerProvider;

    @Inject
    private SearchProvider<IndexSearcher> searcherProvider;

    @Inject
    private Analyzer analyzer;

    private QueryParser queryParser;

    /**
     * @verifies index the documents returned by the document builder
     * @see DocumentManager#index(Object, com.lucene.basic.provider.SearchProvider)
     */
    @Test
    public void index_shouldIndexTheDocumentsReturnedByTheDocumentBuilder() throws Exception {
        try {
//            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
//            DocumentManagerTest documentManagerTest = injector.getInstance(DocumentManagerTest.class);
//
//            DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
//            if(writerProvider == null)
//                System.out.println("Writer provider null");
//            int indexedDocs = documentManager.index(HotelDatabase.getHotels(),writerProvider);

//            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
//            MainTransform mainTransform = injector.getInstance(MainTransform.class);
//            int indexedDocs = mainTransform.analyzeIndex(HotelDatabase.getHotels());

            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
            DocumentManagerTest documentManagerTest = injector.getInstance(DocumentManagerTest.class);
            int indexedDocs = documentManagerTest.analyzeIndex(HotelDatabase.getHotels());

            assertThat(indexedDocs, Is.is(HotelDatabase.getHotels().length));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @verifies return 0 when there are no documents to index
     * @see DocumentManager#index(Object, com.lucene.basic.provider.SearchProvider)
     */
    @Test
    public void index_shouldReturn0WhenThereAreNoDocumentsToIndex() throws Exception {
        try {

            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
            DocumentManagerTest documentManagerTest = injector.getInstance(DocumentManagerTest.class);
            int indexedDocs = documentManagerTest.analyzeIndex(new Hotel[0]);

            assertThat(indexedDocs, Is.is(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int analyzeIndex(Hotel[] hotels) throws Exception {
        DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
        return documentManager.index(hotels, writerProvider);
    }

    private ScoreDoc[] analyzeSearch(Hotel[] hotels, String text, String searchIn) throws Exception {
        DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
        Query query = queryParser.parse(text);
        return documentManager.search(query, searchIn, searcherProvider);
    }
}
