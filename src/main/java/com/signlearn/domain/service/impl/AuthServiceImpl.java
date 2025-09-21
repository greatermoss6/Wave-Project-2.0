package com.signlearn.domain.service.impl;

import com.signlearn.domain.model.User;
import com.signlearn.domain.service.AuthService;
import com.signlearn.domain.value.Email;
import com.signlearn.persistence.repo.UserRepository;
import com.signlearn.security.PasswordHasher;
import com.signlearn.app.context.SessionManager;
import com.signlearn.util.Result;
import com.signlearn.util.SignupStatus;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordHasher hasher;
    private final SessionManager session;

    public AuthServiceImpl(UserRepository userRepo, PasswordHasher hasher, SessionManager session) {
        this.userRepo = userRepo;
        this.hasher = hasher;
        this.session = session;
    }

    @Override
    public Result<SignupStatus> shallowSignUp(String email) {
        Email emailObj = new Email(email);
        if (userRepo.findByEmail(emailObj).isPresent()) {
            return Result.success(SignupStatus.DUPLICATE_EMAIL);
        }
        // store email temporarily for deep signup
        session.setAttribute("pendingEmail", emailObj);
        return Result.success(SignupStatus.SUCCESS);
    }

    @Override
    public Result<User> deepSignUpWithPassword(User user, String rawPassword) {

        String hashed = hasher.hash(rawPassword);
        user.setPasswordHash(hashed);

        // set createdAt
        user.setCreatedAt(java.time.Instant.now());

        // persist user
        long id = userRepo.insert(user);
        user.setId(id);

        // log in
        session.set(user);
        return Result.success(user);

    }

    @Override
    public Result<User> login(Email email, String password) {
        Optional<User> maybeUser = userRepo.findByEmail(email);
        if (maybeUser.isPresent()) {
            User u = maybeUser.get();
            if (u.getPasswordHash() != null && hasher.matches(password, u.getPasswordHash())) {
                session.set(u);
                return Result.success(u);
            }
            return Result.failure("Invalid credentials");
        }
        return Result.failure("User not found");
    }

    @Override
    public void logout() {
        session.clear();
    }
}