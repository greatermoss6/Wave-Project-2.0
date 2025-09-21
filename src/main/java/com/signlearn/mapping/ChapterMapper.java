package com.signlearn.mapping;

import com.signlearn.domain.model.Chapter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChapterMapper {

    public static Chapter map(ResultSet rs) throws SQLException {
        return new Chapter(
                rs.getLong("id"),
                rs.getLong("module_id"),
                rs.getString("title")
        );
    }
}
