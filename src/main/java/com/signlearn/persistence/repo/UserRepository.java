package com.signlearn.persistence.repo;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);
    Optional<User> findByEmail(Email email);
    Optional<User> findByUsername(String username); // <-- NEW
    List<User> findAll();
    long insert(User user);
    void update(User user);
    void delete(long id);
}