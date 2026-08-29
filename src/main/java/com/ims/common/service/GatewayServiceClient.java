package com.ims.common.service;

import com.ims.common.config.GatewayClientConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class GatewayServiceClient {

    private final HttpClient httpClient;
    private final GatewayClientConfig gatewayClientConfig;

    public GatewayServiceClient(HttpClient httpClient, GatewayClientConfig gatewayClientConfig) {
        this.httpClient = httpClient;
        this.gatewayClientConfig = gatewayClientConfig;
    }

    public String getStudent(String path) {
        return call(gatewayClientConfig.getStudentServiceUrl() + path, "GET", null);
    }

    public String postStudent(String path, String body) {
        return call(gatewayClientConfig.getStudentServiceUrl() + path, "POST", body);
    }

    public String deleteStudent(String path) {
        return call(gatewayClientConfig.getStudentServiceUrl() + path, "DELETE", null);
    }

    public String getTeacher(String path) {
        return call(gatewayClientConfig.getTeacherServiceUrl() + path, "GET", null);
    }

    public String postTeacher(String path, String body) {
        return call(gatewayClientConfig.getTeacherServiceUrl() + path, "POST", body);
    }

    public String deleteTeacher(String path) {
        return call(gatewayClientConfig.getTeacherServiceUrl() + path, "DELETE", null);
    }

    public String getAdmin(String path) {
        return call(gatewayClientConfig.getAdminServiceUrl() + path, "GET", null);
    }

    public String postAdmin(String path, String body) {
        return call(gatewayClientConfig.getAdminServiceUrl() + path, "POST", body);
    }

    public String deleteAdmin(String path) {
        return call(gatewayClientConfig.getAdminServiceUrl() + path, "DELETE", null);
    }

    private String call(String url, String method, String body) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json");

            if (body != null && !body.isBlank()) {
                builder.method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
            } else {
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            }

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Downstream service call failed with status " + response.statusCode() + ": " + response.body());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to call downstream service: " + url, e);
        }
    }
}
