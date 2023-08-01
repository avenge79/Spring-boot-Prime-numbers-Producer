package com.rado.producer.service;

import com.rado.producer.config.ConfigureProperties;
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
public class ProducerService {
    private final ConfigureProperties configureProperties;
    RestTemplate restTemplate;

    @Autowired
    public ProducerService(ConfigureProperties configureProperties, RestTemplate restTemplate) {
        this.configureProperties = configureProperties;
        this.restTemplate = restTemplate;
    }

    public List<Integer> sendRandomNumbersToClient(List<Integer> randomNumbers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Integer>> request = new HttpEntity<>(randomNumbers, headers);
        ResponseEntity<List> response = null;

        try {
            response = restTemplate.postForEntity(configureProperties.getConsumerUrlRest(), request, List.class);
            log.info("Http Response: {}", response);
        } catch (Exception e) {
            log.error("ProducerService error: {}", e.getMessage());
            return Collections.emptyList();
        }

        return response.getBody();
    }
}
