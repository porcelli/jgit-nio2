package me.porcelli.nio.jgit.daemon;

public interface GitDaemon extends Daemon {

    String PROTOCOL = "git";
    int DEFAULT_DAEMON_DEFAULT_PORT = 9418;

}
