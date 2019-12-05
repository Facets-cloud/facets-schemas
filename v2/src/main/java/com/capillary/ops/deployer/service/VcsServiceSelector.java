package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.VCSProvider;
import com.capillary.ops.deployer.service.impl.BitbucketVcsService;
import com.capillary.ops.deployer.service.impl.GithubVcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!dev")
@Service
public class VcsServiceSelector implements IVcsServiceSelector {

    @Autowired
    private GithubVcsService githubVcsService;

    @Autowired
    private BitbucketVcsService bitbucketVcsService;

    @Override
    public VcsService selectVcsService(VCSProvider vcsProvider) {
        switch (vcsProvider) {
            case GITHUB:
                return githubVcsService;
            case BITBUCKET:
                return bitbucketVcsService;
        }

        return githubVcsService;
    }
}
