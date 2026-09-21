package eu.bilch.timetables.bahnclient;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"messages"})
public class Arrival {

    @XmlAttribute
    private String ppth; // Route (Pfad)

    @XmlAttribute
    private String pp; // Plattform

    @XmlAttribute
    private String ct; // Ankunftszeit (Timestamp)

    @XmlAttribute
    private String pt; // Geplante Ankunftszeit

    @XmlAttribute
    private String l; // Zugnummer/Name

    @XmlAttribute
    private String fb; // Zugtyp (z. B. "TGV 9583")

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

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
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
        return "Arrival{" +
                "ppth='" + ppth + '\'' +
                ", pp='" + pp + '\'' +
                ", ct='" + ct + '\'' +
                ", pt='" + pt + '\'' +
                ", l='" + l + '\'' +
                ", fb='" + fb + '\'' +
                ", cpth='" + cpth + '\'' +
                ", ppthAlternative='" + ppthAlternative + '\'' +
                ", messages=" + messages +
                '}';
    }
}