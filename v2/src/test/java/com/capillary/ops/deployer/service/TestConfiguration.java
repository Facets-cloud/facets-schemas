package com.capillary.ops.deployer.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class TestConfiguration {

    @Bean
    public HttpServletRequest httpServletRequest() {
        return new MockHttpServletRequest();
    }
}
