package com.rado.producer.dto;

import com.rado.producer.model.NumberMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProducerDTO {
    NumberMessage message;
    String error;
}
