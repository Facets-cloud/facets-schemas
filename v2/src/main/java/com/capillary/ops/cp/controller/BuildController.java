package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.facade.BuildFacade;
import com.capillary.ops.deployer.bo.Build;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cc/v1/build/")
public class BuildController {

    @Autowired
    BuildFacade buildFacade;

    /**
     * Get build from deployer
     *
     * @param applicationId Id of application in Deployer
     * @return
     */
    @GetMapping("/deployer/{applicationId}")
    synchronized Build getImageFromDeployer(@PathVariable String applicationId, @RequestParam BuildStrategy strategy,
        @RequestParam(defaultValue = "RELEASE") ReleaseType releaseType) {
        //        return  "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/demoapiservice:101e298";
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buildFacade.getImageFromDeployer(applicationId, strategy, releaseType);
    }

}
