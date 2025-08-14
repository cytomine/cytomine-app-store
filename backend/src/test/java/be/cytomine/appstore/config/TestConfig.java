package be.cytomine.appstore.config;

import be.cytomine.appstore.utils.ApiClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig
{
    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }

}
