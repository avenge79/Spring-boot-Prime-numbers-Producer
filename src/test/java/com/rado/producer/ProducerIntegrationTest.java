package com.rado.producer;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.RandomNumbersGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProducerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RandomNumbersGeneratorService randomNumbersGeneratorService;

    @Autowired
    ConfigureProperties configureProperties;

    @Test
    public void testProducerGeneratesPrimeNumbers() {
        String consumerUrl = configureProperties.getConsumerUrlRest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Integer> randomNumbers = new ArrayList<>();

        int totalNumbers = 100;
        // Generate random numbers
        for (int i = 0; i < totalNumbers; i++) {
            randomNumbers.add(randomNumbersGeneratorService.generateRandomNumber());
        }

        HttpEntity<List<Integer>> request = new HttpEntity<>(randomNumbers, headers);
        ResponseEntity<List> response = restTemplate.postForEntity(consumerUrl, request, List.class);

        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
        List<Integer> primeNumbers = response.getBody();

        for (Integer number : primeNumbers) {
            Assertions.assertTrue(isPrime(number), "Number " + number + " is not prime");
            Assertions.assertTrue(number >= 0 && number <= 1000, "Number " + number + " is out of range");
        }
    }

    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
