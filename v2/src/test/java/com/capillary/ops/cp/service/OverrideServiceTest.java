package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.OverrideObject;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.repository.OverrideObjectRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OverrideServiceTest {

    @Tested
    OverrideService overrideService;

    @Injectable
    OverrideObjectRepository overrideObjectRepository;

    @Test
    public void saveNew() {
        String clusterId = "1";
        OverrideRequest request = new OverrideRequest();
        request.setResourceType("Mysql");
        request.setResourceName("db1");
        Map<String, Object> abc = new HashMap<>();
        abc.put("origkey1", "origval1");
        abc.put("origkey2", "origval2");
        request.setOverrides(abc);

        new Expectations() {

            {
                overrideObjectRepository
                    .findOneByClusterIdAndResourceTypeAndResourceName(clusterId, anyString, anyString);
                result = Optional.empty();
            }

        };
        overrideService.save(clusterId, request);

        new Verifications() {

            {
                OverrideObject obj;
                overrideObjectRepository.save(obj = withCapture());
                times = 1;
                assert obj.getOverrides().size() == 2;
            }
        };
    }

    @Test
    public void saveOverwrite1stLevel() {
        String clusterId = "1";
        OverrideRequest request = new OverrideRequest();
        String type = "Mysql";
        request.setResourceType(type);
        String name = "db1";
        request.setResourceName(name);
        Map<String, Object> abc = new HashMap<>();
        abc.put("origkey2", "newval2");
        request.setOverrides(abc);

        OverrideObject overrideObject = new OverrideObject();
        overrideObject.setClusterId(clusterId);
        overrideObject.setResourceType(type);
        overrideObject.setResourceName(name);
        Map<String, Object> hmap = new HashMap<>();
        hmap.put("origkey1", "origval1");
        hmap.put("origkey2", "origval2");
        overrideObject.setOverrides(hmap);

        new Expectations() {

            {
                overrideObjectRepository
                    .findOneByClusterIdAndResourceTypeAndResourceName(clusterId, anyString, anyString);
                result = Optional.of(overrideObject);
            }

        };
        overrideService.save(clusterId, request);

        new Verifications() {

            {
                OverrideObject obj;
                overrideObjectRepository.save(obj = withCapture());
                times = 1;
                assert obj.getOverrides().size() == 2;
                assert obj.getOverrides().get("origkey2").equals("newval2");
            }
        };
    }
}