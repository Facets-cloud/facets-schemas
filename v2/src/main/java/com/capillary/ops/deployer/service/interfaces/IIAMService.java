package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;

import java.time.Instant;
import java.util.List;

public interface IIAMService {
    String getApplicationRole(Application application, Environment environment);
}
