package com.blooddonation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Email or phone is required")
    private String email; // Accepts both email and phone number

    @NotBlank(message = "Password is required")
    private String password;
}
