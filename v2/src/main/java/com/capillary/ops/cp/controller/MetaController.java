package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.components.SupportedVersions;
import com.capillary.ops.cp.facade.MetaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("cc/v1/meta")
public class MetaController {

    @Autowired
    private MetaFacade metaFacade;

    @GetMapping("/components/{componentType}/supportedVersion")
    public SupportedVersions getSupportedComponentVersion(@PathVariable ComponentType componentType) {
        return metaFacade.getSupportedComponentVersions(componentType);
    }

    @GetMapping("/components/supportedVersion")
    public List<SupportedVersions> getSupportedComponentVersions() {
        return metaFacade.getSupportedComponentVersions();
    }
}
