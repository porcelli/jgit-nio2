package me.porcelli.nio.jgit;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.porcelli.nio.jgit.daemon.GitDaemon;
import me.porcelli.nio.jgit.impl.daemon.RepositoryResolverImpl;
import me.porcelli.nio.jgit.impl.daemon.git.Daemon;
import me.porcelli.nio.jgit.impl.daemon.git.DaemonClient;
import me.porcelli.nio.jgit.impl.daemon.git.GitDaemonImpl;
import me.porcelli.nio.jgit.impl.util.DescriptiveThreadFactory;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import static me.porcelli.nio.jgit.daemon.Daemon.DEFAULT_HOST_ADDR;
import static me.porcelli.nio.jgit.daemon.Daemon.DEFAULT_HOST_NAME;
import static me.porcelli.nio.jgit.daemon.GitDaemon.DEFAULT_DAEMON_DEFAULT_PORT;

public class DaemonBuilder {

    private DaemonBuilder() {
    }

    private static RepositoryResolverImpl repositoryResolver = new RepositoryResolverImpl(JGitFileSystemBuilder.PROVIDER.getFSManager());

    public static GitDaemon buildGitDaemon() {
        return buildGitDaemon(DEFAULT_HOST_ADDR, DEFAULT_DAEMON_DEFAULT_PORT);
    }

    public static GitDaemon buildGitDaemon(String hostName) {
        return buildGitDaemon(DEFAULT_HOST_NAME, DEFAULT_DAEMON_DEFAULT_PORT);
    }

    public static GitDaemon buildGitDaemon(String hostName, int hostPort) {
        return setupDaemon(hostPort);
    }

    private static GitDaemon setupDaemon(int port) {
        return buildAndStartDaemon(DEFAULT_HOST_ADDR,
                                   port,
                                   Executors.newCachedThreadPool(new DescriptiveThreadFactory()));
    }

    static GitDaemon buildAndStartDaemon(final String daemonHostAddr,
                                         final int daemonPort,
                                         final ExecutorService executorService) {
        Daemon daemonService = new Daemon(new InetSocketAddress(daemonHostAddr,
                                                                daemonPort),
                                          executorService);
        final GitDaemonImpl gitDaemon = new GitDaemonImpl(daemonService, daemonHostAddr, DEFAULT_HOST_NAME);
        daemonService.setRepositoryResolver(new RepositoryResolver<DaemonClient>() {
            @Override
            public Repository open(DaemonClient req, String name)
                    throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
                if (gitDaemon.isAccepted(name)) {
                    return repositoryResolver.open(req, name);
                }
                throw new RepositoryNotFoundException("s");
            }
        });
        try {
            daemonService.start();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return gitDaemon;
    }
}
