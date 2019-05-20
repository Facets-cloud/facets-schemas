package com.capillary.ops.controller.helm;

import com.capillary.ops.bo.helm.BuildStatus;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications/build")
public class HelmBuildController {

  // Async call - returns build number
  @PostMapping("/trigger")
  public ResponseEntity<BuildStatus> triggerBuild(
      @RequestParam(name = "appName") String appName,
      @RequestParam(name = "branch") String branch) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/status/{buildId}")
  public ResponseEntity<BuildStatus> getBuildStatus(@PathVariable Long buildId) {
    return new ResponseEntity<>(new BuildStatus(), HttpStatus.OK);
  }

  // Only return pending builds if pending=true
  @GetMapping("/status/{appName}")
  public ResponseEntity<List<BuildStatus>> getAllBuildStatusesForApp(
      @PathVariable String appName, @RequestParam(required = false) Boolean pending) {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
