package eu.bilch.timetables;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import eu.bilch.timetables.bahnclient.Stations;
import eu.bilch.timetables.bahnclient.Timetable;

/**
 *
 * BahnApiService
 *
 * passenger information service for train stations operated by DB
 * Station&Service AG
 */
@Service
public class BahnApiService {
    private final WebClient webClient;

    public BahnApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Allows access to information about a station.
     *
     * @param station can be a station name (prefix), eva number, ds100/rl100 code,
     *                wildcard (*); doesn't seem to work with umlauten in station
     *                name (prefix)
     * @return all matching stations
     */
    public Stations fetchStations(String station) {
        return webClient.get()
                .uri("/station/" + station)
                .retrieve()
                .bodyToMono(Stations.class)
                .block();
    }

    /**
     * Returns a Timetable object (see Timetable) that contains all known changes
     * for the station given by evaNo. The data includes all known changes from now
     * on until ndefinitely into the future. Once changes become obsolete (because
     * their trip departs from the station) they are removed from this resource.
     * Changes may include messages. On event level, they usually contain one or
     * more of the 'changed' attributes ct, cp, cs or cpth. Changes may also include
     * 'planned' attributes if there is no associated planned data for the change
     * (e.g. an unplanned stop or trip). Full changes are updated every 30s and
     * should be cached for that period by web caches.
     *
     * @param station eva number of the station
     * @return a timetable
     */
    public Timetable fetchFchg(String station) {
        return webClient.get()
                .uri("/fchg/" + station)
                .retrieve()
                .bodyToMono(Timetable.class)
                .block();
    }

    /**
     * Returns a Timetable object (see Timetable) that contains all recent changes
     * for the station given by evaNo. Recent changes are always a subset of the
     * full changes. They may equal full changes but are typically much smaller.
     * Data includes only those changes that became known within the last 2 minutes.
     * A client that updates its state in intervals of less than 2 minutes should
     * load full changes initially and then proceed to periodically load only the
     * recent changes in order to save bandwidth.
     * Recent changes are updated every 30s as well and should be cached for that
     * period by web caches.
     *
     * @param station eva number of the station
     * @return a timetable
     */
    public Timetable fetchRchg(String station) {
        return webClient.get()
                .uri("/rchg/" + station)
                .retrieve()
                .bodyToMono(Timetable.class)
                .block();
    }

    /**
     * Returns a Timetable object (see Timetable) that contains planned data for the
     * specified station (evaNo) within the hourly time slice given by date (format
     * YYMMDD) and hour (format HH). The data includes stops for all trips that
     * arrive or depart within that slice. There is a small overlap between slices
     * since some trips arrive in one slice and depart in another.
     * Planned data does never contain messages. On event level, planned data
     * contains the 'plannned' attributes pt, pp, ps and ppth while the 'changed'
     * attributes ct, cp, cs and cpth are absent.
     * Planned data is generated many hours in advance and is static, i.e. it does
     * never change. It should be cached by web caches.public interface allows
     * access to information about a station.
     *
     * @param station eva number of the station
     * @param date    hourly time slice by date
     * @param hour    hourly time slice by hour
     * @return a timetable
     */
    public Timetable fetchPlan(String station, String date, String hour) {
        return webClient.get()
                .uri("/plan/" + station + "/" + date + "/" + hour)
                .retrieve()
                .bodyToMono(Timetable.class)
                .block();
    }
}