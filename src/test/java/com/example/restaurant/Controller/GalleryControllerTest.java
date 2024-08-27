package com.example.restaurant.Controller;

import com.example.restaurant.Model.Gallery;
import com.example.restaurant.Service.GalleryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GalleryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GalleryService galleryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Gallery gallery;

    @BeforeEach
    public void setup() {
        gallery = new Gallery(1L, "testImage.jpg", new byte[0]);
    }

    @Test
    public void testGetAllItems() throws Exception {
        when(galleryService.getAllItems()).thenReturn(new ArrayList<Gallery>() {
            {
                add(gallery);
            }
        });

        mockMvc.perform(get("/gallery")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].image_name").value("testImage.jpg"));
    }

    @Test
    public void testGetItem() throws Exception {
        when(galleryService.getItemById(anyLong())).thenReturn(Optional.of(gallery));

        mockMvc.perform(get("/gallery/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image_name").value("testImage.jpg"));
    }

    @Test
    public void testGetItemNotFound() throws Exception {
        when(galleryService.getItemById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/gallery/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddItem() throws Exception {
        when(galleryService.addItem(any(Gallery.class))).thenReturn(gallery);

        mockMvc.perform(post("/gallery/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gallery)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.image_name").value("testImage.jpg"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        when(galleryService.deleteItem(anyLong())).thenReturn(Optional.of(gallery));

        mockMvc.perform(delete("/gallery/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Gallery Item removed sucessfully"));
    }

    @Test
    public void testDeleteItemNotFound() throws Exception {
        when(galleryService.deleteItem(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/gallery/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Gallery item not found"));
    }

    @Test
    public void testUploadImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image_data", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        doNothing().when(galleryService).saveImageItem(any(MockMultipartFile.class), any(String.class));

        mockMvc.perform(multipart("/gallery/upload")
                .file(image)
                .param("image_name", "testImage.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string("item uploaded successfully"));
    }

    @Test
    public void testUploadImageFailure() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image_data", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        doThrow(new RuntimeException("Upload failed")).when(galleryService).saveImageItem(any(MockMultipartFile.class),
                any(String.class));

        mockMvc.perform(multipart("/gallery/upload")
                .file(image)
                .param("image_name", "testImage.jpg"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to add item: Upload failed"));
    }
}
