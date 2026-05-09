package com.restaurant.app.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Email(message = "Enter valid email")
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @NotBlank(message = "Phone is required")
    private String customerPhone;

    @Min(value = 1, message = "At least 1 person required")
    private Integer totalPersons;

    @NotBlank(message = "Booking date is required")
    private String bookingDate;

    @NotBlank(message = "Booking time is required")
    private String bookingTime;

    private String specialRequest;
}