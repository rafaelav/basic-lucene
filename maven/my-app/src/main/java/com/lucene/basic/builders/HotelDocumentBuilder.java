package com.lucene.basic.builders;

import com.lucene.basic.data.Hotel;
import com.lucene.basic.data.HotelDatabase;
import com.lucene.basic.interfaces.DocumentBuilder;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.util.ArrayList;
import java.util.List;

public class HotelDocumentBuilder implements DocumentBuilder<Hotel[]>{
    @Override
    public List<Document> buildDocument(Hotel[] hotels) {
        List<Document> docs = new ArrayList<Document>();

        for(int i=0; i<hotels.length; i++){
            Document doc=new Document();

            doc.add(new Field("id", hotels[i].getId(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("name", hotels[i].getName(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("city", hotels[i].getCity(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("description", hotels[i].getDescription(), Field.Store.YES, Field.Index.ANALYZED));
            String fullSearchableText = hotels[i].getName() + " " + hotels[i].getCity() + " "
                    + hotels[i].getDescription();
            doc.add(new Field("content", fullSearchableText, Field.Store.YES, Field.Index.ANALYZED));

            docs.add(doc);
        }

        return docs;
    }
}
