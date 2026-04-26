package com.blooddonation.controller;

import com.blooddonation.dto.ApiResponseDto;
import com.blooddonation.dto.request.*;
import com.blooddonation.dto.response.AuthResponseDto;
import com.blooddonation.service.AuthService;
import com.blooddonation.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto dto) {
        AuthResponseDto response = authService.register(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Registration successful", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResponseDto response = authService.login(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Login successful", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout() {
        return ResponseEntity.ok(ApiResponseDto.success("Logged out successfully", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDto<Map<String, String>>> forgotPassword(@Valid @RequestBody ForgotPasswordDto dto) {
        otpService.sendOtp(dto.getPhone());
        return ResponseEntity.ok(ApiResponseDto.success("OTP sent successfully",
                Map.of("message", "OTP sent to " + dto.getPhone())));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> verifyOtp(@Valid @RequestBody VerifyOtpDto dto) {
        String resetToken = otpService.verifyOtp(dto.getPhone(), dto.getOtp());
        return ResponseEntity.ok(ApiResponseDto.success("OTP verified",
                Map.of("verified", true, "resetToken", resetToken)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto<Map<String, String>>> resetPassword(@Valid @RequestBody ResetPasswordDto dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Password reset successful",
                Map.of("message", "Password has been reset successfully")));
    }
}
