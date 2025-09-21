package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.Video;
import com.signlearn.persistence.Database;
import com.signlearn.persistence.repo.VideoRepository;
import com.signlearn.mapping.VideoMapper;
import com.signlearn.exceptions.PersistenceException;

import java.sql.*;
import java.util.Optional;

public class SqliteVideoRepository implements VideoRepository {

    @Override
    public Optional<Video> findByLessonId(long lessonId) {
        final String sql = "SELECT lesson_id, file_path, length_seconds FROM videos WHERE lesson_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, lessonId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(VideoMapper.map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching video by lessonId=" + lessonId, e);
        }
    }

    @Override
    public void insert(long lessonId, Video video) {
        final String sql = "INSERT INTO videos (lesson_id, file_path, length_seconds) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, lessonId);
            stmt.setString(2, video.getFilePath());
            stmt.setInt(3, video.getLengthSeconds());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting video for lessonId=" + lessonId, e);
        }
    }

    @Override
    public void update(long lessonId, Video video) {
        final String sql = "UPDATE videos SET file_path = ?, length_seconds = ? WHERE lesson_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, video.getFilePath());
            stmt.setInt(2, video.getLengthSeconds());
            stmt.setLong(3, lessonId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating video for lessonId=" + lessonId, e);
        }
    }

    @Override
    public void deleteByLessonId(long lessonId) {
        final String sql = "DELETE FROM videos WHERE lesson_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, lessonId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting video for lessonId=" + lessonId, e);
        }
    }
}