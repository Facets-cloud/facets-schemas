package com.capillary.ops.service;

import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class DeisApiErrorHandler extends DefaultResponseErrorHandler {
    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        String statusText = response.getStatusText();
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody()))) {
            String data = buffer.lines().collect(Collectors.joining());
            if(data.contains("already exists")) {
                System.out.println(data);
                throw new ResourceAlreadyExists();
            }
        }
        super.handleError(response, statusCode);
    }
}
