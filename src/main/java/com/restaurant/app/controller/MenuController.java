package com.restaurant.app.controller;

import com.restaurant.app.entity.MenuItem;
import com.restaurant.app.repository.MenuRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin("*")
public class MenuController {

    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public List<MenuItem> getAllMenu() {
        return menuRepository.findAll();
    }

    @PostMapping
    public MenuItem addMenu(@RequestBody MenuItem item) {
        return menuRepository.save(item);
    }

    @PutMapping("/{id}")
    public MenuItem updateMenu(
            @PathVariable Long id,
            @RequestBody MenuItem updated
    ) {

        MenuItem item = menuRepository.findById(id)
                .orElseThrow();

        item.setName(updated.getName());
        item.setDescription(updated.getDescription());
        item.setPrice(updated.getPrice());
        item.setImage(updated.getImage());
        item.setAvailable(updated.getAvailable());

        return menuRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public String deleteMenu(@PathVariable Long id) {

        menuRepository.deleteById(id);

        return "Menu deleted";
    }
}