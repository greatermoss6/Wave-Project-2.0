package com.signlearn.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.signlearn.domain.model.Video;

public class VideoMapper {
    public static Video map(ResultSet rs) throws SQLException {
        long lessonId = rs.getLong("lesson_id");
        String path = rs.getString("file_path");
        int len = rs.getObject("length_seconds") == null ? -1 : rs.getInt("length_seconds");
        return new Video(lessonId, path, len);
    }
}