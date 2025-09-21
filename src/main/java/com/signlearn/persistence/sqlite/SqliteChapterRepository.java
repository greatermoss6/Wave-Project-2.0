package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.Chapter;
import com.signlearn.persistence.Database;
import com.signlearn.persistence.repo.ChapterRepository;
import com.signlearn.mapping.ChapterMapper;
import com.signlearn.exceptions.PersistenceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteChapterRepository implements ChapterRepository {

    @Override
    public Optional<Chapter> findById(long id) {
        final String sql = "SELECT * FROM chapters WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(ChapterMapper.map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching chapter by id=" + id, e);
        }
    }

    @Override
    public List<Chapter> findByModuleId(long moduleId) {
        final String sql = "SELECT * FROM chapters WHERE module_id = ?";
        List<Chapter> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, moduleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(ChapterMapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching chapters for moduleId=" + moduleId, e);
        }
    }

    @Override
    public long insert(Chapter chapter) {
        final String sql = "INSERT INTO chapters (module_id, title) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, chapter.getModuleId());
            stmt.setString(2, chapter.getTitle());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            }
            throw new PersistenceException("Failed to retrieve inserted chapter ID");
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting chapter", e);
        }
    }

    @Override
    public void update(Chapter chapter) {
        final String sql = "UPDATE chapters SET title = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chapter.getTitle());
            stmt.setLong(2, chapter.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating chapter id=" + chapter.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM chapters WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting chapter id=" + id, e);
        }
    }
}