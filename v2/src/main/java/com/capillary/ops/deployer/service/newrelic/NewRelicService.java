package com.capillary.ops.deployer.service.newrelic;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.HelmService;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import javafx.scene.control.Alert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class NewRelicService implements INewRelicService {

    @Value("${newrelic.apiKey}")
    private String newrelicApiKey;

    private static HttpClient httpClient = HttpClientBuilder.create()
            .setConnectionManager(new PoolingHttpClientConnectionManager()).build();

    private static final Logger logger = LoggerFactory.getLogger(NewRelicService.class);

    enum AlertChannel{
        Email
    }

    @Override
    public void upsertDashboard(Application application,
                                Environment environment) {
        if(environment.getEnvironmentConfiguration().getNewRelicClusterName() == null) {
            throw new RuntimeException("NewRelic cluster name not configured");
        }
        try {
            String jsonTemplate =
                    getDashboardTemplate();
            Template template = Mustache.compiler().compile(jsonTemplate);
            String dashboardName = getDashboardTitle(application, environment);
            String dashboardId = getDashboardId(application, environment);
            if (dashboardId != null) {
                String payload = template.execute(ImmutableMap.of(
                        "dashboardId", dashboardId,
                        "dashboardTitle", dashboardName,
                        "cluster", environment.getEnvironmentConfiguration().getNewRelicClusterName(),
                        "applicationName", application.getName()
                ));

                updateDashboard(payload);
            } else {
                String payload = template.execute(ImmutableMap.of(
                        "dashboardTitle", dashboardName,
                        "cluster", environment.getEnvironmentConfiguration().getNewRelicClusterName(),
                        "applicationName", application.getName()
                ));
                createDashboard(payload);
            }
        } catch (Throwable t) {
            throw new RuntimeException("NewRelic API exception", t);
        }
    }

    private void createDashboard(String payload) {
        try {
            logger.info("newrelic api payload: ", payload);
            URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/dashboards.json");
            HttpPost request = new HttpPost(builder.build());
            request.setEntity(new StringEntity(payload));
            request.setHeader("X-Api-Key", newrelicApiKey);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.info("newrelic api response: ", responseBody);
            int[][] r = new int[][]{};
        } catch (Throwable t) {
            throw new RuntimeException("NewRelic API exception", t);
        }

    }

    private void updateDashboard(String payload) {
        try {
            logger.info("newrelic api payload: ", payload);
            URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/dashboards.json");
            HttpPut request = new HttpPut(builder.build());
            request.setEntity(new StringEntity(payload));
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");
            request.setHeader("X-Api-Key", newrelicApiKey);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.info("newrelic api response: ", responseBody);
        } catch (Throwable t) {
            throw new RuntimeException("NewRelic API exception", t);
        }
    }

    private String getDashboardTitle(Application application, Environment environment) {
        return "[DEPLOYERAUTO] " + application.getApplicationFamily() + "-" +
                environment.getEnvironmentMetaData().getName() + "-" + application.getName();
    }

    private String getDashboardId(Application application,
                           Environment environment) {
        try {
            URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/dashboards.json");
            builder.setParameter("filter[title]", getDashboardTitle(application, environment));
            HttpGet request = new HttpGet(builder.build());
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");
            request.setHeader("X-Api-Key", newrelicApiKey);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
            String id = jsonObject.getAsJsonArray("dashboards")
                    .get(0).getAsJsonObject().get("id").getAsString();
            return id;
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public String getDashboardURL(Application application, Environment environment) {
        try {
            String dashboardId = getDashboardId(application, environment);
            URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/dashboards/" + dashboardId + ".json");
            HttpGet request = new HttpGet(builder.build());
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");
            request.setHeader("X-Api-Key", newrelicApiKey);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
            String id = jsonObject.getAsJsonObject("dashboard")
                    .getAsJsonObject().get("ui_url").getAsString();
            return id;
        } catch (Throwable t) {
            logger.warn("NewRelic API exception", t);
            return null;
        }
    }

    @Override
    public void deleteDashboard(Application application, Environment environment) {
        try {
            String dashboardId = getDashboardId(application, environment);
            URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/dashboards/" + dashboardId + ".json");
            HttpDelete request = new HttpDelete(builder.build());
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");
            request.setHeader("X-Api-Key", newrelicApiKey);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        } catch (Throwable t) {
            throw new RuntimeException("NewRelic API exception", t);
        }
    }

    @Override
    public String createAlerts(Application application, Environment environment) {
        String policyId= "";
        try {
            //check if policy exists
            //else create policy and conditions
            policyId = createAlertPolicy(application.getName());
        }
        catch (Exception e){
            throw new RuntimeException("NewRelic API exception", e);
        }
        return policyId;
    }

    private String getDashboardTemplate() {
        try {
            URL url = this.getClass().getResource("/newrelic-dashboard.json.mustache");
            return Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createAlertPolicy(String alertPolicyName) throws Exception{
        URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_policies.json");
        HttpPost request = new HttpPost(builder.build());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("X-Api-Key", newrelicApiKey);
        String jsonPayload = new JSONObject().put("policy", new JSONObject()
                .put("incident_preference","PER_CONDITION_AND_TARGET")
                .put("name", alertPolicyName)).toString();
        HttpEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        String policyId = jsonObject.getAsJsonObject("policy")
                .getAsJsonObject().get("id").getAsString();
        return policyId;
    }

    private String createAlertChannel(String channelName, String configurationJson, AlertChannel alertChannel) throws Exception{
        URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_channels.json");
        HttpPost request = new HttpPost(builder.build());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("X-Api-Key", newrelicApiKey);
        String jsonPayload = new JSONObject().put("channel", new JSONObject()
                .put("name",channelName)
                .put("type", alertChannel.name())).toString();
        HttpEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        String policyId = jsonObject.getAsJsonObject("policy")
                .getAsJsonObject().get("id").getAsString();
        return policyId;
    }
}
