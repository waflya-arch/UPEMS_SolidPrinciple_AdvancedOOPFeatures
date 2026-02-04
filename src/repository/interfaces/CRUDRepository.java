package repository.interfaces;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;

import java.util.List;

public interface CRUDRepository<T, ID> {

    T create(T entity) throws DatabaseOperationException;

    T findById(ID id) throws ResourceNotFoundException, DatabaseOperationException;

    List<T> findAll() throws DatabaseOperationException;

    T update(T entity) throws ResourceNotFoundException, DatabaseOperationException;

    void delete(ID id) throws ResourceNotFoundException, DatabaseOperationException;

    boolean exists(ID id);
}