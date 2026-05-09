package com.restaurant.app.controller;

import com.restaurant.app.service.CloudinaryService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("*")
public class UploadController {

    private final CloudinaryService cloudinaryService;

    public UploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/image")
    public String uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        return cloudinaryService.uploadImage(file);
    }
}