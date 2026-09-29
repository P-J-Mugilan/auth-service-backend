package com.onrender.pjmugilan.authservice.identity.domain;

import java.time.Instant;
import java.util.UUID;

public class User {

    private final Long id;
    private final UUID publicId;
    private final String email;
    private final String phoneNumber;
    private final String passwordHash;
    private final UserStatus status;
    private final boolean emailVerified;
    private final boolean phoneVerified;
    private final Instant createdAt;
    private final Instant updatedAt;

    public User(
            Long id,
            UUID publicId,
            String email,
            String phoneNumber,
            String passwordHash,
            UserStatus status,
            boolean emailVerified,
            boolean phoneVerified,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.publicId = publicId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.status = status;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}