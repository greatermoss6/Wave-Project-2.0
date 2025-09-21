package com.signlearn.validation;

import com.signlearn.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T t) throws ValidationException;
}