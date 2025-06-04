package com.rado.producer;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.impl.RandomNumbersGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.impl.RandomNumbersGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RandomNumbersGeneratorServiceImplTest {

    @Mock
    private ConfigureProperties configureProperties;

    @Mock
    private Random random;

    @InjectMocks
    private RandomNumbersGeneratorServiceImpl randomNumbersGeneratorService;

    @Test
    void generateRandomNumber_withinExpectedRange() {
        when(configureProperties.getRandomNumberMax()).thenReturn(100);
        when(random.nextInt(100)).thenReturn(42); // Мокваме рандом генератора

        int randomNumber = randomNumbersGeneratorService.generateRandomNumber();
        assertEquals(43, randomNumber); // +1 е в кода на метода
    }

    @Test
    void generateBoolean_returnsTrueOrFalse() {
        when(random.nextBoolean()).thenReturn(true);
        assertTrue(randomNumbersGeneratorService.generateBoolean());

        when(random.nextBoolean()).thenReturn(false);
        assertFalse(randomNumbersGeneratorService.generateBoolean());
    }
}