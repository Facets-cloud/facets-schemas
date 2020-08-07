package com.capillary.ops.deployer.service.newrelic;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("!dev")
public class NewRelicService implements INewRelicService {

    @Value("${newrelic.apiKey}")
    private String newrelicApiKey;

    @Value("${newrelic.queryKey}")
    private String newrelicQueryKey;

    private static final String newRelicAccountId = "67421";

    private static HttpClient httpClient = HttpClientBuilder.create()
            .setConnectionManager(new PoolingHttpClientConnectionManager()).build();

    private static final Logger logger = LoggerFactory.getLogger(NewRelicService.class);

    enum AlertChannel {
        Email
    }

    @Override
    public void upsertDashboard(Application application,
                                Environment environment) {
        if (environment.getEnvironmentConfiguration().getNewRelicClusterName() == null) {
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

    private String getDashboardTemplate() {
        try {
            URL url = this.getClass().getResource("/newrelic-dashboard.json.mustache");
            return Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAlerts(Application application, Environment environment) {
        if (environment.getEnvironmentConfiguration().getNewRelicClusterName() == null) {
            throw new RuntimeException("NewRelic cluster name not configured");
        }
        if (application.getNewRelicAlertRecipients() == null) {
            throw new RuntimeException("NewRelic alert recipients list is empty");
        }
        String appName = application.getName();
        String recipients = application.getNewRelicAlertRecipients();
        Integer policyId = null;
        Integer conditionId = null;
        try {
            policyId = getAlertPolicy(application.getName());
            if (policyId == null) {
                policyId = createAlertPolicy(appName);
                createAlertChannel(appName, policyId, recipients);
                createAlertConditions(application, environment, policyId);
            } else {
                conditionId = getAlertConditions(application, environment, policyId);
                if (conditionId == null) {
                    createAlertConditions(application, environment, policyId);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("NewRelic API exception", e);
        }
    }

    @Override
    public void disableAlerts(Application application, Environment environment) {
        try {
            deleteAlertChannel(application, environment);
            Integer policyId = getAlertPolicy(application.getName());
            URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_policies/" + policyId + ".json");
            HttpDelete httpDelete = new HttpDelete(builder.build());
            httpDelete.setHeader("X-Api-Key", newrelicApiKey);
            HttpResponse response = httpClient.execute(httpDelete);
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException("NewRelic API exception", e);
        }
    }

    private void deleteAlertChannel(Application application, Environment environment) throws Exception {
        Integer channelId = getAlertChannelId(application.getName());
        URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_channels/" + channelId + ".json");
        HttpDelete httpDelete = new HttpDelete(builder.build());
        httpDelete.setHeader("X-Api-Key", newrelicApiKey);
        HttpResponse response = httpClient.execute(httpDelete);
        EntityUtils.consume(response.getEntity());
    }

    private Integer getAlertChannelId(String applicationName) throws Exception {
        Integer channelId = null;
        URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_channels.json");
        HttpGet getRequest = new HttpGet(builder.build());
        getRequest.setHeader("X-Api-Key", newrelicApiKey);
        HttpResponse response = httpClient.execute(getRequest);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        if (jsonObject.getAsJsonArray("channels").size() > 0) {
            for (JsonElement channel : jsonObject.getAsJsonArray("channels")) {
                if (channel.getAsJsonObject().get("name").getAsString().equals(applicationName)) {
                    channelId = channel.getAsJsonObject().get("id").getAsInt();
                }
            }
        }
        return channelId;
    }

    @Override
    public String getAlertsURL(Application application, Environment environment) {
        String alertsURL = null;
        String baseURL = "https://alerts.newrelic.com/accounts/" + newRelicAccountId + "/policies/";
        try {
            Integer policyId = getAlertPolicy(application.getName());
            if (policyId != null && getAlertConditions(application, environment, policyId) != null) {
                alertsURL = baseURL + getAlertPolicy(application.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("NewRelic API exception", e);
        }
        return alertsURL;
    }

    private Integer getAlertPolicy(String name) throws Exception {
        Integer policyId = null;
        URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_policies.json?filter[exact_match]=true&filter[name]=" + name);
        HttpGet getRequest = new HttpGet(builder.build());
        getRequest.setHeader("Content-Type", "application/json");
        getRequest.setHeader("X-Api-Key", newrelicApiKey);
        HttpResponse response = httpClient.execute(getRequest);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        if (jsonObject.getAsJsonArray("policies").size() > 0) {
            policyId = jsonObject.getAsJsonArray("policies").get(0).getAsJsonObject().get("id").getAsInt();
        }
        return policyId;
    }

    private Integer getAlertConditions(Application application, Environment environment, Integer policyId) throws Exception {
        Integer conditionId = null;
        String alertName = application.getName() + "-" + environment.getEnvironmentMetaData().getName() + "-pod-not-ready";
        Map<String, Integer> alerts = new HashMap<>();
        URIBuilder builder = new URIBuilder("https://infra-api.newrelic.com/v2/alerts/conditions?policy_id=" + policyId);
        HttpGet getRequest = new HttpGet(builder.build());
        getRequest.setHeader("Content-Type", "application/json");
        getRequest.setHeader("X-Api-Key", newrelicApiKey);
        HttpResponse response = httpClient.execute(getRequest);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);

        if (jsonObject.getAsJsonArray("data").size() > 0) {
            for (JsonElement alert : jsonObject.getAsJsonArray("data")) {
                alerts.put(alert.getAsJsonObject().get("name").getAsString(), alert.getAsJsonObject().get("id").getAsInt());
            }
        }
        if (alerts.keySet().contains(alertName)) {
            conditionId = alerts.get(alertName);
        }
        return conditionId;
    }

    private Integer createAlertPolicy(String alertPolicyName) throws Exception {
        URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_policies.json");
        HttpPost request = new HttpPost(builder.build());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("X-Api-Key", newrelicApiKey);
        String jsonPayload = new JSONObject().put("policy", new JSONObject()
                .put("incident_preference", "PER_CONDITION_AND_TARGET")
                .put("name", alertPolicyName)).toString();
        HttpEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        return jsonObject.getAsJsonObject("policy")
                .getAsJsonObject().get("id").getAsInt();
    }

    private Integer createAlertChannel(String channelName, Integer policyId, String recipientEmail) throws Exception {
        URIBuilder builder = new URIBuilder("https://api.newrelic.com/v2/alerts_channels.json?policy_ids[]=" + policyId);
        HttpPost request = new HttpPost(builder.build());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("X-Api-Key", newrelicApiKey);
        String jsonPayload = new JSONObject().put("channel",
                new JSONObject()
                        .put("name", channelName)
                        .put("type", AlertChannel.Email)
                        .put("configuration", new JSONObject()
                                .put("recipients", recipientEmail))
                        .put("include_json_attachment", false))
                .toString();
        HttpEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        return jsonObject.getAsJsonArray("channels").get(0)
                .getAsJsonObject().get("id").getAsInt();
    }

    private void createAlertConditions(Application application, Environment environment, Integer policyId) throws Exception {
        List<String> conditionList = Arrays.asList("/newrelic-condition-not-ready.json.mustache", "/newrelic-condition-restarts.json.mustache");
        for (String condition : conditionList) {
            String jsonTemplate = getConditionsTemplate(condition);
            Template template = Mustache.compiler().compile(jsonTemplate);
            String payload = template.execute(ImmutableMap.of(
                    "alertName", application.getName() + "-" + environment.getEnvironmentMetaData().getName(),
                    "applicationName", application.getName(),
                    "policyId", policyId,
                    "clusterName", environment.getEnvironmentConfiguration().getNewRelicClusterName()
            ));
            URIBuilder builder = new URIBuilder("https://infra-api.newrelic.com/v2/alerts/conditions");
            HttpPost request = new HttpPost(builder.build());
            request.setHeader("Content-Type", "application/json");
            request.setHeader("X-Api-Key", newrelicApiKey);
            HttpEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        }
    }

    private String getConditionsTemplate(String fileName) {
        try {
            URL url = this.getClass().getResource(fileName);
            return Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Double> getMetrics(String applicationName, Date startDate, Date endDate) {

        String query = "SELECT count(apm.service.error.count) / count(apm.service.transaction.duration) as 'Non-web errors'," +
                " max(apm.service.transaction.duration) as instances, " +
                "max(apm.service.transaction.error.count) as error_count " +
                "FROM Metric WHERE (entity.guid = 'Njc0MjF8QVBNfEFQUExJQ0FUSU9OfDM3MTE0Mzc4OQ') " +
                "AND (transactionType = 'Other') SINCE 1596619192390 UNTIL 1596620992390 " +
                " ";
        Map<String, Double> ret = new HashMap<>();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder("https://insights-api.newrelic.com/v1/accounts/" + newRelicAccountId +
                    "/query?nrql=" + URLEncoder.encode(query, "UTF-8"));
            HttpGet request = new HttpGet(builder.build());
            request.setHeader("Content-Type", "application/json");
            request.setHeader("X-Query-Key", newrelicQueryKey);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
            //JsonElement results = jsonObject.get("results").getAsJsonArray().get(0).getAsJsonObject().get("key");
            int size = jsonObject.get("results").getAsJsonArray().size();

            JsonArray valuesList = jsonObject.get("results").getAsJsonArray();
            JsonArray keysList = jsonObject.get("metadata").getAsJsonObject().get("contents").getAsJsonArray();

            for (int i = 0; i < size; i++) {
                String alias = valuesList.get(i).getAsJsonObject().keySet().toArray()[0].toString();

                ret.putIfAbsent(
                        keysList.get(i).getAsJsonObject().get("alias").getAsString(),
                        valuesList.get(i).getAsJsonObject().get(alias).getAsDouble()
                );
            }

            System.out.println(ret);
        } catch (Exception e) {
            logger.error("Failed to get ", e);
        }

        return ret;
    }
}
