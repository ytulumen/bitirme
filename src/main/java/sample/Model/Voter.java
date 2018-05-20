package sample.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name = "voters")
public class Voter extends Human {

    @Column(name = "fprint")
    private int fingerPrintID;

    public Voter() {
    }

    public Voter(String name, String surname, long identityNumber, String password, String street, String number, String town, String city) {
        super(name, surname, identityNumber, password, street, number, town, city);
        setFingerPrintID(1);
    }


    public int getFingerPrintID() {
        return fingerPrintID;
    }

    public void setFingerPrintID(int fingerPrintID) {
        this.fingerPrintID = fingerPrintID;
    }
}
