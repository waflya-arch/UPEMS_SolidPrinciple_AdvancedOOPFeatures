package service.interfaces;

import exception.*;
import model.Candidate;
import java.util.List;

public interface CandidateService {
    Candidate createCandidate(Candidate candidate) throws InvalidInputException, DatabaseOperationException;
    Candidate getCandidateById(int id) throws ResourceNotFoundException, DatabaseOperationException;
    List<Candidate> getAllCandidates() throws DatabaseOperationException;
    Candidate updateCandidate(Candidate candidate) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException;
    void deleteCandidate(int id) throws ResourceNotFoundException, DatabaseOperationException;
    List<Candidate> getCandidatesByElection(int electionId) throws DatabaseOperationException;
    List<Candidate> getCandidatesBymajor(String major) throws DatabaseOperationException;
    List<Candidate> getCandidatesSortedByVotes() throws DatabaseOperationException;
}