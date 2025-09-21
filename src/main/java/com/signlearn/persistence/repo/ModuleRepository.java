package com.signlearn.persistence.repo;

import com.signlearn.domain.model.Module;
import java.util.List;
import java.util.Optional;

public interface ModuleRepository {
    Optional<Module> findById(long id);
    List<Module> findAll();
    long insert(Module module);
    void update(Module module);
    void delete(long id);
}