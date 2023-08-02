package com.rado.producer.service.implementation;

import com.rado.producer.ProducerDTO;
import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProducerServiceImpl implements ProducerService {
    private final ConfigureProperties configureProperties;
    RestTemplate restTemplate;

    @Autowired
    public ProducerServiceImpl(ConfigureProperties configureProperties, RestTemplate restTemplate) {
        this.configureProperties = configureProperties;
        this.restTemplate = restTemplate;
    }

    public ProducerDTO sendRandomNumbersToClient(List<Integer> randomNumbers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Integer>> request = new HttpEntity<>(randomNumbers, headers);
        ResponseEntity<List> primeNumbersList = null;
        ProducerDTO consumerResponse = new ProducerDTO();

        try {
            primeNumbersList = restTemplate.postForEntity(configureProperties.getConsumerUrlRest(), request, List.class);
            log.info("Http Response: {}", consumerResponse);
        } catch (Exception e) {
            String error = "ProducerService error: ";
            log.error(error + e.getMessage());
            consumerResponse.setError(error + e.getMessage());
            return consumerResponse;
        }

        consumerResponse.setPrimeNumbers(primeNumbersList.getBody());

        return consumerResponse;
    }
}
