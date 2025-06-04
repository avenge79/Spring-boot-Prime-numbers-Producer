package com.rado.producer.dto;

import com.rado.producer.dto.model.NumberMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerDTO {
    NumberMessage message;
    String error;
}
