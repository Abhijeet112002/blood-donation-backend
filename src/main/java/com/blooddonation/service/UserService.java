package com.blooddonation.service;

import com.blooddonation.dto.request.UpdateProfileDto;
import com.blooddonation.dto.response.UserProfileDto;
import com.blooddonation.entity.User;
import com.blooddonation.exception.ResourceNotFoundException;
import com.blooddonation.repository.BloodRequestRepository;
import com.blooddonation.repository.DonorRepository;
import com.blooddonation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final BloodRequestRepository bloodRequestRepository;

    public UserProfileDto getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long donatedCount = donorRepository.countByUserId(userId);
        long requestedCount = bloodRequestRepository.countByRequesterId(userId);

        return UserProfileDto.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .bloodGroup(user.getBloodGroup())
                .state(user.getState())
                .district(user.getDistrict())
                .taluk(user.getTaluk())
                .isAvailable(user.getIsAvailable())
                .role(user.getRole().name())
                .donatedCount(donatedCount)
                .requestedCount(requestedCount)
                .build();
    }

    public UserProfileDto updateProfile(UUID userId, UpdateProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getBloodGroup() != null) user.setBloodGroup(dto.getBloodGroup());
        if (dto.getState() != null) user.setState(dto.getState());
        if (dto.getDistrict() != null) user.setDistrict(dto.getDistrict());
        if (dto.getTaluk() != null) user.setTaluk(dto.getTaluk());
        if (dto.getIsAvailable() != null) user.setIsAvailable(dto.getIsAvailable());

        userRepository.save(user);
        return getProfile(userId);
    }
}
