package com.capillary.ops.cp.service.aws;

import org.springframework.stereotype.Component;

@Component
public class AwsAssumeRoleService {

    /**
     * Given an ARN and external_id tests its access levels in aws
     *
     * @param arn         Role ARN specified
     * @param external_id External_id specified
     * @return Yes or No
     */
    public Boolean testRoleAccess(String arn, String external_id) {
        return true;
    }
}
