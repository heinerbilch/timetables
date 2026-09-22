package eu.bilch.timetables.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fahrten")
public class Fahrt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fahrtId;

    @ManyToOne
    @JoinColumn(name = "zug_nummer", nullable = false,
            foreignKey = @ForeignKey(name = "fk_fahrt_zug"))
    private Zug zug;

    @ManyToOne
    @JoinColumn(name = "eva_nummer",
            foreignKey = @ForeignKey(name = "fk_fahrt_bahnhof"))
    private BahnhofEntity bahnhof;

    private String startBahnhof;
    private String zielBahnhof;
    private LocalDateTime abfahrtszeitPlan;
    private LocalDateTime ankunftszeitPlan;
    private LocalDateTime abfahrtszeitIst;
    private LocalDateTime ankunftszeitIst;
    private LocalDateTime timestamp;

    public Fahrt() {
        this.timestamp = LocalDateTime.now();
    }

    public Fahrt(Zug zug, String startBahnhof, String zielBahnhof,
            LocalDateTime abfahrtszeitPlan, LocalDateTime ankunftszeitPlan,
            LocalDateTime abfahrtszeitIst, LocalDateTime ankunftszeitIst) {
        this(zug, null, startBahnhof, zielBahnhof, abfahrtszeitPlan, ankunftszeitPlan,
                abfahrtszeitIst, ankunftszeitIst);
    }

    public Fahrt(Zug zug, BahnhofEntity bahnhof, String startBahnhof, String zielBahnhof,
            LocalDateTime abfahrtszeitPlan, LocalDateTime ankunftszeitPlan,
            LocalDateTime abfahrtszeitIst, LocalDateTime ankunftszeitIst) {
        this.zug = zug;
        this.bahnhof = bahnhof;
        this.startBahnhof = startBahnhof;
        this.zielBahnhof = zielBahnhof;
        this.abfahrtszeitPlan = abfahrtszeitPlan;
        this.ankunftszeitPlan = ankunftszeitPlan;
        this.abfahrtszeitIst = abfahrtszeitIst;
        this.ankunftszeitIst = ankunftszeitIst;
        this.timestamp = LocalDateTime.now();
    }

    public Long getFahrtId() {
        return fahrtId;
    }

    public void setFahrtId(Long fahrtId) {
        this.fahrtId = fahrtId;
    }

    public Zug getZug() {
        return zug;
    }

    public void setZug(Zug zug) {
        this.zug = zug;
    }

    public BahnhofEntity getBahnhof() {
        return bahnhof;
    }

    public void setBahnhof(BahnhofEntity bahnhof) {
        this.bahnhof = bahnhof;
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
                ", zug=" + zug +
                ", bahnhof=" + (bahnhof != null ? bahnhof.getEvaNummer() : null) +
                ", startBahnhof='" + startBahnhof + '\'' +
                ", zielBahnhof='" + zielBahnhof + '\'' +
                ", abfahrtszeitPlan=" + abfahrtszeitPlan +
                ", ankunftszeitPlan=" + ankunftszeitPlan +
                ", abfahrtszeitIst=" + abfahrtszeitIst +
                ", ankunftszeitIst=" + ankunftszeitIst +
                ", timestamp=" + timestamp +
                '}';
    }
}
