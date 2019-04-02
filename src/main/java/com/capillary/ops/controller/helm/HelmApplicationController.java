package com.capillary.ops.controller.helm;

import com.capillary.ops.bo.helm.CrmHelmApplication;
import com.capillary.ops.bo.helm.HelmApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications/create")
public class HelmApplicationController {

    @PostMapping("/ecommerce")
    public ResponseEntity<HelmApplication> createEcomApplication(@RequestBody HelmApplication helmApplication) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/integration")
    public ResponseEntity<HelmApplication> createIntegrationApplication(@RequestBody HelmApplication helmApplication) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/crm")
    public ResponseEntity<CrmHelmApplication> createCrmApplication(@RequestBody CrmHelmApplication crmHelmApplication) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
