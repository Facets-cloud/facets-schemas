package com.capillary.ops.cp.auth;

import com.capillary.ops.cp.bo.auth.CustomOAuth2ClientRegistration;
import com.capillary.ops.cp.repository.CustomClientRegistrationRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class CustomClientRegistrationServiceTest {

    @InjectMocks
    CustomClientRegistrationService customClientRegistrationService;
    @Mock
    CustomClientRegistrationRepository customClientRegistrationRepository;
    @Mock
    CustomClientRegistrationRepository clientRegistrationRepository;

    CustomOAuth2ClientRegistration registration =
            CustomOAuth2ClientRegistration.builder().registrationId("rid").clientId("cid").clientSecret("cst")
                    .provider(CommonOAuth2Provider.GOOGLE).scope("email").build();

    @Test
    public void testInitNoValues() {
        ReflectionTestUtils.setField(customClientRegistrationService, "googleClientId", null);
        ReflectionTestUtils.setField(customClientRegistrationService, "googleClientSecret", null);
        customClientRegistrationService.init();
        verify(customClientRegistrationRepository, times(0)).save(any());
    }

    @Test
    public void testInitWithPropertiesValue() {
        ReflectionTestUtils.setField(customClientRegistrationService, "googleClientId", "foo");
        ReflectionTestUtils.setField(customClientRegistrationService, "googleClientSecret", "foo");
        when(clientRegistrationRepository.findByRegistrationId("google")).thenReturn(Optional.empty());
        customClientRegistrationService.init();
        verify(customClientRegistrationRepository, times(1)).save(any());
    }

    @Test
    public void testInitWithAlreadyExisting() {
        ReflectionTestUtils.setField(customClientRegistrationService, "googleClientId", "foo");
        ReflectionTestUtils.setField(customClientRegistrationService, "googleClientSecret", "foo");
        when(clientRegistrationRepository.findByRegistrationId("google")).thenReturn(Optional.of(registration));
        customClientRegistrationService.init();
        verify(customClientRegistrationRepository, times(0)).save(any());
    }

    @Test
    public void testFindByRegistrationIdNonExisting() {
        when(clientRegistrationRepository.findByRegistrationId("xyz")).thenReturn(Optional.empty());
        ClientRegistration xyz = customClientRegistrationService.findByRegistrationId("xyz");
        assert xyz == null;
    }

    @Test
    public void testFindByRegistrationIdValid() {
        registration.setProvider(CommonOAuth2Provider.GOOGLE);
        when(clientRegistrationRepository.findByRegistrationId("xyz")).thenReturn(Optional.of(registration));
        ClientRegistration xyz = customClientRegistrationService.findByRegistrationId("xyz");
        assert xyz.getClientId().equals(registration.getClientId());
    }

}