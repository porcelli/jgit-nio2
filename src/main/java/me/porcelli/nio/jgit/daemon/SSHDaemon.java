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
    //authentication


//    private String sshHostAddr;
//    private String sshHostName;
//    private File sshFileCertDir;
//    private String sshAlgorithm;
//    private String sshPassphrase;
//    private String sshIdleTimeout;
//    private String gitSshCiphers;
//    private String gitSshMACs;


//    private void buildAndStartSSH() {
//        final ReceivePackFactory receivePackFactory = (req, db) -> getReceivePack("ssh", req, db);
//
//        final UploadPackFactory uploadPackFactory = (UploadPackFactory<BaseGitCommand>) (req, db) -> new UploadPack(db) {{
//            final JGitFileSystem fs = fsManager.get(db);
//            fs.filterBranchAccess(this,
//                                  req.getUser());
//        }};
//
//        gitSSHService = new GitSSHService();
//
//        gitSSHService.setup(config.getSshFileCertDir(),
//                            InetSocketAddress.createUnresolved(config.getSshHostAddr(),
//                                                               config.getSshPort()),
//                            config.getSshIdleTimeout(),
//                            config.getSshAlgorithm(),
//                            receivePackFactory,
//                            uploadPackFactory,
//                            getRepositoryResolver(),
//                            executorService,
//                            config.getGitSshCiphers(),
//                            config.getGitSshMACs());
//
//        gitSSHService.start();
//    }
}
