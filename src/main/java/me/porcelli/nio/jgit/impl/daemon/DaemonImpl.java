package me.porcelli.nio.jgit.impl.daemon;

import java.util.HashSet;
import java.util.Set;

import me.porcelli.nio.jgit.daemon.Daemon;
import me.porcelli.nio.jgit.impl.JGitFileSystem;

public abstract class DaemonImpl implements Daemon {

    private final Set<String> accepted = new HashSet<>();
    private final String hostName;
    private final String hostAddr;
    private final int port;

    public DaemonImpl(final String hostAddr,
                      final String hostName,
                      final int port){
        this.hostName = hostName;
        this.hostAddr = hostAddr;
        this.port = port;
    }

    public boolean isAccepted(String name) {
        return accepted.contains(name);
    }

    public void accept(JGitFileSystem fs) {
        accepted.add(fs.getName());
    }

    @Override
    public String fullHostname() {
        return hostName() + ":" + hostPort();
    }

    @Override
    public String hostName() {
        return hostName;
    }

    @Override
    public String hostAddr() {
        return hostAddr;
    }

    @Override
    public int hostPort() {
        return port;
    }

}
