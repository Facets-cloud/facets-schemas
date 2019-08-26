package com.capillary.ops.deployer.service.impl;

import com.capillary.ops.deployer.service.VcsService;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BitbucketVcsService implements VcsService {

    private String baseUri = "https://api.bitbucket.org/2.0";

    private CloseableHttpClient getGETClient() {
        return HttpClients.custom().build();
    }

    private void getPaginatedResponse(List<JSONObject> jsonValues, String requestUri) throws IOException {
        String username = System.getenv("BITBUCKET_USERNAME");
        String password = System.getenv("BITBUCKET_PASSWORD");
        CloseableHttpClient httpClient = this.getGETClient();

        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        Header authorization = new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
        Header contentType = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpUriRequest request = RequestBuilder.get()
                .addHeader(contentType)
                .addHeader(authorization)
                .setUri(requestUri)
                .build();

        CloseableHttpResponse httpResponse = httpClient.execute(request);

        HttpEntity entity = httpResponse.getEntity();
        Header encodingHeader = entity.getContentEncoding();
        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
        String json = EntityUtils.toString(entity, encoding);

        JSONObject responseJson = new JSONObject(json);
        JSONArray values = responseJson.getJSONArray("values");
        addPaginatedValuesToList(jsonValues, values);

        if (!responseJson.has("next") || (values.length() == 0)) {
            return;
        }

        getPaginatedResponse(jsonValues, responseJson.getString("next"));
    }

    private void addPaginatedValuesToList(List<JSONObject> jsonValues, JSONArray values) {
        if (values.length() > 0) {
            for (Object object: values) {
                jsonValues.add((JSONObject) object);
            }
        }
    }

    @Override
    public List<String> getBranches(String owner, String repository) throws IOException {
        CloseableHttpClient httpClient = this.getGETClient();
        String requestUri = new StringJoiner("/")
                .add(this.baseUri)
                .add("repositories")
                .add(owner)
                .add(repository)
                .add("refs")
                .add("branches")
                .toString();

        List<JSONObject> jsonValues = new ArrayList<>();
        this.getPaginatedResponse(jsonValues, requestUri);

        List<String> branchList = jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());
        httpClient.close();

        return branchList;
    }

    @Override
    public List<String> getTags(String owner, String repository) throws IOException {
        CloseableHttpClient httpClient = this.getGETClient();
        String requestUri = new StringJoiner("/")
                .add(this.baseUri)
                .add("repositories")
                .add(owner)
                .add(repository)
                .add("refs")
                .add("tags")
                .toString();

        List<JSONObject> jsonValues = new ArrayList<>();
        this.getPaginatedResponse(jsonValues, requestUri);

        List<String> tagList = jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());
        httpClient.close();

        return tagList;
    }
}
