package com.capillary.ops.deployer.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SonarCallbackAccessController {

    @Value("${sonar.user}")
    private String sonarUser;

    @Value("${sonar.password}")
    private String sonarPassword;

    public boolean authenticate(HttpServletRequest request) throws IOException {
        String authorization = request.getHeader("Authorization");
        String base64encodedString = Base64.getEncoder().encodeToString(
                String.format("%s:%s", sonarUser, sonarPassword).getBytes(StandardCharsets.UTF_8));
        String expected = "Basic " + base64encodedString;
        return authorization.equals(expected);
    }
}
