package com.capillary.ops.controller;

import com.capillary.ops.bo.helm.CrmHelmApplication;
import com.capillary.ops.bo.helm.HelmApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelmApplicationController {

    @PostMapping("/application/ecommerce")
    public ResponseEntity<HelmApplication> createEcomApplication(@RequestBody HelmApplication helmApplication) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/application/integration")
    public ResponseEntity<HelmApplication> createCrmApplication(@RequestBody HelmApplication helmApplication) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/application/crm")
    public ResponseEntity<CrmHelmApplication> createCrmApplication(@RequestBody CrmHelmApplication crmHelmApplication) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
