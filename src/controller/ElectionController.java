package controller;

import exception.*;
import model.*;
import service.interfaces.*;
import utils.*;
import java.util.List;

public class ElectionController {
    private final ElectionService electionService;
    private final CandidateService candidateService;
    private final StudentService studentService;

    public ElectionController(ElectionService electionService,
                              CandidateService candidateService,
                              StudentService studentService) {
        this.electionService = electionService;
        this.candidateService = candidateService;
        this.studentService = studentService;
    }

    // Election operations
    public void createElection(Election election) {
        try {
            Election created = electionService.createElection(election);
            System.out.println("✓ Election created successfully: " + created.getName());
        } catch (InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Error creating election: " + e.getMessage());
        }
    }

    public void getAllElections() {
        try {
            List<Election> elections = electionService.getAllElections();
            System.out.println("\n=== ALL ELECTIONS ===");
            elections.forEach(System.out::println); // Lambda
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Error fetching elections: " + e.getMessage());
        }
    }

    // Candidate operations
    public void createCandidate(Candidate candidate) {
        try {
            Candidate created = candidateService.createCandidate(candidate);
            System.out.println("✓ Candidate created successfully: " + created.getName());
        } catch (InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Error creating candidate: " + e.getMessage());
        }
    }

    public void getAllCandidatesSorted() {
        try {
            List<Candidate> candidates = candidateService.getCandidatesSortedByVotes();
            System.out.println("\n=== CANDIDATES (Sorted by Votes) ===");
            candidates.forEach(c -> System.out.println(c.getName() + " - Votes: " + c.getVoteCount())); // Lambda
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Error fetching candidates: " + e.getMessage());
        }
    }

    // Student operations
    public void createStudent(Student student) {
        try {
            Student created = studentService.createStudent(student);
            System.out.println("✓ Student created successfully: " + created.getName());
        } catch (InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Error creating student: " + e.getMessage());
        }
    }

    public void castVote(int studentId, int candidateId) {
        try {
            studentService.castVote(studentId, candidateId);
            System.out.println("✓ Vote cast successfully");
        } catch (ResourceNotFoundException | InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Error casting vote: " + e.getMessage());
        }
    }

    // Polymorphism demonstration
    public void displayEntityInfo(BaseEntity entity) {
        entity.displayInfo(); // Polymorphic behavior
    }

    // Lambda and filtering demonstration
    public void displayEligibleVoters() {
        try {
            List<Student> students = studentService.getAllStudents();
            List<Student> eligible = SortingUtils.filterEligibleVoters(students); // Lambda
            System.out.println("\n=== ELIGIBLE VOTERS ===");
            System.out.println("Total: " + eligible.size());
            eligible.forEach(s -> System.out.println("  - " + s.getName())); // Lambda
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
    }
}