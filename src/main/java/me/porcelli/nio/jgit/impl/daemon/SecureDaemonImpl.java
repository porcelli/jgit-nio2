package me.porcelli.nio.jgit.impl.daemon;

import me.porcelli.nio.jgit.daemon.SecureDaemon;

public abstract class SecureDaemonImpl extends DaemonImpl implements SecureDaemon {

    public SecureDaemonImpl(String hostAddr, String hostName, int port) {
        super(hostAddr, hostName, port);
    }
}
