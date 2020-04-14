package com.capillary.ops.cp.service;

import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PromotionIntent;
import com.capillary.ops.deployer.repository.BuildRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import java.util.Optional;

public class BuildServiceTest {

    @Tested
    BuildService buildService;

    @Injectable
    BuildRepository ccBuildRepository;

    @Test
    public void getHotFixBuildLastReleaseBuildPresent() {
        Build hfBuild = new Build();
        hfBuild.setTimestamp(0L);
        hfBuild.setCodeBuildId("HF1");

        Build rBuild = new Build();
        rBuild.setTimestamp(100L);
        rBuild.setCodeBuildId("R1");

        String applicationId = "test";

        new Expectations() {

            {
                ccBuildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(applicationId);
                result = Optional.of(rBuild);
            }

            {
                ccBuildRepository
                    .findFirstByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(applicationId,
                        PromotionIntent.HOTFIX);
                result = Optional.of(hfBuild);
            }
        };
        Optional<Build> hotFixBuild = buildService.getHotFixBuild(applicationId);
        assert !hotFixBuild.isPresent();
    }

    @Test
    public void getHotFixBuildLastHotfixBuildPresent() {
        Build rBuild = new Build();
        rBuild.setTimestamp(0L);
        rBuild.setCodeBuildId("R1");


        Build hfBuild = new Build();
        hfBuild.setTimestamp(100L);
        hfBuild.setCodeBuildId("HF1");


        String applicationId = "test";

        new Expectations() {

            {
                ccBuildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(applicationId);
                result = Optional.of(rBuild);
            }

            {
                ccBuildRepository
                    .findFirstByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(applicationId,
                        PromotionIntent.HOTFIX);
                result = Optional.of(hfBuild);
            }
        };
        Optional<Build> hotFixBuild = buildService.getHotFixBuild(applicationId);
        assert hotFixBuild.isPresent();
        assert hotFixBuild.get().getCodeBuildId().equals("HF1");
    }

    @Test
    public void getHotFixBuildNoHotfixBuildPresent() {
        Build rBuild = new Build();
        rBuild.setTimestamp(0L);
        rBuild.setCodeBuildId("R1");


        String applicationId = "test";

        new Expectations() {

            {
                ccBuildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(applicationId);
                result = Optional.of(rBuild);
            }

            {
                ccBuildRepository
                    .findFirstByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(applicationId,
                        PromotionIntent.HOTFIX);
                result = Optional.empty();
            }
        };
        Optional<Build> hotFixBuild = buildService.getHotFixBuild(applicationId);
        assert !hotFixBuild.isPresent();
    }

    @Test
    public void getHotFixBuildNoReleaseBuildPresent() {
        Build hfBuild = new Build();
        hfBuild.setTimestamp(0L);
        hfBuild.setCodeBuildId("HF1");


        String applicationId = "test";

        new Expectations() {

            {
                ccBuildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(applicationId);
                result = Optional.empty();
            }

            {
                ccBuildRepository
                    .findFirstByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(applicationId,
                        PromotionIntent.HOTFIX);
                result = Optional.of(hfBuild);
            }
        };
        Optional<Build> hotFixBuild = buildService.getHotFixBuild(applicationId);
        assert hotFixBuild.isPresent();
        assert hotFixBuild.get().getCodeBuildId().equals("HF1");
    }
}