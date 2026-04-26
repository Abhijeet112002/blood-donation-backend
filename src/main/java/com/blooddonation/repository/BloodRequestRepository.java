package com.blooddonation.repository;

import com.blooddonation.entity.BloodRequest;
import com.blooddonation.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, UUID> {

    List<BloodRequest> findByRequesterIdOrderByCreatedAtDesc(UUID requesterId);

    List<BloodRequest> findByStatus(RequestStatus status);

    long countByStatus(RequestStatus status);

    long countByRequesterId(UUID requesterId);

    @Query("SELECT COUNT(b) FROM BloodRequest b WHERE b.status = 'FULFILLED' AND CAST(b.updatedAt AS date) = CURRENT_DATE")
    long countFulfilledToday();

    @Query(value = "SELECT * FROM blood_requests br WHERE br.status = 'PENDING' AND " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(br.latitude)) * " +
            "cos(radians(br.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(br.latitude)))) <= 50 " +
            "ORDER BY br.created_at DESC",
            nativeQuery = true)
    List<BloodRequest> findNearby(@Param("lat") double lat, @Param("lng") double lng);
}
