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

import me.porcelli.nio.jgit.fs.attribute.BranchDiff;
import me.porcelli.nio.jgit.fs.attribute.DiffAttributes;

public class DiffAttributesImpl
        extends AbstractJGitBasicAttributesImpl
        implements DiffAttributes {

    private final BranchDiff branchDiff;

    public DiffAttributesImpl(final BasicFileAttributes attributes,
                              final BranchDiff branchDiff) {
        super(attributes);
        this.branchDiff = branchDiff;
    }

    @Override
    public BranchDiff branchDiff() {
        return branchDiff;
    }
}
