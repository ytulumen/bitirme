package sample.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name = "candidates")
public class Candidate extends Human {

    @Column(name = "description")
    private String description;

    @Column(name = "imagePath")
    private String imagePath;

    @Column(name = "electionid")
    private int electionid;

    @Column(name = "votecounter")
    private int votecounter;

    public Candidate() {
    }

    public Candidate(String name, String surname, long identityNumber, String password, String street,
                     String number, String town, String city, String description, String imagePath,
                     int electionid, int votecounter) {
        super(name, surname, identityNumber, password, street, number, town, city);
        this.description = description;
        this.imagePath = imagePath;
        this.electionid = electionid;
        this.votecounter = votecounter;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getElectionid() {
        return electionid;
    }

    public void setElectionid(int electionid) {
        this.electionid = electionid;
    }

    public int getVotecounter() {
        return votecounter;
    }

    public void setVotecounter(int votecounter) {
        this.votecounter = votecounter;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
