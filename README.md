# Timetables

Ein Spring-Boot-Dienst, der Fahrplan- und Betriebsdaten deutscher Bahnhöfe über die
[DB Timetables API](https://developers.deutschebahn.com/db-api-marketplace/apis/timetables/v1)
abruft, in ein eigenes Datenmodell überführt und in PostgreSQL speichert.

## Funktionen

- **Periodischer Datenabruf:** Ein Scheduler (`TimetableService`) lädt alle 60 Sekunden die
  vollständigen Änderungsdaten (FCHG – *full changes*) aller Bahnhöfe aus `bahnclient/Bahnhof`
  (Haupt- und S-Bahn-EVA-Nummern, derzeit 13 Abfragen).
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
| `bahnhof`| `evaNummer` (PK), `name`, `typ` (nullable, `HBF`/`SBAHN`), `rang` (nullable) |

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

- **Datenbank-Migration:** Das Schema wird von [Flyway](https://flywaydb.org) verwaltet. Migrationen
  liegen in `src/main/resources/db/migration` (`V1__init.sql` = aktuelles Baseline-Schema). Hibernate
  prüft das Schema beim Start nur noch (`ddl-auto: validate`). Neue Schema-Änderungen als weitere
  versionierte SQL-Dateien hinzufügen (`V2__...`, `V3__...` usw.).
- Die unterstützten Bahnhöfe liegen in der Tabelle `bahnhof` (Spring Data: `BahnhofRepository`)
  und werden beim Start aus dem Enum `bahnclient/Bahnhof` geseedet (Neu anlegen/Aktualisieren,
  nie Löschen – manuelle Ergänzungen in der DB bleiben erhalten). S-Bahn-EVAs sind eigene Zeilen
  mit `typ = 'SBAHN'`. Der Scheduler ruft alle in der DB hinterlegten EVAs ab.
- Beim Start prüft `BahnhofVerifikation` die hinterlegten EVA-Nummern gegen die API
  (`/station/{eva}`) und loggt Warnungen bei Abweichungen; der Start bricht dabei nie ab.
- **Rate-Limit:** Die DB Timetables API erlaubt maximal eine Abfrage pro Sekunde. `ApiRateLimiter`
  (in `BahnApiService`) erzwingt den Mindestabstand global für alle Aufrufe – auch zwischen
  Scheduler und Startup-Verifikation.
