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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.porcelli.nio.jgit.impl.op.Git;
import me.porcelli.nio.jgit.impl.op.GitImpl;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;

import static me.porcelli.nio.jgit.impl.util.Preconditions.checkInstanceOf;
import static me.porcelli.nio.jgit.impl.util.Preconditions.checkNotNull;

public class Push {

    private final GitImpl git;
    private final CredentialsProvider credentialsProvider;
    private final Map.Entry<String, String> remote;
    private final boolean force;
    private final Collection<RefSpec> refSpecs;

    public Push(final Git git,
                final CredentialsProvider credentialsProvider,
                final Map.Entry<String, String> remote,
                final boolean force,
                final Collection<RefSpec> refSpecs) {
        this.git = checkInstanceOf("git",
                                   git,
                                   GitImpl.class);
        this.credentialsProvider = credentialsProvider;
        this.remote = checkNotNull("remote",
                                   remote);
        this.force = force;
        this.refSpecs = refSpecs;
    }

    public void execute() throws InvalidRemoteException {
        try {
            final List<RefSpec> specs = new UpdateRemoteConfig(git,
                                                               remote,
                                                               refSpecs).execute();
            git._push()
                    .setCredentialsProvider(credentialsProvider)
                    .setRefSpecs(specs)
                    .setRemote(remote.getKey())
                    .setForce(force)
                    .setPushAll()
                    .call();
        } catch (final InvalidRemoteException e) {
            throw e;
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
