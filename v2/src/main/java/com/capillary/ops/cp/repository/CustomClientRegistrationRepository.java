package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.ClusterTask;
import com.capillary.ops.cp.bo.auth.CustomOAuth2ClientRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Optional;

public interface CustomClientRegistrationRepository extends MongoRepository<CustomOAuth2ClientRegistration, String>  {
    Optional<CustomOAuth2ClientRegistration> findByRegistrationId(String registrationId);
}
