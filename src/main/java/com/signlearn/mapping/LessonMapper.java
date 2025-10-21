package com.signlearn.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.signlearn.domain.model.Lesson;

public class LessonMapper {
    public static Lesson map(ResultSet rs) throws SQLException {
        return new Lesson(
                rs.getLong("id"),
                rs.getLong("chapter_id"),
                rs.getString("title")
        );
    }
}