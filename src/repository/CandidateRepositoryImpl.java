package repository;

import utils.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Candidate;
import model.Election;
import repository.interfaces.CandidateRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateRepositoryImpl implements CandidateRepository {

    private ElectionRepositoryImpl electionRepository = new ElectionRepositoryImpl();

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    @Override
    public Candidate create(Candidate candidate) throws DatabaseOperationException {
        String sql = "INSERT INTO candidates (name, major, year_of_study, campaign, election_id, vote_count) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, candidate.getName());
            stmt.setString(2, candidate.getmajor());
            stmt.setInt(3, candidate.getYearOfStudy());
            stmt.setString(4, candidate.getCampaign());
            stmt.setInt(5, candidate.getElection().getId());
            stmt.setInt(6, candidate.getVoteCount());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                candidate.setId(rs.getInt("id"));
                return candidate;
            }
            throw new DatabaseOperationException("Failed to create candidate");
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating candidate: " + e.getMessage(), e);
        }
    }

    @Override
    public Candidate findById(Integer id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "SELECT c.*, e.* FROM candidates c " +
                "JOIN elections e ON c.election_id = e.id " +
                "WHERE c.id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCandidate(rs);
            }
            throw new ResourceNotFoundException("Candidate not found with id: " + id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding candidate: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Candidate> findAll() throws DatabaseOperationException {
        String sql = "SELECT c.*, e.* FROM candidates c " +
                "JOIN elections e ON c.election_id = e.id " +
                "ORDER BY c.name";
        List<Candidate> candidates = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                candidates.add(mapResultSetToCandidate(rs));
            }
            return candidates;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding all candidates: " + e.getMessage(), e);
        }
    }

    @Override
    public Candidate update(Candidate candidate) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "UPDATE candidates SET name = ?, major = ?, year_of_study = ?, " +
                "campaign = ?, election_id = ?, vote_count = ? WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, candidate.getName());
            stmt.setString(2, candidate.getmajor());
            stmt.setInt(3, candidate.getYearOfStudy());
            stmt.setString(4, candidate.getCampaign());
            stmt.setInt(5, candidate.getElection().getId());
            stmt.setInt(6, candidate.getVoteCount());
            stmt.setInt(7, candidate.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Candidate not found with id: " + candidate.getId());
            }
            return candidate;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating candidate: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "DELETE FROM candidates WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Candidate not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting candidate: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(Integer id) {
        String sql = "SELECT COUNT(*) FROM candidates WHERE id = ?";

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
    public List<Candidate> findByElectionId(int electionId) throws DatabaseOperationException {
        String sql = "SELECT c.*, e.* FROM candidates c " +
                "JOIN elections e ON c.election_id = e.id " +
                "WHERE c.election_id = ? " +
                "ORDER BY c.vote_count DESC";
        List<Candidate> candidates = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, electionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                candidates.add(mapResultSetToCandidate(rs));
            }
            return candidates;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding candidates by election: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Candidate> findBymajor(String major) throws DatabaseOperationException {
        String sql = "SELECT c.*, e.* FROM candidates c " +
                "JOIN elections e ON c.election_id = e.id " +
                "WHERE c.major = ?";
        List<Candidate> candidates = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, major);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                candidates.add(mapResultSetToCandidate(rs));
            }
            return candidates;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding candidates by major: " + e.getMessage(), e);
        }
    }

    private Candidate mapResultSetToCandidate(ResultSet rs) throws SQLException {
        Election election = new Election(
                rs.getInt("election_id"),
                rs.getString("name"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("academic_year")
        );

        Candidate candidate = new Candidate(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("major"),
                rs.getInt("year_of_study"),
                rs.getString("campaign"),
                election
        );
        candidate.setVoteCount(rs.getInt("vote_count"));

        return candidate;
    }
}