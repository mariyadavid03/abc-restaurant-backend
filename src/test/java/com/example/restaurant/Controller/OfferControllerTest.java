package com.example.restaurant.Controller;

import com.example.restaurant.Model.Offer;
import com.example.restaurant.Service.OfferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Offer offer;

    @BeforeEach
    public void setup() {
        offer = new Offer(1L, "Summer Sale", "Discount on summer items", "20%", "2024-09-30", new byte[0]);
    }

    @Test
    public void testGetAllOffers() throws Exception {
        List<Offer> offers = new ArrayList<>();
        offers.add(offer);
        when(offerService.getAllOffers()).thenReturn(offers);

        mockMvc.perform(get("/offer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].offer_name").value("Summer Sale"));
    }

    @Test
    public void testGetOffer() throws Exception {
        when(offerService.getOfferById(anyLong())).thenReturn(Optional.of(offer));

        mockMvc.perform(get("/offer/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offer_name").value("Summer Sale"));
    }

    @Test
    public void testGetOfferNotFound() throws Exception {
        when(offerService.getOfferById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/offer/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUploadGalleryImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        doNothing().when(offerService).saveOffer(any(MultipartFile.class), anyString(), anyString(), anyString(),
                anyString());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/offer/upload")
                .file(image)
                .param("offerName", "Summer Sale")
                .param("offerDesc", "Discount on summer items")
                .param("discount", "20%")
                .param("validPeriod", "2024-09-30"))
                .andExpect(status().isOk())
                .andExpect(content().string("Offer image uploaded successfully"));
    }

    @Test
    public void testUploadGalleryImageFailure() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        doThrow(new RuntimeException("Upload failed")).when(offerService).saveOffer(any(MultipartFile.class),
                anyString(), anyString(), anyString(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/offer/upload")
                .file(image)
                .param("offerName", "Summer Sale")
                .param("offerDesc", "Discount on summer items")
                .param("discount", "20%")
                .param("validPeriod", "2024-09-30"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to upload image: Upload failed"));
    }

    @Test
    public void testUpdateOffer() throws Exception {
        Offer updatedOffer = new Offer(1L, "Updated Sale", "Updated description", "25%", "2024-12-31", new byte[0]);
        when(offerService.updateOffer(anyLong(), any(MultipartFile.class), anyString(), anyString(), anyString(),
                anyString())).thenReturn(updatedOffer);

        MockMultipartFile image = new MockMultipartFile("image", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/offer/update/{id}", 1L)
                .file(image)
                .param("offerName", "Updated Sale")
                .param("offerDesc", "Updated description")
                .param("discount", "25%")
                .param("validPeriod", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offer_name").value("Updated Sale"));
    }

    @Test
    public void testUpdateOfferNotFound() throws Exception {
        when(offerService.updateOffer(anyLong(), any(MultipartFile.class), anyString(), anyString(), anyString(),
                anyString())).thenThrow(new RuntimeException("Offer not found"));

        MockMultipartFile image = new MockMultipartFile("image", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/offer/update/{id}", 1L)
                .file(image)
                .param("offerName", "Updated Sale")
                .param("offerDesc", "Updated description")
                .param("discount", "25%")
                .param("validPeriod", "2024-12-31"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Offer not found"));
    }

    @Test
    public void testDeleteOffer() throws Exception {
        when(offerService.deleteOffer(anyLong())).thenReturn(Optional.of(offer));

        mockMvc.perform(delete("/offer/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Offer removed sucessfully"));
    }

    @Test
    public void testDeleteOfferNotFound() throws Exception {
        when(offerService.deleteOffer(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/offer/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Offer not found"));
    }

    @Test
    public void testGetImage() throws Exception {
        byte[] imageData = new byte[] { 1, 2, 3, 4 };
        when(offerService.getImageById(anyLong())).thenReturn(imageData);

        mockMvc.perform(get("/offer/image/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(imageData));
    }

    @Test
    public void testGetImageNotFound() throws Exception {
        when(offerService.getImageById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/offer/image/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
