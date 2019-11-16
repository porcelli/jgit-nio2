package me.porcelli.nio.jgit.impl.daemon.git;

import java.io.IOException;

import me.porcelli.nio.jgit.daemon.GitDaemon;
import me.porcelli.nio.jgit.impl.daemon.DaemonImpl;

public class GitDaemonImpl extends DaemonImpl implements GitDaemon {

    private final Daemon daemon;
    private final String hostName;

    public GitDaemonImpl(final Daemon daemon,
                         final String hostName) {
        this.daemon = daemon;
        this.hostName = hostName;
    }

    @Override
    public String hostName() {
        return hostName;
    }

    @Override
    public int hostPort() {
        return daemon.getAddress().getPort();
    }

    @Override
    public String fullHostname() {
        return hostName() + ":" + hostPort();
    }

    @Override
    public void close() throws IOException {
        daemon.stop();
    }
}
