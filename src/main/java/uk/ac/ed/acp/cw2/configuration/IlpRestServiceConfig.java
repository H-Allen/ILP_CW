package uk.ac.ed.acp.cw2.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;
import uk.ac.ed.acp.cw2.data.RuntimeEnvironment;

import java.net.URL;

@Configuration
@EnableScheduling
public class IlpRestServiceConfig {
    @Bean
    public WebClient webClient() {
        String ilpEndpoint = System.getenv("ILP_ENDPOINT");
        if (ilpEndpoint == null) {
            ilpEndpoint = "https://ilp-rest-2025-bvh6e9hschfagrgy.ukwest-01.azurewebsites.net/";
        }
        return WebClient.builder().baseUrl(ilpEndpoint).build();
    }
}
