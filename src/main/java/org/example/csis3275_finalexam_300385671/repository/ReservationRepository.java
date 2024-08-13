package org.example.csis3275_finalexam_300385671.repository;

import org.example.csis3275_finalexam_300385671.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Additional query methods if needed
}
