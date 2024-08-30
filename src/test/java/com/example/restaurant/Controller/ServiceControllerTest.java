package com.example.restaurant.Controller;

import com.example.restaurant.Model.Facility;
import com.example.restaurant.Service.FacilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private FacilityService restaurantServiceService;

        @Autowired
        private ObjectMapper objectMapper;

        private Facility restaurantService;

        @BeforeEach
        public void setup() {
                restaurantService = new Facility(1L, "Test Service", "Test Description", null);
        }

        @Test
        public void testGetAllServices() throws Exception {
                List<Facility> services = new ArrayList<>();
                services.add(restaurantService);

                when(restaurantServiceService.getAllServices()).thenReturn(services);

                mockMvc.perform(get("/service")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].service_name").value("Test Service"));
        }

        @Test
        public void testGetServiceById() throws Exception {
                when(restaurantServiceService.getServiceById(anyLong())).thenReturn(Optional.of(restaurantService));

                mockMvc.perform(get("/service/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.service_name").value("Test Service"));
        }

        @Test
        public void testGetServiceByIdNotFound() throws Exception {
                when(restaurantServiceService.getServiceById(anyLong())).thenReturn(Optional.empty());

                mockMvc.perform(get("/service/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testGetImage() throws Exception {
                byte[] imageBytes = "test image data".getBytes();
                when(restaurantServiceService.getImageById(anyLong())).thenReturn(imageBytes);

                mockMvc.perform(get("/service/image/{id}", 1L)
                                .contentType(MediaType.IMAGE_JPEG))
                                .andExpect(status().isOk())
                                .andExpect(content().bytes(imageBytes));
        }

        @Test
        public void testGetImageNotFound() throws Exception {
                when(restaurantServiceService.getImageById(anyLong())).thenReturn(null);

                mockMvc.perform(get("/service/image/{id}", 1L)
                                .contentType(MediaType.IMAGE_JPEG))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testAddService() throws Exception {
                when(restaurantServiceService.addService(any(Facility.class))).thenReturn(restaurantService);

                mockMvc.perform(post("/service/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(restaurantService)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.service_name").value("Test Service"));
        }

        @Test
        public void testDeleteService() throws Exception {
                when(restaurantServiceService.deleteService(anyLong())).thenReturn(true);

                mockMvc.perform(delete("/service/remove/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Service removed successfully"));
        }

        @Test
        public void testDeleteServiceNotFound() throws Exception {
                when(restaurantServiceService.deleteService(anyLong())).thenReturn(false);

                mockMvc.perform(delete("/service/remove/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Service not found"));
        }

        @Test
        public void testUploadImage() throws Exception {
                MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE,
                                "test image".getBytes());

                mockMvc.perform(multipart("/service/upload")
                                .file(image)
                                .param("serviceName", "New Service")
                                .param("serviceDesc", "New Description"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Image uploaded successfully"));
        }

        @Test
        public void testUpdateService() throws Exception {
                when(restaurantServiceService.updateService(anyLong(), any(), any(), any()))
                                .thenReturn(restaurantService);

                MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE,
                                "updated image".getBytes());

                mockMvc.perform(multipart("/service/update/{id}", 1L)
                                .file(image)
                                .param("serviceName", "Updated Service")
                                .param("serviceDesc", "Updated Description"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.service_name").value("Test Service"));
        }

        @Test
        public void testUpdateServiceNotFound() throws Exception {
                when(restaurantServiceService.updateService(anyLong(), any(), any(), any()))
                                .thenThrow(new RuntimeException("Service not found"));

                MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE,
                                "updated image".getBytes());

                mockMvc.perform(multipart("/service/update/{id}", 1L)
                                .file(image)
                                .param("serviceName", "Updated Service")
                                .param("serviceDesc", "Updated Description"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Service not found"));
        }
}
