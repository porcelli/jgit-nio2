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
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.List;

import me.porcelli.nio.jgit.fs.attribute.DiffAttributes;
import me.porcelli.nio.jgit.fs.attribute.FileDiff;
import me.porcelli.nio.jgit.fs.attribute.VersionAttributeView;

/**
 *
 */
public class JGitDiffAttributeViewImpl extends DiffAttributeViewImpl<JGitPathImpl> {

    private DiffAttributes attrs = null;
    private final String params;

    public JGitDiffAttributeViewImpl(final JGitPathImpl path,
                                     final String params) {
        super(path);
        this.params = params;
    }

    @Override
    public DiffAttributes readAttributes() throws IOException {
        if (attrs == null) {
            attrs = buildAttrs(path.getFileSystem(),
                               params);
        }
        return attrs;
    }

    @Override
    public Class<? extends BasicFileAttributeView>[] viewTypes() {
        return new Class[]{VersionAttributeView.class, VersionAttributeViewImpl.class, JGitDiffAttributeViewImpl.class};
    }

    private DiffAttributes buildAttrs(final JGitFileSystem fs,
                                      final String params) throws IOException {
        final String[] branches = params.split(",");
        final String branchA = branches[0];
        final String branchB = branches[1];
        final List<FileDiff> diffs = fs.getGit().diffRefs(branchA, branchB);

        return new DiffAttributesImpl(new JGitBasicAttributeView(this.path).readAttributes(),
                                      () -> diffs);
    }
}
