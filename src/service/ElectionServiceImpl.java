package service;

import exception.*;
import model.Election;
import model.interfaces.Validatable;
import repository.interfaces.ElectionRepository;
import service.interfaces.ElectionService;

import java.time.LocalDate;
import java.util.List;

public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository electionRepository;

    public ElectionServiceImpl(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    @Override
    public Election createElection(Election election) throws InvalidInputException, DatabaseOperationException {
        // Validation - business rule
        validateElection(election);

        // Delegate to repository
        return electionRepository.create(election);
    }

    @Override
    public Election getElectionById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (id <= 0) {
            throw new ResourceNotFoundException("Invalid election ID");
        }
        return electionRepository.findById(id);
    }

    @Override
    public List<Election> getAllElections() throws DatabaseOperationException {
        return electionRepository.findAll();
    }

    @Override
    public Election updateElection(Election election) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        validateElection(election);

        // Check if exists
        if (!electionRepository.exists(election.getId())) {
            throw new ResourceNotFoundException("Election not found with id: " + election.getId());
        }

        return electionRepository.update(election);
    }

    @Override
    public void deleteElection(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (!electionRepository.exists(id)) {
            throw new ResourceNotFoundException("Election not found with id: " + id);
        }
        electionRepository.delete(id);
    }

    @Override
    public List<Election> getActiveElections() throws DatabaseOperationException {
        return electionRepository.findActiveElections();
    }

    @Override
    public List<Election> getElectionsByAcademicYear(String academicYear) throws DatabaseOperationException {
        if (!Validatable.isValidString(academicYear)) {
            throw new DatabaseOperationException("Invalid academic year");
        }
        return electionRepository.findByAcademicYear(academicYear);
    }

    /**
     * Private validation method - SRP
     * Validates election business rules
     */
    private void validateElection(Election election) throws InvalidInputException {
        if (election == null) {
            throw new InvalidInputException("Election cannot be null");
        }

        if (!Validatable.isValidString(election.getName())) {
            throw new InvalidInputException("Election name is required");
        }

        if (election.getStartDate() == null || election.getEndDate() == null) {
            throw new InvalidInputException("Start date and end date are required");
        }

        if (election.getEndDate().isBefore(election.getStartDate())) {
            throw new InvalidInputException("End date must be after start date");
        }

        if (!Validatable.isValidString(election.getAcademicYear())) {
            throw new InvalidInputException("Academic year is required");
        }
    }
}