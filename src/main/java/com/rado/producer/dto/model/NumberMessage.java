package com.rado.producer.dto.model;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NumberMessage {
    private List<Integer> numbers;
}