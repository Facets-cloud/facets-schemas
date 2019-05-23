package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;

import java.time.Instant;
import java.util.List;

public interface IECRService {
    void createRepository(Application application);
    List<String> listImages(Application application);

    String findImageBetweenTimes(Application application, Instant from, Instant to);
}
