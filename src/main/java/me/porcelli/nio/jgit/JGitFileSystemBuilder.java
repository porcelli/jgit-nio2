package me.porcelli.nio.jgit;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.util.HashMap;
import java.util.Map;

import me.porcelli.nio.jgit.daemon.Daemon;
import me.porcelli.nio.jgit.impl.JGitFileSystem;
import me.porcelli.nio.jgit.impl.daemon.DaemonImpl;
import me.porcelli.nio.jgit.security.FileSystemAuthorization;

import static me.porcelli.nio.jgit.impl.JGitFileSystemProvider.PROVIDER;
import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.GIT_ENV_KEY_AUTHZ_PROVIDER;
import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.GIT_ENV_KEY_DEFAULT_REMOTE_NAME;
import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.GIT_ENV_KEY_FULL_HOST_NAMES;

public final class JGitFileSystemBuilder {

    private static final Map<String, String> DEFAULT_OPTIONS = new HashMap<>();

    private JGitFileSystemBuilder() {
        DEFAULT_OPTIONS.put("init", "true");
    }

    public static FileSystem newFileSystem(final String repoName) throws IOException {
        return newFileSystem(repoName, (user) -> true);
    }

    public static FileSystem newFileSystem(final String repoName,
                                           final Daemon... daemons) throws IOException {
        return newFileSystem(repoName, (user) -> true, daemons);
    }

    public static FileSystem newFileSystem(final String repoName,
                                           final FileSystemAuthorization authorization,
                                           final Daemon... daemons) throws IOException {
        final Map env = new HashMap<>(DEFAULT_OPTIONS);
        env.put(GIT_ENV_KEY_FULL_HOST_NAMES, buildHostNames(daemons));
        env.put(GIT_ENV_KEY_AUTHZ_PROVIDER, authorization);
        final FileSystem fs = newFileSystem(repoName, env);
        for (Daemon daemon : daemons) {
            ((DaemonImpl) daemon).accept((JGitFileSystem) fs);
        }
        return fs;
    }

    public static FileSystem newFileSystem(final String repoName,
                                           final Map env) throws IOException {
        return PROVIDER.newFileSystem(URI.create("git://" + repoName),
                                      env);
    }

    private static Map<String, String> buildHostNames(Daemon[] daemons) {
        final Map<String, String> hostNames = new HashMap<>();
        for (final Daemon daemon : daemons) {
            hostNames.put(daemon.protocol(), daemon.fullHostname());
        }
        return hostNames;
    }

    public static FileSystem newFileSystem(final String repoName,
                                           final String origin) throws IOException {
        final Map env = new HashMap<>(DEFAULT_OPTIONS);
        env.put(GIT_ENV_KEY_DEFAULT_REMOTE_NAME, origin);
        return newFileSystem(repoName, env);
    }
}
