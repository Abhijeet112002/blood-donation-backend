package com.blooddonation.service;

import com.blooddonation.dto.response.NotificationDto;
import com.blooddonation.entity.Notification;
import com.blooddonation.exception.ResourceNotFoundException;
import com.blooddonation.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    public List<NotificationDto> getNotifications(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    private NotificationDto toDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId().toString())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .createdAt(n.getCreatedAt() != null ? n.getCreatedAt().format(FORMATTER) : "")
                .isRead(n.getIsRead() != null && n.getIsRead())
                .build();
    }
}
