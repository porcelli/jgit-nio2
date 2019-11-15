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

import me.porcelli.nio.jgit.fs.attribute.HiddenAttributes;

/**
 * HiddenAttribute implementation. Receives a BasicFIleAttributes,
 * and if file is hidden or not so creates a new object that has all those
 * attributes together.
 */
public class HiddenAttributesImpl
        extends AbstractJGitBasicAttributesImpl
        implements HiddenAttributes {

    private final boolean hidden;

    public HiddenAttributesImpl(final BasicFileAttributes attributes,
                                final boolean isHidden) {
        super(attributes);
        this.hidden = isHidden;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }
}
