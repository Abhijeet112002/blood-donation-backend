package com.blooddonation.dto.request;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private String name;
    private String email;
    private String phone;
    private String bloodGroup;
    private String state;
    private String district;
    private String taluk;
    private Boolean isAvailable;
}
