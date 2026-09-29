package com.onrender.pjmugilan.authservice.identity.api;

import com.onrender.pjmugilan.authservice.identity.application.dto.request.SignupRequest;
import com.onrender.pjmugilan.authservice.identity.application.dto.response.SignupResponse;
import com.onrender.pjmugilan.authservice.identity.application.service.SignupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private SignupService signupService;

    @Test
    void shouldSignupSuccessfully() throws Exception {
        SignupRequest request = new SignupRequest(
                "user@example.com",
                "+919876543210",
                "StrongPassword123!"
        );

        UUID publicId = UUID.randomUUID();

        SignupResponse response = new SignupResponse(
                publicId,
                request.email(),
                request.phoneNumber(),
                "PENDING_VERIFICATION"
        );

        when(signupService.signup(any(SignupRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.publicId").value(publicId.toString()))
                .andExpect(jsonPath("$.data.email").value(request.email()))
                .andExpect(jsonPath("$.data.phoneNumber").value(request.phoneNumber()))
                .andExpect(jsonPath("$.data.status").value("PENDING_VERIFICATION"));

        verify(signupService).signup(any(SignupRequest.class));
    }

    @Test
    void shouldRejectInvalidRequest() throws Exception {
        SignupRequest request = new SignupRequest(
                "invalid-email",
                "+919876543210",
                "short"
        );

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}