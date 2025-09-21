package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;
import com.signlearn.persistence.repo.UserRepository;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteUserRepository implements UserRepository {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd"); // for dob
    private static final DateTimeFormatter DATE_TIME_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // for created_at

    private Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", "jdbc:sqlite:signlearn.db");
        return DriverManager.getConnection(url);
    }

    // --- Row mapper ---
    private User mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        Email email = new Email(rs.getString("email"));
        String passwordHash = rs.getString("password_hash");
        String name = rs.getString("name");
        String username = rs.getString("username");

        // dob
        LocalDate dob = null;
        String dobStr = rs.getString("dob");
        if (dobStr != null) {
            dob = LocalDate.parse(dobStr, DATE_FMT);
        }

        // created_at
        Instant createdAt = null;
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null) {
            LocalDateTime ldt = LocalDateTime.parse(createdAtStr, DATE_TIME_FMT);
            createdAt = ldt.toInstant(ZoneOffset.UTC);
        }

        return new User(id, email, passwordHash, name, username, dob, createdAt);
    }

    @Override
    public Optional<User> findById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById failed", e);
        }
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email.value());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByEmail failed", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByUsername failed", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY id";
        List<User> out = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("findAll failed", e);
        }
    }

    @Override
    public long insert(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(Instant.now());
        }

        String sql = "INSERT INTO users(email, password_hash, name, username, dob, created_at) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getEmail().value());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getName());
            ps.setString(4, user.getUsername());

            // dob
            if (user.getDateOfBirth() != null) {
                ps.setString(5, DATE_FMT.format(user.getDateOfBirth()));
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            // created_at
            ps.setString(6, DATE_TIME_FMT.withZone(ZoneOffset.UTC).format(user.getCreatedAt()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }

            try (PreparedStatement ps2 = c.prepareStatement("SELECT last_insert_rowid()")) {
                try (ResultSet rs = ps2.executeQuery()) {
                    if (rs.next()) return rs.getLong(1);
                }
            }

            throw new RuntimeException("Insert succeeded but no ID returned");
        } catch (SQLException e) {
            throw new RuntimeException("insert failed", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email=?, password_hash=?, name=?, username=?, dob=?, created_at=? " +
                "WHERE id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, user.getEmail().value());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getName());
            ps.setString(4, user.getUsername());

            if (user.getDateOfBirth() != null) {
                ps.setString(5, DATE_FMT.format(user.getDateOfBirth()));
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.setString(6, DATE_TIME_FMT.withZone(ZoneOffset.UTC).format(user.getCreatedAt()));
            ps.setLong(7, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("update failed", e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("delete failed", e);
        }
    }
}