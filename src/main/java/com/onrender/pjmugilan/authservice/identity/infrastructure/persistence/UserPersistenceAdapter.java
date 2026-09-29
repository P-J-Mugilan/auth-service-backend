package com.onrender.pjmugilan.authservice.identity.infrastructure.persistence;

import com.onrender.pjmugilan.authservice.identity.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserPersistenceAdapter {

    private final UserRepository userRepository;

    public UserPersistenceAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        UserEntity entity = UserEntity.fromDomain(user);
        UserEntity savedEntity = userRepository.save(entity);
        return savedEntity.toDomain();
    }

    public Optional<User> findByPublicId(UUID publicId) {
        return userRepository.findByPublicId(publicId)
                .map(UserEntity::toDomain);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserEntity::toDomain);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(UserEntity::toDomain);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}