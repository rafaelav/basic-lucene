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
import com.google.inject.Provider;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class AnalyzerProvider implements Provider<Analyzer> {

    private final Version version;

    // TODO: create a factory that takes a hint of what type of analyzer should be returned here
    // see the example for checked provider
    @Inject
    protected AnalyzerProvider(final Version version) {
        this.version = version;
    }

    @Override
    public Analyzer get() {
        return new StandardAnalyzer(version);
    }
}