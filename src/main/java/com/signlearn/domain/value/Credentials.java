package com.signlearn.domain.value;

import com.signlearn.domain.value.Email;
import com.signlearn.domain.value.Password;

/**
 * Credentials value (email + password hash).
 * Password may be null while the user is in the shallow-signup step.
 */
public final class Credentials {
    private final Email email;
    private final Password password; // may be null until set

    public Credentials(Email email, Password password) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        this.email = email;
        this.password = password;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Credentials withPassword(Password newPassword) {
        return new Credentials(this.email, newPassword);
    }

    public Credentials withEmail(Email newEmail) {
        return new Credentials(newEmail, this.password);
    }
}