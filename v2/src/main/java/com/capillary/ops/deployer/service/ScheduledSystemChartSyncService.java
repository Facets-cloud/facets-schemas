package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.AbstractSystemChart;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ScheduledSystemChartSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledSystemChartSyncService.class);

    @Autowired
    private List<AbstractSystemChart> systemCharts;

    @Autowired
    private HelmService helmService;

    @Scheduled(fixedRate = 900)
    public void syncSystemCharts() {
        for (ApplicationFamily applicationFamily : ApplicationFamily.values()) {
            List<Environment> environments;
            try {
                environments = applicationFamily.getEnvironments();
            } catch (FileNotFoundException e) {
                logger.info("no cluster details file found for application family: {}", applicationFamily.name());
                continue;
            }

            environments.parallelStream().forEach(environment -> {
                logger.info("syncing charts for environment: {}", environment.getName());
                systemCharts.stream().forEach(chart -> {
                    deployIfNotPresent(applicationFamily, environment, chart);
                });
            });
        }
    }

    private void deployIfNotPresent(ApplicationFamily applicationFamily,
                                    Environment environment, AbstractSystemChart chart) {
        try {
            Map<String, Object> valueMap = chart.getValues(applicationFamily, environment);
            if(valueMap == null) {
                return;
            }
            String releaseName = chart.getReleaseName(applicationFamily, environment);
            logger.info("looking for deployment with name: {}", releaseName);
            if (!helmService.doesReleaseExist(applicationFamily, environment, releaseName)) {
                logger.info("chart is not deployed, going to deply chart with release name: {}", releaseName);
                helmService.deploy(environment, releaseName, chart.getChartPath(), valueMap);
            }
        } catch (Throwable t) {
            logger.error("Could not sync chart for " + environment.getName(), t);
        }
    }
}
