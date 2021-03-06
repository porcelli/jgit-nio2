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
import java.nio.file.NoSuchFileException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import me.porcelli.nio.jgit.impl.op.model.PathInfo;
import me.porcelli.nio.jgit.impl.op.model.PathType;
import org.eclipse.jgit.lib.Ref;

/**
 *
 */
public class JGitBasicAttributeView extends AbstractBasicFileAttributeView<JGitPathImpl> {

    private BasicFileAttributes attrs = null;

    public JGitBasicAttributeView(final JGitPathImpl path) {
        super(path);
    }

    @Override
    public BasicFileAttributes readAttributes() throws IOException {
        if (attrs == null) {
            attrs = buildAttrs((JGitFileSystem) path.getFileSystem(),
                               path.getRefTree(),
                               path.getPath());
        }
        return attrs;
    }

    @Override
    public Class<? extends BasicFileAttributeView>[] viewTypes() {
        return new Class[]{BasicFileAttributeView.class, JGitBasicAttributeView.class};
    }

    private BasicFileAttributes buildAttrs(final JGitFileSystem fs,
                                           final String branchName,
                                           final String path) throws NoSuchFileException {
        final PathInfo pathInfo = fs.getGit().getPathInfo(branchName,
                                                          path);

        if (pathInfo == null || pathInfo.getPathType().equals(PathType.NOT_FOUND)) {
            throw new NoSuchFileException(path);
        }

        final Ref ref = fs.getGit().getRef(branchName);

        return new BasicFileAttributes() {

            private FileTime lastModifiedDate = null;
            private FileTime creationDate = null;

            @Override
            public FileTime lastModifiedTime() {
                if (lastModifiedDate == null) {
                    try {
                        lastModifiedDate = FileTime.fromMillis(fs.getGit().getLastCommit(ref).getCommitterIdent().getWhen().getTime());
                    } catch (final Exception e) {
                        lastModifiedDate = FileTime.fromMillis(0);
                    }
                }
                return lastModifiedDate;
            }

            @Override
            public FileTime lastAccessTime() {
                return lastModifiedTime();
            }

            @Override
            public FileTime creationTime() {
                if (creationDate == null) {
                    try {
                        creationDate = FileTime.fromMillis(fs.getGit().getFirstCommit(ref).getCommitterIdent().getWhen().getTime());
                    } catch (final Exception e) {
                        creationDate = FileTime.fromMillis(0);
                    }
                }
                return creationDate;
            }

            @Override
            public boolean isRegularFile() {
                return pathInfo.getPathType().equals(PathType.FILE);
            }

            @Override
            public boolean isDirectory() {
                return pathInfo.getPathType().equals(PathType.DIRECTORY);
            }

            @Override
            public boolean isSymbolicLink() {
                return false;
            }

            @Override
            public boolean isOther() {
                return false;
            }

            @Override
            public long size() {
                return pathInfo.getSize();
            }

            @Override
            public Object fileKey() {
                return pathInfo.getObjectId() == null ? null : pathInfo.getObjectId().toString();
            }
        };
    }
}
