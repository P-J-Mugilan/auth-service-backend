package com.onrender.pjmugilan.authservice.identity.infrastructure.persistence;

import com.onrender.pjmugilan.authservice.identity.domain.User;
import com.onrender.pjmugilan.authservice.identity.domain.UserStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_public_id", columnNames = "public_id"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_phone_number", columnNames = "phone_number")
        }
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "public_id", nullable = false, length = 36, unique = true)
    private UUID publicId;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UserStatus status;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserEntity() {
        // Required by JPA
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


    public User toDomain() {
        return new User(
                id,
                publicId,
                email,
                phoneNumber,
                passwordHash,
                status,
                emailVerified,
                phoneVerified,
                createdAt,
                updatedAt
        );


    }

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();

        entity.publicId = user.getPublicId();
        entity.email = user.getEmail();
        entity.phoneNumber = user.getPhoneNumber();
        entity.passwordHash = user.getPasswordHash();
        entity.status = user.getStatus();
        entity.emailVerified = user.isEmailVerified();
        entity.phoneVerified = user.isPhoneVerified();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();

        return entity;
    }
}