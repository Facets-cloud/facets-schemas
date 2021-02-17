package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.Artifactory;
import com.capillary.ops.cp.bo.ECRArtifactory;
import com.capillary.ops.cp.facade.ArtifactFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cc/v1/artifactories")
public class ArtifactoryController {

    @Autowired
    private ArtifactFacade artifactFacade;

    @PostMapping()
    public ECRArtifactory createECRArtifactory(@RequestBody ECRArtifactory ecrArtifactory) {
        return artifactFacade.createECRArtifactory(ecrArtifactory);
    }

    @PutMapping("{artifactoryId}")
    public ECRArtifactory updateECRArtifactory(@RequestBody ECRArtifactory ecrArtifactory, @PathVariable String artifactoryId) {
        return artifactFacade.updateECRArtifactory(ecrArtifactory, artifactoryId);
    }

    @GetMapping()
    public List<Artifactory> getAllArtifactories() {
        return artifactFacade.getAllArtifactories();
    }
}
