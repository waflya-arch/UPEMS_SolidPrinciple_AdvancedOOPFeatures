package repository;

import utils.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Election;
import repository.interfaces.ElectionRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ElectionRepositoryImpl implements ElectionRepository {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    @Override
    public Election create(Election election) throws DatabaseOperationException {
        String sql = "INSERT INTO elections (name, start_date, end_date, academic_year) VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, election.getName());
            stmt.setDate(2, Date.valueOf(election.getStartDate()));
            stmt.setDate(3, Date.valueOf(election.getEndDate()));
            stmt.setString(4, election.getAcademicYear());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                election.setId(rs.getInt("id"));
                return election;
            }
            throw new DatabaseOperationException("Failed to create election");
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating election: " + e.getMessage(), e);
        }
    }

    @Override
    public Election findById(Integer id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "SELECT * FROM elections WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToElection(rs);
            }
            throw new ResourceNotFoundException("Election not found with id: " + id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding election: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Election> findAll() throws DatabaseOperationException {
        String sql = "SELECT * FROM elections ORDER BY start_date DESC";
        List<Election> elections = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                elections.add(mapResultSetToElection(rs));
            }
            return elections;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding all elections: " + e.getMessage(), e);
        }
    }

    @Override
    public Election update(Election election) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "UPDATE elections SET name = ?, start_date = ?, end_date = ?, academic_year = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, election.getName());
            stmt.setDate(2, Date.valueOf(election.getStartDate()));
            stmt.setDate(3, Date.valueOf(election.getEndDate()));
            stmt.setString(4, election.getAcademicYear());
            stmt.setInt(5, election.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Election not found with id: " + election.getId());
            }
            return election;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating election: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "DELETE FROM elections WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Election not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting election: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(Integer id) {
        String sql = "SELECT COUNT(*) FROM elections WHERE id = ?";

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
    public List<Election> findActiveElections() throws DatabaseOperationException {
        String sql = "SELECT * FROM elections WHERE CURRENT_DATE BETWEEN start_date AND end_date";
        List<Election> elections = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                elections.add(mapResultSetToElection(rs));
            }
            return elections;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding active elections: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Election> findByAcademicYear(String academicYear) throws DatabaseOperationException {
        String sql = "SELECT * FROM elections WHERE academic_year = ?";
        List<Election> elections = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, academicYear);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                elections.add(mapResultSetToElection(rs));
            }
            return elections;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding elections by academic year: " + e.getMessage(), e);
        }
    }

    private Election mapResultSetToElection(ResultSet rs) throws SQLException {
        return new Election(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("academic_year")
        );
    }
}