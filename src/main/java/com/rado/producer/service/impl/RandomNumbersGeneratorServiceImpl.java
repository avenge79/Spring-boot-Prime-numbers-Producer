package com.rado.producer.service.impl;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.RandomNumbersGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomNumbersGeneratorServiceImpl implements RandomNumbersGeneratorService {
    private final ConfigureProperties configureProperties;
    private final Random random;

    public int generateRandomNumber() {
        return random.nextInt(configureProperties.getRandomNumberMax()) + 1;
    }

    public boolean generateBoolean() {
        return random.nextBoolean();
    }
}
