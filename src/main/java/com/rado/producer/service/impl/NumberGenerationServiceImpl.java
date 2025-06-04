package com.rado.producer.service.impl;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.NumberGenerationService;
import com.rado.producer.service.RandomNumbersGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberGenerationServiceImpl implements NumberGenerationService {
    private final ConfigureProperties configureProperties;
    private final RandomNumbersGeneratorService randomNumbersGeneratorService;
    private int generatedNumbersCount = 0;

    public List<Integer> generateNumbers() {
        List<Integer> randomNumbers = new ArrayList<>();

        for (int i = 0; i < configureProperties.getNumbersPerBatch(); i++) {
            if (randomNumbersGeneratorService.generateBoolean()) {
                int randomNumber = randomNumbersGeneratorService.generateRandomNumber();
                randomNumbers.add(randomNumber);
                generatedNumbersCount++;
            }

            if (generatedNumbersCount >= configureProperties.getMaxStreamSize()) {
                break;
            }
        }

        if (randomNumbers.isEmpty()) {
            randomNumbers.add(randomNumbersGeneratorService.generateRandomNumber());
        }
        return randomNumbers;
    }

}
