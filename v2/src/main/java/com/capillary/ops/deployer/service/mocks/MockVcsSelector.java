package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.VCSProvider;
import com.capillary.ops.deployer.service.IVcsServiceSelector;
import com.capillary.ops.deployer.service.VcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class MockVcsSelector implements IVcsServiceSelector {
    @Autowired
    private MockVcsService mockVcsService;

    @Override
    public VcsService selectVcsService(VCSProvider vcsProvider) {
        return mockVcsService;
    }
}
