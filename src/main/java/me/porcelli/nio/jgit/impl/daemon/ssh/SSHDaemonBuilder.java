package me.porcelli.nio.jgit.impl.daemon.ssh;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import me.porcelli.nio.jgit.daemon.SSHDaemon;
import me.porcelli.nio.jgit.impl.JGitFileSystem;
import me.porcelli.nio.jgit.impl.daemon.BaseDaemonBuilder;
import org.eclipse.jgit.transport.UploadPack;
import org.eclipse.jgit.transport.resolver.ReceivePackFactory;
import org.eclipse.jgit.transport.resolver.UploadPackFactory;

public class SSHDaemonBuilder extends BaseDaemonBuilder {

    public static SSHDaemon buildAndStartSSH(String fileCert,
                                             String hostAddr,
                                             int port,
                                             int iddleTimeout,
                                             String sshAlgo,
                                             String chiphers,
                                             String macs,
                                             final ExecutorService executorService) {
        final ReceivePackFactory receivePackFactory = (req, db) -> getReceivePack("ssh", req, db);

        final UploadPackFactory uploadPackFactory = (UploadPackFactory<BaseGitCommand>) (req, db) -> new UploadPack(db) {{
            final JGitFileSystem fs = getFSManager().get(db);
            fs.filterBranchAccess(this,
                                  req.getUser());
        }};

        GitSSHService gitSSHService = new GitSSHService();

        final SSHDaemonImpl sshDaemon = new SSHDaemonImpl(gitSSHService, hostAddr, hostAddr, port);

        gitSSHService.setup(new File(fileCert),
                            InetSocketAddress.createUnresolved(hostAddr,
                                                               port),
                            iddleTimeout,
                            sshAlgo,
                            receivePackFactory,
                            uploadPackFactory,
                            getRepositoryResolver(sshDaemon),
                            executorService,
                            chiphers,
                            macs);

        gitSSHService.start();

        return sshDaemon;
    }
}
