package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "timetable")
@XmlAccessorType(XmlAccessType.FIELD)
public class Timetable {

    @XmlAttribute
    private String station;

    @XmlAttribute
    private Long eva;

    @XmlElement(name = "s")
    private List<Stop> stops;

    // Getter und Setter
    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Long getEva() {
        return eva;
    }

    public void setEva(Long eva) {
        this.eva = eva;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "station='" + station + '\'' +
                ", eva=" + eva +
                ", stops=" + stops +
                '}';
    }
}