package com.lucene.basic.provider;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.lucene.basic.objects.Password;

public class PasswordProvider implements SearchProvider<Password> {
    private final String password;

    @Inject
    protected PasswordProvider(final @Named("configuration.lucene.password") String password) {
        this.password = password;
    }

    @Override
    public Password get() throws Exception {
        return new Password(password);
    }
}
