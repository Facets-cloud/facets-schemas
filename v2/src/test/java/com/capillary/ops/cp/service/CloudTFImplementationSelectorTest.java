package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.deployer.exceptions.InvalidCloudProviderException;
import mockit.Deencapsulation;
import mockit.Tested;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudTFImplementationSelectorTest {

    @Tested
    private CloudTFImplementationSelector tfImplementationSelector;

    @Test
    public void selectTFProviderShouldReturnAwsCNProviderForAwsCNRegion() {
        AwsCluster awsClusterBeijing = new AwsCluster("beijing-nightly");
        awsClusterBeijing.setAwsRegion("cn-north-1");

        AwsCluster awsClusterNingxia = new AwsCluster("ningxia-nightly");
        awsClusterNingxia.setAwsRegion("cn-northwest-1");

        String tfProviderBeijing = tfImplementationSelector.selectTFProvider(awsClusterBeijing);
        assertEquals("tfawscn", tfProviderBeijing);

        String tfProviderNingxia = tfImplementationSelector.selectTFProvider(awsClusterNingxia);
        assertEquals("tfawscn", tfProviderNingxia);
    }

    @Test
    public void selectTFProviderShouldReturnAwsDefaultProviderForNonAwsCNRegion() {
        AwsCluster awsClusterSingapore = new AwsCluster("sg-nightly");
        awsClusterSingapore.setAwsRegion("ap-southeast-1");

        String tfProviderSingapore = tfImplementationSelector.selectTFProvider(awsClusterSingapore);
        assertEquals("tfaws", tfProviderSingapore);
    }

    @Test(expected = InvalidCloudProviderException.class)
    public void selectTFProviderShouldThrowInvalidCloudProviderExceptionIfNoCloudProviderPresent() {
        AwsCluster awsClusterFake = new AwsCluster("fake-nightly");
        Deencapsulation.setField(awsClusterFake, "cloud", null);
        tfImplementationSelector.selectTFProvider(awsClusterFake);
    }
}