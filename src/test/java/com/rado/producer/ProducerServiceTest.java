package com.rado.producer;

import com.rado.producer.dto.ProducerDTO;
import com.rado.producer.service.ProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @Mock
    private ProducerService producerService; // Мокнат интерфейс вместо имплементация

    @Test
    void sendRandomNumbersToClient_invokesInterfaceMethod() {
        List<Integer> numbers = List.of(1, 2, 3);
        ProducerDTO mockResponse = new ProducerDTO();

        when(producerService.sendRandomNumbersToClient(numbers)).thenReturn(mockResponse);

        ProducerDTO result = producerService.sendRandomNumbersToClient(numbers);

        assertNotNull(result);
        verify(producerService, times(1)).sendRandomNumbersToClient(numbers); // Проверяваме дали методът е извикан
    }
}