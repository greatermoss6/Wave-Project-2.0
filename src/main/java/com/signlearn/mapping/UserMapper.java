package com.signlearn.mapping;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;
import com.signlearn.domain.enums.Gender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UserMapper {

    private static final DateTimeFormatter SQLITE_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static User mapRow(ResultSet rs) throws SQLException {
        // DOB
        LocalDate dob = null;
        String dobStr = rs.getString("dob");
        if (dobStr != null && !dobStr.isBlank()) {
            dob = LocalDate.parse(dobStr);
        }

        // created_at: prefer "yyyy-MM-dd HH:mm:ss" but tolerate ISO-8601 if legacy rows exist
        Instant createdAt = null;
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null && !createdAtStr.isBlank()) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(createdAtStr, SQLITE_TIMESTAMP);
                createdAt = ldt.atZone(ZoneId.systemDefault()).toInstant();
            } catch (Exception ignored) {
                createdAt = Instant.parse(createdAtStr);
            }
        }

        return new User(
                rs.getLong("id"),
                new Email(rs.getString("email")),
                rs.getString("password_hash"),
                rs.getString("name"),
                rs.getString("username"),
                dob,
                createdAt,
                rs.getString("gender") != null
                        ? Gender.valueOf(rs.getString("gender"))
                        : Gender.PREFER_NOT_TO_SAY
        );
    }
}
