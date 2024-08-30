package com.example.restaurant.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Facility;
import com.example.restaurant.Repository.FacilityRepository;

@Service
public class FacilityService {
    @Autowired
    private FacilityRepository repository;

    public List<Facility> getAllServices() {
        return repository.findAll();
    }

    public Optional<Facility> getServiceById(Long id) {
        return repository.findById(id);
    }

    public Facility addService(Facility service) {
        return repository.save(service);
    }

    public boolean deleteService(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public byte[] getImageById(Long id) {
        return repository.findImageById(id);
    }

    public void saveServiceImage(MultipartFile image, String serviceName, String serviceDesc) throws IOException {
        Facility service = new Facility();
        service.setService_name(serviceName);
        service.setService_desc(serviceDesc);
        service.setService_image_data(image.getBytes());

        repository.save(service);
    }

    public Facility updateService(Long id, MultipartFile image, String serviceName, String serviceDesc)
            throws IOException {
        Optional<Facility> optional = repository.findById(id);

        if (optional.isPresent()) {
            Facility service = optional.get();
            service.setService_name(serviceName);
            service.setService_desc(serviceDesc);

            if (image != null && !image.isEmpty()) {
                service.setService_image_data(image.getBytes());
            }

            return repository.save(service);
        } else {
            throw new RuntimeException("Service with id " + id + " not found");
        }
    }
}
