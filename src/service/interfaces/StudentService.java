package service.interfaces;

import exception.*;
import model.Student;
import java.util.List;

public interface StudentService {
    Student createStudent(Student student) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException;
    Student getStudentById(int id) throws ResourceNotFoundException, DatabaseOperationException;
    Student getStudentByStudentId(String studentId) throws ResourceNotFoundException, DatabaseOperationException;
    List<Student> getAllStudents() throws DatabaseOperationException;
    Student updateStudent(Student student) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException;
    void deleteStudent(int id) throws ResourceNotFoundException, DatabaseOperationException;
    List<Student> getStudentsBymajor(String major) throws DatabaseOperationException;
    List<Student> getVotedStudents() throws DatabaseOperationException;
    List<Student> getNonVotedStudents() throws DatabaseOperationException;
    void castVote(int studentId, int candidateId) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException;
}