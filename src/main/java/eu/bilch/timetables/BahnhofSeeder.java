package eu.bilch.timetables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.bilch.timetables.bahnclient.Bahnhof;
import eu.bilch.timetables.model.BahnhofEntity;

/**
 * Füllt die Tabelle bahnhof beim Start aus dem Enum {@link Bahnhof} (Seed).
 * Bestehende Einträge werden aktualisiert, niemals gelöscht – manuelle
 * Ergänzungen in der Datenbank bleiben erhalten.
 */
@Component
public class BahnhofSeeder {

    static final String TYP_HBF = "HBF";
    static final String TYP_SBAHN = "SBAHN";

    private final BahnhofRepository bahnhofRepository;
    private final Logger logger = LoggerFactory.getLogger(BahnhofSeeder.class);

    public BahnhofSeeder(BahnhofRepository bahnhofRepository) {
        this.bahnhofRepository = bahnhofRepository;
    }

    @Order(1)
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seed() {
        int neu = 0;
        int aktualisiert = 0;
        for (Bahnhof bahnhof : Bahnhof.values()) {
            int[] ergebnis = new int[2];
            neu += upsert(bahnhof.getEvaNummer(), bahnhof.getName(), TYP_HBF,
                    bahnhof.getRang(), ergebnis);
            if (bahnhof.getSbahnNummer() != null) {
                neu += upsert(bahnhof.getSbahnNummer(), bahnhof.getName(), TYP_SBAHN,
                        bahnhof.getRang(), ergebnis);
            }
            aktualisiert += ergebnis[0];
        }
        logger.info("Bahnhof-Seed abgeschlossen: {} neu angelegt, {} aktualisiert",
                neu, aktualisiert);
    }

    /**
     * Legt den Eintrag an oder aktualisiert ihn. Zählt Neuanlagen im Rückgabewert
     * und Änderungen im ersten Element des Zähler-Arrays.
     */
    private int upsert(String evaNummer, String name, String typ, int rang, int[] zaehler) {
        BahnhofEntity bestehend = bahnhofRepository.findById(evaNummer).orElse(null);
        if (bestehend == null) {
            bahnhofRepository.save(new BahnhofEntity(evaNummer, name, typ, rang));
            return 1;
        }
        boolean geaendert = false;
        if (!name.equals(bestehend.getName())) {
            bestehend.setName(name);
            geaendert = true;
        }
        if (!typ.equals(bestehend.getTyp())) {
            bestehend.setTyp(typ);
            geaendert = true;
        }
        if (rang != bestehend.getRang()) {
            bestehend.setRang(rang);
            geaendert = true;
        }
        if (geaendert) {
            bahnhofRepository.save(bestehend);
            zaehler[0] += 1;
        }
        return 0;
    }
}
