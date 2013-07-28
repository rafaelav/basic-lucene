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

        for(Hotel hotel:HotelDatabase.getHotels()){
            Document doc=new Document();

            doc.add(new Field("id", hotel.getId(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("name", hotel.getName(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("city", hotel.getCity(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("description", hotel.getDescription(), Field.Store.YES, Field.Index.ANALYZED));
            String fullSearchableText = hotel.getName() + " " + hotel.getCity() + " "
                    + hotel.getDescription();
            doc.add(new Field("content", fullSearchableText, Field.Store.YES, Field.Index.ANALYZED));

            docs.add(doc);
        }

        return docs;
    }
}
