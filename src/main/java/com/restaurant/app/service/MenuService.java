package com.restaurant.app.service;

import com.restaurant.app.entity.MenuItem;

import com.restaurant.app.repository.MenuRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(
            MenuRepository menuRepository
    ) {
        this.menuRepository = menuRepository;
    }

    public List<MenuItem> getAllMenu() {
        return menuRepository.findAll();
    }

    public MenuItem addMenu(
            MenuItem item
    ) {
        return menuRepository.save(item);
    }

    public MenuItem updateMenu(
            Long id,
            MenuItem updated
    ) {

        MenuItem item =
                menuRepository.findById(id)
                        .orElseThrow();

        item.setName(updated.getName());
        item.setDescription(updated.getDescription());
        item.setPrice(updated.getPrice());
        item.setImage(updated.getImage());
        item.setAvailable(updated.getAvailable());

        return menuRepository.save(item);
    }

    public void deleteMenu(
            Long id
    ) {
        menuRepository.deleteById(id);
    }
}