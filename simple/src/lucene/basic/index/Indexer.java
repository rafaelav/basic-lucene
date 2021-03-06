package lucene.basic.index;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import lucene.basic.data.Hotel;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.transform.TransformedDirectory;
import org.apache.lucene.store.transform.algorithm.security.DataDecryptor;
import org.apache.lucene.store.transform.algorithm.security.DataEncryptor;
import org.apache.lucene.util.Version;

public class Indexer {

	private IndexWriter iWriter = null;

	// constructor
	public Indexer() {
		super();
	}

	public IndexWriter getIndexWriter() throws IOException, GeneralSecurityException {
		if (iWriter == null) {
			// create a new analyzer
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			// open the needed directory to store the indexes
			Directory directory = FSDirectory.open(new File("indexed"));
			
			// added lucene-transform
			byte[] salt = new byte[16];
			String password = "lucenetransform";
			DataEncryptor enc = new DataEncryptor("AES/ECB/PKCS5Padding", password, salt, 128, false);
			DataDecryptor dec = new DataDecryptor(password, salt, false);
			Directory cdir = new TransformedDirectory(directory, enc, dec);
			// finish lucene-transform

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
			iWriter = new IndexWriter(cdir, config);
		}

		return iWriter;
	}

	public void closeIndexWriter() throws IOException {
		if (iWriter != null) {
			System.out.println(iWriter+" ");
			iWriter.close();
		}
	}

	public void indexData(Hotel hotel) throws IOException, GeneralSecurityException {
		iWriter = getIndexWriter();
		if (iWriter != null) {

			Document doc = new Document();

			// adding needed information
			doc.add(new Field("id", hotel.getId(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("name", hotel.getName(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("city", hotel.getCity(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("description", hotel.getDescription(), Field.Store.YES, Field.Index.ANALYZED));
			String fullSearchableText = hotel.getName() + " " + hotel.getCity() + " "
					+ hotel.getDescription();
			doc.add(new Field("content", fullSearchableText, Field.Store.YES, Field.Index.ANALYZED));

			// indexing document
			iWriter.addDocument(doc);
		}
	}

	public void rebuildIndexes(Hotel[] hotels) throws IOException, GeneralSecurityException {
		getIndexWriter();

		// index all entries
		for (Hotel hotel : hotels) {
			indexData(hotel);
		}

		// closing index writer
		closeIndexWriter();
	}
}
