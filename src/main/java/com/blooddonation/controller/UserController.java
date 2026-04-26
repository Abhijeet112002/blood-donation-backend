package com.blooddonation.controller;

import com.blooddonation.dto.ApiResponseDto;
import com.blooddonation.dto.request.UpdateProfileDto;
import com.blooddonation.dto.response.UserProfileDto;
import com.blooddonation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> getProfile(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        UserProfileDto profile = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponseDto.success("Profile fetched", profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> updateProfile(Authentication auth,
                                                                         @RequestBody UpdateProfileDto dto) {
        UUID userId = UUID.fromString(auth.getName());
        UserProfileDto profile = userService.updateProfile(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Profile updated", profile));
    }
}
