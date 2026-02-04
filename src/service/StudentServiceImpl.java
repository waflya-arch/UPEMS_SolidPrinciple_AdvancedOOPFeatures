package service;

import exception.*;
import model.Candidate;
import model.Student;
import repository.interfaces.CandidateRepository;
import repository.interfaces.StudentRepository;
import service.interfaces.StudentService;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CandidateRepository candidateRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CandidateRepository candidateRepository) {
        this.studentRepository = studentRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Student createStudent(Student student) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException {
        if (!student.validate()) {
            throw new InvalidInputException(student.getValidationMessage());
        }

        try {
            studentRepository.findByStudentId(student.getStudentId());
            throw new DuplicateResourceException("Student with ID " + student.getStudentId() + " already exists");
        } catch (ResourceNotFoundException e) {
            // Good - student doesn't exist
        }

        return studentRepository.create(student);
    }

    @Override
    public Student getStudentById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        return studentRepository.findById(id);
    }

    @Override
    public Student getStudentByStudentId(String studentId) throws ResourceNotFoundException, DatabaseOperationException {
        return studentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Student> getAllStudents() throws DatabaseOperationException {
        return studentRepository.findAll();
    }

    @Override
    public Student updateStudent(Student student) throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        if (!student.validate()) {
            throw new InvalidInputException(student.getValidationMessage());
        }
        return studentRepository.update(student);
    }

    @Override
    public void deleteStudent(int id) throws ResourceNotFoundException, DatabaseOperationException {
        studentRepository.delete(id);
    }

    @Override
    public List<Student> getStudentsBymajor(String major) throws DatabaseOperationException {
        return studentRepository.findBymajor(major);
    }

    @Override
    public List<Student> getVotedStudents() throws DatabaseOperationException {
        return studentRepository.findVotedStudents();
    }

    @Override
    public List<Student> getNonVotedStudents() throws DatabaseOperationException {
        return studentRepository.findNonVotedStudents();
    }

    @Override
    public void castVote(int studentId, int candidateId) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException {
        Student student = studentRepository.findById(studentId);

        if (!student.canVote()) {
            throw new InvalidInputException("Student cannot vote: " + student.getVoteStatusDescription());
        }

        Candidate candidate = candidateRepository.findById(candidateId);

        // Mark student as voted
        student.vote();
        studentRepository.update(student);

        // Increment candidate vote count
        candidate.incrementVoteCount();
        candidateRepository.update(candidate);
    }
}