package com.lucene.basic.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.throwingproviders.ThrowingProviderBinder;
import com.lucene.basic.provider.AnalyzerProvider;
import com.lucene.basic.provider.DirectoryProvider;
import com.lucene.basic.provider.ReaderProvider;
import com.lucene.basic.provider.SearchProvider;
import com.lucene.basic.provider.SearcherProvider;
import com.lucene.basic.provider.WriterProvider;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.util.Map;

/**
 * TODO: Write brief description about the class here.
 */
public class SearchModule extends AbstractModule {

    /**
     * Configures a {@link com.google.inject.Binder} via the exposed methods.
     */
    @Override
    protected void configure() {

        bind(String.class).annotatedWith(Names.named("configuration.lucene.directory")).toInstance("indexed");

        bind(Version.class).toInstance(Version.LUCENE_36);
        bind(Analyzer.class).toProvider(AnalyzerProvider.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, Directory.class)
                .to(DirectoryProvider.class)
                .in(Singleton.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, IndexReader.class)
                .to(ReaderProvider.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, IndexSearcher.class)
                .to(SearcherProvider.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, IndexWriter.class)
                .to(WriterProvider.class);
    }
}
