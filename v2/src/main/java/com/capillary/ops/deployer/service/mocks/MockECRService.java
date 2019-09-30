package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.service.interfaces.IECRService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    @Override
    public String findImageBetweenTimes(Application application, Instant from, Instant to) {
        return "someimage";
    }

    @Override
    public void syncToChinaECR(String ImageURL) {}
}
