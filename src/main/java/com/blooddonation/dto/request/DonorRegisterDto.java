package com.blooddonation.dto.request;

import lombok.Data;

@Data
public class DonorRegisterDto {
    private String bloodGroup;
    private String city;
    private Double lat;
    private Double lng;
    private String lastDonated;
    private String fullName;
    private Integer age;
}
