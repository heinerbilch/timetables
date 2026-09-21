package eu.bilch.timetables.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fahrten")
public class Fahrt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fahrtId;

    private String zugNummer;
    private String zugTyp;
    private String startBahnhof;
    private String zielBahnhof;
    private LocalDateTime abfahrtszeitPlan;
    private LocalDateTime ankunftszeitPlan;
    private LocalDateTime abfahrtszeitIst;
    private LocalDateTime ankunftszeitIst;
    private Integer verspaetungMinuten;
    private String status;
    private LocalDateTime timestamp;

    // Standard-Konstruktor
    public Fahrt() {
        this.timestamp = LocalDateTime.now();
    }

    // Konstruktor mit allen Feldern
    public Fahrt(String zugNummer, String zugTyp, String startBahnhof, String zielBahnhof,
            LocalDateTime abfahrtszeitPlan, LocalDateTime ankunftszeitPlan,
            LocalDateTime abfahrtszeitIst, LocalDateTime ankunftszeitIst,
            Integer verspaetungMinuten, String status) {
        this.zugNummer = zugNummer;
        this.zugTyp = zugTyp;
        this.startBahnhof = startBahnhof;
        this.zielBahnhof = zielBahnhof;
        this.abfahrtszeitPlan = abfahrtszeitPlan;
        this.ankunftszeitPlan = ankunftszeitPlan;
        this.abfahrtszeitIst = abfahrtszeitIst;
        this.ankunftszeitIst = ankunftszeitIst;
        this.verspaetungMinuten = verspaetungMinuten;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getter und Setter
    public Long getFahrtId() {
        return fahrtId;
    }

    public void setFahrtId(Long fahrtId) {
        this.fahrtId = fahrtId;
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

    public String getStartBahnhof() {
        return startBahnhof;
    }

    public void setStartBahnhof(String startBahnhof) {
        this.startBahnhof = startBahnhof;
    }

    public String getZielBahnhof() {
        return zielBahnhof;
    }

    public void setZielBahnhof(String zielBahnhof) {
        this.zielBahnhof = zielBahnhof;
    }

    public LocalDateTime getAbfahrtszeitPlan() {
        return abfahrtszeitPlan;
    }

    public void setAbfahrtszeitPlan(LocalDateTime abfahrtszeitPlan) {
        this.abfahrtszeitPlan = abfahrtszeitPlan;
    }

    public LocalDateTime getAnkunftszeitPlan() {
        return ankunftszeitPlan;
    }

    public void setAnkunftszeitPlan(LocalDateTime ankunftszeitPlan) {
        this.ankunftszeitPlan = ankunftszeitPlan;
    }

    public LocalDateTime getAbfahrtszeitIst() {
        return abfahrtszeitIst;
    }

    public void setAbfahrtszeitIst(LocalDateTime abfahrtszeitIst) {
        this.abfahrtszeitIst = abfahrtszeitIst;
    }

    public LocalDateTime getAnkunftszeitIst() {
        return ankunftszeitIst;
    }

    public void setAnkunftszeitIst(LocalDateTime ankunftszeitIst) {
        this.ankunftszeitIst = ankunftszeitIst;
    }

    public Integer getVerspaetungMinuten() {
        return verspaetungMinuten;
    }

    public void setVerspaetungMinuten(Integer verspaetungMinuten) {
        this.verspaetungMinuten = verspaetungMinuten;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Fahrt{" +
                "fahrtId=" + fahrtId +
                ", zugNummer='" + zugNummer + '\'' +
                ", zugTyp='" + zugTyp + '\'' +
                ", startBahnhof='" + startBahnhof + '\'' +
                ", zielBahnhof='" + zielBahnhof + '\'' +
                ", abfahrtszeitPlan=" + abfahrtszeitPlan +
                ", ankunftszeitPlan=" + ankunftszeitPlan +
                ", abfahrtszeitIst=" + abfahrtszeitIst +
                ", ankunftszeitIst=" + ankunftszeitIst +
                ", verspaetungMinuten=" + verspaetungMinuten +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}