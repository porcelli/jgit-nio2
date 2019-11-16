package me.porcelli.nio.jgit.daemon;

import java.io.Closeable;

public interface Daemon extends Closeable {

    String DEFAULT_HOST_ADDR = "127.0.0.1";
    String DEFAULT_HOST_NAME = "localhost";

    String hostName();

    String hostAddr();

    int hostPort();

    String protocol();

    String fullHostname();
}
