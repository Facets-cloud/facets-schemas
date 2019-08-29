package com.capillary.ops.deployer.service.impl;

import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.VcsService;
import com.google.common.collect.Lists;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class BitbucketVcsService implements VcsService {

    private static final Logger logger = LoggerFactory.getLogger(BitbucketVcsService.class);

    private String baseUri = "https://api.bitbucket.org/2.0";

    private CloseableHttpClient getGETClient() {
        return HttpClients.custom().build();
    }

    private JSONObject makeRequest(String requestUri, String username, String password) throws IOException {
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
        httpClient.close();

        return new JSONObject(json);
    }

    private List<JSONObject> getPaginatedResponse(String requestUri, String username, String password) throws IOException {
        List<JSONObject> jsonValues = new ArrayList<>();

        JSONObject currentPage = makeRequest(requestUri, username, password);
        List<JSONObject> responseValues = convertJsonArrayToList(currentPage);
        jsonValues.addAll(responseValues);

        for (int page = 2; page <= currentPage.getInt("pagelen") ; page++) {
            if (responseValues.size() > 0 && currentPage.has("next")) {
                currentPage = makeRequest(currentPage.getString("next"), username, password);
                jsonValues.addAll(convertJsonArrayToList(currentPage));
            }
        }

        return jsonValues;
    }

    private List<JSONObject> convertJsonArrayToList(JSONObject jsonObject) {
        JSONArray responseValues = jsonObject.getJSONArray("values");
        List<JSONObject> values = Lists.newArrayListWithExpectedSize(responseValues.length());
        for (Object object : responseValues) {
            values.add((JSONObject) object);
        }

        return values;
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

        String requestUri = new StringJoiner("/")
                .add(this.baseUri)
                .add("repositories")
                .add(owner)
                .add(repository)
                .add("refs")
                .add("branches")
                .toString();

        List<JSONObject> jsonValues = this.getPaginatedResponse(requestUri, username, password);
        List<String> branchList = jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());

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

        String requestUri = new StringJoiner("/")
                .add(this.baseUri)
                .add("repositories")
                .add(owner)
                .add(repository)
                .add("refs")
                .add("tags")
                .toString();

        List<JSONObject> jsonValues = this.getPaginatedResponse(requestUri, username, password);
        List<String> tagList = jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());

        return tagList;
    }

    public static void main(String[] args) throws IOException {
        BitbucketVcsService vcsService = new BitbucketVcsService();
        List<String> branches = vcsService.getBranches("capillarymartjack", "deisdeployer");
        List<String> tags = vcsService.getTags("capillarymartjack", "deisdeployer");
        System.out.println("branches = " + branches);
        System.out.println("tags = " + tags);
    }
}
