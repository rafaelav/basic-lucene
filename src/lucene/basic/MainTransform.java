package lucene.basic;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.transform.TransformedDirectory;
import org.apache.lucene.store.transform.algorithm.security.DataDecryptor;
import org.apache.lucene.store.transform.algorithm.security.DataEncryptor;
import org.apache.lucene.util.Version;

public class MainTransform {
	public MainTransform() {

	}

	public static void main(String[] args) throws IOException, ParseException, GeneralSecurityException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);

		// Store the index in memory:
		// Directory directory = new RAMDirectory();
		// To store an index on disk, use this instead:
		Directory directory = FSDirectory.open(new File("indexed"));
		byte[] salt = new byte[16];
		String password = "lucenetransform";
		DataEncryptor enc = new DataEncryptor("AES/ECB/PKCS5Padding", password, salt, 128, false);
		DataDecryptor dec = new DataDecryptor(password, salt, false);
		Directory cdir = new TransformedDirectory(directory, enc, dec);

//		if (cdir == null)
//			System.out.println("null");
//		else
//			System.out.println("not null");

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		IndexWriter iwriter = new IndexWriter(cdir, config);

		// making 3 docs for 3 phrases
		Document doc = new Document();
		Document doc2 = new Document();
		Document doc3 = new Document();
		String text = "This is the text to be indexed.";
		String text2 = "My text is here";
		String text3 = "Nothing about the word";
		doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
		doc2.add(new Field("fieldname", text2, TextField.TYPE_STORED));
		doc3.add(new Field("fieldname", text3, TextField.TYPE_STORED));

		// and indexing them
		iwriter.addDocument(doc);
		iwriter.addDocument(doc2);
		iwriter.addDocument(doc3);

		iwriter.close();

		// Now search the index:
		DirectoryReader ireader = DirectoryReader.open(cdir);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser(Version.LUCENE_43, "fieldname", analyzer);
		Query query = parser.parse("text");
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		// assertEquals(1, hits.length);
		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			// prints only the phrases that contain "text"
			System.out.println(hitDoc.get("fieldname"));
			// assertEquals("This is the text to be indexed.",
			// hitDoc.get("fieldname"));
		}
		ireader.close();
		directory.close();
	}
}
