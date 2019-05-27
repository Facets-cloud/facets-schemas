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
        JsonNode chartConfigs = loadSystemChartConfigs();
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
                syncSystemChartsPerEnvironmentAndFamily(applicationFamily, environment, chartConfigs);
            });
        }
    }

    private JsonNode loadSystemChartConfigs() {
        File file = new File(getClass().getResource("system-chart-configs.json").getFile());
        JsonNode configRoot = null;
        try {
            configRoot = new ObjectMapper().readTree(file);
        } catch (IOException e) {
            logger.info("cannot read the system chart config file");
        }

        return configRoot;
    }

    private void syncSystemChartsPerEnvironmentAndFamily(ApplicationFamily applicationFamily, Environment environment, JsonNode chartConfigs) {
        systemCharts.parallelStream().forEach(chart -> {
            String chartName = chart.getName();
            JsonNode chartNode = chartConfigs.path(chartName);
            JsonNode configs = chartNode.path("configs");

            if (!chartNode.isMissingNode() && !configs.isMissingNode()) {
                logger.info("found multiple config variations for chart: {}", chartName);
                deployIfNotPresentWithConfig(applicationFamily, environment, chart, configs);
            } else {
                logger.info("no config variation was found for chart: {}", chartName);
                deployIfNotPresent(applicationFamily, environment, chart);
            }
        });
    }

    private void deployIfNotPresentWithConfig(ApplicationFamily applicationFamily, Environment environment, AbstractSystemChart chart, JsonNode configs) {
        configs.forEach(config -> {
            chart.setConfig(new ObjectMapper().convertValue(config, Map.class));
            deployIfNotPresent(applicationFamily, environment, chart);
        });
    }

    private void deployIfNotPresent(ApplicationFamily applicationFamily, Environment environment, AbstractSystemChart chart) {
        Map<String, Object> valueMap = chart.getValues(applicationFamily, environment);
        String releaseName = chart.getReleaseName(applicationFamily, environment);
        logger.info("looking for deployment with name: {}", releaseName);
        if (!helmService.doesReleaseExist(applicationFamily, environment, releaseName)) {
            logger.info("chart is not deployed, going to deply chart with release name: {}" ,releaseName);
            helmService.deploy(environment, releaseName, chart.getChartPath(), valueMap);
        }
    }
}
