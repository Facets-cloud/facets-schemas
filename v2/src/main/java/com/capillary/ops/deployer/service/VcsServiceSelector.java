package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.VCSProvider;
import com.capillary.ops.deployer.service.impl.BitbucketVcsService;
import com.capillary.ops.deployer.service.impl.GithubVcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VcsServiceSelector {

    @Autowired
    private GithubVcsService githubVcsService;

    @Autowired
    private BitbucketVcsService bitbucketVcsService;

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
