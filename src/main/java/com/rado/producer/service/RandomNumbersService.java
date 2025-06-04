package com.rado.producer.service;

import java.util.List;

public interface RandomNumbersService {
    public List<Integer> generateNumbers(int batchSize, int maxStreamSize, int generatedNumbersCount);
}
