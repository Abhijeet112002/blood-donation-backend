package com.blooddonation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodGroupSummaryDto {
    private String bloodGroup;
    private long count;
    private long available;
}
