package com.signlearn.persistence.sqlite;

import com.signlearn.domain.model.Module;
import com.signlearn.persistence.Database;
import com.signlearn.persistence.repo.ModuleRepository;
import com.signlearn.mapping.ModuleMapper;
import com.signlearn.exceptions.PersistenceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteModuleRepository implements ModuleRepository {

    @Override
    public Optional<Module> findById(long id) {
        final String sql = "SELECT * FROM modules WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(ModuleMapper.map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching module by id=" + id, e);
        }
    }

    @Override
    public List<Module> findAll() {
        final String sql = "SELECT * FROM modules";
        List<Module> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(ModuleMapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new PersistenceException("Error fetching modules", e);
        }
    }

    @Override
    public long insert(Module module) {
        final String sql = "INSERT INTO modules (title, description) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, module.getTitle());
            stmt.setString(2, module.getDescription());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            }
            throw new PersistenceException("Failed to retrieve inserted module ID");
        } catch (SQLException e) {
            throw new PersistenceException("Error inserting module", e);
        }
    }

    @Override
    public void update(Module module) {
        final String sql = "UPDATE modules SET title = ?, description = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, module.getTitle());
            stmt.setString(2, module.getDescription());
            stmt.setLong(3, module.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error updating module id=" + module.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM modules WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error deleting module id=" + id, e);
        }
    }
}