package me.porcelli.nio.jgit;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.util.HashMap;
import java.util.Map;

import me.porcelli.nio.jgit.impl.JGitFileSystemProvider;

public final class JGitFileSystemBuilder {

    private static final JGitFileSystemProvider PROVIDER = new JGitFileSystemProvider();
    private static final Map<String, String> DEFAULT_OPTIONS = new HashMap<>();

    private JGitFileSystemBuilder() {
        DEFAULT_OPTIONS.put("init", "true");
    }

    public static FileSystem newFileSystem(final String repoName) throws IOException {
        return PROVIDER.newFileSystem(URI.create("git://" + repoName),
                                      DEFAULT_OPTIONS);
    }
}
