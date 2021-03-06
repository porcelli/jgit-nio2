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
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.GIT_DAEMON_ENABLED;
import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.GIT_DAEMON_PORT;
import static org.junit.Assert.assertTrue;

public class JGitFileSystemProxyTest extends AbstractTestInfra {

    private int gitDaemonPort;

    @Override
    public Map<String, String> getGitPreferences() {
        Map<String, String> gitPrefs = super.getGitPreferences();
        gitPrefs.put(GIT_DAEMON_ENABLED,
                     "true");
        // use different port for every test -> easy to run tests in parallel
        gitDaemonPort = findFreePort();
        gitPrefs.put(GIT_DAEMON_PORT,
                     String.valueOf(gitDaemonPort));
        return gitPrefs;
    }

    @Test
    public void proxyTest() throws IOException {
        final URI originRepo = URI.create("git://encoding-origin-name");

        final JGitFileSystem origin = (JGitFileSystem) provider.newFileSystem(originRepo,
                                                                              Collections.emptyMap());

        assertTrue(origin instanceof JGitFileSystemProxy);
        JGitFileSystemProxy proxy = (JGitFileSystemProxy) origin;
        JGitFileSystem realJGitFileSystem = proxy.getRealJGitFileSystem();
        assertTrue(realJGitFileSystem instanceof JGitFileSystemImpl);

        assertTrue(proxy.equals(realJGitFileSystem));
        assertTrue(realJGitFileSystem.equals(proxy));
    }
}