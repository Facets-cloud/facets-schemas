package com.capillary.ops.bo.helm;

import java.util.List;
import java.util.Map;

public class CrmHelmApplication extends HelmApplication {

    public CrmHelmApplication() {
    }

    public CrmHelmApplication(ApplicationFamily applicationFamily, String name, String instanceType, Integer replicas, Map<String, Object> configs, List<String> domains, ExposureType exposureType, SourceType sourceType, String sourceUrl, Map<String, Object> params) {
        super(applicationFamily, name, instanceType, replicas, configs, domains, exposureType, sourceType, sourceUrl);
        this.params = params;
    }

    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
