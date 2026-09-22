package eu.bilch.timetables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.bilch.timetables.bahnclient.Arrival;
import eu.bilch.timetables.bahnclient.Departure;
import eu.bilch.timetables.bahnclient.Stop;
import eu.bilch.timetables.bahnclient.Timetable;
import eu.bilch.timetables.bahnclient.TrainLine;
import eu.bilch.timetables.model.Fahrt;
import eu.bilch.timetables.model.Zug;

class TimetableServiceTest {

    private final TimetableService service = new TimetableService(null, null, null, null);

    @Test
    void toFahrtMitAnkunftUndAbfahrt() {
        Stop stop = new Stop();
        Arrival ankunft = new Arrival();
        ankunft.setL("ICE 500");
        ankunft.setPt("2609220830");
        ankunft.setCt("2609220835");
        ankunft.setPpth("Berlin Hbf|Hamburg-Harburg");
        Departure abfahrt = new Departure();
        abfahrt.setL("ICE 500");
        abfahrt.setPt("2609220833");
        abfahrt.setCt("2609220839");
        abfahrt.setPpth("Hamburg-Harburg|Hamburg Hbf|Kiel Hbf");
        TrainLine trainLine = new TrainLine();
        trainLine.setC("ICE");
        trainLine.setN("500");
        stop.setArrivals(List.of(ankunft));
        stop.setDepartures(List.of(abfahrt));
        stop.setTrainLine(trainLine);

        Fahrt fahrt = service.toFahrt(stop);

        assertThat(fahrt).isNotNull();
        assertThat(fahrt.getZug().getZugNummer()).isEqualTo("ICE 500");
        assertThat(fahrt.getZug().getZugTyp()).isEqualTo("ICE");
        assertThat(fahrt.getStartBahnhof()).isEqualTo("Berlin Hbf");
        assertThat(fahrt.getZielBahnhof()).isEqualTo("Kiel Hbf");
        assertThat(fahrt.getAnkunftszeitPlan()).isEqualTo(LocalDateTime.of(2026, 9, 22, 8, 30));
        assertThat(fahrt.getAnkunftszeitIst()).isEqualTo(LocalDateTime.of(2026, 9, 22, 8, 35));
        assertThat(fahrt.getAbfahrtszeitPlan()).isEqualTo(LocalDateTime.of(2026, 9, 22, 8, 33));
        assertThat(fahrt.getAbfahrtszeitIst()).isEqualTo(LocalDateTime.of(2026, 9, 22, 8, 39));
    }

    @Test
    void toFahrtNurAbfahrt() {
        Stop stop = new Stop();
        Departure abfahrt = new Departure();
        abfahrt.setL("RE 3");
        abfahrt.setPt("2609221200");
        abfahrt.setPpth("Hamburg Hbf|Lübeck Hbf");
        stop.setDepartures(List.of(abfahrt));

        Fahrt fahrt = service.toFahrt(stop);

        assertThat(fahrt).isNotNull();
        assertThat(fahrt.getZug().getZugNummer()).isEqualTo("RE 3");
        assertThat(fahrt.getStartBahnhof()).isNull();
        assertThat(fahrt.getZielBahnhof()).isEqualTo("Lübeck Hbf");
        assertThat(fahrt.getAnkunftszeitPlan()).isNull();
        assertThat(fahrt.getAnkunftszeitIst()).isNull();
    }

    @Test
    void toFahrtZugnummerAusTrainLineWennLFehlt() {
        Stop stop = new Stop();
        Departure abfahrt = new Departure();
        abfahrt.setPpth("Hamburg Hbf|Bremen Hbf");
        TrainLine trainLine = new TrainLine();
        trainLine.setC("IC");
        trainLine.setN("2041");
        stop.setDepartures(List.of(abfahrt));
        stop.setTrainLine(trainLine);

        Fahrt fahrt = service.toFahrt(stop);

        assertThat(fahrt).isNotNull();
        assertThat(fahrt.getZug().getZugNummer()).isEqualTo("IC 2041");
        assertThat(fahrt.getZug().getZugTyp()).isEqualTo("IC");
    }

    @Test
    void toFahrtOhneAnkunftUndAbfahrtLiefertNull() {
        Stop stop = new Stop();

        assertThat(service.toFahrt(stop)).isNull();

        Stop stopMitLeerenListen = new Stop();
        stopMitLeerenListen.setArrivals(List.of());
        stopMitLeerenListen.setDepartures(List.of());
        assertThat(service.toFahrt(stopMitLeerenListen)).isNull();
    }

