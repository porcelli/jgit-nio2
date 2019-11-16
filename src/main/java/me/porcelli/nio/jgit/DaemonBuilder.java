package me.porcelli.nio.jgit;

import java.util.concurrent.Executors;

import me.porcelli.nio.jgit.daemon.GitDaemon;
import me.porcelli.nio.jgit.daemon.SSHDaemon;
import me.porcelli.nio.jgit.impl.daemon.git.GitDaemonBuilder;
import me.porcelli.nio.jgit.impl.daemon.ssh.SSHDaemonBuilder;
import me.porcelli.nio.jgit.impl.util.DescriptiveThreadFactory;

import static me.porcelli.nio.jgit.daemon.Daemon.DEFAULT_HOST_ADDR;
import static me.porcelli.nio.jgit.daemon.GitDaemon.DEFAULT_DAEMON_DEFAULT_PORT;
import static me.porcelli.nio.jgit.daemon.SSHDaemon.DEFAULT_SSH_ALGORITHM;
import static me.porcelli.nio.jgit.daemon.SSHDaemon.DEFAULT_SSH_FILE_CERT_DIR;
import static me.porcelli.nio.jgit.daemon.SSHDaemon.DEFAULT_SSH_IDLE_TIMEOUT;
import static me.porcelli.nio.jgit.daemon.SSHDaemon.DEFAULT_SSH_PORT;

public class DaemonBuilder {

    private DaemonBuilder() {
    }

    public static GitDaemon buildGitDaemon() {
        return buildGitDaemon(DEFAULT_HOST_ADDR, DEFAULT_DAEMON_DEFAULT_PORT);
    }

    public static GitDaemon buildGitDaemon(int port) {
        return buildGitDaemon(DEFAULT_HOST_ADDR, port);
    }

    public static GitDaemon buildGitDaemon(String hostName, int hostPort) {
        return setupGitDaemon(hostName, hostPort);
    }


    public static SSHDaemon buildSSHDaemon() {
        return setupSSHDaemon(DEFAULT_HOST_ADDR, DEFAULT_SSH_PORT);
    }

    private static GitDaemon setupGitDaemon(final String hostName,
                                            final int port) {
        return GitDaemonBuilder.buildAndStartDaemon(hostName,
                                                    port,
                                                    Executors.newCachedThreadPool(new DescriptiveThreadFactory()));
    }

    private static SSHDaemon setupSSHDaemon(final String hostName,
                                            final int port) {
        return SSHDaemonBuilder.buildAndStartSSH(DEFAULT_SSH_FILE_CERT_DIR,
                                                 hostName,
                                                 port,
                                                 DEFAULT_SSH_IDLE_TIMEOUT,
                                                 DEFAULT_SSH_ALGORITHM,
                                                 null,
                                                 null,
                                                 Executors.newCachedThreadPool(new DescriptiveThreadFactory()));
    }

}
