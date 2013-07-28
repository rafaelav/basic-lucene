package com.lucene.basic.interfaces;

import org.apache.lucene.document.Document;

import java.util.List;


public interface DocumentBuilder<E> {
    public List<Document> buildDocument(E obj);
}
