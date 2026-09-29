package com.onrender.pjmugilan.authservice.identity.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @Size(max = 20)
        String phoneNumber,

        @NotBlank
        @Size(min = 8, max = 128)
        String password
) {
}