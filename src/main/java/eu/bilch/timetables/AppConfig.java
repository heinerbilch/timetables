package eu.bilch.timetables;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
public class AppConfig {
        // API for passenger information for train stations operated by DB
        // Station&Service AG
        private static final String API_URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/timetables/v1";
        // Authentifizierungsdaten aus application.yml injizieren
        @Value("${bahn.api.client-id}")
        private String clientId;

        @Value("${bahn.api.key}")
        private String apiKey;

        @Bean
        WebClient webClient() {
                return WebClient.builder()
                                .defaultHeader("DB-Client-ID", clientId)
                                .defaultHeader("DB-Api-Key", apiKey)
                                .defaultHeader("accept", "application/xml")
                                .baseUrl(API_URL)
                                .exchangeStrategies(ExchangeStrategies
                                                .builder()
                                                .codecs(configurer -> {
                                                        configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder());
                                                        configurer.defaultCodecs().maxInMemorySize(1000 * 1024);
                                                })
                                                .build())
                                .build();
        }
}