    @Test
    void toFahrtOhneZugnummerLiefertNull() {
        Stop stop = new Stop();
        Departure abfahrt = new Departure();
        abfahrt.setPt("2609221200");
        abfahrt.setPpth("Hamburg Hbf|Lübeck Hbf");
        stop.setDepartures(List.of(abfahrt));

        assertThat(service.toFahrt(stop)).isNull();
    }

    @Test
    void toFahrtMitUnparsebarerZeit() {
        Stop stop = new Stop();
        Departure abfahrt = new Departure();
        abfahrt.setL("ICE 500");
        abfahrt.setPt("keine-zeit");
        stop.setDepartures(List.of(abfahrt));

        Fahrt fahrt = service.toFahrt(stop);

        assertThat(fahrt).isNotNull();
        assertThat(fahrt.getAbfahrtszeitPlan()).isNull();
        assertThat(fahrt.getAbfahrtszeitIst()).isNull();
    }

    @Test
    void verspaetungMinutenBevorzugtAbfahrt() {
        Fahrt fahrt = new Fahrt();
        fahrt.setAbfahrtszeitPlan(LocalDateTime.of(2026, 9, 22, 8, 33));
        fahrt.setAbfahrtszeitIst(LocalDateTime.of(2026, 9, 22, 8, 39));
        fahrt.setAnkunftszeitPlan(LocalDateTime.of(2026, 9, 22, 8, 30));
        fahrt.setAnkunftszeitIst(LocalDateTime.of(2026, 9, 22, 8, 31));

        assertThat(service.verspaetungMinuten(fahrt)).isEqualTo(6);
    }

    @Test
    void verspaetungMinutenFehltAufAnkunftZurueck() {
        Fahrt fahrt = new Fahrt();
        fahrt.setAnkunftszeitPlan(LocalDateTime.of(2026, 9, 22, 8, 30));
        fahrt.setAnkunftszeitIst(LocalDateTime.of(2026, 9, 22, 8, 35));

        assertThat(service.verspaetungMinuten(fahrt)).isEqualTo(5);
    }

    @Test
    void verspaetungMinutenNegativBeiFrueherAnkunft() {
        Fahrt fahrt = new Fahrt();
        fahrt.setAnkunftszeitPlan(LocalDateTime.of(2026, 9, 22, 8, 30));
        fahrt.setAnkunftszeitIst(LocalDateTime.of(2026, 9, 22, 8, 27));

        assertThat(service.verspaetungMinuten(fahrt)).isEqualTo(-3);
    }

    @Test
    void verspaetungMinutenNullBeiFehlendenDaten() {
        assertThat(service.verspaetungMinuten(null)).isNull();

        Fahrt nurPlan = new Fahrt();
        nurPlan.setAnkunftszeitPlan(LocalDateTime.of(2026, 9, 22, 8, 30));
        assertThat(service.verspaetungMinuten(nurPlan)).isNull();

        assertThat(service.verspaetungMinuten(new Fahrt())).isNull();
    }

    @Test
    void verspaetungMinutenMitZug() {
        Zug zug = new Zug("ICE 500", "ICE");
        Fahrt fahrt = new Fahrt(zug, "Berlin Hbf", "Kiel Hbf",
                LocalDateTime.of(2026, 9, 22, 8, 33), LocalDateTime.of(2026, 9, 22, 8, 30),
                LocalDateTime.of(2026, 9, 22, 8, 40), null);

        assertThat(service.verspaetungMinuten(fahrt)).isEqualTo(7);
    }

    @Test
    void aktualisiereFahrtenFuerEvaFaengtApiFehlerAb() {
        BahnApiService api = mock(BahnApiService.class);
        when(api.fetchFchg("8002549")).thenThrow(new RuntimeException("API nicht erreichbar"));
        TimetableService serviceMitApi = new TimetableService(api, null, null, null);

        assertThatCode(() -> serviceMitApi.aktualisiereFahrtenFuerEva("8002549"))
                .doesNotThrowAnyException();
        verify(api).fetchFchg("8002549");
    }

    @Test
    void aktualisiereFahrtenFuerEvaMitLeeremTimetableTutNichts() {
        BahnApiService api = mock(BahnApiService.class);
        when(api.fetchFchg("8000105")).thenReturn(new Timetable());
        TimetableService serviceMitApi = new TimetableService(api, null, null, null);

        assertThatCode(() -> serviceMitApi.aktualisiereFahrtenFuerEva("8000105"))
                .doesNotThrowAnyException();
    }
}
