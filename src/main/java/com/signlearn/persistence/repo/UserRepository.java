package com.signlearn.persistence.repo;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;

import java.util.Optional;

public interface UserRepository {
    // -------- Reads --------
    Optional<User> findById(long id);

    // Primary signature uses value object
    Optional<User> findByEmail(Email email);

    // Convenience overload for legacy call sites
    default Optional<User> findByEmail(String email) {
        return findByEmail(new Email(email));
    }

    Optional<User> findByUsername(String username);

    // -------- Existence checks --------
    boolean existsByEmail(String email);

    // Convenience overload for legacy call sites
    default boolean existsByEmail(Email email) {
        return existsByEmail(email.getValue());
    }

    boolean existsByUsername(String username);

    // -------- Writes --------
    /** Insert a new user and return the generated id. */
    long insert(User user);

    /** Insert if id <= 0, otherwise update. */
    void save(User user);
}
