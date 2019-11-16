package me.porcelli.nio.jgit.impl.daemon.ssh;

import java.io.IOException;

import me.porcelli.nio.jgit.daemon.SSHDaemon;
import me.porcelli.nio.jgit.impl.daemon.SecureDaemonImpl;

public class SSHDaemonImpl extends SecureDaemonImpl implements SSHDaemon {

    private final GitSSHService gitSSHService;

    public SSHDaemonImpl(GitSSHService gitSSHService, String hostAddr, String hostName, int port) {
        super(hostAddr, hostName, port);
        this.gitSSHService = gitSSHService;
    }

    @Override
    public String protocol() {
        return "ssh";
    }

    @Override
    public void close() throws IOException {
        gitSSHService.stop();
    }

    @Override
    public String sshFileCertDir() {
        return SSHDaemon.DEFAULT_SSH_FILE_CERT_DIR;
    }

    @Override
    public String sshAlgorithm() {
        return SSHDaemon.DEFAULT_SSH_ALGORITHM;
    }

    @Override
    public String sshPassphrase() {
        return SSHDaemon.DEFAULT_SSH_CERT_PASSPHRASE;
    }

    @Override
    public int sshIdleTimeout() {
        return SSHDaemon.DEFAULT_SSH_IDLE_TIMEOUT;
    }

    @Override
    public String gitSshCiphers() {
        return null;
    }

    @Override
    public String gitSshMACs() {
        return null;
    }
}
