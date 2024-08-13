package org.example.csis3275_finalexam_300385671.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.example.csis3275_finalexam_300385671.model.Reservation;
import org.example.csis3275_finalexam_300385671.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Reservation reservation1 = new Reservation(1L, "1A", "John Doe", LocalDate.now());
        Reservation reservation2 = new Reservation(2L, "1B", "Jane Doe", LocalDate.now());
        List<Reservation> reservationList = Arrays.asList(reservation1, reservation2);

        when(reservationRepository.findAll()).thenReturn(reservationList);

        List<Reservation> result = reservationService.findAll();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getCustomerName());
        assertEquals("Jane Doe", result.get(1).getCustomerName());
    }

    @Test
    void testFindById() {
        Reservation reservation = new Reservation(1L, "1A", "John Doe", LocalDate.now());

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.findById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getCustomerName());
    }

    @Test
    void testSave() {
        Reservation reservation = new Reservation(1L, "1A", "John Doe", LocalDate.now());

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.save(reservation);

        assertNotNull(result);
        assertEquals("John Doe", result.getCustomerName());
    }

    @Test
    void testDelete() {
        Long reservationId = 1L;

        doNothing().when(reservationRepository).deleteById(reservationId);

        reservationService.delete(reservationId);

        verify(reservationRepository, times(1)).deleteById(reservationId);
    }
}
