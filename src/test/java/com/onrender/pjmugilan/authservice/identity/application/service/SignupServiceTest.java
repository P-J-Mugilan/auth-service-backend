package com.onrender.pjmugilan.authservice.identity.application.service;

import com.onrender.pjmugilan.authservice.identity.application.dto.request.SignupRequest;
import com.onrender.pjmugilan.authservice.identity.application.dto.response.SignupResponse;
import com.onrender.pjmugilan.authservice.identity.application.exception.EmailAlreadyExistsException;
import com.onrender.pjmugilan.authservice.identity.application.exception.PhoneNumberAlreadyExistsException;
import com.onrender.pjmugilan.authservice.identity.application.security.PasswordHasher;
import com.onrender.pjmugilan.authservice.identity.domain.User;
import com.onrender.pjmugilan.authservice.identity.domain.UserStatus;
import com.onrender.pjmugilan.authservice.identity.infrastructure.persistence.UserPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    private UserPersistenceAdapter userPersistenceAdapter;

    @Mock
    private PasswordHasher passwordHasher;

    private SignupService signupService;

    @BeforeEach
    void setUp() {
        signupService = new SignupService(
                userPersistenceAdapter,
                passwordHasher
        );
    }

    @Test
    void shouldSignupSuccessfully() {
        SignupRequest request = new SignupRequest(
                "user@example.com",
                "+919876543210",
                "StrongPassword123!"
        );

        when(userPersistenceAdapter.existsByEmail(request.email()))
                .thenReturn(false);

        when(userPersistenceAdapter.existsByPhoneNumber(request.phoneNumber()))
                .thenReturn(false);

        when(passwordHasher.hash(request.password()))
                .thenReturn("hashed-password");

        User savedUser = new User(
                1L,
                java.util.UUID.randomUUID(),
                request.email(),
                request.phoneNumber(),
                "hashed-password",
                UserStatus.PENDING_VERIFICATION,
                false,
                false,
                java.time.Instant.now(),
                java.time.Instant.now()
        );

        when(userPersistenceAdapter.save(any(User.class)))
                .thenReturn(savedUser);

        SignupResponse response = signupService.signup(request);

        assertThat(response.publicId()).isEqualTo(savedUser.getPublicId());
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.phoneNumber()).isEqualTo(request.phoneNumber());
        assertThat(response.status()).isEqualTo("PENDING_VERIFICATION");

        verify(passwordHasher).hash(request.password());
        verify(userPersistenceAdapter).save(any(User.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        SignupRequest request = new SignupRequest(
                "existing@example.com",
                "+919876543210",
                "StrongPassword123!"
        );

        when(userPersistenceAdapter.existsByEmail(request.email()))
                .thenReturn(true);

        assertThatThrownBy(() -> signupService.signup(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(passwordHasher, never()).hash(any());
        verify(userPersistenceAdapter, never()).save(any());
    }

    @Test
    void shouldRejectDuplicatePhoneNumber() {
        SignupRequest request = new SignupRequest(
                "user@example.com",
                "+919876543210",
                "StrongPassword123!"
        );

        when(userPersistenceAdapter.existsByEmail(request.email()))
                .thenReturn(false);

        when(userPersistenceAdapter.existsByPhoneNumber(request.phoneNumber()))
                .thenReturn(true);

        assertThatThrownBy(() -> signupService.signup(request))
                .isInstanceOf(PhoneNumberAlreadyExistsException.class);

        verify(passwordHasher, never()).hash(any());
        verify(userPersistenceAdapter, never()).save(any());
    }

    @Test
    void shouldHashPasswordBeforeSavingUser() {
        SignupRequest request = new SignupRequest(
                "user@example.com",
                null,
                "StrongPassword123!"
        );

        when(userPersistenceAdapter.existsByEmail(request.email()))
                .thenReturn(false);

        when(passwordHasher.hash(request.password()))
                .thenReturn("hashed-password");

        when(userPersistenceAdapter.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        signupService.signup(request);

        verify(passwordHasher).hash(request.password());

        verify(userPersistenceAdapter).save(argThat(user ->
                user.getPasswordHash().equals("hashed-password")
                        && !user.getPasswordHash().equals(request.password())
                        && user.getStatus() == UserStatus.PENDING_VERIFICATION
        ));
    }
}