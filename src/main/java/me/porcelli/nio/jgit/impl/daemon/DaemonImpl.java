package me.porcelli.nio.jgit.impl.daemon;

import java.util.HashSet;
import java.util.Set;

import me.porcelli.nio.jgit.daemon.Daemon;
import me.porcelli.nio.jgit.impl.JGitFileSystem;

public abstract class DaemonImpl implements Daemon {

    private final Set<String> accepted = new HashSet<>();

    public abstract String fullHostname();

    public void accept(JGitFileSystem fs) {
        accepted.add(fs.getName());
    }

    public boolean isAccepted(String name) {
        return accepted.contains(name);
    }
}
