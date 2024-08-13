package org.example.csis3275_finalexam_300385671.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.csis3275_finalexam_300385671.model.Reservation;
import org.example.csis3275_finalexam_300385671.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SeatReservationController {

    // Inject the ReservationService dependency
    @Autowired
    private ReservationService reservationService;

    // Handler method for the root URL ("/")
    @GetMapping("/")
    public String showReservationForm(Model model) throws Exception {
        // Fetch all reservations from the service
        List<Reservation> reservations = reservationService.findAll();
        // Calculate remaining seats
        int remainingSeats = 20 - reservations.size();

        // Initialize an array to hold the reservations for the table display
        Reservation[] reservationsArray = new Reservation[20];
        for (Reservation reservation : reservations) {
            String seatCode = reservation.getSeatCode();
            // Calculate the index based on seat code (e.g., 1A -> 0, 2B -> 6)
            int index = (seatCode.charAt(0) - '1') * 5 + (seatCode.charAt(1) - 'A');
            reservationsArray[index] = reservation;
        }

        // Configure ObjectMapper to handle Java 8 date/time types
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String reservationsJson = mapper.writeValueAsString(reservations);

        // Add attributes to the model for rendering in the view
        model.addAttribute("remainingSeats", remainingSeats);
        model.addAttribute("reservations", reservationsArray);
        model.addAttribute("reservationList", reservations);
        model.addAttribute("reservationsJson", reservationsJson);

        // Return the name of the view template (reservation.html)
        return "reservation";
    }

    // Handler method for the reserve endpoint ("/reserve")
    @PostMapping("/reserve")
    public String reserveSeat(@RequestParam String seatCode,
                              @RequestParam String customerName,
                              @RequestParam String transactionDate,
                              Model model) {
        // Validate the seat code format (case-insensitive)
        if (!seatCode.matches("(?i)^[1-4][A-E]$")) {
            model.addAttribute("error", "Invalid seat code. It should be in the format 1A, 2B, etc.");
            try {
                return showReservationForm(model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // Check if the seat is already taken
        List<Reservation> reservations = reservationService.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getSeatCode().equalsIgnoreCase(seatCode)) {
                model.addAttribute("error", "This seat has been taken.");
                try {
                    return showReservationForm(model);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Create a new reservation and set its attributes
        Reservation reservation = new Reservation();
        reservation.setSeatCode(seatCode.toUpperCase());
        reservation.setCustomerName(customerName);
        reservation.setTransactionDate(LocalDate.parse(transactionDate));

        // Save the reservation using the service
        reservationService.save(reservation);
        // Redirect to the root URL ("/")
        return "redirect:/";
    }

    // Handler method for deleting a reservation ("/delete/{id}")
    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id, Model model) {
        // Delete the reservation by id
        reservationService.delete(id);
        // Redirect to the root URL ("/")
        return "redirect:/";
    }

    // Handler method for displaying the edit reservation form ("/edit/{id}")
    @GetMapping("/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        // Find the reservation by id
        Reservation reservation = reservationService.findById(id);
        // Add the reservation to the model
        model.addAttribute("reservation", reservation);
        // Return the name of the view template (editReservation.html)
        return "editReservation";
    }

    // Handler method for updating a reservation ("/update")
    @PostMapping("/update")
    public String updateReservation(@RequestParam Long id,
                                    @RequestParam String seatCode,
                                    @RequestParam String customerName,
                                    @RequestParam String transactionDate,
                                    Model model) throws Exception {
        // Fetch all reservations from the service
        List<Reservation> reservations = reservationService.findAll();
        // Configure ObjectMapper to handle Java 8 date/time types
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String reservationsJson = mapper.writeValueAsString(reservations);
        model.addAttribute("reservationsJson", reservationsJson);

        // Validate the seat code format (case-insensitive)
        if (!seatCode.matches("(?i)^[1-4][A-E]$")) {
            model.addAttribute("error", "Please follow the seat code format.");
            return editReservation(id, model);
        }

        // Check if the seat is already reserved by another reservation
        for (Reservation reservation : reservations) {
            if (reservation.getSeatCode().equalsIgnoreCase(seatCode) && !reservation.getId().equals(id)) {
                model.addAttribute("error", "This seat has been taken.");
                return editReservation(id, model);
            }
        }

        // Find the reservation by id and update its attributes
        Reservation reservation = reservationService.findById(id);
        reservation.setSeatCode(seatCode.toUpperCase());
        reservation.setCustomerName(customerName);
        reservation.setTransactionDate(LocalDate.parse(transactionDate));

        // Save the updated reservation using the service
        reservationService.save(reservation);
        // Redirect to the root URL ("/")
        return "redirect:/";
    }
}
