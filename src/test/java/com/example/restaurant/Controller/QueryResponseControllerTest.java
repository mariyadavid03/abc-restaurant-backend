package com.example.restaurant.Controller;

import com.example.restaurant.Model.QueryResponse;
import com.example.restaurant.Service.QueryResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QueryResponseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryResponseService queryResponseService;

    @Autowired
    private ObjectMapper objectMapper;

    private QueryResponse queryResponse;

    @BeforeEach
    public void setup() {
        queryResponse = new QueryResponse(
                1L, null, null, "Test response", new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void testGetAllResponses() throws Exception {
        List<QueryResponse> responses = new ArrayList<>();
        responses.add(queryResponse);
        when(queryResponseService.getAllResponses()).thenReturn(responses);

        mockMvc.perform(get("/response")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].response_message").value("Test response"));
    }

    @Test
    public void testGetResponse() throws Exception {
        when(queryResponseService.getResponseById(anyLong())).thenReturn(Optional.of(queryResponse));

        mockMvc.perform(get("/response/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response_message").value("Test response"));
    }

    @Test
    public void testGetResponseNotFound() throws Exception {
        when(queryResponseService.getResponseById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/response/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetResponseByQueryId() throws Exception {
        when(queryResponseService.getResponseByQueryId(anyLong())).thenReturn(queryResponse);

        mockMvc.perform(get("/response/query/{queryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response_message").value("Test response"));
    }

    @Test
    public void testGetResponseByQueryIdNotFound() throws Exception {
        when(queryResponseService.getResponseByQueryId(anyLong())).thenReturn(null);

        mockMvc.perform(get("/response/query/{queryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddResponse() throws Exception {
        QueryResponse newResponse = new QueryResponse(
                2L, null, null, "New response", new Timestamp(System.currentTimeMillis()));
        when(queryResponseService.addResponse(any(QueryResponse.class))).thenReturn(newResponse);

        mockMvc.perform(post("/response/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newResponse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response_message").value("New response"));
    }

    @Test
    public void testDeleteResponse() throws Exception {
        when(queryResponseService.deleteResponse(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/response/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Response removed successfully"));
    }

    @Test
    public void testDeleteResponseNotFound() throws Exception {
        when(queryResponseService.deleteResponse(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/response/remove/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Response not found"));
    }
}
