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

import java.io.Closeable;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

import me.porcelli.nio.jgit.fs.options.CommentedOption;
import me.porcelli.nio.jgit.impl.op.Git;
import me.porcelli.nio.jgit.impl.op.model.CommitInfo;
import me.porcelli.nio.jgit.security.User;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.UploadPack;

public abstract class JGitFileSystem extends FileSystem implements FileSystemStateAware,
                                                                   FileSystemId,
                                                                   LockableFileSystem,
                                                                   Closeable {

    abstract public Git getGit();

    abstract CredentialsProvider getCredential();

    abstract void checkClosed() throws IllegalStateException;

    abstract void publishEvents(Path watchable,
                                List<WatchEvent<?>> elist);

    abstract boolean isOnBatch();

    abstract void setState(String state);

    abstract CommitInfo buildCommitInfo(String defaultMessage,
                                        CommentedOption op);

    abstract void setBatchCommitInfo(String defaultMessage,
                                     CommentedOption op);

    abstract void setHadCommitOnBatchState(Path path,
                                           boolean hadCommitOnBatchState);

    abstract void setHadCommitOnBatchState(boolean value);

    abstract boolean isHadCommitOnBatchState(Path path);

    abstract void setBatchCommitInfo(CommitInfo batchCommitInfo);

    abstract CommitInfo getBatchCommitInfo();

    abstract int incrementAndGetCommitCount();

    abstract void resetCommitCount();

    abstract int getNumberOfCommitsSinceLastGC();

    abstract void addPostponedWatchEvents(List<WatchEvent<?>> postponedWatchEvents);

    abstract List<WatchEvent<?>> getPostponedWatchEvents();

    abstract void clearPostponedWatchEvents();

    abstract boolean hasPostponedEvents();

    abstract public boolean hasBeenInUse();

    abstract void notifyExternalUpdate();

    abstract void notifyPostCommit(int exitCode);

    abstract void checkBranchAccess(ReceiveCommand command,
                                    User user);

    abstract void filterBranchAccess(UploadPack uploadPack,
                                     User user);

    public abstract String getName();
}
