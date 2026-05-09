package com.restaurant.app.repository;

import com.restaurant.app.entity.MenuItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository
        extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategory(
            String category
    );

    List<MenuItem> findByAvailable(
            Boolean available
    );

    List<MenuItem> findByNameContainingIgnoreCase(
            String keyword
    );
}