package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.Artifactory;
import com.capillary.ops.cp.bo.ECRArtifactory;
import com.capillary.ops.cp.controller.ArtifactController;
import com.capillary.ops.cp.controller.ArtifactoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/artifactories")
public class UiArtifactoryController {
    @Autowired
    private ArtifactoryController artifactoryController;

    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping()
    public ECRArtifactory createECRArtifactory(@RequestBody ECRArtifactory ecrArtifactory) {
        return artifactoryController.createECRArtifactory(ecrArtifactory);
    }

    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping("{artifactoryId}")
    public ECRArtifactory updateECRArtifactory(@RequestBody ECRArtifactory ecrArtifactory, @PathVariable String artifactoryId) {
        return artifactoryController.updateECRArtifactory(ecrArtifactory, artifactoryId);
    }

    @GetMapping()
    public List<Artifactory> getAllArtifactories() {
        return artifactoryController.getAllArtifactories();
    }
}
