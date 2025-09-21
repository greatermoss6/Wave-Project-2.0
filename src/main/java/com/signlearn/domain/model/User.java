package com.signlearn.domain.model;

import com.signlearn.domain.value.Email;
import com.signlearn.domain.value.Password;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Domain model for a user.
 * - Credentials holds Email + Password (hash wrapper)
 * - Replaces 'age' with 'dateOfBirth'
 * - Adds 'username' (unique) and 'createdAt'
 */
public class User {
    private long id;
    private Credentials credentials;     // email + password (hash)
    private String name;                 // real name (optional, keep as-is)
    private String username;             // display name / unique handle
    private LocalDate dateOfBirth;       // replaces 'age'
    private Instant createdAt;           // set by service on creation

    // --- Constructors used by repositories/mappers ---
    public User(long id,
                Email email,
                String passwordHash,
                String name,
                String username,
                LocalDate dateOfBirth,
                Instant createdAt) {
        this.id = id;
        this.credentials = new Credentials(email, passwordHash != null ? Password.hashed(passwordHash) : null);
        this.name = name;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
    }

    // Used by signup (service will set createdAt and hash the password)
    public User(Email email,
                String passwordHash,
                String name,
                String username,
                LocalDate dateOfBirth) {
        this(-1, email, passwordHash, name, username, dateOfBirth, null);
    }

    // --- Getters/Setters (back-compat preserved where sensible) ---
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Email getEmail() { return credentials != null ? credentials.getEmail() : null; }

    /** Returns the password hash string (or null). */
    public String getPasswordHash() {
        if (credentials == null || credentials.getPassword() == null) return null;
        return credentials.getPassword().getHash();
    }

    /** Set password from raw hash (used after service hashes). */
    public void setPasswordHash(String passwordHash) {
        if (credentials == null) throw new IllegalStateException("Credentials not initialized");
        this.credentials = (passwordHash != null)
                ? credentials.withPassword(Password.hashed(passwordHash))
                : credentials.withPassword(null);
    }

    public Credentials getCredentials() { return credentials; }
    public void setEmail(Email email) {
        if (credentials == null) this.credentials = new Credentials(email, null);
        else this.credentials = credentials.withEmail(email);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}