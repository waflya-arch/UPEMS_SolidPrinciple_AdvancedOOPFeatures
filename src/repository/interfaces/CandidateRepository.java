package repository.interfaces;

import exception.DatabaseOperationException;
import model.Candidate;

import java.util.List;

public interface CandidateRepository extends CRUDRepository<Candidate, Integer> {

    List<Candidate> findByElectionId(int electionId) throws DatabaseOperationException;

    List<Candidate> findBymajor(String major) throws DatabaseOperationException;
}