package com.example.restaurant.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.example.restaurant.Model.Offer;
import com.example.restaurant.Repository.OfferRepository;

public class OfferServiceTest {
    @Mock
    private OfferRepository repository;

    @InjectMocks
    private OfferService offerService;

    private Offer mockOffer;
    private MultipartFile mockImage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockOffer = new Offer();
        mockOffer.setId(1L);
        mockOffer.setOffer_name("Sample Offer");
        mockOffer.setOffer_desc("20% off on all items");
        mockOffer.setDiscount("20%");
        mockOffer.setValid_period("2024-12-31");

        mockImage = mock(MultipartFile.class);
        try {
            when(mockImage.getBytes()).thenReturn("image data".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveOffer_Success() throws IOException {
        when(repository.save(any(Offer.class))).thenReturn(mockOffer);

        offerService.saveOffer(mockImage, "Sample Offer", "20% off on all items", "20%", "2024-12-31");

        verify(repository, times(1)).save(any(Offer.class));
        System.out.println("Save Offer Success test passed");
    }

    @Test
    public void testUpdateOffer_Success() throws IOException {
        when(repository.findById(1L)).thenReturn(Optional.of(mockOffer));
        when(repository.save(any(Offer.class))).thenReturn(mockOffer);

        Offer updatedOffer = offerService.updateOffer(1L, mockImage, "Updated Discount", "30% off on selected items",
                "30%", "2025-12-31");

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Offer.class));
        assertEquals("Updated Discount", updatedOffer.getOffer_name());
        assertEquals("30% off on selected items", updatedOffer.getOffer_desc());
        System.out.println("Update Offer Success test passed");
    }

    @Test
    public void testUpdateOffer_NotFound() throws IOException {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            offerService.updateOffer(2L, mockImage, "Updated Discount", "30% off on selected items", "30%",
                    "2025-12-31");
        });

        verify(repository, times(1)).findById(2L);
        verify(repository, times(0)).save(any(Offer.class));
        System.out.println("Update Offer Not Found test passed");
    }
}
