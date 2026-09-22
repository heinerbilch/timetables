package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "stations")
public class Stations {
    private List<Station> stationList;

    public Stations() {}

    @XmlElement(name = "station")
    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    @Override
    public String toString() {
        return "Stations [stationList=" + stationList + ", getStationList()=" + getStationList() + ", getClass()="
                + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
}