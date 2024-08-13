package org.example.csis3275_finalexam_300385671.controller;

import org.example.csis3275_finalexam_300385671.model.Reservation;
import org.example.csis3275_finalexam_300385671.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@Controller
public class SeatReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String showReservationForm(Model model) {
        List<Reservation> reservations = reservationService.findAll();
        int remainingSeats = 20 - reservations.size();

        // Initialize reservations array for the table
        Reservation[] reservationsArray = new Reservation[20];
        for (Reservation reservation : reservations) {
            String seatCode = reservation.getSeatCode();
            int index = (seatCode.charAt(0) - '1') * 5 + (seatCode.charAt(1) - 'A');
            reservationsArray[index] = reservation;
        }

        model.addAttribute("remainingSeats", remainingSeats);
        model.addAttribute("reservations", reservationsArray);
        model.addAttribute("reservationList", reservations);
        return "reservation";
    }

    @PostMapping("/reserve")
    public String reserveSeat(@RequestParam String seatCode,
                              @RequestParam String customerName,
                              @RequestParam String transactionDate,
                              Model model) {
        // Validate the seat code
        if (!seatCode.matches("^[1-4][A-E]$")) {
            model.addAttribute("error", "Invalid seat code. It should be in the format 1A, 2B, etc.");
            return showReservationForm(model);
        }

        Reservation reservation = new Reservation();
        reservation.setSeatCode(seatCode);
        reservation.setCustomerName(customerName);
        reservation.setTransactionDate(LocalDate.parse(transactionDate));

        reservationService.save(reservation);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id, Model model) {
        reservationService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id);
        model.addAttribute("reservation", reservation);
        return "editReservation";
    }

    @PostMapping("/update")
    public String updateReservation(@RequestParam Long id,
                                    @RequestParam String seatCode,
                                    @RequestParam String customerName,
                                    @RequestParam String transactionDate) {
        Reservation reservation = reservationService.findById(id);
        reservation.setSeatCode(seatCode);
        reservation.setCustomerName(customerName);
        reservation.setTransactionDate(LocalDate.parse(transactionDate));

        reservationService.save(reservation);
        return "redirect:/";
    }
}
