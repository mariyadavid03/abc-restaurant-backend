package com.example.restaurant.Controller;

import java.io.IOException;
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

import com.example.restaurant.Model.Facility;
import com.example.restaurant.Service.FacilityService;

@RestController
@RequestMapping("/service")
public class FacilityController {
    @Autowired
    private FacilityService service;

    @GetMapping
    public ResponseEntity<List<Facility>> gettAllServices() {
        return new ResponseEntity<>(service.getAllServices(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getService(@PathVariable Long id) {
        Optional<Facility> optional = service.getServiceById(id);
        return optional.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

    @PostMapping("/add")
    public ResponseEntity<Facility> addService(@RequestBody Facility Restservice) {
        Facility newService = service.addService(Restservice);
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

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateService(
            @PathVariable Long id,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("serviceDesc") String serviceDesc,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Facility updatedService = service.updateService(id, image, serviceName, serviceDesc);
            return new ResponseEntity<>(updatedService, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error updating", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
