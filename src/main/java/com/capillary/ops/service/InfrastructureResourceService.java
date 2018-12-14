package com.capillary.ops.service;

import com.capillary.ops.bo.AbstractInfrastructureResource;

public interface InfrastructureResourceService {
    public AbstractInfrastructureResource create(AbstractInfrastructureResource resource);

    public AbstractInfrastructureResource update(AbstractInfrastructureResource resource);
}
