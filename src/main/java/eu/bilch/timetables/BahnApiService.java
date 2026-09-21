package eu.bilch.timetables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@Service
public class BahnApiService {
    // API for passenger information for train stations operated by DB
    // Station&Service AG
    private static final String API_URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/timetables/v1/station/H";
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(BahnApiService.class);

    public BahnApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Scheduled(fixedRate = 100000) // 10 Sekunden
    public void fetchAndPublishFahrten() {
        try {
            String response = webClient.get()
                    .uri(API_URL)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Blockiert, um synchron zu arbeiten (für Demo okay)
            logger.info("Empfangene Nachricht: {}", response);
        } catch (WebClientRequestException e) {
            logger.warn("I/O Problem {}", e.getLocalizedMessage());
        }
    }
}