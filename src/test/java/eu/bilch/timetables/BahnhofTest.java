package eu.bilch.timetables;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.bilch.timetables.bahnclient.Bahnhof;

class BahnhofTest {

    @Test
    void hamburgHbfHatEvaUndSbahnNummer() {
        assertThat(Bahnhof.HAMBURG_HBF.getName()).isEqualTo("Hamburg Hbf");
        assertThat(Bahnhof.HAMBURG_HBF.getEvaNummer()).isEqualTo("8002549");
        assertThat(Bahnhof.HAMBURG_HBF.getSbahnNummer()).isEqualTo("8098549");
    }

    @Test
    void alleBahnhofeHabenEindeutigeEvaNummern() {
        assertThat(Bahnhof.values())
                .extracting(Bahnhof::getEvaNummer)
                .doesNotHaveDuplicates();
    }

    @Test
    void frankfurtHatKeineSbahnNummer() {
        assertThat(Bahnhof.FRANKFURT_MAIN_HBF.getEvaNummer()).isEqualTo("8000105");
        assertThat(Bahnhof.FRANKFURT_MAIN_HBF.getSbahnNummer()).isNull();
    }

    @Test
    void evaNummernOhneSbahnEnthaltenNurHauptId() {
        assertThat(Bahnhof.FRANKFURT_MAIN_HBF.getEvaNummern()).containsExactly("8000105");
    }

    @Test
    void evaNummernMitSbahnEnthaltenHauptIdUndSbahn() {
        assertThat(Bahnhof.HAMBURG_HBF.getEvaNummern())
                .containsExactly("8002549", "8098549");
    }

    @Test
    void alleEvaNummernUeberAlleBahnhoefeSindEindeutig() {
        List<String> alle = new ArrayList<>();
        for (Bahnhof bahnhof : Bahnhof.values()) {
            alle.addAll(bahnhof.getEvaNummern());
        }
        assertThat(alle).doesNotHaveDuplicates();
    }
}
