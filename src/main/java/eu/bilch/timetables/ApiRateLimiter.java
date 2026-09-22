package eu.bilch.timetables;

/**
 * Begrenzt API-Aufrufe auf höchstens einen pro Sekunde über alle Threads hinweg.
 * Die DB Timetables API erlaubt maximal eine Abfrage pro Sekunde.
 */
class ApiRateLimiter {

    private static final long MINDESTABSTAND_MILLIS = 1000;

    private long naechsteFreigabeMillis = System.currentTimeMillis();

    /**
     * Blockiert den aufrufenden Thread, bis der nächste API-Aufruf erlaubt ist.
     */
    synchronized void erwarteFreigabe() {
        long jetzt = System.currentTimeMillis();
        long warten = naechsteFreigabeMillis - jetzt;
        if (warten > 0) {
            try {
                Thread.sleep(warten);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        long freigabe = Math.max(jetzt, naechsteFreigabeMillis);
        naechsteFreigabeMillis = freigabe + MINDESTABSTAND_MILLIS;
    }
}
