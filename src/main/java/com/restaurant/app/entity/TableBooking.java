package com.restaurant.app.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "table_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingCode;

    /*
     =====================================
     BOOKING OWNER
     =====================================
    */

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private Integer totalPersons;

    private String bookingDate;

    private String bookingTime;

    private String specialRequest;

    private Boolean checkedIn;

    @Column(length = 2000)
    private String qrCode;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();

        if (checkedIn == null) {
            checkedIn = false;
        }
    }
}