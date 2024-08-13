package org.example.csis3275_finalexam_300385671.controller;

import org.example.csis3275_finalexam_300385671.model.Reservation;
import org.example.csis3275_finalexam_300385671.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SeatReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private Model model;

    @InjectMocks
    private SeatReservationController seatReservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowReservationForm() throws Exception {
        // Given
        Reservation reservation1 = new Reservation(1L, "1A", "John Doe", LocalDate.now());
        Reservation reservation2 = new Reservation(2L, "1B", "Jane Doe", LocalDate.now());
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.findAll()).thenReturn(reservations);

        // When
        String viewName = seatReservationController.showReservationForm(model);

        // Then
        assertEquals("reservation", viewName);
        verify(model, times(1)).addAttribute(eq("remainingSeats"), anyInt());
        verify(model, times(1)).addAttribute(eq("reservations"), any());
        verify(model, times(1)).addAttribute(eq("reservationList"), eq(reservations));
        verify(model, times(1)).addAttribute(eq("reservationsJson"), anyString());
    }

    @Test
    void testReserveSeat() throws Exception {
        // Given
        String seatCode = "1A";
        String customerName = "John Doe";
        String transactionDate = LocalDate.now().toString();
        Reservation reservation = new Reservation(1L, seatCode, customerName, LocalDate.parse(transactionDate));
        List<Reservation> reservations = Arrays.asList(reservation);

        when(reservationService.findAll()).thenReturn(reservations);

        // When
        String viewName = seatReservationController.reserveSeat(seatCode, customerName, transactionDate, model);

        // Then
        assertEquals("redirect:/", viewName);
        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void testDeleteReservation() {
        // Given
        Long id = 1L;

        // When
        String viewName = seatReservationController.deleteReservation(id, model);

        // Then
        assertEquals("redirect:/", viewName);
        verify(reservationService, times(1)).delete(id);
    }

    @Test
    void testEditReservation() {
        // Given
        Long id = 1L;
        Reservation reservation = new Reservation(id, "1A", "John Doe", LocalDate.now());

        when(reservationService.findById(id)).thenReturn(reservation);

        // When
        String viewName = seatReservationController.editReservation(id, model);

        // Then
        assertEquals("editReservation", viewName);
        verify(model, times(1)).addAttribute("reservation", reservation);
    }

    @Test
    void testUpdateReservation() throws Exception {
        // Given
        Long id = 1L;
        String seatCode = "1A";
        String customerName = "John Doe";
        String transactionDate = LocalDate.now().toString();
        Reservation reservation = new Reservation(id, seatCode, customerName, LocalDate.parse(transactionDate));
        List<Reservation> reservations = Arrays.asList(reservation);

        when(reservationService.findById(id)).thenReturn(reservation);
        when(reservationService.findAll()).thenReturn(reservations);

        // When
        String viewName = seatReservationController.updateReservation(id, seatCode, customerName, transactionDate, model);

        // Then
        assertEquals("redirect:/", viewName);
        verify(reservationService, times(1)).save(any(Reservation.class));
    }
}
