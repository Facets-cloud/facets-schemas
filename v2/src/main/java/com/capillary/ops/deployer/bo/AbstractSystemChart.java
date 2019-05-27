package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSystemChart {

    protected String chartPath;
    protected Map<String, Object> values = new HashMap<>();

    public abstract String getChartPath();

    public void setChartPath(String chartPath) {
        this.chartPath = chartPath;
    }

    public abstract Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment);

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public abstract String getReleaseName(ApplicationFamily applicationFamily, Environment environment);
}
