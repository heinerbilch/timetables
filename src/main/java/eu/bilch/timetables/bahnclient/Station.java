package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlType(propOrder = {})
@XmlRootElement(name = "station")
public class Station {
    private String p;
    private String meta;
    private String name;
    private String eva;
    private String ds100;
    private boolean db;
    private LocalDateTime creationTs;

    public Station() {}

    @XmlAttribute(name = "p")
    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    @XmlAttribute(name = "meta")
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "eva")
    public String getEva() {
        return eva;
    }

    public void setEva(String eva) {
        this.eva = eva;
    }

    @XmlAttribute(name = "ds100")
    public String getDs100() {
        return ds100;
    }

    public void setDs100(String ds100) {
        this.ds100 = ds100;
    }

    @XmlAttribute(name = "db")
    public boolean isDb() {
        return db;
    }

    public void setDb(boolean db) {
        this.db = db;
    }

    @XmlAttribute(name = "creationts")
    public String getCreationTsAsString() {
        return creationTs != null ?
            creationTs.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss.SSS")) : null;
    }

    public void setCreationTsAsString(String creationTs) {
        if (creationTs != null) {
            this.creationTs = LocalDateTime.parse(creationTs, DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss.SSS"));
        }
    }

    public LocalDateTime getCreationTs() {
        return creationTs;
    }

    public void setCreationTs(LocalDateTime creationTs) {
        this.creationTs = creationTs;
    }

    @Override
    public String toString() {
        return "Station [p=" + p + ", meta=" + meta + ", name=" + name + ", eva=" + eva + ", ds100=" + ds100 + ", db="
                + db + ", creationTs=" + creationTs + ", getP()=" + getP() + ", getMeta()=" + getMeta() + ", getName()="
                + getName() + ", getEva()=" + getEva() + ", getDs100()=" + getDs100() + ", isDb()=" + isDb()
                + ", getCreationTsAsString()=" + getCreationTsAsString() + ", getCreationTs()=" + getCreationTs()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }
}