package eu.bilch.timetables.bahnclient;

import java.util.List;

/**
 * Von der DB Timetables API unterstützte Bahnhöfe mit ihrer EVA-Nummer als
 * Haupt-ID und optionaler separater ID für S-Bahn- bzw. Tiefbahnsteige.
 * Dient als Seed-Quelle für die Tabelle bahnhof.
 */
public enum Bahnhof {

    HAMBURG_HBF("Hamburg Hbf", "8002549", "8098549", 1),
    FRANKFURT_MAIN_HBF("Frankfurt (Main) Hbf", "8000105", null, 2),
    MUENCHEN_HBF("München Hbf", "8000261", "8098263", 3),
    BERLIN_HBF("Berlin Hbf", "8011160", "8089021", 4),
    KOELN_HBF("Köln Hbf", "8000207", null, 5),
    HANNOVER_HBF("Hannover Hbf", "8000152", null, 6),
    STUTTGART_HBF("Stuttgart Hbf", "8000096", null, 7),
    DUESSELDORF_HBF("Düsseldorf Hbf", "8000085", null, 8),
    NUERNBERG_HBF("Nürnberg Hbf", "8000284", null, 9),
    ESSEN_HBF("Essen Hbf", "8000098", null, 10);

    private final String name;
    private final String evaNummer;
    private final String sbahnNummer;
    private final int rang;

    Bahnhof(String name, String evaNummer, String sbahnNummer, int rang) {
        this.name = name;
        this.evaNummer = evaNummer;
        this.sbahnNummer = sbahnNummer;
        this.rang = rang;
    }

    public String getName() {
        return name;
    }

    public String getEvaNummer() {
        return evaNummer;
    }

    public String getSbahnNummer() {
        return sbahnNummer;
    }

    public int getRang() {
        return rang;
    }

    /**
     * Alle abzufragenden EVA-Nummern des Bahnhofs: Haupt-ID und, sofern vorhanden,
     * die separate S-Bahn-ID.
     */
    public List<String> getEvaNummern() {
        return sbahnNummer == null ? List.of(evaNummer) : List.of(evaNummer, sbahnNummer);
    }
}
