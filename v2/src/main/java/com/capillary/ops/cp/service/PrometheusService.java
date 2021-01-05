package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.requests.SilenceAlarmRequest;
import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

@Service
@Loggable
public class PrometheusService {

    public static final String HTTPS_PROMETHEUS = "https://prometheus.";
    public static final String HTTPS_ALERTMANAGER = "https://alertmanager.";
    @Autowired
    private RestTemplate restTemplate;

    public JsonObject getAllAlerts(String baseUrl, String auth) {
        String url = HTTPS_PROMETHEUS + baseUrl + "/api/v1/rules?type=alert";
        JsonObject allAlerts = this.callAPI(baseUrl, auth, url);
        JsonObject openAlerts = this.getOpenAlerts(baseUrl, auth);
        if(!allAlerts.get("status").getAsString().equals("success") || !openAlerts.get("status").getAsString().equals("success")){
            return  allAlerts;
        }
        allAlerts.getAsJsonObject("data").getAsJsonArray("groups").get(0).getAsJsonObject().getAsJsonArray("rules").forEach(
                r->{
                   r.getAsJsonObject().add("alerts",new JsonArray());
                }
        );

        openAlerts.getAsJsonArray("data").forEach(x->
        {
            String alertName = x.getAsJsonObject().get("labels").getAsJsonObject().get("alertname").getAsString();
            allAlerts.getAsJsonObject("data").getAsJsonArray("groups").get(0).getAsJsonObject().getAsJsonArray("rules").forEach(
                    r->{
                        if(r.getAsJsonObject().get("name").getAsString().equals(alertName)){
                            r.getAsJsonObject().getAsJsonArray("alerts").add(x);
                        }
                    }
            );
        });
        return allAlerts;
    }

    public JsonObject getOpenAlerts(String baseUrl, String auth) {
        String url2 = HTTPS_ALERTMANAGER + baseUrl + "/api/v2/alerts";
        JsonObject dummy = this.callAPI(baseUrl, auth, url2);
        String url = HTTPS_ALERTMANAGER + baseUrl + "/api/v1/alerts";
        JsonObject allAlerts = this.callAPI(baseUrl, auth, url);
        if(allAlerts.get("status").getAsString().equals("success")){
            JsonArray data = allAlerts.getAsJsonArray("data");
            data.forEach(x ->
            {
                JsonObject obj = x.getAsJsonObject();
                String state = obj.getAsJsonObject("status").get("state").getAsString();
                if(state.equals("suppressed")){
                    JsonArray silencedIds = obj.getAsJsonObject("status").getAsJsonArray("silencedBy");
                    JsonArray silenceArr = new JsonArray();
                    silencedIds.forEach(sj -> {
                        String silencedId = sj.getAsString();
                        JsonObject silenceInfo = this.getSilenceById(baseUrl, auth, silencedId);
                        if(silenceInfo.get("status").getAsString().equals("success")) {
                            silenceArr.add(silenceInfo.getAsJsonObject("data"));
                        }
                    });
                    obj.add("silenceDetails", silenceArr);
                }
            });
        }
        return allAlerts;
    }

    public JsonObject getSilenceById(String baseUrl, String auth, String silenceId) {
        String url = "https://alertmanager." + baseUrl + "/api/v1/silence/" + silenceId;
        return this.callAPI(baseUrl, auth, url);
    }

    public JsonObject silenceAlert(String baseUrl, String auth, SilenceAlarmRequest request, String userName) {
        String url = "https://alertmanager." + baseUrl + "/api/v1/silences";
        String startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now().plus(request.getSnoozeInHours(), ChronoUnit.HOURS));
        JsonObject payload = new JsonParser().parse("{\"status\":{\"state\": \"active\" }}").getAsJsonObject();
        payload.addProperty("startsAt", startTime);
        payload.addProperty("endsAt", endTime);
        payload.addProperty("createdBy", userName);
        payload.addProperty("comment", request.getComment());
        JsonArray matchers = new JsonArray();
        request.getLabels().entrySet().stream().map(e-> {
            JsonObject o = new JsonObject();
            o.addProperty("name",e.getKey());
            o.addProperty("value",e.getValue());
            return o;
        }).forEach(e -> matchers.add(e));
        payload.add("matchers", matchers);
        return this.callAPI(baseUrl, auth, url, payload);
    }

    public JsonObject callAPI(String baseUrl, String auth, String url) {
        return this.callAPI(baseUrl, auth, url, null);
    }

    public JsonObject callAPI(String baseUrl, String auth, String url, JsonObject body) {
        try {
            if (baseUrl == null || auth == null || baseUrl.equals("") || auth.equals("")) {
                JsonObject json = new JsonObject();
                json.addProperty("status", "fail");
                json.addProperty("reason", "Unable to Fetch tools password from the resources.");
                return json;
            }
            //RestTemplate restTemplate= new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + auth);
            // create request

            ResponseEntity<String> response;
            if(body == null) {
                HttpEntity<String> request = new HttpEntity<>(headers);
                 response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            }else{
                HttpEntity<HashMap> request = new HttpEntity<>((new Gson()).fromJson(body, HashMap.class), headers);
                response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            }
            if (response.getStatusCode() != HttpStatus.OK) {
                JsonObject json = new JsonObject();
                json.addProperty("status", "fail");
                json.addProperty("reason", "Non 200 response from prometheus: " + response.getStatusCode());
                return json;
            }
            JsonObject json = new JsonParser().parse(response.getBody()).getAsJsonObject();
            return json;
        } catch (Throwable t) {
            JsonObject json = new JsonObject();
            json.addProperty("status", "fail");
            json.addProperty("reason", "Non 200 response from prometheus: " + t.getClass() + " - " + t.getMessage());
            return json;
        }
    }

}
