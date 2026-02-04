package repository.interfaces;

import exception.DatabaseOperationException;
import model.Election;

import java.util.List;


public interface ElectionRepository extends CRUDRepository<Election, Integer> {

    List<Election> findActiveElections() throws DatabaseOperationException;

    List<Election> findByAcademicYear(String academicYear) throws DatabaseOperationException;
}