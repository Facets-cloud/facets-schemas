package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.components.SupportedVersions;
import com.capillary.ops.cp.repository.ComponentVersionRepository;
import com.capillary.ops.cp.service.MetaService;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetaFacade {

    @Autowired
    private MetaService metaService;

    public List<SupportedVersions> getSupportedComponentVersions() {
        return metaService.getSupportedComponentVersions();
    }

    public SupportedVersions getSupportedComponentVersions(ComponentType componentType) {
        return metaService.getSupportedComponentVersions(componentType);
    }
}
