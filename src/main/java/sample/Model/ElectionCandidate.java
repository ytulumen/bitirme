package sample.Model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "electioncandidate")
public class ElectionCandidate {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false )
    private int id;

    @Column(name = "electionid")
    private int electionid;

    @Column(name = "candidateid")
    private int candidateid;

    @Column(name = "voteNumber")
    private int voteNumber;

    public ElectionCandidate(int electionid, int candidateid, int voteNumber) {
        this.electionid = electionid;
        this.candidateid = candidateid;
        this.voteNumber = voteNumber;
    }

    public int getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(int voteNumber) {
        this.voteNumber = voteNumber;
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

    public int getCandidateid() {
        return candidateid;
    }

    public void setCandidateid(int candidateid) {
        this.candidateid = candidateid;
    }
}
