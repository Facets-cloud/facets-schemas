package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.auth.CustomClientRegistrationService;
import com.capillary.ops.cp.bo.auth.CustomOAuth2ClientRegistration;
import com.capillary.ops.cp.bo.components.SupportedVersions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/oauth")
public class UiOAuthController {

    @Autowired
    CustomClientRegistrationService customClientRegistrationService;

    @GetMapping()
    public List<CustomOAuth2ClientRegistration> getAllIntegrations() {
        return customClientRegistrationService.getAllIntegrations();
    }

    @PostMapping
    public List<CustomOAuth2ClientRegistration> addIntegrations(@RequestBody CustomOAuth2ClientRegistration client) {
        return customClientRegistrationService.addNewIntegration(client);
    }

    @PutMapping("/{registrationId}")
    public List<CustomOAuth2ClientRegistration> updateIntegrations(
            @PathVariable String registrationId,
            @RequestBody CustomOAuth2ClientRegistration client) {
        return customClientRegistrationService.editIntegration(registrationId, client);
    }

    @DeleteMapping("/{registrationId}")
    public List<CustomOAuth2ClientRegistration> deleteIntegrations(
            @PathVariable String registrationId) {
        return customClientRegistrationService.deleteIntegration(registrationId);
    }

}
