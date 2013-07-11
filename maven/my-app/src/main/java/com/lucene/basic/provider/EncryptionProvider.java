package com.lucene.basic.provider;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.lucene.basic.objects.Encryption;

public class EncryptionProvider implements SearchProvider<Encryption>{
    private final String encryption;

    @Inject
    protected EncryptionProvider(final @Named("configuration.lucene.encryption") String encryption) {
        this.encryption = encryption;
    }

    @Override
    public Encryption get() throws Exception {
        return new Encryption(encryption);
    }
}
