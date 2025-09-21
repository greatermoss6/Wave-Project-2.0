package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.Question;
import com.signlearn.domain.model.MultipleChoiceQuestion;
import com.signlearn.domain.model.ChoiceOption;
import com.signlearn.persistence.Database;
import com.signlearn.persistence.repo.QuestionRepository;
import com.signlearn.mapping.QuestionMapper;
import com.signlearn.exceptions.PersistenceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteQuestionRepository implements QuestionRepository {

    @Override
    public Optional<Question> findById(long id) {
        final String sql = "SELECT * FROM questions WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(QuestionMapper.map(rs, conn));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching question by id=" + id, e);
        }
    }

    @Override
    public List<Question> findByLessonId(long lessonId) {
        final String sql = "SELECT * FROM questions WHERE lesson_id = ?";
        List<Question> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, lessonId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(QuestionMapper.map(rs, conn));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching questions by lessonId=" + lessonId, e);
        }
        return result;
    }

    @Override
    public long insert(Question question) {
        if (!(question instanceof MultipleChoiceQuestion mcq)) {
            throw new UnsupportedOperationException("Only MCQs are supported");
        }

        final String qSql = "INSERT INTO questions (lesson_id, prompt, correct_index) VALUES (?, ?, ?)";
        final String oSql = "INSERT INTO choice_options (question_id, idx, text, is_correct) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            long qid;

            try (PreparedStatement qStmt = conn.prepareStatement(qSql, Statement.RETURN_GENERATED_KEYS)) {
                qStmt.setLong(1, mcq.getLessonId());
                qStmt.setString(2, mcq.getPrompt());
                qStmt.setInt(3, mcq.getCorrectIndex());
                qStmt.executeUpdate();

                ResultSet keys = qStmt.getGeneratedKeys();
                if (keys.next()) {
                    qid = keys.getLong(1);
                } else {
                    throw new PersistenceException("Failed to retrieve inserted question ID");
                }
            }

            try (PreparedStatement oStmt = conn.prepareStatement(oSql)) {
                for (ChoiceOption option : mcq.getOptions()) {
                    oStmt.setLong(1, qid);
                    oStmt.setInt(2, option.getIndex());
                    oStmt.setString(3, option.getText());
                    oStmt.setBoolean(4, option.isCorrect());
                    oStmt.addBatch();
                }
                oStmt.executeBatch();
            }

            conn.commit();
            return qid;
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting question (lessonId=" + question.getLessonId() + ")", e);
        }
    }

    @Override
    public void update(Question question) {
        if (!(question instanceof MultipleChoiceQuestion mcq)) {
            throw new UnsupportedOperationException("Only MCQs are supported");
        }

        final String qSql = "UPDATE questions SET prompt = ?, correct_index = ? WHERE id = ?";
        final String delSql = "DELETE FROM choice_options WHERE question_id = ?";
        final String oSql = "INSERT INTO choice_options (question_id, idx, text, is_correct) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement qStmt = conn.prepareStatement(qSql)) {
                qStmt.setString(1, mcq.getPrompt());
                qStmt.setInt(2, mcq.getCorrectIndex());
                qStmt.setLong(3, mcq.getId());
                qStmt.executeUpdate();
            }

            try (PreparedStatement delStmt = conn.prepareStatement(delSql)) {
                delStmt.setLong(1, mcq.getId());
                delStmt.executeUpdate();
            }

            try (PreparedStatement oStmt = conn.prepareStatement(oSql)) {
                for (ChoiceOption option : mcq.getOptions()) {
                    oStmt.setLong(1, mcq.getId());
                    oStmt.setInt(2, option.getIndex());
                    oStmt.setString(3, option.getText());
                    oStmt.setBoolean(4, option.isCorrect());
                    oStmt.addBatch();
                }
                oStmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating question id=" + question.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting question id=" + id, e);
        }
    }
}