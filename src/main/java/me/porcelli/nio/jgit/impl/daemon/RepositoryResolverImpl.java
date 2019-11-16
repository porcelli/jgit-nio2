package me.porcelli.nio.jgit.impl.daemon;

import java.nio.file.FileSystem;

import javax.servlet.http.HttpServletRequest;

import me.porcelli.nio.jgit.impl.JGitFileSystem;
import me.porcelli.nio.jgit.impl.daemon.ssh.BaseGitCommand;
import me.porcelli.nio.jgit.impl.manager.JGitFileSystemsManager;
import me.porcelli.nio.jgit.security.FileSystemAuthorization;
import me.porcelli.nio.jgit.security.User;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;

public class RepositoryResolverImpl<T> implements RepositoryResolver<T> {

    private JGitFileSystemsManager fsManager;
    private FileSystemAuthorization authorizer = (fs, user) -> true;

    public RepositoryResolverImpl(JGitFileSystemsManager fsManager) {
        this.fsManager = fsManager;
    }

    @Override
    public Repository open(final T client,
                           final String name)
            throws RepositoryNotFoundException, ServiceNotAuthorizedException {
        final User user = extractUser(client);
        final JGitFileSystem fs = fsManager.get(name);
        if (fs == null) {
            throw new RepositoryNotFoundException(name);
        }

        if (authorizer != null && !authorizer.authorize(fs,
                                                        user)) {
            throw new ServiceNotAuthorizedException("User not authorized.");
        }

        return fs.getGit().getRepository();
    }

    public JGitFileSystem resolveFileSystem(final Repository repository) {
        return fsManager.get(repository);
    }

    private User extractUser(Object client) {
        if (client instanceof HttpServletRequest) {
            return null;
        } else if (client instanceof BaseGitCommand) {
            return ((BaseGitCommand) client).getUser();
        }

        return () -> "ANONYMOUS";
    }

}
