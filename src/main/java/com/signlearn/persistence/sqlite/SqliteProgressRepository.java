package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.Progress;
import com.signlearn.persistence.Database;
import com.signlearn.persistence.repo.ProgressRepository;
import com.signlearn.mapping.ProgressMapper;
import com.signlearn.exceptions.PersistenceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteProgressRepository implements ProgressRepository {

    @Override
    public Optional<Progress> find(long userId, long lessonId) {
        final String sql = "SELECT * FROM progress WHERE user_id = ? AND lesson_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, lessonId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(ProgressMapper.map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching progress for userId=" + userId + " lessonId=" + lessonId, e);
        }
    }

    @Override
    public List<Progress> findByUser(long userId) {
        final String sql = "SELECT * FROM progress WHERE user_id = ?";
        List<Progress> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(ProgressMapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching progress list for userId=" + userId, e);
        }
    }

    @Override
    public void upsert(Progress progress) {
        final String sql = """
            INSERT INTO progress (user_id, lesson_id, completed, score_percent)
            VALUES (?, ?, ?, ?)
            ON CONFLICT(user_id, lesson_id) DO UPDATE SET
                completed = excluded.completed,
                score_percent = excluded.score_percent
        """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, progress.getUserId());
            stmt.setLong(2, progress.getLessonId());
            stmt.setBoolean(3, progress.isCompleted());
            stmt.setInt(4, progress.getScorePercent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error upserting progress for userId=" + progress.getUserId() +
                    " lessonId=" + progress.getLessonId(), e);
        }
    }

    @Override
    public void delete(long userId, long lessonId) {
        final String sql = "DELETE FROM progress WHERE user_id = ? AND lesson_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, lessonId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting progress for userId=" + userId +
                    " lessonId=" + lessonId, e);
        }
    }
}