package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"messages"})
public class Departure {

    @XmlAttribute
    private String ppth; // Route (Pfad)

    @XmlAttribute
    private String pp; // Plattform

    @XmlAttribute
    private String cp; // Aktuelle Plattform

    @XmlAttribute
    private String ct; // Abfahrtszeit (Timestamp)

    @XmlAttribute
    private String pt; // Geplante Abfahrtszeit

    @XmlAttribute
    private String l; // Zugnummer/Name

    @XmlAttribute(name = "cpth")
    private String cpth; // Alternative Route

    @XmlAttribute(name = "ppth")
    private String ppthAlternative; // Alternative Plattform

    @XmlElement(name = "m")
    private List<Message> messages;

    // Getter und Setter
    public String getPpth() {
        return ppth;
    }

    public void setPpth(String ppth) {
        this.ppth = ppth;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getCpth() {
        return cpth;
    }

    public void setCpth(String cpth) {
        this.cpth = cpth;
    }

    public String getPpthAlternative() {
        return ppthAlternative;
    }

    public void setPpthAlternative(String ppthAlternative) {
        this.ppthAlternative = ppthAlternative;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Departure{" +
                "ppth='" + ppth + '\'' +
                ", pp='" + pp + '\'' +
                ", cp='" + cp + '\'' +
                ", ct='" + ct + '\'' +
                ", pt='" + pt + '\'' +
                ", l='" + l + '\'' +
                ", cpth='" + cpth + '\'' +
                ", ppthAlternative='" + ppthAlternative + '\'' +
                ", messages=" + messages +
                '}';
    }
}