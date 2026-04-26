package com.blooddonation.controller;

import com.blooddonation.dto.ApiResponseDto;
import com.blooddonation.dto.request.BloodRequestDto;
import com.blooddonation.dto.response.BloodRequestResponseDto;
import com.blooddonation.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<BloodRequestResponseDto>> createRequest(Authentication auth,
                                                                                  @RequestBody BloodRequestDto dto) {
        UUID userId = UUID.fromString(auth.getName());
        BloodRequestResponseDto response = bloodRequestService.createRequest(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Request created", response));
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponseDto<List<BloodRequestResponseDto>>> getNearbyRequests(
            @RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        List<BloodRequestResponseDto> requests = bloodRequestService.getNearbyRequests(lat, lng);
        return ResponseEntity.ok(ApiResponseDto.success("Nearby requests fetched", requests));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponseDto<List<BloodRequestResponseDto>>> getMyRequests(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        List<BloodRequestResponseDto> requests = bloodRequestService.getMyRequests(userId);
        return ResponseEntity.ok(ApiResponseDto.success("My requests fetched", requests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BloodRequestResponseDto>> getRequestById(@PathVariable("id") String id) {
        BloodRequestResponseDto request = bloodRequestService.getRequestById(UUID.fromString(id));
        return ResponseEntity.ok(ApiResponseDto.success("Request fetched", request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseDto<Void>> updateRequestStatus(@PathVariable("id") String id,
                                                                      @RequestBody Map<String, String> body) {
        bloodRequestService.updateRequestStatus(UUID.fromString(id), body.get("status"));
        return ResponseEntity.ok(ApiResponseDto.success("Status updated", null));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponseDto<Void>> acceptRequest(@PathVariable("id") String id) {
        bloodRequestService.acceptRequest(UUID.fromString(id));
        return ResponseEntity.ok(ApiResponseDto.success("Request accepted", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteRequest(@PathVariable("id") String id) {
        bloodRequestService.deleteRequest(UUID.fromString(id));
        return ResponseEntity.ok(ApiResponseDto.success("Request deleted", null));
    }
}
