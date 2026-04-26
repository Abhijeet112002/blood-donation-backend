package com.blooddonation.repository;

import com.blooddonation.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonorRepository extends JpaRepository<Donor, UUID> {

    Optional<Donor> findByUserId(UUID userId);

    List<Donor> findByBloodGroupAndCityContainingIgnoreCase(String bloodGroup, String city);

    List<Donor> findByBloodGroupIgnoreCase(String bloodGroup);

    List<Donor> findByCityContainingIgnoreCase(String city);

    List<Donor> findByIsAvailableTrue();

    @Query(value = "SELECT * FROM donors d WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(d.latitude)) * " +
            "cos(radians(d.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(d.latitude)))) <= :radius",
            nativeQuery = true)
    List<Donor> findNearby(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") int radius);

    @Query("SELECT d.bloodGroup, COUNT(d), SUM(CASE WHEN d.isAvailable = true THEN 1 ELSE 0 END) " +
            "FROM Donor d GROUP BY d.bloodGroup")
    List<Object[]> getBloodGroupSummary();

    long countByUserId(UUID userId);
}
