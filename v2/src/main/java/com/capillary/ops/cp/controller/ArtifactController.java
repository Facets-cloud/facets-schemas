package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.facade.ArtifactFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cc/v1/artifacts")
public class ArtifactController {

    @Autowired
    private ArtifactFacade artifactFacade;

    @PostMapping("/register")
    public void registerArtifact(@RequestBody Artifact artifact){
        artifactFacade.registerArtifact(artifact);
    }
}
