package me.porcelli.nio.jgit.daemon;

import java.io.Closeable;

public interface Daemon extends Closeable {

    public static final String DEFAULT_HOST_ADDR = "127.0.0.1";
    public static final String DEFAULT_HOST_NAME = "localhost";
}
