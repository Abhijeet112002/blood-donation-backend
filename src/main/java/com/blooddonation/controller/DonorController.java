package com.blooddonation.controller;

import com.blooddonation.dto.ApiResponseDto;
import com.blooddonation.dto.request.DonorRegisterDto;
import com.blooddonation.dto.response.DonorResponseDto;
import com.blooddonation.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto<List<DonorResponseDto>>> getAllDonors() {
        List<DonorResponseDto> donors = donorService.getAllDonors();
        return ResponseEntity.ok(ApiResponseDto.success("Donors fetched", donors));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<DonorResponseDto>> registerDonor(Authentication auth,
                                                                           @RequestBody DonorRegisterDto dto) {
        UUID userId = UUID.fromString(auth.getName());
        DonorResponseDto donor = donorService.registerDonor(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Donor registered", donor));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<List<DonorResponseDto>>> searchDonors(
            @RequestParam(value = "blood_group", required = false) String bloodGroup,
            @RequestParam(value = "city", required = false) String city) {
        List<DonorResponseDto> donors = donorService.searchDonors(bloodGroup, city);
        return ResponseEntity.ok(ApiResponseDto.success("Donors found", donors));
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponseDto<List<DonorResponseDto>>> getNearbyDonors(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam(value = "radius", defaultValue = "10") int radius) {
        List<DonorResponseDto> donors = donorService.getNearbyDonors(lat, lng, radius);
        return ResponseEntity.ok(ApiResponseDto.success("Nearby donors found", donors));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponseDto<DonorResponseDto>> getDonorProfile(@PathVariable("id") String donorId) {
        DonorResponseDto donor = donorService.getDonorProfile(UUID.fromString(donorId));
        return ResponseEntity.ok(ApiResponseDto.success("Donor profile fetched", donor));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<ApiResponseDto<DonorResponseDto>> updateDonorProfile(@PathVariable("id") String donorId,
                                                                                @RequestBody DonorRegisterDto dto) {
        DonorResponseDto donor = donorService.updateDonorProfile(UUID.fromString(donorId), dto);
        return ResponseEntity.ok(ApiResponseDto.success("Donor profile updated", donor));
    }
}
