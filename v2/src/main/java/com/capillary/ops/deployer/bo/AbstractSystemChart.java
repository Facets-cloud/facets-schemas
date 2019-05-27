package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSystemChart {

    protected String name;
    protected String chartPath;
    protected Map<String, Object> values = new HashMap<>();
    protected Map<String, Object> config = new HashMap<>();

    public abstract String getName();

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getChartPath();

    public void setChartPath(String chartPath) {
        this.chartPath = chartPath;
    }

    public abstract Map<String, Object> getValues(ApplicationFamily applicationFamily, Environment environment);

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public abstract String getReleaseName(ApplicationFamily applicationFamily, Environment environment);
}
