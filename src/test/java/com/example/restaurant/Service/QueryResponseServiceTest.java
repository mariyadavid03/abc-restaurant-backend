package com.example.restaurant.Service;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restaurant.Model.CustomerQuery;
import com.example.restaurant.Model.QueryResponse;
import com.example.restaurant.Model.User;
import com.example.restaurant.Repository.QueryReponseRepository;

public class QueryResponseServiceTest {

    @Mock
    private QueryReponseRepository repository;

    @InjectMocks
    private QueryResponseService responseService;

    private QueryResponse mockResponse;
    private CustomerQuery mockQuery;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockQuery = new CustomerQuery();
        mockQuery.setId(1L);

        mockUser = new User();
        mockUser.setId(2L);

        mockResponse = new QueryResponse();
        mockResponse.setId(3L);
        mockResponse.setQuery(mockQuery);
        mockResponse.setUser(mockUser);
        mockResponse.setResponse_message("Sample response message");
    }

    @Test
    public void testAddResponse_Success() {
        when(repository.save(mockResponse)).thenReturn(mockResponse);

        QueryResponse result = responseService.addResponse(mockResponse);

        verify(repository, times(1)).save(mockResponse);
        assertTrue(result.getId().equals(mockResponse.getId()));
        System.out.println("Add Response Success test passed");
    }

    @Test
    public void testGetResponseByQueryId_Success() {
        when(repository.findByQueryId(1L)).thenReturn(mockResponse);

        QueryResponse result = responseService.getResponseByQueryId(1L);

        verify(repository, times(1)).findByQueryId(1L);
        assertTrue(result.getQuery().equals(mockQuery));
        System.out.println("Get Response by Query ID Success test passed");
    }

    @Test
    public void testGetResponseByQueryId_NotFound() {
        when(repository.findByQueryId(2L)).thenReturn(null);

        QueryResponse result = responseService.getResponseByQueryId(2L);

        verify(repository, times(1)).findByQueryId(2L);
        assertNull(result);
        System.out.println("Get Response by Query ID Not Found test passed");
    }
}
