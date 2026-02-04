package model;

import model.interfaces.Validatable;

public class Candidate extends BaseEntity implements Validatable<Candidate> {
    private String major;
    private int yearOfStudy;
    private String campaign;
    private Election election; // Composition - candidate belongs to an election
    private int voteCount;

    public Candidate() {}

    public Candidate(int id, String name, String major, int yearOfStudy,
                     String campaign, Election election) {
        super(id, name);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.campaign = campaign;
        this.election = election;
        this.voteCount = 0;
    }

    @Override
    public String getDescription() {
        return String.format("Candidate from %s, Year %d\nCampaign: %s\nElection: %s\nVotes: %d",
                major, yearOfStudy, campaign,
                election != null ? election.getName() : "No election",
                voteCount);
    }

    @Override
    public boolean isEligible() {
        return yearOfStudy >= 2 && yearOfStudy <= 4;
    }

    @Override
    public boolean validate() {
        return Validatable.isValidString(getName()) &&
                Validatable.isValidString(major) &&
                Validatable.isValidYear(yearOfStudy, 2, 4) &&
                election != null;
    }

    @Override
    public String getValidationMessage() {
        if (!Validatable.isValidString(getName())) {
            return "Invalid name";
        }
        if (!Validatable.isValidString(major)) {
            return "Invalid major";
        }
        if (!isEligible()) {
            return "Candidates must be in year 2-4";
        }
        if (election == null) {
            return "Candidate must be associated with an election";
        }
        return "Validation passed";
    }

    public void incrementVoteCount() {
        this.voteCount++;
    }

    // Getters and Setters

    public String getmajor() {
        return major;
    }

    public void setmajor(String major) {
        this.major = major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}