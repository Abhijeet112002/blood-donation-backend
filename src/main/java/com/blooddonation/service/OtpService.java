package com.blooddonation.service;

import com.blooddonation.entity.OtpToken;
import com.blooddonation.repository.OtpTokenRepository;
import com.blooddonation.repository.UserRepository;
import com.blooddonation.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Value("${app.otp.expiration-minutes}")
    private int otpExpirationMinutes;

    public void sendOtp(String phone) {
        if (!userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("No account found with this phone number");
        }

        String otpCode = String.format("%06d", new Random().nextInt(999999));

        OtpToken otpToken = OtpToken.builder()
                .phone(phone)
                .otpCode(otpCode)
                .expiresAt(LocalDateTime.now().plusMinutes(otpExpirationMinutes))
                .used(false)
                .build();

        otpTokenRepository.save(otpToken);

        // Print OTP to console for development
        log.info("========================================");
        log.info("   OTP for phone {}: {}", phone, otpCode);
        log.info("   Expires in {} minutes", otpExpirationMinutes);
        log.info("========================================");
    }

    public String verifyOtp(String phone, String otp) {
        OtpToken otpToken = otpTokenRepository
                .findTopByPhoneAndUsedFalseOrderByCreatedAtDesc(phone)
                .orElseThrow(() -> new IllegalArgumentException("No OTP found for this phone number"));

        if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired. Please request a new one.");
        }

        if (!otpToken.getOtpCode().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP code");
        }

        otpToken.setUsed(true);
        otpTokenRepository.save(otpToken);

        // Generate a short-lived reset token
        return jwtUtils.generateResetToken(phone);
    }
}
