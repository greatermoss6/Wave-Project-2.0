package com.signlearn.mapping;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

public class UserMapper {

    public static User map(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        Email email = new Email(rs.getString("email"));
        String passwordHash = rs.getString("password_hash");
        String name = rs.getString("name");
        String username = rs.getString("username");

        String dobStr = rs.getString("dob");
        LocalDate dob = (dobStr != null) ? LocalDate.parse(dobStr) : null;

        String createdAtStr = rs.getString("created_at");
        Instant createdAt = (createdAtStr != null) ? Instant.parse(createdAtStr) : null;

        return new User(id, email, passwordHash, name, username, dob, createdAt);
    }
}