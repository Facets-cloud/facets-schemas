package com.capillary.ops.cp.bo.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

import javax.validation.constraints.NotNull;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomOAuth2ClientRegistration {
    @Id
    @NotNull
    private String registrationId;
    @NotNull
    private CommonOAuth2Provider provider;
    @NotNull
    private String clientId;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clientSecret;
    @NotNull
    private String scope;
    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isSystemConfigured = false;

    @NotNull
    private String loginButtonText;

    public CustomOAuth2ClientRegistration(String registrationId) {
        this.registrationId = registrationId;
    }

    public CustomOAuth2ClientRegistration() {
    }
}
