package eu.bilch.timetables;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApiRateLimiterTest {

    @Test
    void erlaubtErstenAufrufOhneWarten() {
        ApiRateLimiter limiter = new ApiRateLimiter();

        long start = System.currentTimeMillis();
        limiter.erwarteFreigabe();
        long dauer = System.currentTimeMillis() - start;

        assertThat(dauer).isLessThan(900);
    }

    @Test
    void weitererAufrufWartetMindestensEineSekunde() throws InterruptedException {
        ApiRateLimiter limiter = new ApiRateLimiter();

        limiter.erwarteFreigabe();
        Thread.sleep(50);
        long start = System.currentTimeMillis();
        limiter.erwarteFreigabe();
        long dauer = System.currentTimeMillis() - start;

        assertThat(dauer).isGreaterThanOrEqualTo(900);
    }

    @Test
    void vieleAufrufeHaltenMindestabstandVonEinerSekunde() {
        ApiRateLimiter limiter = new ApiRateLimiter();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            limiter.erwarteFreigabe();
        }
        long gesamtDauer = System.currentTimeMillis() - start;

        assertThat(gesamtDauer).isGreaterThanOrEqualTo(2000);
    }

    @Test
    void interruptionSetztFlagUndBlockiertNicht() {
        ApiRateLimiter limiter = new ApiRateLimiter();

        Thread thread = new Thread(limiter::erwarteFreigabe);
        thread.start();
        thread.interrupt();
        try {
            thread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertThat(thread.isInterrupted() || !thread.isAlive()).isTrue();
    }
}
