package com.blooddonation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User profile response — used by ProfileFragment.
 * Includes stats (donatedCount, requestedCount).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String bloodGroup;
    private String state;
    private String district;
    private String taluk;
    private Boolean isAvailable;
    private String role;
    private long donatedCount;
    private long requestedCount;
}
