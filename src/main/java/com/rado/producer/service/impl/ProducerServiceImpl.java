package com.rado.producer.service.impl;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.dto.ProducerDTO;
import com.rado.producer.dto.model.NumberMessage;
import com.rado.producer.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final ConfigureProperties configureProperties;
    private final RestTemplate restTemplate;

    public ProducerDTO sendRandomNumbersToClient(List<Integer> numbers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NumberMessage numberMessage = new NumberMessage();
        numberMessage.setNumbers(numbers);
        HttpEntity<NumberMessage> request = new HttpEntity<>(numberMessage, headers);

        ProducerDTO consumerResponse = new ProducerDTO();

        try {
            ResponseEntity<NumberMessage> response = restTemplate.exchange(
                    configureProperties.getConsumerUrlRest(),
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {
                    }
            );
            log.info("Http Response status: {}, Body: {}",
                    response.getStatusCode(), response.getBody());
            consumerResponse.setMessage(response.getBody());
        } catch (Exception e) {
            String error = "ProducerService error: " + e.getMessage();
            log.error(error);
            consumerResponse.setError(error);
        }

        return consumerResponse;
    }
}
