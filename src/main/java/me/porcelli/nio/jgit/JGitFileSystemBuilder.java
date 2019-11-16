package me.porcelli.nio.jgit;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.util.HashMap;
import java.util.Map;

import me.porcelli.nio.jgit.daemon.GitDaemon;
import me.porcelli.nio.jgit.impl.JGitFileSystem;
import me.porcelli.nio.jgit.impl.JGitFileSystemProvider;
import me.porcelli.nio.jgit.impl.daemon.git.GitDaemonImpl;

import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.GIT_ENV_KEY_DEFAULT_REMOTE_NAME;
import static me.porcelli.nio.jgit.impl.JGitFileSystemProviderConfiguration.GIT_ENV_KEY_FULL_HOST_NAMES;

public final class JGitFileSystemBuilder {

    static final JGitFileSystemProvider PROVIDER = new JGitFileSystemProvider();
    private static final Map<String, String> DEFAULT_OPTIONS = new HashMap<>();

    private JGitFileSystemBuilder() {
        DEFAULT_OPTIONS.put("init", "true");
    }

    public static FileSystem newFileSystem(final String repoName) throws IOException {
        return newFileSystem(repoName, DEFAULT_OPTIONS);
    }

    public static FileSystem newFileSystem(final String repoName,
                                           final Map env) throws IOException {
        return PROVIDER.newFileSystem(URI.create("git://" + repoName),
                                      env);
    }

    public static FileSystem newFileSystem(final String repoName,
                                           final GitDaemon _daemon) throws IOException {
        final GitDaemonImpl daemon = (GitDaemonImpl) _daemon;
        final Map env = new HashMap<>(DEFAULT_OPTIONS);
        final Map<String, String> hostNames = new HashMap<>();
        hostNames.put("git", daemon.fullHostname());
        env.put(GIT_ENV_KEY_FULL_HOST_NAMES, hostNames);
        final FileSystem fs = newFileSystem(repoName, env);
        daemon.accept((JGitFileSystem) fs);
        return fs;
    }

    public static FileSystem newFileSystem(final String repoName,
                                           final String origin) throws IOException {
        final Map env = new HashMap<>(DEFAULT_OPTIONS);
        env.put(GIT_ENV_KEY_DEFAULT_REMOTE_NAME, origin);
        return newFileSystem(repoName, env);
    }
}
