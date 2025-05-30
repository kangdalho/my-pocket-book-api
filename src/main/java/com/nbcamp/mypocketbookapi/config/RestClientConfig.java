package com.nbcamp.mypocketbookapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${kakao.api.baseurl}")
    private String baseUrl;

    @Value("${kakao.api.path.book}")
    private String pathBookUrl;

    @Value("${kakao.restapi.key}")
    private String apiKey;

    @Bean
    public RestClient kakaoBookRestClient(RestClient.Builder builder) {
        return builder.baseUrl(baseUrl + pathBookUrl)
                .defaultHeader("Authorization", apiKey)
                .build();
    }

}
