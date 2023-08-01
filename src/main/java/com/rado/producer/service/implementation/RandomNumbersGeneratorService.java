package com.rado.producer.service.implementation;

import com.rado.producer.config.ConfigureProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomNumbersGeneratorService implements com.rado.producer.service.RandomNumbersGeneratorService {
    private final ConfigureProperties configureProperties;
    private final Random random;

    @Autowired
    public RandomNumbersGeneratorService(ConfigureProperties configureProperties, Random random) {
        this.configureProperties = configureProperties;
        this.random = new Random();
    }

    public int generateRandomNumber() {
        return random.nextInt(configureProperties.getRandomNumberMax()) + 1;
    }

    public boolean generateBoolean() {
        return random.nextBoolean();
    }
}
