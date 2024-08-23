package com.example.restaurant.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Gallery;
import com.example.restaurant.Service.GalleryService;

@RestController
@RequestMapping("/gallery")
public class GalleryController {
    @Autowired
    private GalleryService galleryService;

    @GetMapping
    public ResponseEntity<List<Gallery>> gettAllItems() {
        return new ResponseEntity<>(galleryService.getAllItems(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gallery> getItem(@PathVariable Long id) {
        Optional<Gallery> galllery = galleryService.getItemById(id);
        return galllery.map(g -> new ResponseEntity<>(g, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Gallery> addItem(@RequestBody Gallery gallery) {
        Gallery newGallery = galleryService.addItem(gallery);
        return new ResponseEntity<>(newGallery, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        Optional<Gallery> gallery = galleryService.deleteItem(id);
        if (gallery.isPresent()) {
            return new ResponseEntity<>("Gallery Item removed sucessfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Gallery item not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image_data") MultipartFile image_data,
            @RequestParam("image_name") String image_name) {
        try {
            galleryService.saveImageItem(image_data, image_name);
            return ResponseEntity.ok("item uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add item: " + e.getMessage());
        }
    }

}
