package com.lucene.basic.search;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.transform.TransformedDirectory;
import org.apache.lucene.store.transform.algorithm.security.DataDecryptor;
import org.apache.lucene.store.transform.algorithm.security.DataEncryptor;
import org.apache.lucene.util.Version;

public class Searcher {
	private Directory directory;
	private IndexSearcher isearcher = null;
	private QueryParser parser = null;

	// constructor
	public Searcher() throws IOException, GeneralSecurityException {
		// create a new analyzer
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

		// opening the directory where in indexes are
		directory = FSDirectory.open(new File("indexed"));
		
		// start adding lucene-transform part
		byte[] salt = new byte[16];
		String password = "lucenetransform";
		DataEncryptor enc = new DataEncryptor("AES/ECB/PKCS5Padding", password, salt, 128, false);
		DataDecryptor dec = new DataDecryptor(password, salt, false);
		Directory cdir = new TransformedDirectory(directory, enc, dec);
		// ended adding lucene-transform
		
		IndexReader ireader = IndexReader.open(cdir);
		isearcher = new IndexSearcher(ireader);

		// Parse a simple query
		parser = new QueryParser(Version.LUCENE_36, "content", analyzer);
	}

	public ScoreDoc[] performSearch(String queryString) throws IOException, ParseException {
		Query query = parser.parse(queryString);
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		return hits;
	}

	public IndexSearcher getIsearcher() {
		return isearcher;
	}
	
}
