package com.rado.producer;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.dto.ProducerDTO;
import com.rado.producer.dto.model.NumberMessage;
import com.rado.producer.service.impl.ProducerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProducerServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ConfigureProperties configureProperties;

    @InjectMocks
    private ProducerServiceImpl producerService; // използваме реалната имплементация

    @Test
    void sendRandomNumbersToClient_successfulResponse() {
        // Подготвяне на тестови данни
        List<Integer> numbers = List.of(1, 2, 3);

        NumberMessage responseMessage = new NumberMessage();
        responseMessage.setNumbers(numbers);
        ResponseEntity<NumberMessage> responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);

        // Мокване на зависимостите
        when(configureProperties.getConsumerUrlRest()).thenReturn("http://mock-url");
        when(restTemplate.exchange(eq("http://mock-url"), eq(HttpMethod.POST),
                any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // Извикване на тестирания метод
        ProducerDTO result = producerService.sendRandomNumbersToClient(numbers);

        // Проверка на резултата
        assertNotNull(result);
        assertNull(result.getError());
        assertEquals(numbers, result.getMessage().getNumbers());

        // Проверка на извикванията
        verify(restTemplate, times(1)).exchange(anyString(),
                eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    void sendRandomNumbersToClient_exceptionThrown() {
        // Подготвяне на тестови данни
        List<Integer> numbers = List.of(4, 5, 6);

        // Мокване на зависимостите
        when(configureProperties.getConsumerUrlRest()).thenReturn("http://mock-url");
        when(restTemplate.exchange(eq("http://mock-url"), eq(HttpMethod.POST),
                any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Simulated error"));

        // Извикване на тестирания метод
        ProducerDTO result = producerService.sendRandomNumbersToClient(numbers);

        // Проверка на резултата
        assertNotNull(result);
        assertNotNull(result.getError());
        assertTrue(result.getError().contains("Simulated error"));
    }
}