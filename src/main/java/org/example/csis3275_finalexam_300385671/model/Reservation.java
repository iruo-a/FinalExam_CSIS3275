package org.example.csis3275_finalexam_300385671.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Reservation {

    // Primary key for the Reservation entity
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Seat code for the reservation (e.g., "1A", "2B")
    private String seatCode;

    // Name of the customer who made the reservation
    private String customerName;

    // Date of the transaction
    private LocalDate transactionDate;

    // Default constructor
    public Reservation(){}

    // Getters and Setters for the fields

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for seatCode
    public String getSeatCode() {
        return seatCode;
    }

    // Setter for seatCode
    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    // Getter for transactionDate
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    // Setter for transactionDate
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Getter for customerName
    public String getCustomerName() {
        return customerName;
    }

    // Setter for customerName
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Constructor with parameters
    public Reservation(Long id, String seatCode, String customerName, LocalDate transactionDate) {
        this.id = id;
        this.seatCode = seatCode;
        this.customerName = customerName;
        this.transactionDate = transactionDate;
    }
}
