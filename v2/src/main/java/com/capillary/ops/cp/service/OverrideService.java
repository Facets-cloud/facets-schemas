package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.OverrideObject;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.repository.OverrideObjectRepository;
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

    public OverrideObject save(String clusterId, OverrideRequest request) {
        if (request == null || request.getOverrides() == null) {
            throw new IllegalArgumentException("Wrong OverrideRequest" + request);
        }
        Optional<OverrideObject> existing = overrideObjectRepository
            .findOneByClusterIdAndResourceTypeAndResourceName(clusterId, request.getResourceType(),
                request.getResourceName());
        OverrideObject toSave;
        if (!existing.isPresent()) {
            toSave = new OverrideObject();
            toSave.setClusterId(clusterId);
            toSave.setResourceType(request.getResourceType());
            toSave.setResourceName(request.getResourceName());
            toSave.setOverrides(request.getOverrides());
        } else {
            toSave = existing.get();
            Map<String, Object> existingOverrides = toSave.getOverrides();
            Map<String, Object> newOverrides = request.getOverrides();

            if (existingOverrides != null) {
                Set<String> keys = newOverrides.keySet();
                //TODO: change this to deep merge if required
                keys.stream().forEach(k -> existingOverrides.put(k, newOverrides.get(k)));
                toSave.setOverrides(existingOverrides);
            } else {
                toSave.setOverrides(newOverrides);
            }
        }
        return overrideObjectRepository.save(toSave);

    }

    public List<OverrideObject> findAllByClusterId(String clusterId) {
        return overrideObjectRepository.findAllByClusterId(clusterId);
    }
}
