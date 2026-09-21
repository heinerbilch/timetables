package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class TrainLine {

    @XmlAttribute
    private String f; // Fahrtrichtung (z. B. "F")

    @XmlAttribute
    private String t; // Typ (z. B. "p", "s", "e")

    @XmlAttribute
    private String o; // Operator (z. B. "87", "NZ")

    @XmlAttribute
    private String c; // Kategorie (z. B. "TGV", "ICE", "RB")

    @XmlAttribute
    private String n; // Zugnummer

    // Getter und Setter
    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "TrainLine{" +
                "f='" + f + '\'' +
                ", t='" + t + '\'' +
                ", o='" + o + '\'' +
                ", c='" + c + '\'' +
                ", n='" + n + '\'' +
                '}';
    }
}
