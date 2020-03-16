package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Build;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Optional;

@DataMongoTest
@RunWith(SpringRunner.class)
public class BuildRepositoryTest {

    @Autowired
    BuildRepository buildRepository;

    boolean isSetupDone = false;

    @Before
    public void setup() throws InterruptedException {
        if (!isSetupDone) {
            isSetupDone = true;
            System.out.println("SETUP");

            Build b2 = new Build();
            b2.setApplicationId("1");
            b2.setApplicationFamily(ApplicationFamily.CRM);
            b2.setCodeBuildId("latestNightly");
            b2.setStatus(StatusType.SUCCEEDED);
            b2.setTag("origin/master");
            b2.setPromoted(false);
            b2.setPromotable(false);
            b2.setTimestamp(100L);
            buildRepository.save(b2);

            Build b3 = new Build();
            b3.setApplicationId("1");
            b3.setApplicationFamily(ApplicationFamily.CRM);
            b3.setCodeBuildId("staleProd");
            b3.setStatus(StatusType.SUCCEEDED);
            b3.setTag("tag/1.0");
            b3.setPromoted(true);
            b3.setPromotable(true);
            b3.setTimestamp(600L);
            buildRepository.save(b3);

            Build b4 = new Build();
            b4.setApplicationId("1");
            b4.setApplicationFamily(ApplicationFamily.CRM);
            b4.setCodeBuildId("latestProd");
            b4.setStatus(StatusType.SUCCEEDED);
            b4.setTag("tag/1.1");
            b4.setPromoted(true);
            b4.setPromotable(true);
            b4.setTimestamp(1600L);
            buildRepository.save(b4);

            Build b1 = new Build();
            b1.setApplicationId("1");
            b1.setApplicationFamily(ApplicationFamily.CRM);
            b1.setCodeBuildId("latestStaging");
            b1.setStatus(StatusType.SUCCEEDED);
            b1.setTag("tag/1.0");
            b1.setPromoted(false);
            b1.setPromotable(true);
            b1.setTimestamp(2000L);
            buildRepository.save(b1);
        }
    }

    @Test
    public void testBuildProd() {

        Optional<Build> build = buildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc("1");
        assert build.isPresent();
        assert build.get().getCodeBuildId().equals("latestProd");
    }

    @Test
    public void testBuildStage() {

        Optional<Build> build = buildRepository.findFirstByApplicationIdAndPromotableIsTrueOrderByTimestampDesc("1");
        assert build.isPresent();
        assert build.get().getCodeBuildId().equals("latestStaging");
    }


    @Test
    public void testBuildQA() {

        Optional<Build> build = buildRepository.findFirstByApplicationIdAndPromotableIsFalseOrderByTimestampDesc("1");
        assert build.isPresent();
        assert build.get().getCodeBuildId().equals("latestNightly");
    }
}
