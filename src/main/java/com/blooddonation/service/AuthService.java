package com.blooddonation.service;

import com.blooddonation.dto.request.LoginRequestDto;
import com.blooddonation.dto.request.RegisterRequestDto;
import com.blooddonation.dto.request.ResetPasswordDto;
import com.blooddonation.dto.response.AuthResponseDto;
import com.blooddonation.entity.User;
import com.blooddonation.enums.Role;
import com.blooddonation.repository.UserRepository;
import com.blooddonation.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (dto.getPhone() != null && userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        String token = jwtUtils.generateToken(user.getId(), user.getRole().name());

        return AuthResponseDto.builder()
                .token(token)
                .userId(user.getId().toString())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        // Try email first, then phone
        String identifier = dto.getEmail();
        Optional<User> userOpt = userRepository.findByEmail(identifier);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByPhone(identifier);
        }

        User user = userOpt.orElseThrow(() ->
                new BadCredentialsException("Invalid email/phone or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email/phone or password");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getRole().name());

        return AuthResponseDto.builder()
                .token(token)
                .userId(user.getId().toString())
                .role(user.getRole().name())
                .build();
    }

    public void resetPassword(ResetPasswordDto dto) {
        if (!jwtUtils.validateToken(dto.getResetToken())) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        String phone = jwtUtils.extractSubject(dto.getResetToken());
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
