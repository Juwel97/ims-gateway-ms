package com.ims.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Getter
@Configuration
public class GatewayClientConfig {

    @Value("${services.student.url:http://localhost:8081}")
    private String studentServiceUrl;

    @Value("${services.teacher.url:http://localhost:8082}")
    private String teacherServiceUrl;

    @Value("${services.admin.url:http://localhost:8083}")
    private String adminServiceUrl;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    }

}
