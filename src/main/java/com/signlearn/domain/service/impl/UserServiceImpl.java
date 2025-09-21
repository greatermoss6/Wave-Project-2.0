// file: src/main/java/com/signlearn/domain/service/impl/UserServiceImpl.java
package com.signlearn.domain.service.impl;

import com.signlearn.domain.model.User;
import com.signlearn.domain.service.UserService;
import com.signlearn.domain.value.Email;
import com.signlearn.persistence.repo.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override public Optional<User> findById(long id) { return userRepo.findById(id); }
    @Override public Optional<User> findByEmail(Email email) { return userRepo.findByEmail(email); }
    @Override public List<User> listAll() { return userRepo.findAll(); }
    @Override public void update(User user) { userRepo.update(user); }
    @Override public void delete(long id) { userRepo.delete(id); }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return username != null && !username.isBlank() && userRepo.findByUsername(username).isEmpty();
    }
}