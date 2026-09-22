package eu.bilch.timetables;

import static org.assertj.core.api.Assertions.assertThat;

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
}
