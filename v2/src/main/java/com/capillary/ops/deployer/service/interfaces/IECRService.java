package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;

import java.util.List;

public interface IECRService {
    void createRepository(Application application);
    List<String> listImages(Application application);
}
