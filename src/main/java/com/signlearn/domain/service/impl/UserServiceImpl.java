package com.signlearn.domain.service.impl;

import com.signlearn.domain.model.User;
import com.signlearn.domain.service.UserService;
import com.signlearn.domain.value.Email;
import com.signlearn.persistence.repo.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User findById(int id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepo.insert(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.findByEmail(new Email(email)).isPresent();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    @Override
    public List<String> suggestUsernames(String baseUsername, int count) {
        // trivial naive implementation for now
        return List.of(
                baseUsername + "1",
                baseUsername + "2",
                baseUsername + "3"
        ).subList(0, Math.min(count, 3));
    }
}
