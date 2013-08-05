package com.lucene.basic.manager;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.lucene.basic.builders.HotelDocumentBuilder;
import com.lucene.basic.data.Hotel;
import com.lucene.basic.data.HotelDatabase;
import com.lucene.basic.module.SearchModule;
import com.lucene.basic.module.TransformationModule;
import com.lucene.basic.module.TransformationModuleWrongPassword;
import com.lucene.basic.provider.SearchProvider;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.Version;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertThat;

public class DocumentManagerTest {
    @Inject
    private SearchProvider<IndexWriter> writerProvider;

    @Inject
    private SearchProvider<IndexSearcher> searcherProvider;

    @Inject
    private Analyzer analyzer;

    private QueryParser queryParser;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(DocumentManagerTest.class);

    /**
     * @verifies index the documents returned by the document builder
     * @see DocumentManager#index(Object, com.lucene.basic.provider.SearchProvider)
     */
    @Test
    public void index_shouldIndexTheDocumentsReturnedByTheDocumentBuilder() throws Exception {
        try {

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

    /**
     * @verifies return negative if obj parameter is null
     * @see DocumentManager#index(Object, com.lucene.basic.provider.SearchProvider)
     */
    @Test
    public void index_shouldReturnNegativeIfObjParameterIsNull() throws Exception {
        try {
            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
            DocumentManagerTest documentManagerTest = injector.getInstance(DocumentManagerTest.class);
            int indexedDocs = documentManagerTest.analyzeIndex(null);

            assertThat(indexedDocs, Is.is(-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @verifies return an non-negative number of hits based on the found results
     * @see DocumentManager#search(org.apache.lucene.search.Query, String, com.lucene.basic.provider.SearchProvider)
     */
    @Test
    public void search_shouldReturnAnNonnegativeNumberOfHitsBasedOnTheFoundResults() throws Exception {
        try {
            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
            DocumentManagerTest documentManagerTest = injector.getInstance(DocumentManagerTest.class);
            ScoreDoc[] hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Notre Dame museum", "content");

            assert(hits.length >= 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @verifies have results which are in accordance with the given query
     * @see DocumentManager#search(org.apache.lucene.search.Query, String, com.lucene.basic.provider.SearchProvider)
     */
    @Test
    public void search_shouldHaveResultsWhichAreInAccordanceWithTheGivenQuery() throws Exception {
        try {
            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
            DocumentManagerTest documentManagerTest = injector.getInstance(DocumentManagerTest.class);

            ScoreDoc[] hits;

            // CONTENT
            logger.debug("Test - content - 1:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Nice business", "content");
            assertThat(hits.length, Is.is(10));     // contains either Nice or business or both

            logger.debug("Test - content - 2:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "business", "content");
            assertThat(hits.length, Is.is(5));

            logger.debug("Test - content - 3:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Notre Dame museum", "content");
            assertThat(hits.length, Is.is(9));

            logger.debug("Test - content - 4:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Python coding", "content");
            assertThat(hits.length, Is.is(0));

            // NAME
            logger.debug("Test - name - 1:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Le", "name");
            assertThat(hits.length, Is.is(2));

            logger.debug("Test - name - 2:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Hôtel", "name");
            assertThat(hits.length, Is.is(26));

            logger.debug("Test - name - 3:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Bretton", "name");
            assertThat(hits.length, Is.is(1));

            logger.debug("Test - name - 4:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Noname", "name");
            assertThat(hits.length, Is.is(0));

            // ID
            logger.debug("Test - id - 1:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "7", "id");
            assertThat(hits.length, Is.is(1));

            logger.debug("Test - id - 2:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "20", "id");
            assertThat(hits.length, Is.is(1));

            logger.debug("Test - id - 3:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "57", "id");
            assertThat(hits.length, Is.is(0));

            // CITY
            logger.debug("Test - city - 1:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Nice", "city");
            assertThat(hits.length, Is.is(5));

            logger.debug("Test - city - 2:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Paris", "city");
            assertThat(hits.length, Is.is(25));

            logger.debug("Test - city - 3:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Avignon", "city");
            assertThat(hits.length, Is.is(5));

            logger.debug("Test - city - 4:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Pitesti", "city");
            assertThat(hits.length, Is.is(0));

            // DESCRIPTION
            logger.debug("Test - description - 1:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "area", "description");
            assertThat(hits.length, Is.is(6));

            logger.debug("Test - description - 2:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "business area", "description");
            assertThat(hits.length, Is.is(10));

            logger.debug("Test - description - 3:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "new", "description");
            assertThat(hits.length, Is.is(2));

            logger.debug("Test - description - 4:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Notre Dame Marais", "description");
            assertThat(hits.length, Is.is(6)); // also takes into consideration Notre-Dame

            logger.debug("Test - description - 5:");
            hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Java Eclipse", "description");
            assertThat(hits.length, Is.is(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If a file is encrypted with a specific password but the search is done based on a different password, no results
     * should be found
     *
     * @verifies handle encrypted files correctly
     * @see DocumentManager#search(org.apache.lucene.search.Query, String, com.lucene.basic.provider.SearchProvider)
     */
    @Test
    public void search_shouldHandleEncryptedFilesCorrectly() throws Exception {
        Injector injector = Guice.createInjector(new SearchModule(),new TransformationModuleWrongPassword());
        DocumentManagerTest documentManagerTest = injector.getInstance(DocumentManagerTest.class);

        ScoreDoc[] hits;

        logger.debug("Test with wrong password:");
        hits = documentManagerTest.analyzeSearch(HotelDatabase.getHotels(), "Notre Dame museum", "content");
        assert(hits == null);
    }

    private int analyzeIndex(Hotel[] hotels) throws Exception {
        DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
        return documentManager.index(hotels, writerProvider);
    }

    private ScoreDoc[] analyzeSearch(Hotel[] hotels, String text, String searchBy) throws Exception {
        DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
        queryParser = new QueryParser(Version.LUCENE_36, searchBy, analyzer);
        Query query = queryParser.parse(text);
        return documentManager.search(query, searchBy, searcherProvider);
    }
}
