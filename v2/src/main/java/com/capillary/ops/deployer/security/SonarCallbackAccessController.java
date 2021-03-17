package com.capillary.ops.deployer.security;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;

@Component
public class SonarCallbackAccessController {

    @Value("${sonarCallbackSecret}")
    private String sonarCallbackSecret;

    public boolean authenticate(HttpServletRequest request) throws IOException {
        String receivedSignature = request.getHeader("X-Sonar-Webhook-HMAC-SHA256");
        String requestBody = IOUtils.toString(request.getReader());
        String expectedSignature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, sonarCallbackSecret).hmacHex(requestBody);
        return Objects.equals(expectedSignature, receivedSignature);
    }
}
