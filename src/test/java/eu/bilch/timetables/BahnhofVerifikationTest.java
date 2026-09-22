package eu.bilch.timetables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import eu.bilch.timetables.bahnclient.Station;
import eu.bilch.timetables.bahnclient.Stations;
import eu.bilch.timetables.model.BahnhofEntity;

class BahnhofVerifikationTest {

    private final BahnApiService bahnApiService = mock(BahnApiService.class);
    private final BahnhofRepository bahnhofRepository = mock(BahnhofRepository.class);
    private final BahnhofVerifikation verifikation = new BahnhofVerifikation(bahnApiService,
            bahnhofRepository);

    private Station station(String eva, String name) {
        Station station = new Station();
        station.setEva(eva);
        station.setName(name);
        return station;
    }

    private BahnhofEntity bahnhof(String eva, String name, String typ) {
        return new BahnhofEntity(eva, name, typ, 1);
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

        assertThat(verifikation.pruefe(bahnhof("8002549", "Hamburg Hbf", "HBF"))).isNull();
    }

    @Test
    void abweichendeEvaLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString())).thenReturn(
                stations(station("9999999", "Anderer Bahnhof")));

        String meldung = verifikation.pruefe(bahnhof("8000105", "Frankfurt (Main) Hbf", "HBF"));

        assertThat(meldung).isNotNull();
        assertThat(meldung).contains("8000105").contains("nicht bestätigt");
    }

    @Test
    void leereAntwortLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString())).thenReturn(stations());

        String meldung = verifikation.pruefe(bahnhof("8000207", "Köln Hbf", "HBF"));

        assertThat(meldung).isNotNull();
        assertThat(meldung).contains("keine Station mit EVA 8000207");
    }

    @Test
    void fehlerBeiApiAufrufLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString()))
                .thenThrow(new RuntimeException("Verbindung fehlgeschlagen"));

        String meldung = verifikation.pruefe(bahnhof("8000152", "Hannover Hbf", "HBF"));

        assertThat(meldung).isNotNull();
        assertThat(meldung).contains("fehlgeschlagen").contains("Verbindung fehlgeschlagen");
    }

    @Test
    void nullAntwortLiefertWarnung() {
        when(bahnApiService.fetchStations(anyString())).thenReturn(null);

        assertThat(verifikation.pruefe(bahnhof("8000098", "Essen Hbf", "HBF"))).isNotNull();
    }

    @Test
    void mehrereStationenInAntwortSindOk() {
        when(bahnApiService.fetchStations("8000284")).thenReturn(stations(station("8000001", "Aachen Hbf"),
                station("8000284", "Nürnberg Hbf")));

        assertThat(verifikation.pruefe(bahnhof("8000284", "Nürnberg Hbf", "HBF"))).isNull();
    }
}
