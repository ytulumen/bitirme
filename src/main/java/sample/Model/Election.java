package sample.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "election")
public class Election {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "electionid")
    private int electionID;

    @Column(name = "topic")
    private String topic;

    @Column(name = "title")
    private String title;

    @Column(name = "isVotable")
    private Boolean isVotable;

    public Election() {
    }

    public Election(int electionID, String topic, String title, boolean isVotable) {
        this.electionID = electionID;
        this.topic = topic;
        this.title = title;
        this.isVotable = isVotable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getElectionID() {
        return electionID;
    }

    public void setElectionID(int electionID) {
        this.electionID = electionID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isVotable() {
        return isVotable;
    }

    public void setVotable(boolean votable) {
        isVotable = votable;
    }

    public boolean getIsVotable() {
        return isVotable;
    }

}
