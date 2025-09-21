package com.signlearn.mapping;

import com.signlearn.domain.model.Progress;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProgressMapper {
    public static Progress map(ResultSet rs) throws SQLException {
        return new Progress(
                rs.getLong("user_id"),
                rs.getLong("lesson_id"),
                rs.getBoolean("completed"),
                rs.getInt("score_percent")
        );
    }
}