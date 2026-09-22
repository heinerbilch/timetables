package eu.bilch.timetables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import eu.bilch.timetables.bahnclient.Stations;
import eu.bilch.timetables.bahnclient.Timetable;
import reactor.core.publisher.Mono;

@Service
public class BahnApiService {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(BahnApiService.class);

    public BahnApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Scheduled(fixedRate = 100000) // 10 Sekunden
    public void fetchAndPublishFahrten() {
        try {
            Stations response = webClient.get()
                    .uri("/station/BLS")
                    .retrieve()
                    .bodyToMono(Stations.class)
                    .block(); // Blockiert, um synchron zu arbeiten (für Demo okay)
            logger.info("Empfangene Nachricht: {}", response);
        } catch (WebClientRequestException e) {
            logger.warn("I/O Problem {}", e.getLocalizedMessage());
        }
    }

    public void fetchTimetable() {
    // Verwendung
    Mono<Timetable> timetableMono = webClient.get()
            .uri("/fchg/8000001")
            .retrieve()
            .bodyToMono(Timetable.class);
    logger.info("Mono FCHG Data {}", timetableMono.block());
    }

//    @Scheduled(fixedRate = 100000) // 10 Sekunden
    public void fetchRchg() {
    // Verwendung
    Mono<Timetable> timetableMono = webClient.get()
            .uri("/rchg/8000105")
            .retrieve()
            .bodyToMono(Timetable.class);
    logger.info("Mono RCHG Data {}", timetableMono.block());
    }
}