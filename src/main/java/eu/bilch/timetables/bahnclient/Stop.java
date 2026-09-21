package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"messages", "arrivals", "departures", "trainLine", "reference"})
public class Stop {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private Long eva;

    @XmlElement(name = "m")
    private List<Message> messages;

    @XmlElement(name = "ar")
    private List<Arrival> arrivals;

    @XmlElement(name = "dp")
    private List<Departure> departures;

    @XmlElement(name = "tl")
    private TrainLine trainLine;

    @XmlElement(name = "ref")
    private Reference reference;

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEva() {
        return eva;
    }

    public void setEva(Long eva) {
        this.eva = eva;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Arrival> getArrivals() {
        return arrivals;
    }

    public void setArrivals(List<Arrival> arrivals) {
        this.arrivals = arrivals;
    }

    public List<Departure> getDepartures() {
        return departures;
    }

    public void setDepartures(List<Departure> departures) {
        this.departures = departures;
    }

    public TrainLine getTrainLine() {
        return trainLine;
    }

    public void setTrainLine(TrainLine trainLine) {
        this.trainLine = trainLine;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id='" + id + '\'' +
                ", eva=" + eva +
                ", messages=" + messages +
                ", arrivals=" + arrivals +
                ", departures=" + departures +
                ", trainLine=" + trainLine +
                ", reference=" + reference +
                '}';
    }
}