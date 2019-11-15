/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.porcelli.nio.jgit.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Properties extends HashMap<String, Object> {

    public Properties() {
    }

    public Properties(final Map<String, Object> original) {
        for (Entry<String, Object> e : original.entrySet()) {
            if (e.getValue() != null) {
                put(e.getKey(),
                    e.getValue());
            }
        }
    }

    public Object put(final String key,
                      final Object value) {
        if (value == null) {
            return remove(key);
        }
        return super.put(key,
                         value);
    }

    public void store(final OutputStream out) {
        store(out,
              true);
    }

    public void store(final OutputStream out,
                      boolean closeOnFinish) {
    }

    public void load(final InputStream in) {
        load(in,
             true);
    }

    public void load(final InputStream in,
                     boolean closeOnFinish) {
    }
}
