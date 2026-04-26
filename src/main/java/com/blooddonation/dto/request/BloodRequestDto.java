package com.blooddonation.dto.request;

import lombok.Data;

@Data
public class BloodRequestDto {
    private String bloodGroup;
    private Integer units;
    private String hospital;
    private String city;
    private Double lat;
    private Double lng;
    private String urgency;
}
