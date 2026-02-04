package repository;

import utils.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Student;
import repository.interfaces.StudentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    @Override
    public Student create(Student student) throws DatabaseOperationException {
        String sql = "INSERT INTO students (name, student_id, major, year_of_study, has_voted) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getStudentId());
            stmt.setString(3, student.getmajor());
            stmt.setInt(4, student.getYearOfStudy());
            stmt.setBoolean(5, student.isHasVoted());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                student.setId(rs.getInt("id"));
                return student;
            }
            throw new DatabaseOperationException("Failed to create student");
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating student: " + e.getMessage(), e);
        }
    }

    @Override
    public Student findById(Integer id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
            throw new ResourceNotFoundException("Student not found with id: " + id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding student: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAll() throws DatabaseOperationException {
        String sql = "SELECT * FROM students ORDER BY name";
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding all students: " + e.getMessage(), e);
        }
    }

    @Override
    public Student update(Student student) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "UPDATE students SET name = ?, student_id = ?, major = ?, " +
                "year_of_study = ?, has_voted = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getStudentId());
            stmt.setString(3, student.getmajor());
            stmt.setInt(4, student.getYearOfStudy());
            stmt.setBoolean(5, student.isHasVoted());
            stmt.setInt(6, student.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Student not found with id: " + student.getId());
            }
            return student;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating student: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Student not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting student: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(Integer id) {
        String sql = "SELECT COUNT(*) FROM students WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Student findByStudentId(String studentId) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
            throw new ResourceNotFoundException("Student not found with student_id: " + studentId);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding student by student_id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findBymajor(String major) throws DatabaseOperationException {
        String sql = "SELECT * FROM students WHERE major = ?";
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, major);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding students by major: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findVotedStudents() throws DatabaseOperationException {
        String sql = "SELECT * FROM students WHERE has_voted = true";
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding voted students: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findNonVotedStudents() throws DatabaseOperationException {
        String sql = "SELECT * FROM students WHERE has_voted = false";
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding non-voted students: " + e.getMessage(), e);
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("student_id"),
                rs.getString("major"),
                rs.getInt("year_of_study")
        );
        student.setHasVoted(rs.getBoolean("has_voted"));

        return student;
    }
}