package service;

import exception.*;
import model.Candidate;
import repository.interfaces.CandidateRepository;
import service.interfaces.CandidateService;
import utils.SortingUtils;
import java.util.List;

public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Candidate createCandidate(Candidate candidate) throws InvalidInputException, DatabaseOperationException {
        if (!candidate.validate()) {
            throw new InvalidInputException(candidate.getValidationMessage());
        }
        return candidateRepository.create(candidate);
    }

    @Override
    public Candidate getCandidateById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        return candidateRepository.findById(id);
    }

    @Override
    public List<Candidate> getAllCandidates() throws DatabaseOperationException {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate updateCandidate(Candidate candidate) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        if (!candidate.validate()) {
            throw new InvalidInputException(candidate.getValidationMessage());
        }
        return candidateRepository.update(candidate);
    }

    @Override
    public void deleteCandidate(int id) throws ResourceNotFoundException, DatabaseOperationException {
        candidateRepository.delete(id);
    }

    @Override
    public List<Candidate> getCandidatesByElection(int electionId) throws DatabaseOperationException {
        return candidateRepository.findByElectionId(electionId);
    }

    @Override
    public List<Candidate> getCandidatesBymajor(String major) throws DatabaseOperationException {
        return candidateRepository.findBymajor(major);
    }

    @Override
    public List<Candidate> getCandidatesSortedByVotes() throws DatabaseOperationException {
        List<Candidate> candidates = candidateRepository.findAll();
        return SortingUtils.sortCandidatesByVotes(candidates); // Using lambda
    }
}