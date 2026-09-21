package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String t; // Typ (z. B. "h", "f", "d", "c", "q")

    @XmlAttribute
    private String from;

    @XmlAttribute
    private String to;

    @XmlAttribute
    private String cat; // Kategorie (z. B. "Störung", "Information")

    @XmlAttribute
    private String ts; // Timestamp

    @XmlAttribute(name = "ts-tts")
    private String tsTts; // Menschlich lesbare Zeit

    @XmlAttribute
    private String pr; // Priorität

    @XmlAttribute
    private String c; // Code (z. B. für Fehlercodes)

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getTsTts() {
        return tsTts;
    }

    public void setTsTts(String tsTts) {
        this.tsTts = tsTts;
    }

    public String getPr() {
        return pr;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", t='" + t + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cat='" + cat + '\'' +
                ", ts='" + ts + '\'' +
                ", tsTts='" + tsTts + '\'' +
                ", pr='" + pr + '\'' +
                ", c='" + c + '\'' +
                '}';
    }
}