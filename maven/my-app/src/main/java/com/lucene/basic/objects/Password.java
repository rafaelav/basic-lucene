package com.lucene.basic.objects;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class Password {
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public String getPasswordText() {
        return this.password;
    }
}
