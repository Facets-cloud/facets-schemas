package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.CpClusterRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.HashMap;

public class ReleaseScheduleServiceTest {

    @Injectable
    private CpClusterRepository cpClusterRepository;

    @Injectable
    private TFBuildService tfBuildService;

    @Injectable
    private TaskScheduler poolScheduler;

    @Tested
    private ReleaseScheduleService releaseScheduleService;

    @Test(expected = IllegalArgumentException.class)
    public void illegalCron() {
        AbstractCluster cluster = new AwsCluster("test");
        cluster.setSchedules(new HashMap<ReleaseType, String>() {{
            put(ReleaseType.HOTFIX, "* * * * *");
        }});

        releaseScheduleService.updateSchedule(cluster, ReleaseType.HOTFIX, "* ssds * * *");

    }

    @Test
    public void legalcronNew() {
        AbstractCluster cluster = new AwsCluster("test");

        new Expectations() {

            {
                poolScheduler.schedule((Runnable) any, (CronTrigger) any);
                result = null;
                times = 1;
            }
        };
        releaseScheduleService.updateSchedule(cluster, ReleaseType.HOTFIX, "* * * * * ");
    }


}