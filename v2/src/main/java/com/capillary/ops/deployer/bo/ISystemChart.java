package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public interface ISystemChart {

    public abstract String getChartPath();

    public abstract Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment);

    public abstract String getReleaseName(ApplicationFamily applicationFamily, Environment environment);
}
