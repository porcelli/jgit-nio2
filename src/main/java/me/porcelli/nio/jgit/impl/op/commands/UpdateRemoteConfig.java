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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.porcelli.nio.jgit.impl.op.Git;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;

public class UpdateRemoteConfig {

    private final Git git;
    private final Map.Entry<String, String> remote;
    private final Collection<RefSpec> refSpecs;

    public UpdateRemoteConfig(final Git git,
                              final Map.Entry<String, String> remote,
                              final Collection<RefSpec> refSpecs) {
        this.git = git;
        this.remote = remote;
        this.refSpecs = refSpecs;
    }

    public List<RefSpec> execute() throws IOException, URISyntaxException {
        final List<RefSpec> specs = new ArrayList<>();
        if (refSpecs == null || refSpecs.isEmpty()) {
            specs.add(new RefSpec("+refs/heads/*:refs/remotes/" + remote.getKey() + "/*"));
            specs.add(new RefSpec("+refs/tags/*:refs/tags/*"));
            specs.add(new RefSpec("+refs/notes/*:refs/notes/*"));
        } else {
            specs.addAll(refSpecs);
        }

        final StoredConfig config = git.getRepository().getConfig();
        final String url = config.getString("remote",
                                            remote.getKey(),
                                            "url");
        if (url == null) {
            final RemoteConfig remoteConfig = new RemoteConfig(git.getRepository().getConfig(),
                                                               remote.getKey());
            remoteConfig.addURI(new URIish(remote.getValue()));
            specs.forEach(remoteConfig::addFetchRefSpec);
            remoteConfig.update(git.getRepository().getConfig());
            git.getRepository().getConfig().save();
        }
        return specs;
    }
}
