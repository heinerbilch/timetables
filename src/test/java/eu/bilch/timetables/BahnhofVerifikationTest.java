package eu.bilch.timetables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import eu.bilch.timetables.bahnclient.Bahnhof;
import eu.bilch.timetables.bahnclient.Station;
import eu.bilch.timetables.bahnclient.Stations;

class BahnhofVerifikationTest {

    private final BahnApiService bahnApiService = mock(BahnApiService.class);
    private final BahnhofVerifikation verifikation = new BahnhofVerifikation(bahnApiService);

    private Station station(String eva, String name) {
        Station station = new Station();
        station.setEva(eva);
        station.setName(name);
        return station;
    }

    private Stations stations(Station... stationen) {
        Stations stations = new Stations();
        stations.setStationList(List.of(stationen));
        return stations;
    }

    @Test
    void bestaetigteEvaLiefertKeineAbweichung() {
        when(bahnApiService.fetchStations("8002549"))
                .thenReturn(stations(station("8002549", "Hamburg Hbf")));
        when(bahnApiService.fetchStations("8098549"))
                .thenReturn(stations(station("8098549", "Hamburg Hbf")));

        assertThat(verifikation.pruefe(Bahnhof.HAMBURG_HBF)).isNull();
    }

    @Test
    void abweichendeEvaLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString())).thenReturn(
                stations(station("9999999", "Anderer Bahnhof")));

        String meldung = verifikation.pruefe(Bahnhof.FRANKFURT_MAIN_HBF);

        assertThat(meldung).isNotNull();
        assertThat(meldung).contains("8000105").contains("nicht bestätigt");
    }

    @Test
    void leereAntwortLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString())).thenReturn(stations());

        String meldung = verifikation.pruefe(Bahnhof.KOELN_HBF);

        assertThat(meldung).isNotNull();
        assertThat(meldung).contains("keine Station mit EVA 8000207");
    }

    @Test
    void fehlerBeiApiAufrufLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString()))
                .thenThrow(new RuntimeException("Verbindung fehlgeschlagen"));

        String meldung = verifikation.pruefe(Bahnhof.HANNOVER_HBF);

        assertThat(meldung).isNotNull();
        assertThat(meldung).contains("fehlgeschlagen").contains("Verbindung fehlgeschlagen");
    }

    @Test
    void nullAntwortLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString())).thenReturn(null);

        assertThat(verifikation.pruefe(Bahnhof.ESSEN_HBF)).isNotNull();
    }

    @Test
    void mehrereStationenInAntwortSindOk() {
        when(bahnApiService.fetchStations("8000284")).thenReturn(stations(station("8000001", "Aachen Hbf"),
                station("8000284", "Nürnberg Hbf")));

        assertThat(verifikation.pruefe(Bahnhof.NUERNBERG_HBF)).isNull();
    }
}
