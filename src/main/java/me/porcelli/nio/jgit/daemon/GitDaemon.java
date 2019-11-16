package me.porcelli.nio.jgit.daemon;

public interface GitDaemon extends Daemon {

    public static final int DEFAULT_DAEMON_DEFAULT_PORT = 9418;

    String hostName();

    int hostPort();

}
