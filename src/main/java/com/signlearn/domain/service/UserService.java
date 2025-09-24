package com.signlearn.domain.service;

import com.signlearn.domain.model.User;
import java.util.List;

public interface UserService {
    // ---- Original signatures preserved (back-compat) ----
    User findById(int id);
    User findByUsername(String username);
    void save(User user);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<String> suggestUsernames(String baseUsername, int count);

    // ---- Small quality-of-life additions (non-breaking) ----

    /**
     * Back-compat overload: allow callers passing long.
     * Default delegates to existing int-based method so implementations don't need to duplicate logic.
     */
    default User findById(long id) {
        // safe narrowing — your IDs fit int in current schema
        return findById((int) id);
    }

    /**
     * Some parts of the code call findByEmail; make it part of the contract.
     * Implementations should return null when not found (as in your style).
     */
    default User findByEmail(String email) {
        // default no-op to avoid breaking implementations that don't use it yet
        return null;
    }

    /**
     * Back-compat alias for older code paths.
     */
    default boolean isUsernameTaken(String username) {
        return existsByUsername(username);
    }
}