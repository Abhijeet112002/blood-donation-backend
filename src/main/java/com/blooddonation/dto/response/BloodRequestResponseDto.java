package com.blooddonation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Blood request response — matches Android BloodRequestResponse.java exactly.
 * Fields: id, bloodGroup, units, hospital, city, lat, lng, urgency, status, createdAt, requesterName
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodRequestResponseDto {
    private String id;
    private String bloodGroup;
    private int units;
    private String hospital;
    private String city;
    private double lat;
    private double lng;
    private String urgency;
    private String status;
    private String createdAt;
    private String requesterName;
}
