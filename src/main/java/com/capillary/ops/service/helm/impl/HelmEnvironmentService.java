package com.capillary.ops.service.helm.impl;

import com.capillary.ops.App;
import com.capillary.ops.bo.helm.ApplicationFamily;
import com.capillary.ops.bo.helm.FamilyEnvironment;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class HelmEnvironmentService {
//    @Value("${deployer.family.integrations.accountId}")
    private static final String INTEGRATIONS_ACCOUNT_ID = "538938394727";

//    @Value("${deployer.family.ecommerce.accountId}")
    private static final String ECOMMERCE_ACCOUNT_ID = "909374411237";

    public List<FamilyEnvironment> getEnvironmentsForFamily(ApplicationFamily applicationFamily, String appName) {
        return null;
    }

    public String getAccountIdForFamily(ApplicationFamily applicationFamily) {
        switch (applicationFamily) {
            case INTEGRATION:
                return INTEGRATIONS_ACCOUNT_ID;
            case ECOMMERCE:
                return ECOMMERCE_ACCOUNT_ID;
            default:
                throw new RuntimeException("could not find relevant family to fetch the account id");
        }
    }

    public String getEcrPolicyForFamily(ApplicationFamily applicationFamily) {
        String template = null;
        try {
            template = CharStreams.toString(new InputStreamReader(
                    App.class.getClassLoader().getResourceAsStream("aws/EcrPolicyTemplate.json"), Charsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not find the ecr policy template");
        }

        return template.replace("{{ACCOUNT_ID}}", getAccountIdForFamily(applicationFamily));
    }

}
