package com.signlearn.mapping;

import com.signlearn.domain.model.MultipleChoiceQuestion;
import com.signlearn.domain.model.ChoiceOption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionMapper {
    public static MultipleChoiceQuestion map(ResultSet rs, Connection conn) throws SQLException {
        long qid = rs.getLong("id");
        long lessonId = rs.getLong("lesson_id");
        String prompt = rs.getString("prompt");
        int correctIndex = rs.getInt("correct_index");

        List<ChoiceOption> options = new ArrayList<>();
        final String optSql = "SELECT idx, text, is_correct FROM choice_options WHERE question_id = ? ORDER BY idx ASC";
        try (PreparedStatement stmt = conn.prepareStatement(optSql)) {
            stmt.setLong(1, qid);
            ResultSet ors = stmt.executeQuery();
            while (ors.next()) {
                options.add(new ChoiceOption(
                        ors.getInt("idx"),
                        ors.getString("text"),
                        ors.getInt("is_correct") == 1
                ));
            }
        }

        return new MultipleChoiceQuestion(qid, lessonId, prompt, options, correctIndex);
    }
}