package com.signlearn.domain.service;

import com.signlearn.domain.model.User;
import com.signlearn.domain.value.Email;
import com.signlearn.util.Result;
import com.signlearn.util.SignupStatus;

public interface AuthService {
    Result<SignupStatus> shallowSignUp(String email);
    Result<User> deepSignUpWithPassword(User user, String rawPassword);
    Result<User> login(Email email, String password);
    void logout();
}