package model.interfaces;

public interface Votable {

    void vote();
    boolean canVote();
    default String getVoteStatusDescription() {
        return canVote() ? "Eligible to vote" : "Already voted or ineligible";
    }
    static boolean meetsBasicVotingRequirements(boolean hasVoted, boolean isEligible) {
        return !hasVoted && isEligible;
    }
}