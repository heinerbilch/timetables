package eu.bilch.timetables;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.bilch.timetables.bahnclient.Arrival;
import eu.bilch.timetables.bahnclient.Departure;
import eu.bilch.timetables.bahnclient.Stop;
import eu.bilch.timetables.bahnclient.Timetable;
import eu.bilch.timetables.bahnclient.TrainLine;
import eu.bilch.timetables.model.Fahrt;
import eu.bilch.timetables.model.Zug;

@Service
public class TimetableService {

    private static final String HAMBURG_HBF = "8000105";
    private static final DateTimeFormatter ZEIT_FORMAT = DateTimeFormatter.ofPattern("yyMMddHHmm");

    private final BahnApiService bahnApiService;
    private final FahrtRepository fahrtRepository;
    private final ZugRepository zugRepository;
    private final Logger logger = LoggerFactory.getLogger(TimetableService.class);

    public TimetableService(BahnApiService bahnApiService, FahrtRepository fahrtRepository,
            ZugRepository zugRepository) {
        this.bahnApiService = bahnApiService;
        this.fahrtRepository = fahrtRepository;
        this.zugRepository = zugRepository;
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 60000)
    public void aktualisiereFahrten() {
        try {
            Timetable timetable = bahnApiService.fetchFchg(HAMBURG_HBF);
            if (timetable == null || timetable.getStops() == null) {
                return;
            }
            for (Stop stop : timetable.getStops()) {
                Fahrt fahrt = toFahrt(stop);
                if (fahrt != null) {
                    speichereOderAktualisiere(fahrt);
                }
            }
        } catch (Exception e) {
            logger.warn("Fehler beim Aktualisieren der Fahrten: {}", e.getLocalizedMessage());
        }
    }

    Fahrt toFahrt(Stop stop) {
        Arrival ankunft = erstesElement(stop.getArrivals());
        Departure abfahrt = erstesElement(stop.getDepartures());
        if (ankunft == null && abfahrt == null) {
            return null;
        }
        TrainLine trainLine = stop.getTrainLine();
        String zugNummer = abfahrt != null ? abfahrt.getL() : ankunft.getL();
        if (zugNummer == null && trainLine != null && trainLine.getN() != null) {
            zugNummer = trainLine.getC() + " " + trainLine.getN();
        }
        String zugTyp = trainLine != null ? trainLine.getC() : null;
        if (zugNummer == null) {
            logger.warn("Stop ohne ermittelbare Zugnummer wird übersprungen: {}", stop.getId());
            return null;
        }
        Zug zug = new Zug(zugNummer, zugTyp);
        String startBahnhof = ankunft != null ? ersteStation(ankunft.getPpth()) : null;
        String zielBahnhof = abfahrt != null ? letzteStation(abfahrt.getPpth()) : null;
        LocalDateTime ankunftszeitPlan = ankunft != null ? parseZeit(ankunft.getPt()) : null;
        LocalDateTime ankunftszeitIst = ankunft != null ? parseZeit(ankunft.getCt()) : null;
        LocalDateTime abfahrtszeitPlan = abfahrt != null ? parseZeit(abfahrt.getPt()) : null;
        LocalDateTime abfahrtszeitIst = abfahrt != null ? parseZeit(abfahrt.getCt()) : null;
        return new Fahrt(zug, startBahnhof, zielBahnhof,
                abfahrtszeitPlan, ankunftszeitPlan, abfahrtszeitIst, ankunftszeitIst);
    }

    public Integer verspaetungMinuten(Fahrt fahrt) {
        if (fahrt == null) {
            return null;
        }
        if (fahrt.getAbfahrtszeitPlan() != null && fahrt.getAbfahrtszeitIst() != null) {
            return (int) Duration.between(fahrt.getAbfahrtszeitPlan(), fahrt.getAbfahrtszeitIst()).toMinutes();
        }
        if (fahrt.getAnkunftszeitPlan() != null && fahrt.getAnkunftszeitIst() != null) {
            return (int) Duration.between(fahrt.getAnkunftszeitPlan(), fahrt.getAnkunftszeitIst()).toMinutes();
        }
        return null;
    }

    private void speichereOderAktualisiere(Fahrt fahrt) {
        Zug zug = speichereOderAktualisiereZug(fahrt.getZug());
        fahrt.setZug(zug);
        List<Fahrt> vorhandene = fahrtRepository.findByZugZugNummer(zug.getZugNummer());
        Optional<Fahrt> treffer = vorhandene.stream()
                .filter(f -> Objects.equals(f.getAbfahrtszeitPlan(), fahrt.getAbfahrtszeitPlan())
                        && Objects.equals(f.getAnkunftszeitPlan(), fahrt.getAnkunftszeitPlan()))
                .findFirst();
        if (treffer.isPresent()) {
            Fahrt bestehende = treffer.get();
            bestehende.setStartBahnhof(fahrt.getStartBahnhof());
            bestehende.setZielBahnhof(fahrt.getZielBahnhof());
            bestehende.setAbfahrtszeitIst(fahrt.getAbfahrtszeitIst());
            bestehende.setAnkunftszeitIst(fahrt.getAnkunftszeitIst());
            bestehende.setTimestamp(LocalDateTime.now());
            fahrtRepository.save(bestehende);
        } else {
            fahrtRepository.save(fahrt);
        }
    }

    private Zug speichereOderAktualisiereZug(Zug zug) {
        Zug bestehender = zugRepository.findById(zug.getZugNummer()).orElse(null);
        if (bestehender == null) {
            return zugRepository.save(zug);
        }
        if (zug.getZugTyp() != null && !zug.getZugTyp().equals(bestehender.getZugTyp())) {
            bestehender.setZugTyp(zug.getZugTyp());
            return zugRepository.save(bestehender);
        }
        return bestehender;
    }

    private LocalDateTime parseZeit(String zeit) {
        if (zeit == null || zeit.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(zeit, ZEIT_FORMAT);
        } catch (DateTimeParseException e) {
            logger.warn("Unparsebare Zeitangabe: {}", zeit);
            return null;
        }
    }

    private String ersteStation(String ppth) {
        if (ppth == null) {
            return null;
        }
        int trenner = ppth.indexOf('|');
        return trenner >= 0 ? ppth.substring(0, trenner) : ppth;
    }

    private String letzteStation(String ppth) {
        if (ppth == null) {
            return null;
        }
        int trenner = ppth.lastIndexOf('|');
        return trenner >= 0 ? ppth.substring(trenner + 1) : ppth;
    }

    private <T> T erstesElement(List<T> liste) {
        return liste != null && !liste.isEmpty() ? liste.get(0) : null;
    }
}
