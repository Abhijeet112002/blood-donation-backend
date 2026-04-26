package com.blooddonation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private String id;
    private String title;
    private String message;
    private String type;
    private String createdAt;
    @JsonProperty("isRead")
    private boolean isRead;
}
