package com.capillary.ops.service.helm;

import com.capillary.ops.bo.helm.HelmApplication;

public interface HelmAppCreationService {

    public HelmApplication create(HelmApplication helmApplication);
}
