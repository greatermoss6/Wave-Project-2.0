package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.Lesson;
import com.signlearn.persistence.Database;
import com.signlearn.persistence.repo.LessonRepository;
import com.signlearn.mapping.LessonMapper;
import com.signlearn.exceptions.PersistenceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteLessonRepository implements LessonRepository {

    @Override
    public Optional<Lesson> findById(long id) {
        final String sql = "SELECT * FROM lessons WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(LessonMapper.map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching lesson by id=" + id, e);
        }
    }

    @Override
    public List<Lesson> findByChapterId(long chapterId) {
        final String sql = "SELECT * FROM lessons WHERE chapter_id = ?";
        List<Lesson> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, chapterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(LessonMapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching lessons for chapterId=" + chapterId, e);
        }
    }

    @Override
    public long insert(Lesson lesson) {
        final String sql = "INSERT INTO lessons (chapter_id, title) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, lesson.getChapterId());
            stmt.setString(2, lesson.getTitle());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            }
            throw new PersistenceException("Failed to retrieve inserted lesson ID");
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting lesson", e);
        }
    }

    @Override
    public void update(Lesson lesson) {
        final String sql = "UPDATE lessons SET title = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lesson.getTitle());
            stmt.setLong(2, lesson.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating lesson id=" + lesson.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM lessons WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting lesson id=" + id, e);
        }
    }
}