package com.example.restaurant.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Menu;
import com.example.restaurant.Repository.MenuRepository;

public class MenuServiceTest {
    @Mock
    private MenuRepository repository;

    @InjectMocks
    private MenuService menuService;

    private Menu mockMenu;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMenu = new Menu();
        mockMenu.setId(1L);
        mockMenu.setItem_name("Item Name");
        mockMenu.setItem_desc("Item Description");
        mockMenu.setPrice(new BigDecimal("10.00"));
        mockMenu.setType("Main Course");
    }

    @Test
    public void testAddItem_Success() {
        when(repository.save(mockMenu)).thenReturn(mockMenu);

        Menu result = menuService.addItem(mockMenu);

        verify(repository, times(1)).save(mockMenu);
        assertTrue(result.getId().equals(mockMenu.getId()));
        System.out.println("Add Item test passed");
    }

    @Test
    public void testDeleteItem_Success() {
        Long id = 1L;

        when(repository.existsById(id)).thenReturn(true);

        boolean result = menuService.deleteItem(id);

        verify(repository, times(1)).deleteById(id);
        assertTrue(result);
        System.out.println("Delete Item test passed");
    }

    @Test
    public void testUpdateItem_Success() throws IOException {
        Long id = 1L;
        Menu existingMenu = new Menu();
        existingMenu.setId(id);
        existingMenu.setItem_name("Old Item");
        existingMenu.setItem_desc("Old Description");
        existingMenu.setPrice(new BigDecimal("5.00"));

        Menu updatedMenuDetails = new Menu();
        updatedMenuDetails.setItem_name("Updated Item");
        updatedMenuDetails.setItem_desc("Updated Description");
        updatedMenuDetails.setPrice(new BigDecimal("15.00"));

        MultipartFile mockImage = new MockMultipartFile("file", "updated.png", "image/png",
                "updated image data".getBytes());

        when(repository.findById(id)).thenReturn(Optional.of(existingMenu));
        when(repository.save(existingMenu)).thenReturn(existingMenu);

        Menu result = menuService.updateItem(id, updatedMenuDetails, mockImage);

        verify(repository, times(1)).save(existingMenu);
        assertTrue(result.getItem_name().equals(updatedMenuDetails.getItem_name()));
        assertTrue(result.getItem_desc().equals(updatedMenuDetails.getItem_desc()));
        assertTrue(result.getPrice().equals(updatedMenuDetails.getPrice()));
        assertTrue(result.getItem_image_data().length > 0); // Ensure image data was updated
        System.out.println("Update Item test passed");
    }
}
