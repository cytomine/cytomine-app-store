package be.cytomine.appstore.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import be.cytomine.appstore.utils.ApiClient;

@TestConfiguration
public class TestConfig {
    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }

}
