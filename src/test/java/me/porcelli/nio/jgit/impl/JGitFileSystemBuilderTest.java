package me.porcelli.nio.jgit.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import me.porcelli.nio.jgit.DaemonBuilder;
import me.porcelli.nio.jgit.JGitFileSystemBuilder;
import me.porcelli.nio.jgit.daemon.GitDaemon;
import org.eclipse.jgit.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class JGitFileSystemBuilderTest {

    @After
    @Before
    public void cleanup() {
        try {
            FileUtils.delete(new File(".niogit"),
                             FileUtils.RECURSIVE);
        } catch (IOException ex) {
            //ignore
        }
    }

    @Test
    public void testSimpleBuilderSample() throws IOException {
        final FileSystem fs = JGitFileSystemBuilder.newFileSystem("myrepo1");

        Path foo = fs.getPath("/foo");
        Files.createDirectory(foo);

        Path hello = foo.resolve("hello.txt"); // /foo/hello.txt

        Files.write(hello, Collections.singletonList("hello world"), StandardCharsets.UTF_8);

        assertEquals("hello world", Files.readAllLines(hello).get(0));
    }

    @Test
    public void testBuilderWithGitDaemon() throws IOException {
        try (final GitDaemon daemon = DaemonBuilder.buildGitDaemon()) {
            final FileSystem fs = JGitFileSystemBuilder.newFileSystem("myrepo2",
                                                                      daemon);

            Path foo = fs.getPath("/foo");
            Files.createDirectory(foo);

            Path hello = foo.resolve("hello.txt"); // /foo/hello.txt

            Files.write(hello, Collections.singletonList("hello world"), StandardCharsets.UTF_8);

            assertEquals("hello world", Files.readAllLines(hello).get(0));
        }
    }

    @Test
    public void testCloneBuilderFromGitDaemon() throws IOException {
        try (final GitDaemon daemon = DaemonBuilder.buildGitDaemon()) {
            final FileSystem fs = JGitFileSystemBuilder.newFileSystem("myrepo3",
                                                                      daemon);

            Path foo = fs.getPath("/foo");
            Files.createDirectory(foo);

            Path hello = foo.resolve("hello.txt"); // /foo/hello.txt

            Files.write(hello, Collections.singletonList("hello world"), StandardCharsets.UTF_8);

            assertEquals("hello world", Files.readAllLines(hello).get(0));

            final FileSystem clonedFS = JGitFileSystemBuilder.newFileSystem("myrepo4",
                                                                            fs.toString());

            assertEquals("hello world", Files.readAllLines(clonedFS.getPath("/foo").resolve("hello.txt")).get(0));

            final FileSystem clonedWrongFS = JGitFileSystemBuilder.newFileSystem("myrepo5",
                                                                                 "git://localhost:9418/myrepo4");

            System.out.println(fs.toString());

            try {
                clonedWrongFS.getPath("/foo");
            } catch (RuntimeException ex) {
                assertEquals(ex.getMessage(), "me.porcelli.nio.jgit.impl.op.commands.Clone$CloneException: Error cloning origin <git://localhost:9418/myrepo4>.");
            }
        }
    }
}
