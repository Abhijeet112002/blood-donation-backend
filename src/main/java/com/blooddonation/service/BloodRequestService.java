package com.blooddonation.service;

import com.blooddonation.dto.request.BloodRequestDto;
import com.blooddonation.dto.response.BloodRequestResponseDto;
import com.blooddonation.entity.BloodRequest;
import com.blooddonation.entity.User;
import com.blooddonation.enums.RequestStatus;
import com.blooddonation.enums.Urgency;
import com.blooddonation.exception.ResourceNotFoundException;
import com.blooddonation.repository.BloodRequestRepository;
import com.blooddonation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public BloodRequestResponseDto createRequest(UUID userId, BloodRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BloodRequest request = BloodRequest.builder()
                .requester(user)
                .bloodGroup(dto.getBloodGroup())
                .units(dto.getUnits() != null ? dto.getUnits() : 1)
                .hospital(dto.getHospital())
                .city(dto.getCity())
                .latitude(dto.getLat())
                .longitude(dto.getLng())
                .urgency(parseUrgency(dto.getUrgency()))
                .status(RequestStatus.PENDING)
                .build();

        request = bloodRequestRepository.save(request);
        return toDto(request);
    }

    public List<BloodRequestResponseDto> getNearbyRequests(double lat, double lng) {
        return bloodRequestRepository.findNearby(lat, lng).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public List<BloodRequestResponseDto> getMyRequests(UUID userId) {
        return bloodRequestRepository.findByRequesterIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public BloodRequestResponseDto getRequestById(UUID requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        return toDto(request);
    }

    public void updateRequestStatus(UUID requestId, String status) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        request.setStatus(RequestStatus.valueOf(status.toUpperCase()));
        bloodRequestRepository.save(request);
    }

    public void acceptRequest(UUID requestId) {
        updateRequestStatus(requestId, "FULFILLED");
    }

    public void deleteRequest(UUID requestId) {
        if (!bloodRequestRepository.existsById(requestId)) {
            throw new ResourceNotFoundException("Request not found");
        }
        bloodRequestRepository.deleteById(requestId);
    }

    private BloodRequestResponseDto toDto(BloodRequest request) {
        return BloodRequestResponseDto.builder()
                .id(request.getId().toString())
                .bloodGroup(request.getBloodGroup())
                .units(request.getUnits())
                .hospital(request.getHospital())
                .city(request.getCity())
                .lat(request.getLatitude() != null ? request.getLatitude() : 0.0)
                .lng(request.getLongitude() != null ? request.getLongitude() : 0.0)
                .urgency(request.getUrgency().name())
                .status(request.getStatus().name())
                .createdAt(request.getCreatedAt() != null ? request.getCreatedAt().format(FORMATTER) : "")
                .requesterName(request.getRequester().getName())
                .build();
    }

    private Urgency parseUrgency(String urgency) {
        if (urgency == null) return Urgency.NORMAL;
        try {
            return Urgency.valueOf(urgency.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Urgency.NORMAL;
        }
    }
}
