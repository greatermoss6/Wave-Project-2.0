package com.signlearn.domain.service.impl;

import com.signlearn.app.context.SessionManager;
import com.signlearn.domain.model.User;
import com.signlearn.domain.service.AuthService;
import com.signlearn.domain.value.Email;
import com.signlearn.persistence.repo.UserRepository;
import com.signlearn.security.PasswordHasher;
import com.signlearn.util.Result;
import com.signlearn.domain.enums.SignupStatus;

import java.time.Instant;
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
        session.setAttribute("pendingEmail", emailObj);
        return Result.success(SignupStatus.SUCCESS);
    }

    @Override
    public Result<SignupStatus> deepSignUp(User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return Result.success(SignupStatus.DUPLICATE_USERNAME);
        }

        user.setCreatedAt(Instant.now());
        long id = userRepo.insert(user);
        user.setId(id);

        session.set(user);
        return Result.success(SignupStatus.SUCCESS);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepo.findByUsername(username).isEmpty();
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
}
