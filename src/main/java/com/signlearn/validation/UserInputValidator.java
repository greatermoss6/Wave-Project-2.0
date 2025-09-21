package com.signlearn.validation;

import com.signlearn.domain.model.User;
import com.signlearn.exceptions.ValidationException;

public class UserInputValidator implements Validator<User> {
    @Override
    public void validate(User user) throws ValidationException {
        if (user.getEmail() == null) throw new ValidationException("Email required");
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty())
            throw new ValidationException("Password required");
    }
}