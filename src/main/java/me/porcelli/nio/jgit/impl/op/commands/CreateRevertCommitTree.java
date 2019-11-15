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

package me.porcelli.nio.jgit.impl.op.commands;

import java.util.Optional;

import me.porcelli.nio.jgit.impl.op.Git;
import me.porcelli.nio.jgit.impl.op.model.RevertCommitContent;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEditor;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;

public class CreateRevertCommitTree extends BaseCreateCommitTree<RevertCommitContent> {

    public CreateRevertCommitTree(final Git git,
                                  final ObjectId headId,
                                  final ObjectInserter inserter,
                                  final RevertCommitContent commitContent) {
        super(git,
              headId,
              inserter,
              commitContent);
    }

    public Optional<ObjectId> execute() {
        final DirCacheEditor editor = DirCache.newInCore().editor();

        try {
            iterateOverTreeWalk(git,
                                headId,
                                (walkPath, hTree) -> {
                                    addToTemporaryInCoreIndex(editor,
                                                              new DirCacheEntry(walkPath),
                                                              hTree.getEntryObjectId(),
                                                              hTree.getEntryFileMode());
                                });

            editor.finish();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return buildTree(editor);
    }
}
