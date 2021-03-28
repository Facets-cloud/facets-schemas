package com.capillary.ops.deployer.security;

import com.capillary.ops.deployer.utils.CIDRUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@Component
public class InternalRequestAccessController {

    @Value("${internalApiAuthToken}")
    private String authToken;

    private static final String authTokenHeader = "X-DEPLOYER-INTERNAL-AUTH-TOKEN";

    public boolean authenticate(HttpServletRequest request) throws UnknownHostException {
        String authorization = request.getHeader("Authorization");
        String authTokenHeaderValue = request.getHeader(authTokenHeader);
        if(authorization !=null){
            return authorization.endsWith(authToken);
        }
        if(authTokenHeaderValue == null) {
            return false;
        }
        return authTokenHeaderValue.equals(authToken);
    }
}
