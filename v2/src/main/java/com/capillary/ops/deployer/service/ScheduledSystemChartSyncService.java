package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.ISystemChart;
import com.capillary.ops.deployer.repository.EnvironmentRepository;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Profile("!dev")
//@Service
public class ScheduledSystemChartSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledSystemChartSyncService.class);
    @Autowired
    private List<ISystemChart> systemCharts;

    @Autowired
    private IHelmService helmService;

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Scheduled(fixedRate = 900*1000)
    public void syncSystemCharts() {
        for (ApplicationFamily applicationFamily : ApplicationFamily.values()) {
            List<Environment> environments;
            environments = environmentRepository.findByEnvironmentMetaDataApplicationFamily(applicationFamily);
            environments.stream().forEach(environment -> {
                logger.info("syncing charts for environment: {}", environment.getEnvironmentMetaData().getName());
                systemCharts.stream().forEach(chart -> {
                    deployIfNotPresent(applicationFamily, environment, chart);
                });
            });
        }
    }

    private void deployIfNotPresent(ApplicationFamily applicationFamily,
                                    Environment environment, ISystemChart chart) {
        try {
            Map<String, Object> valueMap = chart.getValues(applicationFamily, environment);
            if(valueMap == null) {
                return;
            }
            String releaseName = chart.getReleaseName(applicationFamily, environment);
            logger.info("looking for deployment with name: {}", releaseName);
            if (!helmService.doesReleaseExist(applicationFamily, environment, releaseName)) {
                logger.info("chart is not deployed, going to deploy chart with release name: {}", releaseName);
                helmService.deploy(environment, releaseName, chart.getChartPath(), valueMap);
            }
        } catch (Throwable t) {
            logger.error("Could not sync chart for " + environment.getEnvironmentMetaData().getName());
        }
    }
}
