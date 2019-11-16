package me.porcelli.nio.jgit.daemon;

import java.io.File;
import java.net.InetSocketAddress;

import me.porcelli.nio.jgit.impl.JGitFileSystem;
import me.porcelli.nio.jgit.impl.daemon.ssh.BaseGitCommand;
import me.porcelli.nio.jgit.impl.daemon.ssh.GitSSHService;
import org.eclipse.jgit.transport.UploadPack;
import org.eclipse.jgit.transport.resolver.ReceivePackFactory;
import org.eclipse.jgit.transport.resolver.UploadPackFactory;

public interface SSHDaemon extends SecureDaemon {

    int DEFAULT_SSH_PORT = 8001;
    String DEFAULT_SSH_FILE_CERT_DIR = ".security";
    String DEFAULT_SSH_ALGORITHM = "RSA";
    int DEFAULT_SSH_IDLE_TIMEOUT = 10000;
    String DEFAULT_SSH_CERT_PASSPHRASE = "";

    String sshFileCertDir();

    String sshAlgorithm();

    String sshPassphrase();

    int sshIdleTimeout();

    String gitSshCiphers();

    String gitSshMACs();
}
