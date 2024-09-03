package com.example.restaurant.Service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restaurant.Model.CustomerQuery;
import com.example.restaurant.Repository.CustomerQueryRepository;

public class CustomerQueryServiceTest {

    @Mock
    private CustomerQueryRepository repository;

    @InjectMocks
    private CustomerQueryService queryService;

    private CustomerQuery mockQuery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockQuery = new CustomerQuery();
        mockQuery.setId(1L);
        mockQuery.setEmail("sample@gmail.com");
        mockQuery.setStatus("Pending");
    }

    @Test
    public void testAddQuery() {
        when(repository.save(mockQuery)).thenReturn(mockQuery);
        CustomerQuery result = queryService.addQuery(mockQuery);

        verify(repository, times(1)).save(mockQuery);
        assertTrue(result.getId().equals(mockQuery.getId()));
        System.out.println("Add Query test passed");
    }

    @Test
    public void testUpdateQueryStatus_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(mockQuery));
        when(repository.save(mockQuery)).thenReturn(mockQuery);

        CustomerQuery updatedQuery = queryService.updateQueryStatus(1L, "Resolved");
        verify(repository, times(1)).save(mockQuery);

        assertTrue(updatedQuery.getStatus().equals("Resolved"));
        System.out.println("Update Query Status Success test passed");
    }

    @Test
    public void testUpdateQueryStatus_NotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        CustomerQuery updatedQuery = queryService.updateQueryStatus(2L, "Resolved");
        verify(repository, times(0)).save(mockQuery);

        assertNull(updatedQuery);
        System.out.println("Update Query Status Not Found test passed");
    }
}
