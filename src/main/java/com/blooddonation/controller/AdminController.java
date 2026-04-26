package com.blooddonation.controller;

import com.blooddonation.dto.ApiResponseDto;
import com.blooddonation.dto.response.BloodGroupSummaryDto;
import com.blooddonation.dto.response.DashboardResponseDto;
import com.blooddonation.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseDto<DashboardResponseDto>> getDashboard() {
        DashboardResponseDto dashboard = adminService.getDashboard();
        return ResponseEntity.ok(ApiResponseDto.success("Dashboard fetched", dashboard));
    }

    @GetMapping("/blood-summary")
    public ResponseEntity<ApiResponseDto<List<BloodGroupSummaryDto>>> getBloodSummary() {
        List<BloodGroupSummaryDto> summary = adminService.getBloodSummary();
        return ResponseEntity.ok(ApiResponseDto.success("Blood summary fetched", summary));
    }
}
