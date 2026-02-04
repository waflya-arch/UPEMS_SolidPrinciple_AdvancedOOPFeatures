package repository.interfaces;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Student;

import java.util.List;

public interface StudentRepository extends CRUDRepository<Student, Integer> {

    Student findByStudentId(String studentId) throws ResourceNotFoundException, DatabaseOperationException;

    List<Student> findBymajor(String major) throws DatabaseOperationException;

    List<Student> findVotedStudents() throws DatabaseOperationException;

    List<Student> findNonVotedStudents() throws DatabaseOperationException;
}