package eu.bilch.timetables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import eu.bilch.timetables.model.BahnhofEntity;

class BahnhofSeederTest {

    private final BahnhofRepository bahnhofRepository = mock(BahnhofRepository.class);
    private final BahnhofSeeder seeder = new BahnhofSeeder(bahnhofRepository);

    @Test
    void seedLegtAlleEintraegeAnWennTabelleLeer() {
        when(bahnhofRepository.findById(any())).thenReturn(Optional.empty());

        seeder.seed();

        // 10 Hauptbahnhöfe + 3 S-Bahn-EVAs
        verify(bahnhofRepository, org.mockito.Mockito.times(13)).save(any(BahnhofEntity.class));
    }

    @Test
    void seedAktualisiertNameUndRangBeiAbweichung() {
        BahnhofEntity bestehend = new BahnhofEntity("8002549", "Hamburg", BahnhofSeeder.TYP_HBF, 99);
        when(bahnhofRepository.findById(any())).thenReturn(Optional.empty());
        when(bahnhofRepository.findById("8002549")).thenReturn(Optional.of(bestehend));

        seeder.seed();

        assertThat(bestehend.getName()).isEqualTo("Hamburg Hbf");
        assertThat(bestehend.getRang()).isEqualTo(1);
        verify(bahnhofRepository, org.mockito.Mockito.times(13)).save(any(BahnhofEntity.class));
    }

    @Test
    void seedFuelltNullFelderNach() {
        BahnhofEntity bestehend = new BahnhofEntity("8002549", "Hamburg Hbf", null, null);
        when(bahnhofRepository.findById(any())).thenReturn(Optional.empty());
        when(bahnhofRepository.findById("8002549")).thenReturn(Optional.of(bestehend));

        seeder.seed();

        assertThat(bestehend.getTyp()).isEqualTo(BahnhofSeeder.TYP_HBF);
        assertThat(bestehend.getRang()).isEqualTo(1);
        verify(bahnhofRepository, org.mockito.Mockito.times(13)).save(any(BahnhofEntity.class));
    }

    @Test
    void seedSpeichertNichtsWennAllesBereitsAktuell() {
        when(bahnhofRepository.findById(any())).thenAnswer(invocation -> {
            String eva = invocation.getArgument(0);
            return Optional.of(new BahnhofEntity(eva, passenderName(eva), passenderTyp(eva),
                    passenderRang(eva)));
        });

        seeder.seed();

        verify(bahnhofRepository, never()).save(any());
    }

    private String passenderName(String eva) {
        for (var bahnhof : eu.bilch.timetables.bahnclient.Bahnhof.values()) {
            if (bahnhof.getEvaNummer().equals(eva) || eva.equals(bahnhof.getSbahnNummer())) {
                return bahnhof.getName();
            }
        }
        throw new IllegalArgumentException("Unbekannte EVA: " + eva);
    }

    private String passenderTyp(String eva) {
        for (var bahnhof : eu.bilch.timetables.bahnclient.Bahnhof.values()) {
            if (bahnhof.getSbahnNummer() != null && bahnhof.getSbahnNummer().equals(eva)) {
                return BahnhofSeeder.TYP_SBAHN;
            }
        }
        return BahnhofSeeder.TYP_HBF;
    }

    private int passenderRang(String eva) {
        for (var bahnhof : eu.bilch.timetables.bahnclient.Bahnhof.values()) {
            if (bahnhof.getEvaNummer().equals(eva) || eva.equals(bahnhof.getSbahnNummer())) {
                return bahnhof.getRang();
            }
        }
        throw new IllegalArgumentException("Unbekannte EVA: " + eva);
    }
}
