# Timetables

Ein Spring-Boot-Dienst, der Fahrplan- und Betriebsdaten deutscher Bahnhöfe über die
[DB Timetables API](https://developers.deutschebahn.com/db-api-marketplace/apis/timetables/v1)
abruft, in ein eigenes Datenmodell überführt und in PostgreSQL speichert.

## Funktionen

- **Periodischer Datenabruf:** Ein Scheduler (`TimetableService`) lädt alle 60 Sekunden die
  vollständigen Änderungsdaten (FCHG – *full changes*) für Hamburg Hbf (EVA 8000105).
- **Mapping:** Die XML-Antwort der API (`Timetable` → `Stop` → `Arrival`/`Departure`) wird auf
  das JPA-Datenmodell (`Zug`, `Fahrt`) abgebildet – inklusive Zugnummer, Zugtyp, Start- und
  Zielbahnhof, Plan- und Ist-Zeiten.
- **Persistenz:** Fahrten werden per Upsert gespeichert (Duplikat-Erkennung über Zugnummer und
  Plan-Zeiten); der Zugtyp wird zentral pro Zug verwaltet.
- **Verspätungsberechnung:** Die Verspätung in Minuten wird aus Plan- und Ist-Zeiten ermittelt
  (Abfahrt vor Ankunft), ohne redundante Spalten in der Datenbank.

## Datenmodell (3NF)

| Tabelle  | Spalten                                                                              |
|----------|--------------------------------------------------------------------------------------|
| `zug`    | `zugNummer` (PK), `zugTyp`                                                            |
| `fahrten`| `fahrtId` (PK), `zug_nummer` (FK → `zug`), `startBahnhof`, `zielBahnhof`, `abfahrtszeitPlan`, `ankunftszeitPlan`, `abfahrtszeitIst`, `ankunftszeitIst`, `timestamp` |

Abgeleitete Attribute wie Verspätung oder Status werden bewusst nicht gespeichert, sondern zur
Laufzeit berechnet (`TimetableService.verspaetungMinuten`).

## Voraussetzungen

- Java 25
- PostgreSQL (lokal, Standard: `localhost:5432/timetables`)
- Zugangsdaten für die [DB Timetables API](https://developers.deutschebahn.com/db-api-marketplace/apis/timetables/v1)
  (kostenlose Registrierung im DB API Marketplace)

## Konfiguration

Die API-Zugangsdaten werden über Umgebungsvariablen injiziert und stehen **nicht** im Repository:

```bash
export BAHN_CLIENT_ID="<Client-ID aus dem DB API Marketplace>"
export BAHN_CLIENT_SECRET="<API-Key>"
```

Die übrige Konfiguration liegt in `src/main/resources/application.yml`
(Datenbank-Verbindung, Logging, DB-API-Endpunkt).

## Bauen und Starten

```bash
./gradlew build
./gradlew bootRun
```

Der Scheduler startet 3 Sekunden nach dem Application-Start und wiederholt den Abruf
anschließend alle 60 Sekunden. Fehler beim API-Abruf werden geloggt, ohne den Scheduler
zu beenden.

## Tests

```bash
./gradlew test
```

- `TimetableServiceTest`: Unit-Tests für das Mapping (`toFahrt`) und die Verspätungsberechnung –
  laufen ohne Datenbank.
- `TimetablesApplicationTests.contextLoads`: startet den vollen Spring-Kontext und erfordert
  eine erreichbare PostgreSQL-Instanz.

## Projektstruktur

```
src/main/java/eu/bilch/timetables/
├── AppConfig.java            # WebClient-Konfiguration (Auth-Header, JAXB-Decoder)
├── BahnApiService.java       # Zugriff auf die DB Timetables API (station, fchg, rchg, plan)
├── TimetableService.java     # Scheduler, Mapping, Persistenz, Verspätungsberechnung
├── FahrtRepository.java     # Spring-Data-Repository für Fahrten
├── ZugRepository.java        # Spring-Data-Repository für Züge
├── model/                    # JPA-Entitäten (Fahrt, Zug)
└── bahnclient/               # JAXB-Modell der XML-Antworten der DB-API

src/test/java/                # Unit- und Kontext-Tests
src/main/resources/           # application.yml
```

## Hinweise

- Mit `ddl-auto: update` legt Hibernate das Schema automatisch an. Schema-Änderungen sollten
  langfristig über ein Migrationstool (z. B. Flyway) versioniert werden.
- Die abgerufene Station (derzeit Hamburg Hbf, EVA 8000105) ist als Konstante in
  `TimetableService` hinterlegt.
