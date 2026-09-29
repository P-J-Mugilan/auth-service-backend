package com.onrender.pjmugilan.authservice.identity.application.service;

import com.onrender.pjmugilan.authservice.identity.application.dto.request.SignupRequest;
import com.onrender.pjmugilan.authservice.identity.application.dto.response.SignupResponse;
import com.onrender.pjmugilan.authservice.identity.application.exception.EmailAlreadyExistsException;
import com.onrender.pjmugilan.authservice.identity.application.exception.PhoneNumberAlreadyExistsException;
import com.onrender.pjmugilan.authservice.identity.application.security.PasswordHasher;
import com.onrender.pjmugilan.authservice.identity.domain.User;
import com.onrender.pjmugilan.authservice.identity.domain.UserStatus;
import com.onrender.pjmugilan.authservice.identity.infrastructure.persistence.UserPersistenceAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class SignupService {

    private final UserPersistenceAdapter userPersistenceAdapter;
    private final PasswordHasher passwordHasher;

    public SignupService(
            UserPersistenceAdapter userPersistenceAdapter,
            PasswordHasher passwordHasher
    ) {
        this.userPersistenceAdapter = userPersistenceAdapter;
        this.passwordHasher = passwordHasher;
    }

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (userPersistenceAdapter.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        if (request.phoneNumber() != null
                && !request.phoneNumber().isBlank()
                && userPersistenceAdapter.existsByPhoneNumber(request.phoneNumber())) {
            throw new PhoneNumberAlreadyExistsException();
        }

        Instant now = Instant.now();

        User user = new User(
                null,
                UUID.randomUUID(),
                request.email(),
                request.phoneNumber(),
                passwordHasher.hash(request.password()),
                UserStatus.PENDING_VERIFICATION,
                false,
                false,
                now,
                now
        );

        User savedUser = userPersistenceAdapter.save(user);

        return new SignupResponse(
                savedUser.getPublicId(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getStatus().name()
        );
    }
}