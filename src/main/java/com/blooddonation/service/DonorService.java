package com.blooddonation.service;

import com.blooddonation.dto.request.DonorRegisterDto;
import com.blooddonation.dto.response.DonorResponseDto;
import com.blooddonation.entity.Donor;
import com.blooddonation.entity.User;
import com.blooddonation.enums.Role;
import com.blooddonation.exception.ResourceNotFoundException;
import com.blooddonation.repository.DonorRepository;
import com.blooddonation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;

    public DonorResponseDto registerDonor(UUID userId, DonorRegisterDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (donorRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("You are already registered as a donor");
        }

        Donor donor = Donor.builder()
                .user(user)
                .bloodGroup(dto.getBloodGroup())
                .city(dto.getCity())
                .latitude(dto.getLat())
                .longitude(dto.getLng())
                .lastDonated(dto.getLastDonated())
                .age(dto.getAge())
                .isAvailable(true)
                .build();

        donor = donorRepository.save(donor);

        // Update user role to DONOR
        user.setRole(Role.DONOR);
        if (dto.getBloodGroup() != null) user.setBloodGroup(dto.getBloodGroup());
        userRepository.save(user);

        return toDto(donor);
    }

    public List<DonorResponseDto> getAllDonors() {
        return donorRepository.findByIsAvailableTrue().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public List<DonorResponseDto> searchDonors(String bloodGroup, String city) {
        List<Donor> donors;
        if (bloodGroup != null && city != null && !bloodGroup.isEmpty() && !city.isEmpty()) {
            donors = donorRepository.findByBloodGroupAndCityContainingIgnoreCase(bloodGroup, city);
        } else if (bloodGroup != null && !bloodGroup.isEmpty()) {
            donors = donorRepository.findByBloodGroupIgnoreCase(bloodGroup);
        } else if (city != null && !city.isEmpty()) {
            donors = donorRepository.findByCityContainingIgnoreCase(city);
        } else {
            donors = donorRepository.findByIsAvailableTrue();
        }
        return donors.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<DonorResponseDto> getNearbyDonors(double lat, double lng, int radius) {
        return donorRepository.findNearby(lat, lng, radius).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public DonorResponseDto getDonorProfile(UUID donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found"));
        return toDto(donor);
    }

    public DonorResponseDto updateDonorProfile(UUID donorId, DonorRegisterDto dto) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found"));

        if (dto.getBloodGroup() != null) donor.setBloodGroup(dto.getBloodGroup());
        if (dto.getCity() != null) donor.setCity(dto.getCity());
        if (dto.getLat() != null) donor.setLatitude(dto.getLat());
        if (dto.getLng() != null) donor.setLongitude(dto.getLng());
        if (dto.getLastDonated() != null) donor.setLastDonated(dto.getLastDonated());

        donor = donorRepository.save(donor);
        return toDto(donor);
    }

    private DonorResponseDto toDto(Donor donor) {
        return DonorResponseDto.builder()
                .id(donor.getId().toString())
                .name(donor.getUser().getName())
                .bloodGroup(donor.getBloodGroup())
                .city(donor.getCity())
                .phone(donor.getUser().getPhone())
                .isAvailable(donor.getIsAvailable() != null && donor.getIsAvailable())
                .latitude(donor.getLatitude() != null ? donor.getLatitude() : 0.0)
                .longitude(donor.getLongitude() != null ? donor.getLongitude() : 0.0)
                .lastDonated(donor.getLastDonated())
                .build();
    }
}
