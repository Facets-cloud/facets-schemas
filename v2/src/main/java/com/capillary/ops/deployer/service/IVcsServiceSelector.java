package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.VCSProvider;

public interface IVcsServiceSelector {
    VcsService selectVcsService(VCSProvider vcsProvider);
}
