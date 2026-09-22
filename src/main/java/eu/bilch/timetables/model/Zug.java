package eu.bilch.timetables.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "zug")
public class Zug {

    @Id
    private String zugNummer;
    private String zugTyp;

    public Zug() {
    }

    public Zug(String zugNummer, String zugTyp) {
        this.zugNummer = zugNummer;
        this.zugTyp = zugTyp;
    }

    public String getZugNummer() {
        return zugNummer;
    }

    public void setZugNummer(String zugNummer) {
        this.zugNummer = zugNummer;
    }

    public String getZugTyp() {
        return zugTyp;
    }

    public void setZugTyp(String zugTyp) {
        this.zugTyp = zugTyp;
    }

    @Override
    public String toString() {
        return "Zug{" +
                "zugNummer='" + zugNummer + '\'' +
                ", zugTyp='" + zugTyp + '\'' +
                '}';
    }
}
