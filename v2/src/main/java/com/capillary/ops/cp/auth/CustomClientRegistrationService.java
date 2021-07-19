package com.capillary.ops.cp.auth;

import com.capillary.ops.cp.bo.auth.CustomOAuth2ClientRegistration;
import com.capillary.ops.cp.repository.CustomClientRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class CustomClientRegistrationService implements ClientRegistrationRepository {

    public static final String GOOGLE = "google";
    @Autowired
    CustomClientRegistrationRepository clientRegistrationRepository;

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    String googleClientSecret;

    @Autowired
    CustomClientRegistrationRepository customClientRegistrationRepository;

    public List<CustomOAuth2ClientRegistration> addNewIntegration(CustomOAuth2ClientRegistration client) {
        Optional<CustomOAuth2ClientRegistration> byRegistrationId = customClientRegistrationRepository
                .findByRegistrationId(client.getRegistrationId());
        if (byRegistrationId.isPresent()) {
            throw new RuntimeException("This Integration is already present, please edit the same.");
        }
        customClientRegistrationRepository.save(client);
        return this.getAllIntegrations();
    }

    public List<CustomOAuth2ClientRegistration> deleteIntegration(String registrationId) {
        Optional<CustomOAuth2ClientRegistration> byRegistrationId = customClientRegistrationRepository
                .findByRegistrationId(registrationId);
        if (!byRegistrationId.isPresent()) {
            throw new RuntimeException("This Integration is not present, please create the same.");
        }
        if(byRegistrationId.get().isSystemConfigured()){
            throw new RuntimeException("System defined integration cannot be deleted");
        }
        customClientRegistrationRepository.delete(byRegistrationId.get());
        return this.getAllIntegrations();
    }

    public List<CustomOAuth2ClientRegistration> editIntegration(String registrationId,
                                                                CustomOAuth2ClientRegistration client) {
        Optional<CustomOAuth2ClientRegistration> byRegistrationId = customClientRegistrationRepository
                .findByRegistrationId(registrationId);
        if (!byRegistrationId.isPresent()) {
            throw new RuntimeException("This Integration is not present, please create the same.");
        }
        client.setSystemConfigured(byRegistrationId.get().isSystemConfigured());
        customClientRegistrationRepository.save(client);
        return this.getAllIntegrations();
    }

    public List<CustomOAuth2ClientRegistration> getAllIntegrations() {
        return customClientRegistrationRepository.findAll();
    }

    @PostConstruct
    public void init() {
        boolean isGoogleClientIdPresent = googleClientId != null && !googleClientId.isEmpty()
                && !("dummy".equalsIgnoreCase(googleClientId));
        boolean isGoogleClientSecretPresent = googleClientSecret != null && !googleClientSecret.isEmpty();
        if (isGoogleClientIdPresent && isGoogleClientSecretPresent) {
            Optional<CustomOAuth2ClientRegistration> google = clientRegistrationRepository.findByRegistrationId(GOOGLE);
            if (!google.isPresent()) {
                // Add the configured account in DB
                CustomOAuth2ClientRegistration customOAuth2ClientRegistration =
                        CustomOAuth2ClientRegistration.builder().clientId(googleClientId)
                                .clientSecret(googleClientSecret).registrationId(GOOGLE).isSystemConfigured(true)
                                .provider(CommonOAuth2Provider.GOOGLE).scope("email").loginButtonText("Google Login")
                                .build();
                customClientRegistrationRepository.save(customOAuth2ClientRegistration);
            }
        }
    }

    @Override
    public ClientRegistration findByRegistrationId(String s) {
        Optional<CustomOAuth2ClientRegistration> registrationO = clientRegistrationRepository.findByRegistrationId(s);
        if (!registrationO.isPresent()) {
            return null;
        }
        CustomOAuth2ClientRegistration registration = registrationO.get();
        ClientRegistration.Builder builder = registration.getProvider().getBuilder(registration.getRegistrationId());
        builder.clientId(registration.getClientId());
        builder.clientSecret(registration.getClientSecret());
        builder.scope(registration.getScope());
        return builder.build();
    }
}
