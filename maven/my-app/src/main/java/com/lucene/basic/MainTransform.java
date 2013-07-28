package com.lucene.basic;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.lucene.basic.builders.HotelDocumentBuilder;
import com.lucene.basic.data.Hotel;
import com.lucene.basic.data.HotelDatabase;
import com.lucene.basic.interfaces.DocumentBuilder;
import com.lucene.basic.manager.DocumentManager;
import com.lucene.basic.module.SearchModule;
import com.lucene.basic.module.TransformationModule;
import com.lucene.basic.provider.SearchProvider;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.Version;

public class MainTransform{

    @Inject
    private SearchProvider<IndexWriter> writerProvider;

    @Inject
    private SearchProvider<IndexSearcher> searcherProvider;

    private QueryParser queryParser;

    @Inject
    public MainTransform(final Version version, final Analyzer analyzer) {
       // queryParser = new QueryParser(version, "fieldname", analyzer);
        queryParser = new QueryParser(Version.LUCENE_36, "content", analyzer);
    }

		//IndexWriter iwriter = new IndexWriter(cdir, config);

    public void analyze() throws Exception {
//        IndexWriter iwriter = writerProvider.get();
//
//        // making 3 docs for 3 phrases
//        Document doc = new Document();
//        Document doc2 = new Document();
//        Document doc3 = new Document();
//        String text = "This is the text to be indexed.";
//        String text2 = "My text is here";
//        String text3 = "Nothing about the word";
//        doc.add(new Field("fieldname", text, Field.Store.YES, Field.Index.ANALYZED));
//        doc2.add(new Field("fieldname", text2, Field.Store.YES, Field.Index.ANALYZED));
//        doc3.add(new Field("fieldname", text3, Field.Store.YES, Field.Index.ANALYZED));
//
//        // and indexing them
//        iwriter.addDocument(doc);
//        iwriter.addDocument(doc2);
//        iwriter.addDocument(doc3);
//
//        iwriter.close();
//
//        IndexSearcher isearcher = searcherProvider.get();
//        // Parse a simple query that searches for "text":
//        Query query = queryParser.parse("text");
//        ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
//        // assertEquals(1, hits.length);
//        // Iterate through the results:
//        for (int i = 0; i < hits.length; i++) {
//            Document hitDoc = isearcher.doc(hits[i].doc);
//
//            // prints only the phrases that contain "text"
//            System.out.println(hitDoc.get("fieldname"));
//        }
//        isearcher.close();
        DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
        documentManager.index(HotelDatabase.getHotels(),writerProvider);

        Query query = queryParser.parse("Notre Dame museum");

        documentManager.search(query, "content", searcherProvider);
    }

	public static void main(String[] args) {
        try {
            Injector injector = Guice.createInjector(new SearchModule(),new TransformationModule());
            MainTransform mainTransform = injector.getInstance(MainTransform.class);
            mainTransform.analyze();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
