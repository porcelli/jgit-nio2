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
import java.nio.file.FileStore;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

import org.eclipse.jgit.lib.Repository;

import static me.porcelli.nio.jgit.impl.util.Preconditions.checkNotEmpty;
import static me.porcelli.nio.jgit.impl.util.Preconditions.checkNotNull;

public class JGitFileStore extends FileStore {

    private final Repository repository;

    JGitFileStore(final Repository repository) {
        this.repository = checkNotNull("repository",
                                       repository);
    }

    @Override
    public String name() {
        return repository.getDirectory().getName();
    }

    @Override
    public String type() {
        return "file";
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public long getTotalSpace() throws IOException {
        return repository.getDirectory().getTotalSpace();
    }

    @Override
    public long getUsableSpace() throws IOException {
        return repository.getDirectory().getUsableSpace();
    }

    @Override
    public long getUnallocatedSpace() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean supportsFileAttributeView(final Class<? extends FileAttributeView> type) {
        checkNotNull("type",
                     type);

        return type.equals(BasicFileAttributeView.class);
    }

    @Override
    public boolean supportsFileAttributeView(final String name) {
        checkNotEmpty("name",
                      name);

        return name.equals("basic");
    }

    @Override
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
        checkNotNull("type",
                     type);

        return null;
    }

    @Override
    public Object getAttribute(final String attribute) throws UnsupportedOperationException, IOException {
        checkNotEmpty("attribute",
                      attribute);

        if (attribute.equals("totalSpace")) {
            return getTotalSpace();
        }
        if (attribute.equals("usableSpace")) {
            return getUsableSpace();
        }
        if (attribute.equals("readOnly")) {
            return isReadOnly();
        }
        if (attribute.equals("name")) {
            return name();
        }
        throw new UnsupportedOperationException("Attribute '" + attribute + "' not available");
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof FileStore)) {
            return false;
        }

        final FileStore ofs = (FileStore) o;

        return name().equals(ofs.name());
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }
}
