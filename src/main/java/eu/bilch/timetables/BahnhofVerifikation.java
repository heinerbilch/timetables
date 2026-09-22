package eu.bilch.timetables;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import eu.bilch.timetables.bahnclient.Bahnhof;
import eu.bilch.timetables.bahnclient.Station;
import eu.bilch.timetables.bahnclient.Stations;

/**
 * Prüft beim Start der Applikation die im Enum {@link Bahnhof} hinterlegten
 * EVA-Nummern gegen die DB Timetables API und loggt Warnungen bei Abweichungen.
 */
@Component
public class BahnhofVerifikation {

    private final BahnApiService bahnApiService;
    private final Logger logger = LoggerFactory.getLogger(BahnhofVerifikation.class);

    public BahnhofVerifikation(BahnApiService bahnApiService) {
        this.bahnApiService = bahnApiService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void pruefeAlleBahnhoefe() {
        for (Bahnhof bahnhof : Bahnhof.values()) {
            String abweichungen = pruefe(bahnhof);
            if (abweichungen != null) {
                logger.warn("Bahnhof {} (EVA {}): {}", bahnhof.getName(), bahnhof.getEvaNummer(), abweichungen);
            } else {
                logger.info("Bahnhof {} (EVA {}) durch die API bestätigt", bahnhof.getName(),
                        bahnhof.getEvaNummer());
            }
        }
    }

    String pruefe(Bahnhof bahnhof) {
        StringBuilder abweichungen = new StringBuilder();
        String evaMeldung = pruefeNummer(bahnhof.getName(), bahnhof.getEvaNummer());
        if (evaMeldung != null) {
            abweichungen.append(evaMeldung);
        }
        if (bahnhof.getSbahnNummer() != null) {
            String sbahnMeldung = pruefeNummer(bahnhof.getName() + " (S-Bahn)", bahnhof.getSbahnNummer());
            if (sbahnMeldung != null) {
                abweichungen.append(sbahnMeldung);
            }
        }
        return abweichungen.length() == 0 ? null : abweichungen.toString();
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
