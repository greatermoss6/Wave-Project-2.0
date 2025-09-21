package com.signlearn.validation;

import com.signlearn.domain.value.Email;
import com.signlearn.exceptions.ValidationException;

public class EmailValidator implements Validator<Email> {
    @Override
    public void validate(Email email) throws ValidationException {
        if (email == null || !email.value().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ValidationException("Invalid email: " + email);
        }
    }
}