package com.onrender.pjmugilan.authservice.identity.infrastructure.persistence;

import com.onrender.pjmugilan.authservice.identity.domain.User;
import com.onrender.pjmugilan.authservice.identity.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
class UserPersistenceAdapterTest {

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    void shouldSaveAndFindUserByEmail() {
        UUID publicId = UUID.randomUUID();
        Instant now = Instant.now();

        String email = "persistence-test-" + UUID.randomUUID() + "@example.com";
        String phoneNumber = "+919" + System.currentTimeMillis();

        User user = new User(
                null,
                publicId,
                email,
                phoneNumber,
                "hashed-password",
                UserStatus.PENDING_VERIFICATION,
                false,
                false,
                now,
                now
        );

        User savedUser = userPersistenceAdapter.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getPublicId()).isEqualTo(publicId);
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedUser.getPasswordHash()).isEqualTo("hashed-password");
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.PENDING_VERIFICATION);
        assertThat(savedUser.isEmailVerified()).isFalse();
        assertThat(savedUser.isPhoneVerified()).isFalse();

        User foundUser = userPersistenceAdapter
                .findByEmail(email)
                .orElseThrow();

        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getPublicId()).isEqualTo(publicId);
        assertThat(foundUser.getEmail()).isEqualTo(email);
    }
}