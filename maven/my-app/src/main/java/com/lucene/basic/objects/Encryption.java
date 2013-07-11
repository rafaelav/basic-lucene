package com.lucene.basic.objects;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class Encryption {
    private String encryption;

    public Encryption(String encryption) {
        this.encryption = encryption;
    }

    public String getEncryptionText() {
        return this.encryption;
    }
}
