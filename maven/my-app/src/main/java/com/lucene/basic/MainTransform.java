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

//    @Inject
//    private SearchProvider<IndexWriter> writerProvider;
//
//    @Inject
//    private SearchProvider<IndexSearcher> searcherProvider;
//
//    private QueryParser queryParser;
//
//    @Inject
//    public MainTransform(final Version version, final Analyzer analyzer) {
//        queryParser = new QueryParser(version, "content", analyzer);
//    }

//    public int analyzeIndex(Hotel[] hotels) throws Exception {
//        DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
//        return documentManager.index(hotels, writerProvider);
//    }
//
//    public ScoreDoc[] analyzeSearch(Hotel[] hotels, String text, String searchIn) throws Exception {
//        DocumentManager<Hotel[]> documentManager = new DocumentManager<Hotel[]>(new HotelDocumentBuilder());
//        Query query = queryParser.parse(text);
//        return documentManager.search(query, searchIn, searcherProvider);
//    }
                  //"Notre Dame museum"
}
