package com.blooddonation.service;

import com.blooddonation.dto.response.BloodGroupSummaryDto;
import com.blooddonation.dto.response.DashboardResponseDto;
import com.blooddonation.enums.RequestStatus;
import com.blooddonation.enums.Role;
import com.blooddonation.repository.BloodRequestRepository;
import com.blooddonation.repository.DonorRepository;
import com.blooddonation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DonorRepository donorRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final UserRepository userRepository;

    public DashboardResponseDto getDashboard() {
        long totalDonors = donorRepository.count();
        long pendingRequests = bloodRequestRepository.countByStatus(RequestStatus.PENDING);
        long fulfilledToday = bloodRequestRepository.countFulfilledToday();

        return DashboardResponseDto.builder()
                .totalDonors(totalDonors)
                .pendingRequests(pendingRequests)
                .fulfilledToday(fulfilledToday)
                .build();
    }

    public List<BloodGroupSummaryDto> getBloodSummary() {
        return donorRepository.getBloodGroupSummary().stream()
                .map(row -> BloodGroupSummaryDto.builder()
                        .bloodGroup((String) row[0])
                        .count((Long) row[1])
                        .available((Long) row[2])
                        .build())
                .collect(Collectors.toList());
    }
}
