package com.rado.producer;

import com.rado.producer.service.RandomNumbersGeneratorService;
import com.rado.producer.service.impl.RandomNumbersServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomNumbersServiceImplTest {

    @Mock
    private RandomNumbersGeneratorService randomNumbersGeneratorService;

    @InjectMocks
    private RandomNumbersServiceImpl randomNumbersService;

    @Test
    void generateNumbers_generatesExpectedCount() {
        when(randomNumbersGeneratorService.generateBoolean()).thenReturn(true);
        when(randomNumbersGeneratorService.generateRandomNumber()).thenReturn(42);

        List<Integer> numbers = randomNumbersService
                .generateNumbers(5, 10, 0);

        assertEquals(5, numbers.size());
        assertTrue(numbers.contains(42));
    }

    @Test
    void generateNumbers_stopsAtMaxStreamSize() {
        when(randomNumbersGeneratorService.generateBoolean()).thenReturn(true);
        when(randomNumbersGeneratorService.generateRandomNumber()).thenReturn(99);

        List<Integer> numbers = randomNumbersService
                .generateNumbers(10, 3, 0);

        assertEquals(3, numbers.size());
    }

    @Test
    void generateNumbers_generatesAtLeastOneNumberIfEmpty() {
        when(randomNumbersGeneratorService.generateBoolean()).thenReturn(false);
        when(randomNumbersGeneratorService.generateRandomNumber()).thenReturn(100);

        List<Integer> numbers = randomNumbersService
                .generateNumbers(5, 10, 0);

        assertEquals(1, numbers.size());
        assertEquals(100, numbers.getFirst());
    }
}
