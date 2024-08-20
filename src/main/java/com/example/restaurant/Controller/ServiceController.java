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

import com.example.restaurant.Model.RestaurantService;
import com.example.restaurant.Service.RestaurantServiceService;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private RestaurantServiceService service;

    @GetMapping
    public ResponseEntity<List<RestaurantService>> gettAllServices() {
        return new ResponseEntity<>(service.getAllServices(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantService> getService(@PathVariable Long id) {
        Optional<RestaurantService> optional = service.getServiceById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<RestaurantService> addService(@RequestBody RestaurantService Restservice) {
        RestaurantService newService = service.addService(Restservice);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        boolean isDeleted = service.deleteService(id);
        if (isDeleted) {
            return new ResponseEntity<>("Service removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Service not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("serviceDesc") String serviceDesc) {
        try {
            service.saveServiceImage(image, serviceName, serviceDesc);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }
}
