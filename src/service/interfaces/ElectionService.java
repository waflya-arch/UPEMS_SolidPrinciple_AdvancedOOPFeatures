package service.interfaces;

import exception.DatabaseOperationException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Election;

import java.util.List;

public interface ElectionService {
    Election createElection(Election election) throws InvalidInputException, DatabaseOperationException;
    Election getElectionById(int id) throws ResourceNotFoundException, DatabaseOperationException;
    List<Election> getAllElections() throws DatabaseOperationException;
    Election updateElection(Election election) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException;
    void deleteElection(int id) throws ResourceNotFoundException, DatabaseOperationException;
    List<Election> getActiveElections() throws DatabaseOperationException;
    List<Election> getElectionsByAcademicYear(String academicYear) throws DatabaseOperationException;
}