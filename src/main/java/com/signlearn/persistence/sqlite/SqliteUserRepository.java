package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;
import com.signlearn.mapping.UserMapper;
import com.signlearn.persistence.repo.UserRepository;
import com.signlearn.persistence.Database;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.Instant;
import java.sql.*;
import java.util.Optional;

public class SqliteUserRepository implements UserRepository {

    private static final DateTimeFormatter SQLITE_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String formatInstant(Instant instant) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .format(SQLITE_TIMESTAMP);
    }

    public SqliteUserRepository() {
        initTable();
    }

    private void initTable() {
        final String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL UNIQUE,
                password_hash TEXT,
                name TEXT,
                username TEXT UNIQUE,
                dob TEXT,
                created_at TEXT,
                gender TEXT
            )
        """;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize users table", e);
        }
    }

    // -------- Reads --------

    @Override
    public Optional<User> findById(long id) {
        final String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(UserMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById failed", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        final String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.getValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(UserMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByEmail failed", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(UserMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByUsername failed", e);
        }
        return Optional.empty();
    }

    // -------- Existence checks --------

    @Override
    public boolean existsByEmail(String email) {
        final String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("existsByEmail failed", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        final String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("existsByUsername failed", e);
        }
    }

    // -------- Writes --------

    @Override
    public long insert(User user) {
        final String sql = """
            INSERT INTO users (email, password_hash, name, username, dob, created_at, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail().getValue());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getName());
            ps.setString(4, user.getUsername());
            ps.setString(5, user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : null);
            ps.setString(6, formatInstant(user.getCreatedAt()));
            ps.setString(7, user.getGender() != null ? user.getGender().name() : null);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("insert failed", e);
        }
        return -1;
    }

    @Override
    public void save(User user) {
        if (user.getId() <= 0) {
            long id = insert(user);
            user.setId(id);
            return;
        }

        final String sql = """
            UPDATE users
            SET email = ?, password_hash = ?, name = ?, username = ?, dob = ?, created_at = ?, gender = ?
            WHERE id = ?
        """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail().getValue());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getName());
            ps.setString(4, user.getUsername());
            ps.setString(5, user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : null);
            ps.setString(6, formatInstant(user.getCreatedAt()));
            ps.setString(7, user.getGender() != null ? user.getGender().name() : null);
            ps.setLong(8, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save failed", e);
        }
    }
}
