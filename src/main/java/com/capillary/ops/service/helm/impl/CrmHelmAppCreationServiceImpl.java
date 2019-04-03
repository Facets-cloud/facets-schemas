package com.capillary.ops.service.helm.impl;

import com.capillary.ops.bo.helm.CrmHelmApplication;
import com.capillary.ops.bo.helm.HelmApplication;
import com.capillary.ops.service.helm.HelmAppCreationService;
import org.springframework.stereotype.Service;

@Service
public class CrmHelmAppCreationServiceImpl extends BaseHelmAppCreationServiceImpl
    implements HelmAppCreationService {

  @Override
  public CrmHelmApplication create(HelmApplication helmApplication) {
    CrmHelmApplication application = (CrmHelmApplication) helmApplication;
    return null;
  }
}
