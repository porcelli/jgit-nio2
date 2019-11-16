package me.porcelli.nio.jgit.impl.daemon.git;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import me.porcelli.nio.jgit.daemon.GitDaemon;
import me.porcelli.nio.jgit.impl.daemon.BaseDaemonBuilder;
import me.porcelli.nio.jgit.impl.daemon.DaemonImpl;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import static me.porcelli.nio.jgit.daemon.Daemon.DEFAULT_HOST_NAME;

public class GitDaemonBuilder extends BaseDaemonBuilder {

    public static GitDaemon buildAndStartDaemon(final String daemonHostAddr,
                                                final int daemonPort,
                                                final ExecutorService executorService) {
        Daemon daemonService = new Daemon(new InetSocketAddress(daemonHostAddr,
                                                                daemonPort),
                                          executorService);
        final GitDaemonImpl gitDaemon = new GitDaemonImpl(daemonService, daemonHostAddr, DEFAULT_HOST_NAME);
        daemonService.setRepositoryResolver(getRepositoryResolver(gitDaemon));
        try {
            daemonService.start();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return gitDaemon;
    }

}