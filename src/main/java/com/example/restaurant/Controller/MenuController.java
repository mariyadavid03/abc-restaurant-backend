package com.example.restaurant.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Menu;
import com.example.restaurant.Service.MenuService;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService service;

    @GetMapping
    public ResponseEntity<List<Menu>> gettAllItems() {
        return new ResponseEntity<>(service.getAllItems(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getItem(@PathVariable Long id) {
        Optional<Menu> optional = service.getItemById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Menu> addItem(@RequestBody Menu menu) {
        Menu newItem = service.addItem(menu);
        return new ResponseEntity<>(newItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        boolean isDeleted = service.deleteItem(id);
        if (isDeleted) {
            return new ResponseEntity<>("Item removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("itemName") String itemName,
            @RequestParam("itemDesc") String itemDesc,
            @RequestParam("price") BigDecimal price,
            @RequestParam("type") String type) {
        try {
            service.saveImage(image, itemName, itemDesc, price, type);
            return ResponseEntity.ok("Item added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add item: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Menu>> getItemsByType(@PathVariable String type) {
        List<Menu> items = service.getItemsByType(type);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        System.out.println("Fetching image with ID: " + id);
        byte[] imageData = service.getImageById(id);
        if (imageData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMenuItem(
            @PathVariable("id") Long id,
            @RequestParam(value = "itemName", required = false) String itemName,
            @RequestParam(value = "itemDesc", required = false) String itemDesc,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            Menu menuDetails = new Menu();
            menuDetails.setItem_name(itemName);
            menuDetails.setItem_desc(itemDesc);
            menuDetails.setPrice(price);

            Menu updatedItem = service.updateItem(id, menuDetails, file);
            if (updatedItem != null) {
                return new ResponseEntity<>(updatedItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating item");
        }
    }
}
