package com.capillary.ops.cp.service;

import com.jcabi.aspects.Loggable;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
@Loggable
public class GitService {

    /**
     * @param vcsUrl
     * @param user
     * @param appPassword
     * @return
     * @throws GitAPIException
     */
    public Path checkout(String vcsUrl, String user, String appPassword) throws GitAPIException {
        UUID uuid = UUID.randomUUID();
        File directory = new File("/tmp/repos/" + uuid);
        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI(vcsUrl);
        if (!user.isEmpty()) {
            cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, appPassword));
        }
        cloneCommand.setDirectory(directory);
        cloneCommand.call();
        return directory.toPath();
    }

    public String getBranchHead(String vcsUrl, String user, String appPassword, String branch) throws GitAPIException {
        LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository();
        lsRemoteCommand.setRemote(vcsUrl);
        lsRemoteCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, appPassword));
        Map<String, Ref> refs = lsRemoteCommand.callAsMap();
        return refs.get("refs/heads/" + branch).getObjectId().name();
    }
}
