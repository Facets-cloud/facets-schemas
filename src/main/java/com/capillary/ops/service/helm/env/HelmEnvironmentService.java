package com.capillary.ops.service.helm.env;

import com.capillary.ops.bo.helm.ApplicationFamily;
import com.capillary.ops.bo.helm.FamilyEnvironment;

import java.util.List;

public interface HelmEnvironmentService {

    List<FamilyEnvironment> getEnvironmentsForFamily(ApplicationFamily applicationFamily, String appName);

}
