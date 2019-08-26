package com.capillary.ops.deployer.service.impl;

import com.capillary.ops.deployer.exceptions.NotFoundException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BitbucketVcsService implements VcsService {

    private static final Logger logger = LoggerFactory.getLogger(BitbucketVcsService.class);

    private String baseUri = "https://api.bitbucket.org/2.0";

    private CloseableHttpClient getGETClient() {
        return HttpClients.custom().build();
    }

    private void getPaginatedResponse(List<JSONObject> jsonValues, String requestUri, String username, String password) throws IOException {

        CloseableHttpClient httpClient = this.getGETClient();

        logger.debug("constructing base64 encoded credentials");
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        Header authorization = new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
        Header contentType = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpUriRequest request = RequestBuilder.get()
                .addHeader(contentType)
                .addHeader(authorization)
                .setUri(requestUri)
                .build();
        logger.debug("building request with uri: {}", requestUri);

        CloseableHttpResponse httpResponse = httpClient.execute(request);

        HttpEntity entity = httpResponse.getEntity();
        Header encodingHeader = entity.getContentEncoding();
        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());

        logger.debug("converting http response to json string with encoding: {}", encoding);
        String json = EntityUtils.toString(entity, encoding);

        JSONObject responseJson = new JSONObject(json);
        JSONArray values = responseJson.getJSONArray("values");
        addPaginatedValuesToList(jsonValues, values);

        if (!responseJson.has("next") || (values.length() == 0)) {
            return;
        }

        getPaginatedResponse(jsonValues, responseJson.getString("next"), username, password);
        httpResponse.close();
    }

    private void addPaginatedValuesToList(List<JSONObject> jsonValues, JSONArray values) {
        if (values.length() > 0) {
            logger.debug("page contains values, adding to list");
            for (Object object: values) {
                jsonValues.add((JSONObject) object);
            }
        }
    }

    @Override
    public List<String> getBranches(String owner, String repository) throws IOException {
        String username = System.getenv("BITBUCKET_USERNAME");
        String password = System.getenv("BITBUCKET_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("bitbucket credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Bitbucket credentials to use this api");
        }

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
        this.getPaginatedResponse(jsonValues, requestUri, username, password);

        List<String> branchList = jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());
        httpClient.close();

        return branchList;
    }

    @Override
    public List<String> getTags(String owner, String repository) throws IOException {
        String username = System.getenv("BITBUCKET_USERNAME");
        String password = System.getenv("BITBUCKET_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("bitbucket credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Bitbucket credentials to use this api");
        }

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
        this.getPaginatedResponse(jsonValues, requestUri, username, password);

        List<String> tagList = jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());
        httpClient.close();

        return tagList;
    }
}
