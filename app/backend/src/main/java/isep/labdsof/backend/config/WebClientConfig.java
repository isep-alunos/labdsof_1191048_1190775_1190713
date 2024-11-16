package isep.labdsof.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientConfig {

    @Value("${spring.security.oauth2.resourceserver.opaque-token.introspection-uri}")
    private String introspectUri;

    @Value("${spring.security.oauth2.authorizationserver.endpoint.token-revocation-uri}")
    private String revocationUri;

    @Bean
    public WebClient userInfoClient() {
        return WebClient.builder().baseUrl(introspectUri).build();
    }

    @Bean
    public WebClient revocationClient() {
        return WebClient.builder().baseUrl(revocationUri).build();
    }
}
