package com.blooddonation.controller;

import com.blooddonation.dto.ApiResponseDto;
import com.blooddonation.dto.response.NotificationDto;
import com.blooddonation.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<NotificationDto>>> getNotifications(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        List<NotificationDto> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(ApiResponseDto.success("Notifications fetched", notifications));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponseDto<Void>> markAsRead(@PathVariable("id") String id) {
        notificationService.markAsRead(UUID.fromString(id));
        return ResponseEntity.ok(ApiResponseDto.success("Marked as read", null));
    }
}
