import controller.ElectionController;
import model.*;
import model.interfaces.Votable;
import model.interfaces.Validatable;
import repository.*;
import service.*;
import service.interfaces.*;
import utils.*;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║   UNIVERSITY PRESIDENT ELECTION MANAGEMENT SYSTEM - SOLID      ║");
        System.out.println("║            Assignment 4: Advanced OOP & Architecture           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        try {
            System.out.println("\n[1] INITIALIZING ARCHITECTURE (DIP - Dependency Inversion)");
            System.out.println("Creating repository implementations...");
            ElectionRepositoryImpl electionRepo = new ElectionRepositoryImpl();
            CandidateRepositoryImpl candidateRepo = new CandidateRepositoryImpl();
            StudentRepositoryImpl studentRepo = new StudentRepositoryImpl();

            System.out.println("Creating service layer...");
            ElectionService electionService = new ElectionServiceImpl(electionRepo);
            CandidateService candidateService = new CandidateServiceImpl(candidateRepo);
            StudentService studentService = new StudentServiceImpl(studentRepo, candidateRepo);

            System.out.println("Creating controller...");
            ElectionController controller = new ElectionController(
                    electionService, candidateService, studentService);

            System.out.println("Architecture initialized successfully!\n");

            // ------ REFLECTION / RTTI DEMONSTRATION --------
            System.out.println("\n[2] REFLECTION / RTTI DEMONSTRATION");
            System.out.println("Inspecting classes...");

            // Inspect BaseEntity
            ReflectionUtils.inspectClass(BaseEntity.class);

            // Inspect Candidate
            ReflectionUtils.inspectClass(Candidate.class);

            // Inspect Student
            ReflectionUtils.inspectClass(Student.class);

            // Inspect Validatable interface
            ReflectionUtils.inspectClass(Validatable.class);

            // ------- CREATE ENTITIES ---------
            System.out.println("\n[3] CREATING ENTITIES (CRUD Operations)");

            // Create Election
            System.out.println("\n--- Creating Election ---");
            Election election = new Election(
                    0,
                    "University President Election 2026",
                    LocalDate.of(2026, 1, 10),
                    LocalDate.of(2026, 1, 19 ),
                    "2026-2027"
            );
            controller.createElection(election);
            election.setId(1); // Simulate DB-generated ID

            // Create Candidates (Composition - Candidate contains Election)
            System.out.println("\n--- Creating Candidates (Demonstrates Composition) ---");

            Candidate candidate1 = new Candidate(
                    0, "Zhubanazarova Ainaz", "Computer Science", 3,
                    "Innovation and Student Welfare", election
            );
            controller.createCandidate(candidate1);
            candidate1.setId(1);

            Candidate candidate2 = new Candidate(
                    0, "Bekbolat Aruzhan", "Software Engineering", 2,
                    "New learning platforms", election
            );
            controller.createCandidate(candidate2);
            candidate2.setId(2);

            Candidate candidate3 = new Candidate(
                    0, "Daurenuly Alisher", "Cybersecurity", 3,
                    "Comfort in Campus", election
            );
            controller.createCandidate(candidate3);
            candidate3.setId(3);

            // Create Students
            System.out.println("\n--- Creating Students ---");

            Student student1 = new Student(0, "Arguan Bakikair", "S001", "Software Engineering", 1);
            controller.createStudent(student1);
            student1.setId(1);

            Student student2 = new Student(0, "Dastan Nursultanov", "S002", "CS", 3);
            controller.createStudent(student2);
            student2.setId(2);

            Student student3 = new Student(0, "Ershat Diasov", "S003", "Data Science", 2);
            controller.createStudent(student3);
            student3.setId(3);

            // ----------- POLYMORPHISM DEMONSTRATION -----------
            System.out.println("\n[4] POLYMORPHISM DEMONSTRATION");
            System.out.println("Using BaseEntity references to call polymorphic methods...\n");

            // LSP: Candidate and Student can be treated as BaseEntity
            BaseEntity entity1 = candidate1;
            BaseEntity entity2 = student1;

            System.out.println("Displaying Candidate as BaseEntity:");
            controller.displayEntityInfo(entity1); // Polymorphic call

            System.out.println("Displaying Student as BaseEntity:");
            controller.displayEntityInfo(entity2); // Polymorphic call

            // --------- INTERFACE DEFAULT & STATIC METHODS ---------
            System.out.println("\n[5] INTERFACE FEATURES (Default & Static Methods)");

            System.out.println("\n--- Using Validatable Interface ---");
            System.out.println("Candidate validation: " + candidate1.validate());
            System.out.println("Validation message: " + candidate1.getValidationMessage()); // Default method

            System.out.println("\nStatic method - isValidString: " +
                    Validatable.isValidString("Test")); // Static method
            System.out.println("Static method - isValidYear: " +
                    Validatable.isValidYear(2, 1, 4)); // Static method

            System.out.println("\n--- Using Votable Interface ---");
            System.out.println("Can student vote? " + student1.canVote());
            System.out.println("Vote status: " + student1.getVoteStatusDescription()); // Default method
            System.out.println("Static voting requirements check: " +
                    Votable.meetsBasicVotingRequirements(false, true)); // Static method

            // --- INVALID INPUT DEMONSTRATION ---
            System.out.println("\n[6] EXCEPTION HANDLING DEMONSTRATION");

            System.out.println("\n--- Attempting to create invalid candidate (Year 1) ---");
            try {
                Candidate invalidCandidate = new Candidate(
                        0, "Invalid Student", "Computer Science", 1, // Year 1 - invalid!
                        "Should fail", election
                );
                controller.createCandidate(invalidCandidate);
            } catch (Exception e) {
                System.out.println("Exception caught correctly!");
            }

            System.out.println("\n--- Attempting to create duplicate student ---");
            try {
                Student duplicate = new Student(0, "Duplicate", "S001", "CS", 2);
                controller.createStudent(duplicate);
            } catch (Exception e) {
                System.out.println("DuplicateResourceException would be caught!");
            }

            // === VOTING DEMONSTRATION ===
            System.out.println("\n[7] VOTING FUNCTIONALITY (Using Votable Interface)");

            System.out.println("\nStudent 1 voting for Candidate 1:");
            controller.castVote(1, 1);
            candidate1.setVoteCount(1);

            System.out.println("Student 2 voting for Candidate 1:");
            controller.castVote(2, 1);
            candidate1.setVoteCount(2);

            System.out.println("Student 3 voting for Candidate 2:");
            controller.castVote(3, 2);
            candidate2.setVoteCount(1);

            // -------- LAMBDA EXPRESSIONS DEMONSTRATION -------
            System.out.println("\n[8] LAMBDA EXPRESSIONS & FUNCTIONAL PROGRAMMING");

            System.out.println("\n--- Sorting candidates by votes (using lambdas) ---");
            controller.getAllCandidatesSorted();

            System.out.println("\n--- Filtering eligible voters (using lambdas) ---");
            controller.displayEligibleVoters();

            System.out.println("\n--- Using Stream API with lambdas ---");
            java.util.List<Candidate> allCandidates = java.util.Arrays.asList(candidate1, candidate2, candidate3);

            System.out.println("Top 2 candidates:");
            SortingUtils.getTopNCandidates(allCandidates, 2)
                    .forEach(c -> System.out.println("  " + c.getName() + ": " + c.getVoteCount() + " votes"));

            System.out.println("\nEligible candidates (year 2-4):");
            SortingUtils.filterEligible(allCandidates)
                    .forEach(c -> System.out.println("  " + c.getName() + " (Year " + c.getYearOfStudy() + ")"));

            // === GENERICS DEMONSTRATION ===
            System.out.println("\n[9] GENERICS DEMONSTRATION");
            System.out.println("Our repository layer uses Generic CrudRepository<T, ID>");
            System.out.println("Examples:");
            System.out.println("  - ElectionRepository extends CrudRepository<Election, Integer>");
            System.out.println("  - CandidateRepository extends CrudRepository<Candidate, Integer>");
            System.out.println("  - StudentRepository extends CrudRepository<Student, Integer>");
            System.out.println("\nThis provides type-safe, reusable CRUD operations!");

            // --- SOLID PRINCIPLES SUMMARY ---
            System.out.println("\n[10] SOLID PRINCIPLES APPLIED");
            System.out.println("════════════════════════════════════════════════");
            System.out.println("SRP: Each class has single responsibility");
            System.out.println("  - Repository: Only database operations");
            System.out.println("  - Service: Only business logic");
            System.out.println("  - Controller: Only user interaction");
            System.out.println("\nOCP: Open for extension, closed for modification");
            System.out.println("  - BaseEntity can be extended without changes");
            System.out.println("  - New subclasses can be added easily");
            System.out.println("\nLSP: Subclasses can replace base class");
            System.out.println("  - Candidate and Student work as BaseEntity");
            System.out.println("\nISP: Interfaces are small and focused");
            System.out.println("  - Validatable: Only validation methods");
            System.out.println("  - Votable: Only voting methods");
            System.out.println("\nDIP: Depend on abstractions, not concretions");
            System.out.println("  - Controller depends on Service interfaces");
            System.out.println("  - Service depends on Repository interfaces");
            System.out.println("════════════════════════════════════════════════");

            System.out.println("\nALL DEMONSTRATIONS COMPLETED SUCCESSFULLY!");
            System.out.println("\nFinal Results:");
            controller.getAllElections();
            controller.getAllCandidatesSorted();

        } catch (Exception e) {
            System.err.println("\nERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}