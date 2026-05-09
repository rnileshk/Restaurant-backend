package com.restaurant.app.repository;

import com.restaurant.app.entity.TableBooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository
        extends JpaRepository<TableBooking, Long> {

    Optional<TableBooking> findByBookingCode(
            String bookingCode
    );

    List<TableBooking> findByCustomerEmail(
            String email
    );

    List<TableBooking> findByUserEmail(String email);

    List<TableBooking> findByBookingDate(String bookingDate);

    @Query("SELECT b FROM TableBooking b WHERE b.checkedIn = :checkedIn")
    List<TableBooking> findByCheckedIn(
            @Param("checkedIn") Boolean checkedIn
    );
}