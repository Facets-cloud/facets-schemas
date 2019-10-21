package com.capillary.ops.deployer.security;

import com.capillary.ops.deployer.component.DeployerHttpClient;
import com.capillary.ops.deployer.utils.CIDRUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

@Component
public class GithubIpAccessController {

    @Autowired
    private DeployerHttpClient deployerHttpClient;

    private static final Logger logger = LoggerFactory.getLogger(GithubIpAccessController.class);

    private static final String WHITELIST_URL = "https://api.github.com/meta";

    private Set<String> getWhitelistedCidrs() {
        Set<String> cidrBlocks = new HashSet<>();
        try {
            JSONObject jsonObject = deployerHttpClient.makeGETRequest(WHITELIST_URL);
            JSONArray hooks = jsonObject.getJSONArray("hooks");
            hooks.forEach(x -> cidrBlocks.add((String) x));
        } catch (IOException e) {
            logger.error("error happened while calling github ip whitelisting endpoint", e);
        } catch (Exception ex) {
            logger.error("error happened while parsing github ip whitelisting response", ex);
        }

        return cidrBlocks;
    }

    private String getIpAddressFromRequest(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    public boolean authenticate(HttpServletRequest request) throws UnknownHostException {
        String sourceIp = getIpAddressFromRequest(request);
        for (String cidr: getWhitelistedCidrs()) {
            CIDRUtils cidrUtils = new CIDRUtils(cidr);
            if (cidrUtils.isInRange(sourceIp)) {
                return true;
            }
        }

        return false;
    }
}
