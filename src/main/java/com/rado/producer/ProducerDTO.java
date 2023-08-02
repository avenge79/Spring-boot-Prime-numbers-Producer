package com.rado.producer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProducerDTO {
    List<Integer> primeNumbers;
    String error;
}
