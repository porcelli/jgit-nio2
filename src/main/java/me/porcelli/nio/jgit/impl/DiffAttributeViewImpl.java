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

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import me.porcelli.nio.jgit.fs.attribute.DiffAttributeView;
import me.porcelli.nio.jgit.fs.attribute.DiffAttributes;

import static me.porcelli.nio.jgit.impl.util.Preconditions.checkNotEmpty;

public abstract class DiffAttributeViewImpl<P extends Path>
        extends AbstractBasicFileAttributeView<P> implements DiffAttributeView {

    public static final String DIFF = "diff";

    public DiffAttributeViewImpl(final P path) {
        super(path);
    }

    @Override
    public String name() {
        return DIFF;
    }

    @Override
    public Map<String, Object> readAttributes(final String... attributes) throws IOException {
        final DiffAttributes attrs = readAttributes();

        return new HashMap<String, Object>(super.readAttributes(attributes)) {{
            for (final String attribute : attributes) {
                checkNotEmpty("attribute",
                              attribute);

                if (attribute.equals("*") || attribute.equals(DIFF)) {
                    put(DIFF,
                        attrs.branchDiff());
                }

                if (attribute.equals("*")) {
                    break;
                }
            }
        }};
    }
}
