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

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class AbstractJGitBasicAttributesImpl implements BasicFileAttributes {

    private BasicFileAttributes attributes;

    public AbstractJGitBasicAttributesImpl(final BasicFileAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public FileTime lastModifiedTime() {
        return attributes.lastModifiedTime();
    }

    @Override
    public FileTime lastAccessTime() {
        return attributes.lastAccessTime();
    }

    @Override
    public FileTime creationTime() {
        return attributes.creationTime();
    }

    @Override
    public boolean isRegularFile() {
        return attributes.isRegularFile();
    }

    @Override
    public boolean isDirectory() {
        return attributes.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        return attributes.isSymbolicLink();
    }

    @Override
    public boolean isOther() {
        return attributes.isOther();
    }

    @Override
    public long size() {
        return attributes.size();
    }

    @Override
    public Object fileKey() {
        return attributes.fileKey();
    }
}
