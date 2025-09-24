package com.signlearn.domain.enums;

/**
 * Represents possible outcomes of signup operations.
 */
public enum SignupStatus {
    SUCCESS,             // signup successful
    DUPLICATE_EMAIL,     // email already exists in DB
    DUPLICATE_USERNAME,  // username already exists in DB
    INVALID_EMAIL,       // email is invalid format
    ERROR;               // catch-all error
}
