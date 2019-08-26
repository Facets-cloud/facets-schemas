package com.capillary.ops.deployer.service.impl;

import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.VcsService;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GithubVcsService implements VcsService {

    private static final Logger logger = LoggerFactory.getLogger(GithubVcsService.class);

    @Override
    public List<String> getBranches(String owner, String repository) throws IOException {
        String username = System.getenv("GIT_USERNAME");
        String password = System.getenv("GIT_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("github credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Github credentials to use this api");
        }

        RepositoryService repositoryService = new RepositoryService();
        repositoryService.getClient().setCredentials(username, password);

        RepositoryId repositoryId = new RepositoryId(owner, repository);
        logger.debug("fetching github branches");
        List<RepositoryBranch> branches = repositoryService.getBranches(repositoryId);
        logger.info("fetched {} branches from github for this application", branches.size());

        return branches.parallelStream().map(x -> x.getName()).collect(Collectors.toList());
    }

    @Override
    public List<String> getTags(String owner, String repository) throws IOException {
        String username = System.getenv("GIT_USERNAME");
        String password = System.getenv("GIT_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("github credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Github credentials to use this api");
        }

        RepositoryService repositoryService = new RepositoryService();
        repositoryService.getClient().setCredentials(username, password);

        RepositoryId repositoryId = new RepositoryId(owner, repository);
        logger.debug("fetching github tags");
        List<RepositoryTag> tags = repositoryService.getTags(repositoryId);
        logger.info("fetched {} tags from github for this application", tags.size());

        return tags.parallelStream().map(x -> x.getName()).collect(Collectors.toList());
    }
}
