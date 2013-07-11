package com.lucene.basic.provider;

/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.lucene.basic.objects.Encryption;
import com.lucene.basic.objects.Password;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.transform.TransformedDirectory;
import org.apache.lucene.store.transform.algorithm.security.DataDecryptor;
import org.apache.lucene.store.transform.algorithm.security.DataEncryptor;

import java.io.File;
import java.io.IOException;

public class DirectoryProvider implements SearchProvider<Directory> {

    private final String directory;

    @Inject
    private PasswordProvider passwordProvider;

    @Inject
    private EncryptionProvider encryption;

    // TODO: create a factory to customize the type of directory returned by this provider
    @Inject
    protected DirectoryProvider(final @Named("configuration.lucene.directory") String directory) {
        this.directory = directory;
    }

    @Override
    public Directory get() throws Exception {
        //Directory directory = FSDirectory.open(new File("indexed"));
        Directory directory = FSDirectory.open(new File(this.directory));
        byte[] salt = new byte[16];
        //String password = "lucenetransform";
        System.out.println("Used password with inject - " + passwordProvider.get().getPasswordText());
        System.out.println("Used encryption with inject - " + encryption.get().getEncryptionText());
        DataEncryptor enc = new DataEncryptor(encryption.get().getEncryptionText(), passwordProvider.get().getPasswordText(), salt, 128, false);
        DataDecryptor dec = new DataDecryptor(passwordProvider.get().getPasswordText(), salt, false);
        return new TransformedDirectory(directory, enc, dec);
    }
}
