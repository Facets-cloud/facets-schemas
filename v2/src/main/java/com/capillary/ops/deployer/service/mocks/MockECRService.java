package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.service.interfaces.IECRService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("dev")
@Service
public class MockECRService implements IECRService {
    @Override
    public void createRepository(Application application) {

    }

    @Override
    public List<String> listImages(Application application) {
        return Arrays.asList("someimage");
    }
}
