package com.signlearn.mapping;

import com.signlearn.domain.model.Video;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VideoMapper {
    public static Video map(ResultSet rs) throws SQLException {
        long lessonId = rs.getLong("lesson_id");
        String path = rs.getString("file_path");
        int len = rs.getObject("length_seconds") == null ? -1 : rs.getInt("length_seconds");
        return new Video(lessonId, path, len);
    }
}