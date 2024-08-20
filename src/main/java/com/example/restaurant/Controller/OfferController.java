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
            service.saveOfferImage(image, offerName, offerDesc, discount, validPeriod);
            return ResponseEntity.ok("Offer image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }
}
