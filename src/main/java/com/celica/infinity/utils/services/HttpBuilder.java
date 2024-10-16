package com.celica.infinity.utils.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpBuilder<T> {
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private String url;
    private String method = "GET";
    private Map<String, String> headers = new HashMap<>();
    private Object body;
    private Map<String, String> formData;
    private final Class<T> responseType;
    private ContentType contentType = ContentType.JSON;

    public enum ContentType {
        JSON,
        FORM_DATA
    }

    public HttpBuilder(Class<T> responseType) {
        this.responseType = responseType;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.formData = new HashMap<>();
    }

    public HttpBuilder<T> url(String url) {
        this.url = url;
        return this;
    }

    public HttpBuilder<T> method(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public HttpBuilder<T> header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpBuilder<T> body(Object body) {
        this.body = body;
        this.contentType = ContentType.JSON;
        return this;
    }

    public HttpBuilder<T> formData(String key, String value) {
        this.formData.put(key, value);
        this.contentType = ContentType.FORM_DATA;
        return this;
    }

    private String encodeFormData() {
        return formData.entrySet().stream()
                .map(e -> encodeParameter(e.getKey()) + "=" + encodeParameter(e.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String encodeParameter(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public T execute() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10));

        // Add headers
        headers.forEach(requestBuilder::header);

        // Handle different content types
        if (!method.equals("GET")) {
            switch (contentType) {
                case JSON:
                    if (body != null) {
                        String jsonBody = objectMapper.writeValueAsString(body);
                        requestBuilder.header("Content-Type", "application/json");
                        requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(jsonBody));
                    }
                    break;

                case FORM_DATA:
                    String formDataString = encodeFormData();
                    requestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
                    requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(formDataString));
                    break;
            }
        } else {
            requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        HttpResponse<String> response = client.send(
                requestBuilder.build(),
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            System.out.println(response.body());
            return objectMapper.readValue(response.body(), responseType);
        } else {
            throw new RuntimeException("HTTP request failed with status: "+response.statusCode()+ "\nUrl:"+this.url+"" +
                    "\nResponse Body:" + response.body());
        }
    }
}
