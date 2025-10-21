package com.signlearn.domain.service;

import com.signlearn.app.context.SessionManager;
import com.signlearn.domain.model.User;
import com.signlearn.domain.service.impl.AuthServiceImpl;
import com.signlearn.domain.value.Email;
import com.signlearn.persistence.repo.UserRepository;
import com.signlearn.security.PasswordHasher;
import com.signlearn.util.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepo;
    @Mock private PasswordHasher hasher;

    private SessionManager session;
    private AuthServiceImpl auth;

    @BeforeEach
    void setUp() {
        session = new SessionManager(); // simple real dependency
        auth = new AuthServiceImpl(userRepo, hasher, session);
    }

    @Test
    void login_withValidCredentials_returnsSuccess_andSetsSession() {
        Email email = new Email("user@example.com");
        User stored = mock(User.class);
        when(stored.getPasswordHash()).thenReturn("HASH123");
        when(stored.getEmail()).thenReturn(email);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(stored));
        when(hasher.matches("secret", "HASH123")).thenReturn(true);

        Result<User> result = auth.login(email, "secret");

        assertTrue(result.isSuccess(), "Expected success result");
        assertEquals(email, result.getValue().getEmail());
        assertNotNull(session.get(), "Session should contain the logged-in user");
        verify(userRepo, times(1)).findByEmail(email);
        verify(hasher, times(1)).matches("secret", "HASH123");
        verifyNoMoreInteractions(userRepo, hasher);
    }

    @Test
    void login_withWrongPassword_returnsFailure_andLeavesSessionNull() {
        Email email = new Email("user@example.com");
        User stored = mock(User.class);
        when(stored.getPasswordHash()).thenReturn("HASH123");

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(stored));
        when(hasher.matches("badpass", "HASH123")).thenReturn(false);

        Result<User> result = auth.login(email, "badpass");

        assertFalse(result.isSuccess(), "Expected failure result");
        assertEquals("Invalid credentials", result.getError());
        assertNull(session.get(), "Session should remain null on failure");
        verify(userRepo).findByEmail(email);
        verify(hasher).matches("badpass", "HASH123");
        verifyNoMoreInteractions(userRepo, hasher);
    }

    @Test
    void login_withUnknownUser_returnsFailure_andLeavesSessionNull() {
        Email email = new Email("nouser@example.com");

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        Result<User> result = auth.login(email, "anything");

        assertFalse(result.isSuccess(), "Expected failure result");
        assertEquals("User not found", result.getError());
        assertNull(session.get(), "Session should remain null when user not found");
        verify(userRepo).findByEmail(email);
        verifyNoMoreInteractions(userRepo, hasher);
    }

    @Test
    void logout_clearsSession() {
        // Preload session as if logged in
        User u = mock(User.class);
        session.set(u);

        auth.logout();

        assertNull(session.get(), "Session should be cleared after logout");
        verifyNoInteractions(userRepo, hasher);
    }
}
