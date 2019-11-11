package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public interface ISystemChart {

    String getChartPath();

    Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment);

    String getReleaseName(ApplicationFamily applicationFamily, Environment environment);
}
