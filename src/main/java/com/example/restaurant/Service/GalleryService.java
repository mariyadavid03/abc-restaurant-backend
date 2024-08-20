package com.example.restaurant.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Gallery;
import com.example.restaurant.Repository.GalleryRepository;

@Service
public class GalleryService {
    @Autowired
    private GalleryRepository galleryRepository;

    public List<Gallery> getAllItems() {
        return galleryRepository.findAll();
    }

    public Optional<Gallery> getItemById(Long id) {
        return galleryRepository.findById(id);
    }

    public Gallery addItem(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    public Optional<Gallery> deleteItem(Long id) {
        Optional<Gallery> gOptional = galleryRepository.findById(id);
        if (gOptional.isPresent()) {
            galleryRepository.deleteById(id);
        }
        return gOptional;
    }

    public void saveImage(MultipartFile image) throws IOException {
        Gallery gallery = new Gallery();
        gallery.setImage_name(image.getOriginalFilename());
        gallery.setImage_data(image.getBytes());
        galleryRepository.save(gallery);
    }
}
