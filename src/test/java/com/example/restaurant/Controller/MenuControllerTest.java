package com.example.restaurant.Controller;

import com.example.restaurant.Model.Menu;
import com.example.restaurant.Service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private Menu menu;

    @BeforeEach
    public void setup() {
        menu = new Menu(1L, "Burger", "Delicious beef burger", BigDecimal.valueOf(9.99), "Food", new byte[0]);
    }

    @Test
    public void testGetAllItems() throws Exception {
        when(menuService.getAllItems()).thenReturn(new ArrayList<Menu>() {
            {
                add(menu);
            }
        });

        mockMvc.perform(get("/menu")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item_name").value("Burger"));
    }

    @Test
    public void testGetItem() throws Exception {
        when(menuService.getItemById(anyLong())).thenReturn(Optional.of(menu));

        mockMvc.perform(get("/menu/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_name").value("Burger"));
    }

    @Test
    public void testGetItemNotFound() throws Exception {
        when(menuService.getItemById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/menu/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddItem() throws Exception {
        when(menuService.addItem(any(Menu.class))).thenReturn(menu);

        mockMvc.perform(post("/menu/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.item_name").value("Burger"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        when(menuService.deleteItem(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/menu/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Item removed successfully"));
    }

    @Test
    public void testDeleteItemNotFound() throws Exception {
        when(menuService.deleteItem(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/menu/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Item not found"));
    }

    @Test
    public void testUploadImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        doNothing().when(menuService).saveImage(any(MultipartFile.class), any(String.class), any(String.class),
                any(BigDecimal.class), any(String.class));

        mockMvc.perform(multipart("/menu/upload")
                .file(image)
                .param("itemName", "Burger")
                .param("itemDesc", "Delicious beef burger")
                .param("price", "9.99")
                .param("type", "Food"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item added successfully"));
    }

    @Test
    public void testUploadImageFailure() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        doThrow(new RuntimeException("Upload failed")).when(menuService).saveImage(any(MultipartFile.class),
                any(String.class), any(String.class), any(BigDecimal.class), any(String.class));

        mockMvc.perform(multipart("/menu/upload")
                .file(image)
                .param("itemName", "Burger")
                .param("itemDesc", "Delicious beef burger")
                .param("price", "9.99")
                .param("type", "Food"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to add item: Upload failed"));
    }

    @Test
    public void testGetItemsByType() throws Exception {
        when(menuService.getItemsByType(any(String.class))).thenReturn(new ArrayList<Menu>() {
            {
                add(menu);
            }
        });

        mockMvc.perform(get("/menu/type/{type}", "Food")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item_name").value("Burger"));
    }

    @Test
    public void testGetImage() throws Exception {
        byte[] imageData = new byte[] { 1, 2, 3, 4 };
        when(menuService.getImageById(anyLong())).thenReturn(imageData);

        mockMvc.perform(get("/menu/image/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(imageData));
    }

    @Test
    public void testGetImageNotFound() throws Exception {
        when(menuService.getImageById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/menu/image/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateMenuItem() throws Exception {
        Menu updatedMenu = new Menu(1L, "Updated Burger", "Updated description", BigDecimal.valueOf(10.99), "Food",
                new byte[0]);
        when(menuService.updateItem(anyLong(), any(Menu.class), any(MultipartFile.class))).thenReturn(updatedMenu);

        mockMvc.perform(
                ((MockMultipartHttpServletRequestBuilder) MockMvcRequestBuilders.multipart("/menu/update/{id}", 1L)
                        .param("itemName", "Updated Burger")
                        .param("itemDesc", "Updated description")
                        .param("price", "10.99"))
                        .file(new MockMultipartFile("file", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_name").value("Updated Burger"));
    }

    @Test
    public void testUpdateMenuItemNotFound() throws Exception {
        when(menuService.updateItem(anyLong(), any(Menu.class), any(MultipartFile.class))).thenReturn(null);

        mockMvc.perform(put("/menu/update/{id}", 1L)
                .param("itemName", "Updated Burger")
                .param("itemDesc", "Updated description")
                .param("price", "10.99"))
                .andExpect(status().isNotFound());
    }
}