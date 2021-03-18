package com.capillary.ops.cp.testSuite.aws;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.helpers.AwsCommonUtils;
import com.capillary.ops.cp.helpers.CommonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Vpc;

import java.util.List;


@TestPropertySource(locations="classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class BaseInfraTests {

    @Autowired
    private AwsCommonUtils awsCommonUtils;

    @Test
    public void getVpcs(){
        Ec2Client ec2Client = awsCommonUtils.getEC2Client();

        DescribeVpcsRequest vpcsRequest = DescribeVpcsRequest.builder().filters(Filter.builder().name("cidr").values("172.22.0.0/16").name("isDefault").values("false").build()).build();
        List<Vpc> vpcList = ec2Client.describeVpcs(vpcsRequest).vpcs();
        vpcList.forEach(System.out::println);
    }
}
