package com.rado.producer.service;


import com.rado.producer.dto.ProducerDTO;
import com.rado.producer.model.NumberMessage;

import java.util.List;

public interface ProducerService {
    ProducerDTO sendRandomNumbersToClient(List<Integer> numbers);
}
