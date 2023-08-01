package com.rado.producer.scheduler;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.handler.StompSessionHandler;
import com.rado.producer.service.CSVWriterService;
import com.rado.producer.service.ProducerService;
import com.rado.producer.service.RandomNumbersGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SendNumbersTaskScheduler {
    private final ThreadPoolTaskScheduler taskScheduler;
    private int generatedNumbersCount = 0;
    private final ProducerService producerService;
    private final ConfigureProperties configureProperties;
    private final RandomNumbersGeneratorService randomNumbersGeneratorService;
    private final CSVWriterService csvWriterService;

    @Autowired
    public SendNumbersTaskScheduler(ThreadPoolTaskScheduler taskScheduler, ProducerService producerService, ConfigureProperties configureProperties, RandomNumbersGeneratorService randomNumbersGeneratorService, CSVWriterService csvWriterService) {
        this.taskScheduler = taskScheduler;
        this.producerService = producerService;
        this.configureProperties = configureProperties;
        this.randomNumbersGeneratorService = randomNumbersGeneratorService;
        this.csvWriterService = csvWriterService;
    }

    @Scheduled(fixedRateString = "${scheduler.milliseconds}")
    public void generateAndSendNumbers() {
        if (generatedNumbersCount >= configureProperties.getMaxStreamSize()) {
            log.info("Generation limit reached: {}", generatedNumbersCount);
            stopScheduler();
            return;
        }

        List<Integer> randomNumbers = new ArrayList<>();

        generateNumbersList(randomNumbers);
        log.info("Random numbers: {}, total generated: {}", randomNumbers, generatedNumbersCount);

        if (configureProperties.isUseWebsocket()) {
            // Send numbers through WebSocket
            WebSocketClient client = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            org.springframework.messaging.simp.stomp.StompSessionHandler sessionHandler = new StompSessionHandler(randomNumbers);
            stompClient.connect(configureProperties.getConsumerUrlWebsocket(), sessionHandler);
        } else {
            // Send numbers through REST
            List<Integer> response = producerService.sendRandomNumbersToClient(randomNumbers);
            log.info("Returned prime numbers: {}", response);
        }
        // Write numbers to CSV file
        csvWriterService.writeRandomNumbersToCSV(randomNumbers);
    }

    public void generateNumbersList(List<Integer> randomNumbers) {
        for (int i = 0; i < configureProperties.getNumbersPerBatch(); i++) {
            if (randomNumbersGeneratorService.generateBoolean()) {
                int randomNumber = randomNumbersGeneratorService.generateRandomNumber();
                randomNumbers.add(randomNumber);
                generatedNumbersCount++;
            }

            if (generatedNumbersCount >= configureProperties.getMaxStreamSize()) {
                break; // Stop generating random numbers if the maximum required numbers reached
            }
        }

        // Make sure there is at least 1 number generated
        if (randomNumbers.isEmpty()) {
            randomNumbers.add(randomNumbersGeneratorService.generateRandomNumber());
        }
    }

    public void stopScheduler() {
        taskScheduler.shutdown();
        log.info("Scheduler stopped.");
        System.exit(0); // Stop the application
    }
}