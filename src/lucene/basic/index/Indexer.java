package lucene.basic.index;

import java.io.File;
import java.io.IOException;

import lucene.basic.data.Hotel;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

	private IndexWriter iWriter = null;

	// constructor
	public Indexer() {
		super();
	}

	public IndexWriter getIndexWriter() throws IOException {
		if (iWriter == null) {
			// create a new analyzer
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			// open the needed directory to store the indexes
			Directory directory = FSDirectory.open(new File("indexed"));

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
			iWriter = new IndexWriter(directory, config);
		}

		return iWriter;
	}

	public void closeIndexWriter() throws IOException {
		if (iWriter != null) {
			iWriter.close();
		}
	}

	public void indexData(Hotel hotel) throws IOException {
		iWriter = getIndexWriter();
		if (iWriter != null) {

		//	for (Hotel hotel : hotels) {
				Document doc = new Document();

				// adding needed information
				doc.add(new Field("id", hotel.getId(), TextField.TYPE_STORED));
				doc.add(new Field("name", hotel.getName(), TextField.TYPE_STORED));
				doc.add(new Field("city", hotel.getCity(), TextField.TYPE_STORED));
				doc.add(new Field("description", hotel.getDescription(), TextField.TYPE_STORED));
				String fullSearchableText = hotel.getName() + " " + hotel.getCity() + " "
						+ hotel.getDescription();
				doc.add(new Field("content", fullSearchableText, TextField.TYPE_STORED));

				// indexing document
				iWriter.addDocument(doc);
		//	}

			// closing index
			//closeIndexWriter();
		}
	}
	
	public void rebuildIndexes(Hotel[] hotels) throws IOException {
		getIndexWriter();

		// index all entries
		for (Hotel hotel : hotels) {
			indexData(hotel);
		}

		// closing index writer
		closeIndexWriter();
	}
}
