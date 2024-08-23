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

import com.example.restaurant.Model.Offer;
import com.example.restaurant.Service.OfferService;

@RestController
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private OfferService service;

    @GetMapping
    public ResponseEntity<List<Offer>> gettAllOffers() {
        return new ResponseEntity<>(service.getAllOffers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOffer(@PathVariable Long id) {
        Optional<Offer> optional = service.getOfferById(id);
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
    public ResponseEntity<Offer> addOffer(@RequestBody Offer offer) {
        Offer newOffer = service.addOffer(offer);
        return new ResponseEntity<>(newOffer, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
        Optional<Offer> optional = service.deleteOffer(id);
        if (optional.isPresent()) {
            return new ResponseEntity<>("Offer removed sucessfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadGalleryImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("offerName") String offerName,
            @RequestParam("offerDesc") String offerDesc,
            @RequestParam("discount") String discount,
            @RequestParam("validPeriod") String validPeriod) {
        try {
            service.saveOffer(image, offerName, offerDesc, discount, validPeriod);
            return ResponseEntity.ok("Offer image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOffer(
            @PathVariable Long id,
            @RequestParam("offerName") String offerName,
            @RequestParam("offerDesc") String offerDesc,
            @RequestParam("discount") String discount,
            @RequestParam("validPeriod") String validPeriod,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Offer updatedOffer = service.updateOffer(id, image, offerName, offerDesc, discount, validPeriod);
            return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error updating offer image", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
