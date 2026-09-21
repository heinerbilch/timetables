package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Reference {

    @XmlElement(name = "tl")
    private TrainLine trainLine;

    // Getter und Setter
    public TrainLine getTrainLine() {
        return trainLine;
    }

    public void setTrainLine(TrainLine trainLine) {
        this.trainLine = trainLine;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "trainLine=" + trainLine +
                '}';
    }
}