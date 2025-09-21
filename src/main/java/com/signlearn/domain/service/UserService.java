package com.signlearn.domain.service;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);
    Optional<User> findByEmail(Email email);
    Optional<User> findByUsername(String username);
    boolean isUsernameAvailable(String username);  // <-- new
    void update(User user);
    void delete(long id);
    List<User> listAll();
}