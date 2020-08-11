package com.capillary.ops.deployer.component;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class DeployerHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(DeployerHttpClient.class);

    private CloseableHttpClient getHTTPClient() {
        return HttpClients.custom().build();
    }

    public JSONObject makeGETRequest(String requestUri, String username, String password) throws IOException {
        CloseableHttpClient httpClient = this.getHTTPClient();

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

    public JSONObject makeGETRequest(String requestUri) throws IOException {
        CloseableHttpClient httpClient = this.getHTTPClient();

        logger.debug("constructing base64 encoded credentials");

        Header contentType = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpUriRequest request = RequestBuilder.get()
                .addHeader(contentType)
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

    public JSONObject makePOSTRequest(String requestUri, String body, String username, String password) throws IOException {
        CloseableHttpClient httpClient = this.getHTTPClient();

        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        Header authorization = new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
        Header contentType = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpPost post = new HttpPost(requestUri);
        post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        post.addHeader(authorization);
        post.addHeader(contentType);

        CloseableHttpResponse httpResponse = httpClient.execute(post);
        HttpEntity entity = httpResponse.getEntity();
        Header encodingHeader = entity.getContentEncoding();
        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());

        logger.debug("converting http response to json string with encoding: {}", encoding);
        String json = EntityUtils.toString(entity, encoding);
        httpClient.close();
        logger.info("Response for request: " + json + " - " + httpResponse.getStatusLine().getStatusCode());
        return new JSONObject(json);
    }
}
