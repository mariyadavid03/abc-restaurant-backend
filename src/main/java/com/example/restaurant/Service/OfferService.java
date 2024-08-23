package com.example.restaurant.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Offer;
import com.example.restaurant.Repository.OfferRepository;

@Service
public class OfferService {
    @Autowired
    private OfferRepository repository;

    public List<Offer> getAllOffers() {
        return repository.findAll();
    }

    public Optional<Offer> getOfferById(Long id) {
        return repository.findById(id);
    }

    public Offer addOffer(Offer offer) {
        return repository.save(offer);
    }

    public Optional<Offer> deleteOffer(Long id) {
        Optional<Offer> optional = repository.findById(id);
        if (optional.isPresent()) {
            repository.deleteById(id);
        }
        return optional;
    }

    public void saveOffer(MultipartFile image, String offerName, String offerDesc, String discount,
            String validPeriod) throws IOException {
        Offer offer = new Offer();
        offer.setOffer_name(offerName);
        offer.setOffer_desc(offerDesc);
        offer.setDiscount(discount);
        offer.setValid_period(validPeriod);
        offer.setOffer_image_data(image.getBytes());
        repository.save(offer);
    }

    public byte[] getImageById(Long id) {
        return repository.findImageById(id);
    }

    public Offer updateOffer(Long id, MultipartFile image, String offerName, String offerDesc, String discount,
            String validPeriod) throws IOException {
        Optional<Offer> optionalOffer = repository.findById(id);

        if (optionalOffer.isPresent()) {
            Offer offer = optionalOffer.get();
            offer.setOffer_name(offerName);
            offer.setOffer_desc(offerDesc);
            offer.setDiscount(discount);
            offer.setValid_period(validPeriod);

            if (image != null && !image.isEmpty()) {
                offer.setOffer_image_data(image.getBytes());
            }

            return repository.save(offer);
        } else {
            throw new RuntimeException("Offer with id " + id + " not found");
        }
    }

}
