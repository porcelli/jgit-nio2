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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.porcelli.nio.jgit.impl.op.Git;
import me.porcelli.nio.jgit.impl.op.model.MergeCommitContent;
import me.porcelli.nio.jgit.impl.op.model.MessageCommitInfo;
import me.porcelli.nio.jgit.impl.op.model.RevertCommitContent;
import org.eclipse.jgit.revwalk.RevCommit;

import static me.porcelli.nio.jgit.impl.util.Preconditions.checkNotEmpty;
import static me.porcelli.nio.jgit.impl.util.Preconditions.checkNotNull;

public class RevertMerge {

    private final Git git;
    private final String sourceBranch;
    private final String targetBranch;
    private final String commonAncestorCommitId;
    private final String mergeCommitId;

    public RevertMerge(final Git git,
                       final String sourceBranch,
                       final String targetBranch,
                       final String commonAncestorCommitId,
                       final String mergeCommitId) {
        this.git = checkNotNull("git",
                                git);
        this.sourceBranch = checkNotEmpty("sourceBranch",
                                          sourceBranch);
        this.targetBranch = checkNotEmpty("targetBranch",
                                          targetBranch);
        this.commonAncestorCommitId = checkNotEmpty("commonAncestorCommitId",
                                                    commonAncestorCommitId);
        this.mergeCommitId = checkNotEmpty("mergeCommitId",
                                           mergeCommitId);
    }

    public boolean execute() {
        BranchUtil.existsBranch(git,
                                sourceBranch);
        BranchUtil.existsBranch(git,
                                targetBranch);

        final RevCommit lastSourceCommit = git.getLastCommit(sourceBranch);
        final RevCommit lastTargetCommit = git.getLastCommit(targetBranch);

        boolean isDone = false;

        if (canRevert(lastSourceCommit,
                      lastTargetCommit)) {

            git.commit(targetBranch,
                       MessageCommitInfo.createRevertMergeMessage(sourceBranch),
                       false,
                       lastTargetCommit.getParent(0),
                       new RevertCommitContent(targetBranch));

            final RevCommit newLastTargetCommit = git.getLastCommit(targetBranch);

            final List<RevCommit> parents = Stream.of(lastSourceCommit,
                                                      newLastTargetCommit).collect(Collectors.toList());

            final Map<String, File> contents = git.mapDiffContent(targetBranch,
                                                                  lastTargetCommit.getName(),
                                                                  newLastTargetCommit.getName());

            git.commit(sourceBranch,
                       MessageCommitInfo.createMergeMessage(targetBranch),
                       false,
                       lastSourceCommit,
                       new MergeCommitContent(contents,
                                              parents));

            git.commit(sourceBranch,
                       MessageCommitInfo.createFixMergeReversionMessage(),
                       false,
                       git.getLastCommit(sourceBranch).getParent(0),
                       new RevertCommitContent(sourceBranch));

            isDone = true;
        }

        return isDone;
    }

    private boolean canRevert(final RevCommit lastSourceCommit,
                              final RevCommit lastTargetCommit) {
        return lastTargetCommit.getParentCount() > 1 &&
                lastTargetCommit.getName().equals(mergeCommitId) &&
                lastTargetCommit.getParent(0).getName().equals(commonAncestorCommitId) &&
                lastTargetCommit.getParent(1).getName().equals(lastSourceCommit.getName());
    }
}
