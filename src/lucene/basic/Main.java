package lucene.basic;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import lucene.basic.data.HotelDatabase;
import lucene.basic.index.Indexer;
import lucene.basic.search.Searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;

public class Main {
	public Main() {
	}

	public static void main(String[] args) throws IOException, ParseException, GeneralSecurityException {
			// build a lucene index
			System.out.println("Indexing ...");
			Indexer indexer = new Indexer();
			//indexer.indexData(HotelDatabase.getHotels());
			indexer.rebuildIndexes(HotelDatabase.getHotels());
			System.out.println("Indexing done!");

			// perform search on "Notre Dame museum" and get result
			System.out.println("Performing search...");
			Searcher instance = new Searcher();
			IndexSearcher isearcher = instance.getIsearcher();
			ScoreDoc[] hits = instance.performSearch("Notre Dame museum");

			System.out.println("Results found: " + hits.length);
			
			  for (int i = 0; i < hits.length; i++) {
			      Document hitDoc = isearcher.doc(hits[i].doc);
			      System.out.println(hitDoc.get("id")+" "+hitDoc.get("name")+" "+hitDoc.get("city"));
			   }
	}
}
