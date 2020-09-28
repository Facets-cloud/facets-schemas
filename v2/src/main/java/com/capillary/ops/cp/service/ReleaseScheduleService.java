package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * Service to handle all scheduled deployments
 */
@Service
@Loggable
public class ReleaseScheduleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CpClusterRepository cpClusterRepository;

    @Autowired
    private TaskScheduler poolScheduler;

    @Autowired
    private HttpServletRequest requestContext;

    @Value("${deployer.scheduler.enabled}")
    private boolean enabled;

    @Autowired
    private TFBuildService tfBuildService;

    private Map<String, ScheduleInfo> schedules = new HashMap<>();

    @Autowired
    private DeploymentFacade deploymentFacade;

    /**
     * Given a cluster update/insert all its schedules
     *
     * @param c The cluster object
     */
    public synchronized void upsertSchedule(AbstractCluster c) {
        if (c.getSchedules() != null) {
            c.getSchedules().entrySet().forEach(e -> {
                updateSchedule(c, e.getKey(), e.getValue());
            });
        }
    }

    /**
     * Update Release Schedule for a particular release type
     *
     * @param c           The cluster object
     * @param releaseType Type of Release
     * @param cron        Cron Pattern
     */
    public void updateSchedule(AbstractCluster c, ReleaseType releaseType, String cron) {
        if(!enabled){
            return;
        }
        String key = c.getId() + releaseType.name();
        if (schedules.containsKey(key)) {
            ScheduleInfo existing = schedules.get(key);
            if (existing.cronPattern.equals(cron)) {
                // Do nothing
                return;
            }
            existing.scheduledFuture.cancel(false);
        }
        if (cron.trim().split(" ").length == 5) {
            cron = "0 " + cron;
        }
        logger.info("Cron for cluster {} and Release type {} is {}", c.getId(), releaseType, cron);
        CronTrigger cronTrigger = new CronTrigger(cron, TimeZone.getTimeZone("UTC"));
        ScheduledFuture<?> schedule = poolScheduler.schedule(() -> {
            logger.info("Initiating Release of type {} for cluster {} ({})", releaseType, c.getId(), c.getName());
            DeploymentRequest deploymentRequest = new DeploymentRequest();
            deploymentRequest.setReleaseType(releaseType);
            deploymentFacade.createDeployment(c.getId(), deploymentRequest);
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
            this.upsertSchedule(c);
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
