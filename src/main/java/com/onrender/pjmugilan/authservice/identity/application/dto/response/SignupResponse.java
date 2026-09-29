package com.onrender.pjmugilan.authservice.identity.application.dto.response;

import java.util.UUID;

public record SignupResponse(
        UUID publicId,
        String email,
        String phoneNumber,
        String status
) {
}