package model;

import model.interfaces.Validatable;
import model.interfaces.Votable;

public class Student extends BaseEntity implements Validatable<Student>, Votable {
    private String studentId;
    private String major;
    private int yearOfStudy;
    private boolean hasVoted;

    public Student() {}

    public Student(int id, String name, String studentId, String major, int yearOfStudy) {
        super(id, name);
        this.studentId = studentId;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.hasVoted = false;
    }

    @Override
    public String getDescription() {
        return String.format("Student ID: %s\nmajor: %s\nYear: %d\nVoting Status: %s",
                studentId, major, yearOfStudy, hasVoted ? "Has Voted" : "Not Voted");
    }

    @Override
    public boolean isEligible() {
        return yearOfStudy >= 1 && yearOfStudy <= 4;
    }

    @Override
    public boolean validate() {
        return Validatable.isValidString(getName()) &&
                Validatable.isValidString(studentId) &&
                Validatable.isValidString(major) &&
                Validatable.isValidYear(yearOfStudy, 1, 4);
    }

    @Override
    public String getValidationMessage() {
        if (!Validatable.isValidString(getName())) {
            return "Invalid name";
        }
        if (!Validatable.isValidString(studentId)) {
            return "Invalid student ID";
        }
        if (!Validatable.isValidString(major)) {
            return "Invalid major";
        }
        if (!isEligible()) {
            return "Students must be in year 1-4";
        }
        return "Validation passed";
    }

    @Override
    public void vote() {
        if (canVote()) {
            this.hasVoted = true;
            System.out.println(getName() + " has successfully voted!");
        } else {
            System.out.println(getName() + " cannot vote. " + getVoteStatusDescription());
        }
    }

    @Override
    public boolean canVote() {
        return Votable.meetsBasicVotingRequirements(hasVoted, isEligible());
    }

    // Getters and Setters

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

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

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}