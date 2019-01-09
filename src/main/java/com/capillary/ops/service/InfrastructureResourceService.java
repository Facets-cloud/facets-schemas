package com.capillary.ops.service;

import com.capillary.ops.bo.AbstractInfrastructureResource;

import java.util.List;

public interface InfrastructureResourceService {

    public AbstractInfrastructureResource create(
        AbstractInfrastructureResource resource);

    public AbstractInfrastructureResource update(
        AbstractInfrastructureResource resource);

    public List<AbstractInfrastructureResource> findAll();

    public AbstractInfrastructureResource findById(String id);
}
