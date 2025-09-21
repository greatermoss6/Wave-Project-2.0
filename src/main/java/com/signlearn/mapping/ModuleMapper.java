package com.signlearn.mapping;

import com.signlearn.domain.model.Module;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleMapper {

    public static Module map(ResultSet rs) throws SQLException {
        return new Module(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description")
        );
    }
}