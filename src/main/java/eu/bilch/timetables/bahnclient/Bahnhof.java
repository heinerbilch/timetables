package eu.bilch.timetables.bahnclient;

import java.util.List;

/**
 * Von der DB Timetables API unterstützte Bahnhöfe mit ihrer EVA-Nummer als
 * Haupt-ID und optionaler separater ID für S-Bahn- bzw. Tiefbahnsteige.
 */
public enum Bahnhof {

    HAMBURG_HBF("Hamburg Hbf", "8002549", "8098549"),
    FRANKFURT_MAIN_HBF("Frankfurt (Main) Hbf", "8000105", null),
    MUENCHEN_HBF("München Hbf", "8000261", "8098263"),
    BERLIN_HBF("Berlin Hbf", "8011160", "8089021"),
    KOELN_HBF("Köln Hbf", "8000207", null),
    HANNOVER_HBF("Hannover Hbf", "8000152", null),
    STUTTGART_HBF("Stuttgart Hbf", "8000096", null),
    DUESSELDORF_HBF("Düsseldorf Hbf", "8000085", null),
    NUERNBERG_HBF("Nürnberg Hbf", "8000284", null),
    ESSEN_HBF("Essen Hbf", "8000098", null);

    private final String name;
    private final String evaNummer;
    private final String sbahnNummer;

    Bahnhof(String name, String evaNummer, String sbahnNummer) {
        this.name = name;
        this.evaNummer = evaNummer;
        this.sbahnNummer = sbahnNummer;
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

    /**
     * Alle abzufragenden EVA-Nummern des Bahnhofs: Haupt-ID und, sofern vorhanden,
     * die separate S-Bahn-ID.
     */
    public List<String> getEvaNummern() {
        return sbahnNummer == null ? List.of(evaNummer) : List.of(evaNummer, sbahnNummer);
    }
}
