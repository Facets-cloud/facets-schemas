package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.auth.CustomClientRegistrationService;
import com.capillary.ops.cp.bo.auth.CustomOAuth2ClientRegistration;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("public/v1/")
@Loggable
public class PublicAPIs {


    @Autowired
    CustomClientRegistrationService customClientRegistrationService;

    @Value("${facets_only:false}")
    private Boolean isFacetsOnly;

    @Value("${logo_url:https://www.facets.cloud/assets/img/logo_blue.png}")
    private String logo;

    @GetMapping("logo")
    public String getLogo() {
        if(isFacetsOnly){
            return logo;
        }
        return "https://www.capillarytech.com/img/CapillaryTech.svg";

    }

    @GetMapping("loginOptions")
    public List<CustomOAuth2ClientRegistration> getLoginOptions() {
        return customClientRegistrationService.getAllIntegrations();
    }
}
