package com.rado.producer.service.impl;

import com.rado.producer.service.RandomNumbersGeneratorService;
import com.rado.producer.service.RandomNumbersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomNumbersServiceImpl implements RandomNumbersService {

    private final RandomNumbersGeneratorService randomNumbersGeneratorService;

    public List<Integer> generateNumbers(int batchSize, int maxStreamSize, int generatedNumbersCount) {
        List<Integer> randomNumbers = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            if (randomNumbersGeneratorService.generateBoolean()) {
                int randomNumber = randomNumbersGeneratorService.generateRandomNumber();
                randomNumbers.add(randomNumber);
                generatedNumbersCount++;
            }

            if (generatedNumbersCount >= maxStreamSize) {
                break; // Stop generating if max limit reached
            }
        }

        if (randomNumbers.isEmpty()) {
            randomNumbers.add(randomNumbersGeneratorService.generateRandomNumber());
        }

        return randomNumbers;
    }

}
