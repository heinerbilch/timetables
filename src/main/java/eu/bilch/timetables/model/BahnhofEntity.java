package eu.bilch.timetables.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bahnhof")
public class BahnhofEntity {

    @Id
    private String evaNummer;
    private String name;
    private String typ;
    private Integer rang;

    public BahnhofEntity() {
    }

    public BahnhofEntity(String evaNummer, String name, String typ, Integer rang) {
        this.evaNummer = evaNummer;
        this.name = name;
        this.typ = typ;
        this.rang = rang;
    }

    public String getEvaNummer() {
        return evaNummer;
    }

    public void setEvaNummer(String evaNummer) {
        this.evaNummer = evaNummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }

    @Override
    public String toString() {
        return "BahnhofEntity{" +
                "evaNummer='" + evaNummer + '\'' +
                ", name='" + name + '\'' +
                ", typ='" + typ + '\'' +
                ", rang=" + rang +
                '}';
    }
}
