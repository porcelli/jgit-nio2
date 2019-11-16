package me.porcelli.nio.jgit.impl.daemon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.porcelli.nio.jgit.impl.JGitFileSystem;
import me.porcelli.nio.jgit.impl.JGitFileSystemProvider;
import me.porcelli.nio.jgit.impl.manager.JGitFileSystemsManager;
import me.porcelli.nio.jgit.security.User;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

public abstract class BaseDaemonBuilder {

    private static RepositoryResolverImpl repositoryResolver = new RepositoryResolverImpl(JGitFileSystemProvider.PROVIDER.getFSManager());

    public static <T> ReceivePack getReceivePack(final String protocol, final T req, final Repository db) {
        return new ReceivePack(db) {
            {

                final JGitFileSystem fs = getFSManager().get(db);
                final Map<String, RevCommit> oldTreeRefs = new HashMap<>();

                setPreReceiveHook((rp, commands2) -> {
                    fs.lock();
                    final User user = extractUser(req);
                    for (final ReceiveCommand command : commands2) {
                        fs.checkBranchAccess(command,
                                             user);
                        final RevCommit lastCommit = fs.getGit().getLastCommit(command.getRefName());
                        oldTreeRefs.put(command.getRefName(),
                                        lastCommit);
                    }
                });

                setPostReceiveHook((rp, commands) -> {
                    fs.unlock();
                    fs.notifyExternalUpdate();
                    final User user = extractUser(req);
                    for (Map.Entry<String, RevCommit> oldTreeRef : oldTreeRefs.entrySet()) {
                        final List<RevCommit> commits = fs.getGit().listCommits(oldTreeRef.getValue(),
                                                                                fs.getGit().getLastCommit(oldTreeRef.getKey()));
                        for (final RevCommit revCommit : commits) {
                            final RevTree parent = revCommit.getParentCount() > 0 ? revCommit.getParent(0).getTree() : null;
                            JGitFileSystemProvider.PROVIDER.notifyDiffs(fs,
                                                                        oldTreeRef.getKey(),
                                                                        "<" + protocol + ">",
                                                                        user.getIdentifier(),
                                                                        revCommit.getFullMessage(),
                                                                        parent,
                                                                        revCommit.getTree());
                        }
                    }
                });
            }
        };
    }

    public static RepositoryResolver getRepositoryResolver(DaemonImpl daemon) {
        return (req, name) -> {
            if (daemon.isAccepted(name)) {
                return repositoryResolver.open(req, name);
            }
            throw new RepositoryNotFoundException(name);
        };
    }

    public static JGitFileSystemsManager getFSManager() {
        return JGitFileSystemProvider.PROVIDER.getFSManager();
    }

    private static User extractUser(Object client) {
//        if (httpAuthenticator != null && client instanceof HttpServletRequest) {
//            return httpAuthenticator.getUser();
//        } else if (client instanceof BaseGitCommand) {
//            return ((BaseGitCommand) client).getUser();
//        }

        return () -> "ANONYMOUS";
    }
}
