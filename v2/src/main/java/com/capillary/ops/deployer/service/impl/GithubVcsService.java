package com.capillary.ops.deployer.service.impl;

import com.capillary.ops.deployer.service.VcsService;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GithubVcsService implements VcsService {
    @Override
    public List<String> getBranches(String owner, String repository) throws IOException {
        String username = System.getenv("GIT_USERNAME");
        String password = System.getenv("GIT_PASSWORD");

        RepositoryService repositoryService = new RepositoryService();
        repositoryService.getClient().setCredentials(username, password);

        RepositoryId repositoryId = new RepositoryId(owner, repository);
        List<RepositoryBranch> branches = repositoryService.getBranches(repositoryId);

        return branches.parallelStream().map(x -> x.getName()).collect(Collectors.toList());
    }

    @Override
    public List<String> getTags(String owner, String repository) throws IOException {
        String username = System.getenv("GIT_USERNAME");
        String password = System.getenv("GIT_PASSWORD");

        RepositoryService repositoryService = new RepositoryService();
        repositoryService.getClient().setCredentials(username, password);

        RepositoryId repositoryId = new RepositoryId(owner, repository);
        List<RepositoryTag> tags = repositoryService.getTags(repositoryId);

        return tags.parallelStream().map(x -> x.getName()).collect(Collectors.toList());
    }
}
