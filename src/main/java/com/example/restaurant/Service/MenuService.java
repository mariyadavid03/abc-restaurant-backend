package com.example.restaurant.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Menu;
import com.example.restaurant.Repository.MenuRepository;

@Service
public class MenuService {
    @Autowired
    private MenuRepository repository;

    public List<Menu> getAllItems() {
        return repository.findAll();
    }

    public Optional<Menu> getItemById(Long id) {
        return repository.findById(id);
    }

    public Menu addItem(Menu menu) {
        return repository.save(menu);
    }

    public boolean deleteItem(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public void saveImage(MultipartFile image, String itemName, String itemDesc, BigDecimal price, String type)
            throws IOException {
        Menu menu = new Menu();
        menu.setItem_name(itemName);
        menu.setItem_desc(itemDesc);
        menu.setPrice(price);
        menu.setType(type);
        menu.setItem_image_data(image.getBytes());
        repository.save(menu);
    }

    public List<Menu> getItemsByType(String type) {
        return repository.findByType(type);
    }

    public byte[] getImageById(Long id) {
        return repository.findImageById(id);
    }

    public Menu updateItem(Long id, Menu menuDetails, MultipartFile image) throws IOException {
        Optional<Menu> optionalMenu = repository.findById(id);
        if (optionalMenu.isPresent()) {
            Menu menu = optionalMenu.get();
            menu.setItem_name(menuDetails.getItem_name());
            menu.setItem_desc(menuDetails.getItem_desc());
            menu.setPrice(menuDetails.getPrice());

            // Handle image data
            if (image != null && !image.isEmpty()) {
                menu.setItem_image_data(image.getBytes());
            }

            // Save the updated menu item
            return repository.save(menu);
        } else {
            // Menu item not found
            return null;
        }
    }
}
