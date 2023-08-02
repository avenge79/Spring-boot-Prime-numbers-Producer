package com.rado.producer.service;


import com.rado.producer.dto.ProducerDTO;

import java.util.List;

public interface ProducerService {
    ProducerDTO sendRandomNumbersToClient(List<Integer> randomNumbers);
}
