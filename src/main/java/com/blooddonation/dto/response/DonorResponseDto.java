package com.blooddonation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Donor response — matches Android DonorResponse.java exactly.
 * Fields: id, name, bloodGroup, city, phone, isAvailable, latitude, longitude, lastDonated
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorResponseDto {
    private String id;
    private String name;
    private String bloodGroup;
    private String city;
    private String phone;
    @JsonProperty("isAvailable")
    private boolean isAvailable;
    private double latitude;
    private double longitude;
    private String lastDonated;
}

