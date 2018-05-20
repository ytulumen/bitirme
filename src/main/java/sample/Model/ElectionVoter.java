package sample.Model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "electionvoter")
public class ElectionVoter {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false )
    private int id;

    @Column(name = "electionid")
    private int electionid;

    @Column(name = "voterid")
    private int voterid;

    public ElectionVoter(int electionid, int voterid) {
        this.electionid = electionid;
        this.voterid = voterid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getElectionid() {
        return electionid;
    }

    public void setElectionid(int electionid) {
        this.electionid = electionid;
    }

    public int getVoterid() {
        return voterid;
    }

    public void setVoterid(int voterid) {
        this.voterid = voterid;
    }
}
