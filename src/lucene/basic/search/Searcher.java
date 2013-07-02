package lucene.basic.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	private Directory directory;
	private IndexSearcher isearcher = null;
	private QueryParser parser = null;

	// constructor
	public Searcher() throws IOException {
		// create a new analyzer
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);

		// opening the directory where in indexes are
		directory = FSDirectory.open(new File("indexed"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(ireader);

		// Parse a simple query
		parser = new QueryParser(Version.LUCENE_43, "content", analyzer);
	}

	public ScoreDoc[] performSearch(String queryString) throws IOException, ParseException {
		Query query = parser.parse(queryString);
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		return hits;
//		assertEquals(1, hits.length);
//		// Iterate through the results:
//		for (int i = 0; i < hits.length; i++) {
//			Document hitDoc = isearcher.doc(hits[i].doc);
//			assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
//		}
	}

	public IndexSearcher getIsearcher() {
		return isearcher;
	}
	
}
