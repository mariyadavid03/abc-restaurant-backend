package com.example.restaurant.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.example.restaurant.Model.Facility;
import com.example.restaurant.Repository.FacilityRepository;

public class FacilityServiceTest {

    @Mock
    private FacilityRepository repository;

    @InjectMocks
    private FacilityService facilityService;

    private Facility mockFacility;
    private MultipartFile mockImage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockFacility = new Facility();
        mockFacility.setId(1L);
        mockFacility.setService_name("Sample Name");
        mockFacility.setService_desc("Some Description");

        mockImage = mock(MultipartFile.class);
        try {
            when(mockImage.getBytes()).thenReturn("image data".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveFacility_Success() throws IOException {
        when(repository.save(any(Facility.class))).thenReturn(mockFacility);

        facilityService.saveServiceImage(mockImage, "Sample Name", "Some Description");

        verify(repository, times(1)).save(any(Facility.class));
        System.out.println("Save Offer Success test passed");
    }

    @Test
    public void testUpdateFacility_Success() throws IOException {
        when(repository.findById(1L)).thenReturn(Optional.of(mockFacility));
        when(repository.save(any(Facility.class))).thenReturn(mockFacility);

        Facility updatedFacility = facilityService.updateService(1L, mockImage, "Updated Name", "Updated Desc");

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Facility.class));
        assertEquals("Updated Name", updatedFacility.getService_name());
        assertEquals("Updated Desc", updatedFacility.getService_desc());
        System.out.println("Update Facilitiy Success test passed");
    }
}