package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.components.SupportedVersions;
import com.capillary.ops.cp.controller.MetaController;
import com.capillary.ops.cp.facade.MetaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/meta")
public class UiMetaController {

    @Autowired
    private MetaController metaController;

    @GetMapping("/components/{componentType}/supportedVersion")
    public SupportedVersions getSupportedComponentVersion(@PathVariable ComponentType componentType) {
        return metaController.getSupportedComponentVersion(componentType);
    }

    @GetMapping("/components/supportedVersion")
    public List<SupportedVersions> getSupportedComponentVersions() {
        return metaController.getSupportedComponentVersions();
    }
}
