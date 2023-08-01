package com.rado.producer.service;

import java.util.List;

public interface ProducerService {
    List<Integer> sendRandomNumbersToClient(List<Integer> randomNumbers);
}
