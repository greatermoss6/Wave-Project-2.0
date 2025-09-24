package com.signlearn.domain.service;

import com.signlearn.util.Result;
import com.signlearn.domain.enums.SignupStatus;
import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;

public interface AuthService {
    Result<SignupStatus> shallowSignUp(String email);
    Result<SignupStatus> deepSignUp(User user);

    boolean isUsernameAvailable(String username);

    /**
     * Authenticate user with email + password.
     */
    Result<User> login(Email email, String password);
}

