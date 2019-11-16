package me.porcelli.nio.jgit.impl.daemon.git;

import java.io.IOException;

import me.porcelli.nio.jgit.daemon.GitDaemon;
import me.porcelli.nio.jgit.impl.daemon.DaemonImpl;

public class GitDaemonImpl extends DaemonImpl implements GitDaemon {

    private final Daemon daemon;

    public GitDaemonImpl(final Daemon daemon,
                         final String hostAddr,
                         final String hostName) {
        super(hostAddr, hostName, daemon.getAddress().getPort());
        this.daemon = daemon;
    }

    @Override
    public String protocol() {
        return GitDaemon.PROTOCOL;
    }

    @Override
    public void close() throws IOException {
        daemon.stop();
    }
}
