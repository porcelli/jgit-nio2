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

import java.util.Collections;
import java.util.HashMap;

import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.Session;
import me.porcelli.nio.jgit.impl.config.ConfigProperties;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.junit.Test;

import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.SSH_OVER_HTTP;
import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.SSH_OVER_HTTPS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

public class JGitSSHConfigSessionFactoryTest {

    @Test
    public void testNoProxy() {
        JGitFileSystemProviderConfiguration config = new JGitFileSystemProviderConfiguration() {

        };
        config.load(new ConfigProperties(Collections.emptyMap()));

        final JGitSSHConfigSessionFactory instance = new JGitSSHConfigSessionFactory(config) {
            @Override
            ProxyHTTP buildProxy(final JGitFileSystemProviderConfiguration config) {
                fail("no proxy should be set");
                return null;
            }
        };
        instance.configure(mock(OpenSshConfig.Host.class), mock(Session.class));
    }

    @Test
    public void testHttpProxy() {
        JGitFileSystemProviderConfiguration config = new JGitFileSystemProviderConfiguration() {

        };
        config.load(new ConfigProperties(new HashMap<String, String>() {{
            put(SSH_OVER_HTTP, "true");
            put("http.proxyHost", "somehost");
            put("http.proxyPort", "10");
        }}));

        final JGitSSHConfigSessionFactory instance = new JGitSSHConfigSessionFactory(config) {
            @Override
            ProxyHTTP buildProxy(final JGitFileSystemProviderConfiguration config) {
                ProxyHTTP proxy = super.buildProxy(config);
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_host", "somehost");
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_port", 10);
                assertThat(proxy).hasFieldOrPropertyWithValue("user", null);
                assertThat(proxy).hasFieldOrPropertyWithValue("passwd", null);
                return proxy;
            }
        };
        instance.configure(mock(OpenSshConfig.Host.class), mock(Session.class));
    }

    @Test
    public void testHttpProxyWithAuthentication() {
        JGitFileSystemProviderConfiguration config = new JGitFileSystemProviderConfiguration() {

        };
        config.load(new ConfigProperties(new HashMap<String, String>() {{
            put(SSH_OVER_HTTP, "true");
            put("http.proxyHost", "somehost");
            put("http.proxyPort", "10");
            put("http.proxyUser", "user");
            put("http.proxyPassword", "passwd");
        }}));

        final JGitSSHConfigSessionFactory instance = new JGitSSHConfigSessionFactory(config) {
            @Override
            ProxyHTTP buildProxy(final JGitFileSystemProviderConfiguration config) {
                ProxyHTTP proxy = super.buildProxy(config);
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_host", "somehost");
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_port", 10);
                assertThat(proxy).hasFieldOrPropertyWithValue("user", "user");
                assertThat(proxy).hasFieldOrPropertyWithValue("passwd", "passwd");
                return proxy;
            }
        };
        instance.configure(mock(OpenSshConfig.Host.class), mock(Session.class));
    }

    @Test
    public void testHttpsProxy() {
        JGitFileSystemProviderConfiguration config = new JGitFileSystemProviderConfiguration() {

        };
        config.load(new ConfigProperties(new HashMap<String, String>() {{
            put(SSH_OVER_HTTPS, "true");
            put("https.proxyHost", "somehost");
            put("https.proxyPort", "10");
        }}));

        final JGitSSHConfigSessionFactory instance = new JGitSSHConfigSessionFactory(config) {
            @Override
            ProxyHTTP buildProxy(final JGitFileSystemProviderConfiguration config) {
                ProxyHTTP proxy = super.buildProxy(config);
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_host", "somehost");
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_port", 10);
                assertThat(proxy).hasFieldOrPropertyWithValue("user", null);
                assertThat(proxy).hasFieldOrPropertyWithValue("passwd", null);
                return proxy;
            }
        };
        instance.configure(mock(OpenSshConfig.Host.class), mock(Session.class));
    }

    @Test
    public void testHttpsProxyWithAuthentication() {
        JGitFileSystemProviderConfiguration config = new JGitFileSystemProviderConfiguration() {

        };
        config.load(new ConfigProperties(new HashMap<String, String>() {{
            put(SSH_OVER_HTTPS, "true");
            put("https.proxyHost", "somehost");
            put("https.proxyPort", "10");
            put("https.proxyUser", "user");
            put("https.proxyPassword", "passwd");
        }}));

        final JGitSSHConfigSessionFactory instance = new JGitSSHConfigSessionFactory(config) {
            @Override
            ProxyHTTP buildProxy(final JGitFileSystemProviderConfiguration config) {
                ProxyHTTP proxy = super.buildProxy(config);
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_host", "somehost");
                assertThat(proxy).hasFieldOrPropertyWithValue("proxy_port", 10);
                assertThat(proxy).hasFieldOrPropertyWithValue("user", "user");
                assertThat(proxy).hasFieldOrPropertyWithValue("passwd", "passwd");
                return proxy;
            }
        };
        instance.configure(mock(OpenSshConfig.Host.class), mock(Session.class));
    }
}
