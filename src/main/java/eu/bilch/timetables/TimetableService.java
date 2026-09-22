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

@Service
public class TimetableService {

    private static final String HAMBURG_HBF = "8000105";
    private static final DateTimeFormatter ZEIT_FORMAT = DateTimeFormatter.ofPattern("yyMMddHHmm");

    private final BahnApiService bahnApiService;
    private final FahrtRepository fahrtRepository;
    private final Logger logger = LoggerFactory.getLogger(TimetableService.class);

    public TimetableService(BahnApiService bahnApiService, FahrtRepository fahrtRepository) {
        this.bahnApiService = bahnApiService;
        this.fahrtRepository = fahrtRepository;
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
        String startBahnhof = ankunft != null ? ersteStation(ankunft.getPpth()) : null;
        String zielBahnhof = abfahrt != null ? letzteStation(abfahrt.getPpth()) : null;
        LocalDateTime ankunftszeitPlan = ankunft != null ? parseZeit(ankunft.getPt()) : null;
        LocalDateTime ankunftszeitIst = ankunft != null ? parseZeit(ankunft.getCt()) : null;
        LocalDateTime abfahrtszeitPlan = abfahrt != null ? parseZeit(abfahrt.getPt()) : null;
        LocalDateTime abfahrtszeitIst = abfahrt != null ? parseZeit(abfahrt.getCt()) : null;
        Integer verspaetungMinuten = abfahrtszeitPlan != null && abfahrtszeitIst != null
                ? verspaetungMinuten(abfahrtszeitPlan, abfahrtszeitIst)
                : verspaetungMinuten(ankunftszeitPlan, ankunftszeitIst);
        String status = abfahrtszeitIst != null || ankunftszeitIst != null ? "aktuell" : "geplant";
        return new Fahrt(zugNummer, zugTyp, startBahnhof, zielBahnhof,
                abfahrtszeitPlan, ankunftszeitPlan, abfahrtszeitIst, ankunftszeitIst,
                verspaetungMinuten, status);
    }

    private void speichereOderAktualisiere(Fahrt fahrt) {
        List<Fahrt> vorhandene = fahrtRepository.findByZugNummer(fahrt.getZugNummer());
        Optional<Fahrt> treffer = vorhandene.stream()
                .filter(f -> Objects.equals(f.getAbfahrtszeitPlan(), fahrt.getAbfahrtszeitPlan())
                        && Objects.equals(f.getAnkunftszeitPlan(), fahrt.getAnkunftszeitPlan()))
                .findFirst();
        if (treffer.isPresent()) {
            Fahrt bestehende = treffer.get();
            bestehende.setZugTyp(fahrt.getZugTyp());
            bestehende.setStartBahnhof(fahrt.getStartBahnhof());
            bestehende.setZielBahnhof(fahrt.getZielBahnhof());
            bestehende.setAbfahrtszeitIst(fahrt.getAbfahrtszeitIst());
            bestehende.setAnkunftszeitIst(fahrt.getAnkunftszeitIst());
            bestehende.setVerspaetungMinuten(fahrt.getVerspaetungMinuten());
            bestehende.setStatus(fahrt.getStatus());
            bestehende.setTimestamp(LocalDateTime.now());
            fahrtRepository.save(bestehende);
        } else {
            fahrtRepository.save(fahrt);
        }
    }

    private Integer verspaetungMinuten(LocalDateTime plan, LocalDateTime ist) {
        if (plan == null || ist == null) {
            return null;
        }
        return (int) Duration.between(plan, ist).toMinutes();
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
