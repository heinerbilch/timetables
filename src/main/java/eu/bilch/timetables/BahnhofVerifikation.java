package eu.bilch.timetables;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import eu.bilch.timetables.bahnclient.Station;
import eu.bilch.timetables.bahnclient.Stations;
import eu.bilch.timetables.model.BahnhofEntity;

/**
 * Prüft beim Start der Applikation die in der Tabelle bahnhof hinterlegten
 * EVA-Nummern gegen die DB Timetables API und loggt Warnungen bei Abweichungen.
 * Läuft nach dem Seeding, damit auch frisch angelegte Einträge geprüft werden.
 */
@Component
public class BahnhofVerifikation {

    private final BahnApiService bahnApiService;
    private final BahnhofRepository bahnhofRepository;
    private final Logger logger = LoggerFactory.getLogger(BahnhofVerifikation.class);

    public BahnhofVerifikation(BahnApiService bahnApiService, BahnhofRepository bahnhofRepository) {
        this.bahnApiService = bahnApiService;
        this.bahnhofRepository = bahnhofRepository;
    }

    @Order(10)
    @EventListener(ApplicationReadyEvent.class)
    public void pruefeAlleBahnhoefe() {
        for (BahnhofEntity bahnhof : bahnhofRepository.findAll()) {
            String abweichungen = pruefe(bahnhof);
            if (abweichungen != null) {
                logger.warn("Bahnhof {} (EVA {}): {}", bahnhof.getName(), bahnhof.getEvaNummer(),
                        abweichungen);
            } else {
                logger.info("Bahnhof {} (EVA {}) durch die API bestätigt", bahnhof.getName(),
                        bahnhof.getEvaNummer());
            }
        }
    }

    String pruefe(BahnhofEntity bahnhof) {
        return pruefeNummer(bahnhof.getName(), bahnhof.getEvaNummer());
    }

    private String pruefeNummer(String bezeichnung, String nummer) {
        Stations stations;
        try {
            stations = bahnApiService.fetchStations(nummer);
        } catch (Exception e) {
            return bezeichnung + ": Prüfung der EVA " + nummer + " fehlgeschlagen ("
                    + e.getLocalizedMessage() + "); ";
        }
        List<Station> liste = stations != null && stations.getStationList() != null
                ? stations.getStationList()
                : List.of();
        if (liste.isEmpty()) {
            return bezeichnung + ": keine Station mit EVA " + nummer + " gefunden; ";
        }
        boolean bestaetigt = liste.stream().anyMatch(s -> nummer.equals(s.getEva()));
        if (!bestaetigt) {
            return bezeichnung + ": EVA " + nummer + " nicht bestätigt (API liefert "
                    + liste.get(0).getEva() + " – " + liste.get(0).getName() + "); ";
        }
        return null;
    }
}
