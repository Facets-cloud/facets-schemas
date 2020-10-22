package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.OverrideObject;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.repository.OverrideObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class OverrideService {

    @Autowired
    private OverrideObjectRepository overrideObjectRepository;

    private static final Logger logger = LoggerFactory.getLogger(OverrideService.class);

    public OverrideObject save(String clusterId, OverrideRequest request) {
        if (request == null || request.getOverrides() == null) {
            throw new IllegalArgumentException("Wrong OverrideRequest" + request);
        }

        Optional<OverrideObject> existing = overrideObjectRepository
            .findOneByClusterIdAndResourceTypeAndResourceName(clusterId, request.getResourceType(),
                request.getResourceName());

        OverrideObject overrideObject;
        if (existing.isPresent()) {
            overrideObject = existing.get();
        } else {
            overrideObject = new OverrideObject();
            overrideObject.setClusterId(clusterId);
            overrideObject.setResourceType(request.getResourceType());
            overrideObject.setResourceName(request.getResourceName());
        }
        overrideObject.setOverrides(request.getOverrides());
        return overrideObjectRepository.save(overrideObject);
    }

    public List<OverrideObject> findAllByClusterId(String clusterId) {
        return overrideObjectRepository.findAllByClusterId(clusterId);
    }

    public List<OverrideObject> delete(String clusterId, String resourceType, String resourceName) {
        List<OverrideObject> deletedOverrides = overrideObjectRepository.deleteByClusterIdAndResourceTypeAndResourceName(
                clusterId, resourceType, resourceName);
        logger.info("deleted following overrides: {}", deletedOverrides);

        return deletedOverrides;
    }
}
