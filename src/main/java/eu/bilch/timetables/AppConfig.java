package eu.bilch.timetables;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    // Authentifizierungsdaten aus application.yml injizieren
    @Value("${bahn.api.client-id}")
    private String clientId;

    @Value("${bahn.api.key}")
    private String apiKey;

    @Bean WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("DB-Client-ID", clientId)
                .defaultHeader("DB-Api-Key", apiKey)
                .defaultHeader("accept", "application/xml")
                .build();
    }
}