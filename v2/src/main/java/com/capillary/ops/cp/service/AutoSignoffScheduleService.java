package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * Service to handle all scheduled deployments
 */
@Service
@Loggable
public class AutoSignoffScheduleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CpClusterRepository cpClusterRepository;

    @Autowired
    private TaskScheduler poolScheduler;

    @Autowired
    private HttpServletRequest requestContext;

    @Value("${deployer.scheduler.enabled}")
    private boolean enabled;

    private Map<String, ScheduleInfo> schedules = new HashMap<>();

    @Autowired
    private DeploymentFacade deploymentFacade;

    @Autowired
    private DeploymentLogRepository deploymentLogRepository;
    /**
     * Update Auto Signoff Schedule
     *
     * @param c           The cluster object
     */
    public void updateSchedule(AbstractCluster c) {
//        if(!enabled || !c.getEnableAutoSignOff()) {
//            return;
//        }

        String key = c.getId();
        String cron = c.getAutoSignOffSchedule();

        if (StringUtils.isEmpty(cron)) {
            return;
        }
        if (schedules.containsKey(key)) {
            ScheduleInfo existing = schedules.get(key);
            if (existing.cronPattern.equals(c.getAutoSignOffSchedule())) {
                // Do nothing
                return;
            }
            existing.scheduledFuture.cancel(false);
        }
        if (cron.trim().split(" ").length == 5) {
            cron = "0 " + cron;
        }
        logger.info("Auto sign-off Cron for cluster {} is {}", c.getId(), cron);
        CronTrigger cronTrigger = new CronTrigger(cron, TimeZone.getTimeZone("UTC"));
        ScheduledFuture<?> schedule = poolScheduler.schedule(() -> {
            logger.info("Initiating sign-off for cluster {} ({})", c.getId(), c.getName());
            DeploymentLog pendingSignOff =
                    deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeAndSignedOffOrderByCreatedOnDesc(
                            c.getId(), StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR, false).get();
            deploymentFacade.signOff(pendingSignOff.getClusterId(), pendingSignOff.getId());
        }, cronTrigger);
        schedules.put(key, new ScheduleInfo(cron, schedule));
    }

    /**
     * Initialize all Cluster Object Schedules at startup
     */
    @PostConstruct
    public void configureTasks() {
        List<AbstractCluster> all = cpClusterRepository.findAll();
        all.forEach(c -> {
            this.updateSchedule(c);
        });
    }

    /**
     * A Simple class to hold pattern as well as scheduledFuture
     */
    private class ScheduleInfo {

        public String cronPattern;

        public ScheduledFuture scheduledFuture;

        public ScheduleInfo(String cron, ScheduledFuture<?> schedule) {
            this.cronPattern = cron;
            this.scheduledFuture = schedule;
        }
    }
}
