# University President Election Management System
## Assignment 4: SOLID Architecture & Advanced OOP Features

**Student Project - Refactored with SOLID Principles**

---

## Table of Contents
1. [SOLID Principles Documentation](#a-solid-principles-documentation)
2. [Advanced OOP Features](#b-advanced-oop-features)
3. [OOP Design](#c-oop-design)
4. [Database Schema](#d-database-schema)
5. [Architecture](#e-architecture)
6. [Compilation and Execution](#f-compilation-and-execution)
7. [Screenshots](#g-screenshots)
8. [Reflection](#h-reflection)

---

## A. SOLID Principles Documentation

### 1. Single Responsibility Principle (SRP)

**Each class has ONE reason to change**

#### Examples:

**BaseEntity**
- Responsibility: Manage common entity attributes (id, name)
- Does NOT handle: Database operations, validation logic, business rules

**ElectionRepositoryImpl**
- Responsibility: Database operations for Election entity ONLY
- Does NOT handle: Business logic, validation, or user interaction

**ElectionServiceImpl**
- Responsibility: Business logic and validation for elections
- Does NOT handle: Database operations or user interaction

**ElectionController**
- Responsibility: Handle user requests and coordinate responses
- Does NOT handle: Business logic or database operations

**ReflectionUtils**
- Responsibility: Runtime class inspection using Java Reflection
- Does NOT handle: Entity logic or database operations

**SortingUtils**
- Responsibility: Sorting and filtering operations using lambdas
- Does NOT handle: Entity creation or persistence

### 2. Open-Closed Principle (OCP)

**Open for extension, closed for modification**

#### Examples:

**BaseEntity Abstract Class**
```java
public abstract class BaseEntity {
    // Existing code doesn't change when adding new subclasses
    public abstract String getDescription();
    public abstract boolean isEligible();
}
```

**Adding New Entity Types:**
- Can create new subclasses (e.g., `Staff`, `Faculty`) without modifying `BaseEntity`
- Each subclass provides its own implementation of abstract methods
- Existing code using `BaseEntity` continues to work

**Service Layer:**
- New services can be added without modifying existing service implementations
- Service interfaces can be extended with new methods

### 3. Liskov Substitution Principle (LSP)

**Subclasses must be substitutable for their base class**

#### Demonstration:

```java
// Both Candidate and Student can be used as BaseEntity
BaseEntity entity1 = new Candidate(...);
BaseEntity entity2 = new Student(...);

// Polymorphic behavior - works correctly for both
entity1.displayInfo();  // Calls Candidate's implementation
entity2.displayInfo();  // Calls Student's implementation

entity1.isEligible();   // Candidate: checks year 2-4
entity2.isEligible();   // Student: checks year 1-4
```

**Why it works:**
- Both subclasses honor the contract defined by `BaseEntity`
- Both implement abstract methods correctly
- Both can be used interchangeably where `BaseEntity` is expected
- No breaking behavior when substituted

### 4. Interface Segregation Principle (ISP)

**Clients should not depend on interfaces they don't use**

#### Examples:

**Validatable<T> Interface**
```java
public interface Validatable<T> {
    boolean validate();
    default String getValidationMessage() { ... }
    static boolean isValidString(String str) { ... }
}
```
- Small, focused interface
- Only validation-related methods
- Used by both Candidate and Student

**Votable Interface**
```java
public interface Votable {
    void vote();
    boolean canVote();
    default String getVoteStatusDescription() { ... }
}
```
- Separate from Validatable
- Only Student implements it (Candidate doesn't need voting capability)
- Clients that don't need voting don't depend on it

**Why this is better than one large interface:**
- `Candidate` doesn't implement empty `vote()` methods
- Each class implements only what it needs
- More maintainable and less coupled

### 5. Dependency Inversion Principle (DIP)

**Depend on abstractions, not concretions**

#### Implementation:

**Repository Layer:**
```java
// High-level module depends on abstraction
public class ElectionServiceImpl implements ElectionService {
    private final ElectionRepository electionRepository; // Interface, not implementation!
    
    public ElectionServiceImpl(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository; // Constructor injection
    }
}
```

**Service Layer:**
```java
// Controller depends on service interface
public class ElectionController {
    private final ElectionService electionService; // Interface!
    
    public ElectionController(ElectionService electionService) {
        this.electionService = electionService; // Constructor injection
    }
}
```

**Main Application:**
```java
// Wiring dependencies (Dependency Injection)
ElectionRepository electionRepo = new ElectionRepositoryImpl();
ElectionService electionService = new ElectionServiceImpl(electionRepo);
ElectionController controller = new ElectionController(electionService);
```

**Benefits:**
- Easy to replace implementations (e.g., switch to MongoDB repository)
- Easy to test (can inject mock implementations)
- Reduces coupling between layers

---

## B. Advanced OOP Features

### 1. Generics

#### Generic Repository Interface:
```java
public interface CrudRepository<T, ID> {
    T create(T entity) throws DatabaseOperationException;
    T findById(ID id) throws ResourceNotFoundException;
    List<T> findAll() throws DatabaseOperationException;
    T update(T entity) throws ResourceNotFoundException;
    void delete(ID id) throws ResourceNotFoundException;
}
```

**Usage:**
- `ElectionRepository extends CrudRepository<Election, Integer>`
- `CandidateRepository extends CrudRepository<Candidate, Integer>`
- `StudentRepository extends CrudRepository<Student, Integer>`

**Benefits:**
- Type safety at compile time
- Code reusability
- Eliminates need for casting
- Clear contract for all repositories

#### Generic Validatable Interface:
```java
public interface Validatable<T> {
    boolean validate();
}
```

### 2. Lambda Expressions

#### Location: `SortingUtils.java`

**Example 1: Sorting with Lambda**
```java
public static <T extends BaseEntity> List<T> sortByName(List<T> entities) {
    entities.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));
    return entities;
}
```

**Example 2: Method Reference**
```java
public static <T extends BaseEntity> List<T> sortByNameDescending(List<T> entities) {
    entities.sort(Comparator.comparing(BaseEntity::getName).reversed());
    return entities;
}
```

**Example 3: Filtering with Lambda**
```java
public static <T extends BaseEntity> List<T> filterEligible(List<T> entities) {
    return entities.stream()
            .filter(entity -> entity.isEligible())
            .collect(Collectors.toList());
}
```

**Example 4: Stream API with Lambda**
```java
public static List<Candidate> sortCandidatesByVotes(List<Candidate> candidates) {
    candidates.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));
    return candidates;
}
```

**Example 5: forEach with Lambda (in Controller)**
```java
elections.forEach(System.out::println); // Method reference
candidates.forEach(c -> System.out.println(c.getName() + " - Votes: " + c.getVoteCount()));
```

### 3. Reflection / RTTI

#### Location: `ReflectionUtils.java`

**Capabilities:**
- Extract class name and package
- List all fields with their types and modifiers
- List all methods with return types and parameters
- List constructors
- Inspect interfaces implemented
- Inspect inheritance hierarchy
- Inspect object instances and field values

**Usage in Main:**
```java
// Inspect classes
ReflectionUtils.inspectClass(BaseEntity.class);
ReflectionUtils.inspectClass(Candidate.class);
ReflectionUtils.inspectClass(Student.class);
ReflectionUtils.inspectClass(Validatable.class);

// Inspect object instance
ReflectionUtils.inspectObject(candidate1);
```

**Output Example:**
```
======================================================================
REFLECTION INSPECTION FOR: model.Candidate
======================================================================

[CLASS INFORMATION]
Simple Name: Candidate
Package: model
Modifiers: public
Is Abstract: false
Is Interface: false

[INHERITANCE]
Superclass: model.BaseEntity

[INTERFACES IMPLEMENTED]
  - model.Validatable

[FIELDS]
  private String faculty
  private int yearOfStudy
  private String campaign
  private Election election
  private int voteCount
...
```

### 4. Interface Default and Static Methods

#### Validatable Interface:

**Default Method:**
```java
default String getValidationMessage() {
    return validate() ? "Validation passed" : "Validation failed";
}
```
- Provides default implementation
- Can be overridden by implementing classes
- Used in Candidate and Student

**Static Method:**
```java
static boolean isValidString(String str) {
    return str != null && !str.trim().isEmpty();
}

static boolean isValidYear(int year, int minYear, int maxYear) {
    return year >= minYear && year <= maxYear;
}
```
- Utility methods related to validation
- Called directly on interface: `Validatable.isValidString("test")`

#### Votable Interface:

**Default Method:**
```java
default String getVoteStatusDescription() {
    return canVote() ? "Eligible to vote" : "Already voted or ineligible";
}
```

**Static Method:**
```java
static boolean meetsBasicVotingRequirements(boolean hasVoted, boolean isEligible) {
    return !hasVoted && isEligible;
}
```

---

## C. OOP Design

### 1. Abstract Class & Inheritance

#### BaseEntity (Abstract Class)

**Purpose:** Common base for all entities

**Abstract Methods:**
- `String getDescription()` - Polymorphic behavior
- `boolean isEligible()` - Different rules per subclass

**Concrete Method:**
- `void displayInfo()` - Shared implementation

**Fields:**
- `id`: Unique identifier
- `name`: Entity name

#### Subclass 1: Candidate

**Extends:** BaseEntity

**Implements:** Validatable<Candidate>

**Additional Fields:**
- `major`: Academic faculty
- `yearOfStudy`: Year of study (2-4)
- `campaign`: Campaign message
- `election`: Election object (Composition)
- `voteCount`: Number of votes received

**Eligibility Rule:** Year of study must be 2, 3, or 4

#### Subclass 2: Student

**Extends:** BaseEntity

**Implements:** Validatable<Student>, Votable

**Additional Fields:**
- `studentId`: Unique student identifier
- `major`: Academic faculty
- `yearOfStudy`: Year of study (1-4)
- `hasVoted`: Voting status

**Eligibility Rule:** Year of study must be 1, 2, 3, or 4

### 2. Composition Relationship

**Candidate → Election**

```java
public class Candidate extends BaseEntity {
    private Election election; // Composition
    
    public Candidate(..., Election election) {
        this.election = election; // Cannot exist without election
    }
}
```

**Why Composition:**
- A candidate cannot exist without an election
- Strong "part-of" relationship
- Election is mandatory for candidate creation

### 3. Polymorphism Examples

#### Example 1: Method Overriding
```java
// In BaseEntity
public abstract String getDescription();

// In Candidate
@Override
public String getDescription() {
    return "Candidate from " + faculty + ", Year " + yearOfStudy;
}

// In Student
@Override
public String getDescription() {
    return "Student ID: " + studentId + ", Faculty: " + faculty;
}
```

#### Example 2: Using Polymorphism
```java
BaseEntity entity = new Candidate(...);
entity.displayInfo(); // Calls Candidate's getDescription()

entity = new Student(...);
entity.displayInfo(); // Calls Student's getDescription()
```

### 4. UML Class Diagram

```
                    ┌──────────────────┐
                    │   <<abstract>>   │
                    │   BaseEntity     │
                    ├──────────────────┤
                    │ -id: int         │
                    │ -name: String    │
                    ├──────────────────┤
                    │ +getDescription()*│
                    │ +isEligible()*   │
                    │ +displayInfo()   │
                    └────────┬─────────┘
                             │
                    ┌────────┴─────────┐
                    │                  │
          ┌─────────▼──────┐  ┌────────▼─────────┐
          │   Candidate    │  │     Student      │
          ├────────────────┤  ├──────────────────┤
          │-major          │  │-studentId        │
          │-yearOfStudy    │  │-major            │
          │-campaign       │  │-yearOfStudy      │
          │-election       │  │-hasVoted         │
          │-voteCount      │  ├──────────────────┤
          ├────────────────┤  │+vote()           │
          │+validate()     │  │+canVote()        │
          │+isEligible()   │  │+validate()       │
          └────────┬───────┘  │+isEligible()     │
                   │          └────────┬─────────┘
                   │                   │
                   │                   │
       ┌───────────▼──────┐    ┌───────▼──────────┐
       │  <<interface>>   │    │  <<interface>>   │
       │  Validatable<T>  │    │    Votable       │
       ├──────────────────┤    ├──────────────────┤
       │+validate()       │    │+vote()           │
       │+getValidation... │    │+canVote()        │
       └──────────────────┘    │+getVoteStatus... │
                               └──────────────────┘

       ┌─────────────┐
       │  Election   │───────────────┐
       ├─────────────┤            Composition
       │-id          │               │
       │-name        │               │
       │-startDate   │◄──────────────┘
       │-endDate     │
       └─────────────┘
```

---

## D. Database Schema

### Tables

#### 1. elections
```sql
CREATE TABLE elections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    CONSTRAINT check_dates CHECK (end_date >= start_date)
);
```

#### 2. candidates
```sql
CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    major VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL,
    campaign TEXT,
    election_id INTEGER NOT NULL REFERENCES elections(id) ON DELETE CASCADE,
    vote_count INTEGER DEFAULT 0,
    CONSTRAINT check_year CHECK (year_of_study BETWEEN 2 AND 4)
);
```

#### 3. students
```sql
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    student_id VARCHAR(50) UNIQUE NOT NULL,
    major VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL,
    has_voted BOOLEAN DEFAULT FALSE,
    CONSTRAINT check_year CHECK (year_of_study BETWEEN 1 AND 4)
);
```

### Constraints

**Primary Keys:**
- All tables use `SERIAL` for auto-incrementing IDs

**Foreign Keys:**
- `candidates.election_id` → `elections.id` (CASCADE on delete)

**Unique Constraints:**
- `students.student_id` must be unique

**Check Constraints:**
- Candidates: `year_of_study BETWEEN 2 AND 4`
- Students: `year_of_study BETWEEN 1 AND 4`
- Elections: `end_date >= start_date`

### Sample Data

```sql
INSERT INTO elections (name, start_date, end_date, academic_year) VALUES
    ('University President Election 2026', '2026-01-10', '2026-01-19', '2026-2027'),

INSERT INTO candidates (name, major, year_of_study, campaign, election_id) VALUES
   ('Zhubanazarova Ainaz', 'Computer Science', 3, 'Innovation and Student Welfare', 1),

INSERT INTO students (name, student_id, major, year_of_study, has_voted) VALUES
   ('Arguan Bakikair', 'S001', 'Software Engineering', 1, TRUE)
```

---

## E. Architecture

### Multi-Layer Architecture

```
┌─────────────────────────────────────────┐
│         Controller Layer                │
│  (User Interaction, No Business Logic)  │
│     ElectionController.java             │
└────────────────┬────────────────────────┘
                 │ depends on
                 ▼
┌─────────────────────────────────────────┐
│          Service Layer                  │
│    (Business Logic & Validation)        │
│  ElectionService, CandidateService...   │
└────────────────┬────────────────────────┘
                 │ depends on
                 ▼
┌─────────────────────────────────────────┐
│        Repository Layer                 │
│    (Database Operations Only)           │
│  ElectionRepository, CandidateRepo...   │
└────────────────┬────────────────────────┘
                 │ uses
                 ▼
┌─────────────────────────────────────────┐
│           Database                      │
│        PostgreSQL                       │
└─────────────────────────────────────────┘
```

### Layer Responsibilities

**Controller Layer:**
- Handle user requests
- Call appropriate service methods
- Format responses
- NO business logic
- NO database access

**Service Layer:**
- Implement business rules
- Validate inputs
- Coordinate between repositories
- Apply SOLID principles (especially SRP, DIP)
- Throw appropriate exceptions

**Repository Layer:**
- Execute database operations
- Map between database and objects
- NO business logic
- Implement generic CRUD interface

**Utilities:**
- `ReflectionUtils`: Runtime class inspection
- `SortingUtils`: Lambda-based sorting/filtering

### Request/Response Flow Example

**Creating a Candidate:**

1. **User** → Controller: "Create candidate Ainaz"
2. **Controller** → Service: `candidateService.createCandidate(candidate)`
3. **Service**: Validates candidate data (`validate()` method)
4. **Service** → Repository: `candidateRepository.create(candidate)`
5. **Repository**: Executes SQL INSERT
6. **Repository** → Service: Returns created candidate with ID
7. **Service** → Controller: Returns candidate
8. **Controller** → User: Displays success message

---

## F. Compilation and Execution

### Prerequisites

1. **Java Development Kit (JDK) 11 or higher**
2. **PostgreSQL Database**
3. **PostgreSQL JDBC Driver** (`postgresql-42.7.9.jar`)

### Database Setup

```bash
# 1. Create database
psql -U postgres
CREATE DATABASE university_election;
\q

# 2. Run schema
psql -U postgres -d university_election -f resources/schema.sql

# 3. Update DatabaseConnection.java with your credentials:
# - URL: jdbc:postgresql://localhost:5432/university_election
# - USER: your_username
# - PASSWORD: your_password
```

### Compilation

**Option 1: Compile all at once**
```bash
cd assignment4-election-system

# Download PostgreSQL driver if not present
# wget https://jdbc.postgresql.org/download/postgresql-42.7.9.jar -O lib/postgresql-42.7.9.jar

javac -d bin -cp "lib/postgresql-42.7.9.jar" \
  $(find src -name "*.java")
```

**Option 2: Compile by layers**
```bash
# Model layer
javac -d bin src/model/*.java

# Exception layer
javac -d bin src/exception/*.java

# Utils layer
javac -d bin src/utils/*.java

# Repository interfaces
javac -d bin -cp bin src/repository/interfaces/*.java

# Repository implementations
javac -d bin -cp "bin:lib/postgresql-42.7.9.jar" \
  src/DatabaseConnection.java \
  src/repository/*.java

# Service interfaces
javac -d bin -cp bin src/service/interfaces/*.java

# Service implementations
javac -d bin -cp "bin:lib/postgresql-42.7.9.jar" \
  src/service/*.java

# Controller
javac -d bin -cp "bin:lib/postgresql-42.7.9.jar" \
  src/controller/*.java

# Main
javac -d bin -cp "bin:lib/postgresql-42.7.9.jar" \
  src/Main.java
```

### Execution

```bash
# Unix/Linux/Mac
java -cp "bin:lib/postgresql-42.7.9.jar" Main

# Windows
java -cp "bin;lib/postgresql-42.7.9.jar" Main
```

---

## G. Screenshots

### 1. Successful CRUD Operations
- Creating elections, candidates, and students
- Reading all entities
- Updating entities
- Deleting entities

### 2. SOLID Architecture in Action
- Dependency injection demonstration
- Polymorphism with BaseEntity
- Service layer validation

### 3. Advanced OOP Features

**Reflection Output:**
- Class inspection showing fields, methods, constructors
- Interface implementation details
- Inheritance hierarchy

**Lambda Expressions:**
- Sorted candidate list (by votes)
- Filtered student lists (eligible voters)
- Stream API operations

**Interface Features:**
- Default method usage
- Static method calls

### 4. Exception Handling
- InvalidInputException (year 1 candidate)
- DuplicateResourceException (duplicate student ID)
- ResourceNotFoundException (non-existent ID)
- Validation failures with custom messages

### 5. Database Structure
- PostgreSQL tables with relationships
- Foreign key constraints
- Check constraints enforced
- Sample data inserted

---

## H. Reflection

### What I Learned

#### 1. SOLID Principles in Practice
- **SRP** made classes easier to understand and maintain
- **OCP** allowed adding new entity types without breaking existing code
- **LSP** ensured subclasses work correctly when substituted
- **ISP** prevented interface pollution and unnecessary dependencies
- **DIP** made testing and swapping implementations trivial

#### 2. Advanced Java Features

**Generics:**
- Type safety is incredibly valuable
- Generic repositories eliminate code duplication
- Compile-time type checking catches errors early

**Lambdas:**
- Make code more concise and readable
- Stream API is powerful for collections
- Functional programming paradigm in Java

**Reflection:**
- Powerful for runtime inspection
- Useful for frameworks and tools
- Must be used carefully (performance impact)

**Interface Evolution:**
- Default methods allow interface extension without breaking implementations
- Static methods provide utility functions in interfaces
- Java 8+ features greatly improved interface capabilities

#### 3. Multi-Layer Architecture
- Clear separation of concerns
- Each layer has specific responsibility
- Easy to test each layer independently
- Dependency injection makes code flexible

### Challenges Faced

#### 1. Understanding DIP
Initially, I directly instantiated repository classes in services. Learning to inject interfaces took time but the benefits (testability, flexibility) are clear.

#### 2. Generic Repository Design
Creating a generic CRUD interface that works for all entities required careful thought about type parameters and exception handling.

#### 3. Balancing Abstraction
Finding the right level of abstraction - not too abstract (complex) but abstract enough (flexible) was challenging.

#### 4. Composition vs Inheritance
Deciding when to use composition (Candidate-Election) vs inheritance (Candidate-BaseEntity) required understanding the "has-a" vs "is-a" relationships.

### Value of SOLID Architecture

#### Benefits Experienced:

1. **Maintainability**
    - Easy to locate and fix bugs
    - Changes in one layer don't break others
    - Clear responsibility boundaries

2. **Testability**
    - Can mock service/repository interfaces
    - Each layer testable independently
    - Dependency injection enables unit testing

3. **Flexibility**
    - Easy to swap database (just implement repository interface)
    - Can add new entities by extending BaseEntity
    - New features added without modifying existing code

4. **Collaboration**
    - Clear interfaces make team collaboration easier
    - Each developer can work on separate layers
    - Well-defined contracts between layers

5. **Understanding**
    - Code structure is logical and intuitive
    - New developers can understand system quickly
    - Documentation maps directly to code structure
